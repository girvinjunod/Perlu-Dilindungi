package com.example.perludilindungi.ui.lokasi

import android.content.Intent
import android.graphics.Color
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.databinding.FragmentLokasiBinding
import com.example.perludilindungi.network.FaskesProperty
import com.example.perludilindungi.network.FaskesResults
import com.example.perludilindungi.network.LocationProperty
import com.example.perludilindungi.room.Bookmark
import com.example.perludilindungi.room.BookmarkDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import com.example.perludilindungi.ui.detail.DetailActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import android.widget.Toast;


class LokasiFragment : Fragment() {

    private var _binding: FragmentLokasiBinding? = null

    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(
                deg2rad(lat2)
            ) * cos(deg2rad(theta))
        dist = acos(dist)
        dist = deg2rad(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lokasiViewModel =
            ViewModelProvider(this).get(LokasiViewModel::class.java)

        _binding = FragmentLokasiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinnerProvinsi: Spinner = binding.spinnerProvinsi
        val spinnerCity : Spinner = binding.spinnerKota

        var currProvinsi = ""
        var currCity = ""

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        var province : LocationProperty
        lokasiViewModel.location.observe(viewLifecycleOwner) {
            province = it
            val provinceArray : MutableList<String> = ArrayList()
            for (item in province.results!!) {
                provinceArray.add(item.value)
            }
            context?.let {
                val  provinceAdapter : ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,provinceArray)
                provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProvinsi.adapter = provinceAdapter
            }

        }
        spinnerProvinsi.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                if (item != null) {
                    lokasiViewModel.getApiCity(item.toString())
                    currProvinsi = item.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        var city: LocationProperty
        lokasiViewModel.city.observe(viewLifecycleOwner) {
            city = it
            val cityArray : MutableList<String> = ArrayList()
            for (item in city.results!!) {
                cityArray.add(item.value)
            }
            context?.let{
                val cityAdapter : ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,cityArray)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCity.adapter = cityAdapter
            }
        }

        spinnerCity.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                if (item != null) {
                    currCity = item.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        val searchBtn = binding.searchButton
        searchBtn.setOnClickListener {
            Timber.i("Search click")
            lokasiViewModel.getApiFaskes(currProvinsi, currCity)

        }


//        <--------------------- DETAIL FASKES --------------------->
        val detailFaskesView : ConstraintLayout? = binding.detailFakses
        val namaFaskesDetail : TextView? = binding.namaFaskesDetail
        val alamatView: TextView? = binding.alamatDetail
        val jenisView: TextView? = binding.jenisFaskesDetail
        val telpView: TextView? = binding.noTelpDetail
        val kodeView: TextView? = binding.kodeFaskesDetail
        val statusView: TextView? = binding.statusFaskesDetail
        val statusImageView : ImageView? = binding.statusImage
        val bookmarkBtn : Button? = binding.bookmarkButton
        val googleBtn : Button? = binding.googleButton
        val faskesLayout : ConstraintLayout? = binding.constraintLayout
        val faskesList : RecyclerView? = binding.faskesList

        var faskes: FaskesProperty
        lokasiViewModel.faskes.observe(viewLifecycleOwner) { res ->
            faskes = res
            Timber.i(faskes.toString())

            var sorted: List<FaskesResults>?



            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.i("No Permission")
                Toast.makeText(context, "Location access is required", Toast.LENGTH_SHORT).show();
            } else{
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location->
                        if (location != null) {
                            Timber.i(location.toString())
                            sorted = faskes.data?.sortedBy { distance(it.latitude.toDouble(), it.longitude.toDouble(), location.altitude, location.longitude) }

                            val temp = ArrayList<FaskesResults>()

                            for (i in sorted?.indices!!){
                                if (i < 5) {
                                    temp.add(sorted!![i])
                                    Timber.i(distance(sorted!![i].latitude.toDouble(), sorted!![i].longitude.toDouble(), location.altitude, location.longitude).toString())
                                }
                            }

                            faskes.data = temp
                            Timber.i("Sorted")
                            val adapter = LokasiAdapter(faskes)

                            adapter.setOnItemClickListener(object : LokasiAdapter.ClickListener {
                                override fun onItemClick(position: Int, v: View?) {
                                    Timber.i("onItemClick position: $position")
                                    detailFaskesView?.visibility = View.VISIBLE
                                    faskesLayout?.visibility = View.INVISIBLE
                                    faskesList?.visibility = View.INVISIBLE

                                    namaFaskesDetail?.text = faskes.data?.get(position)?.nama
                                    alamatView?.text = faskes.data?.get(position)?.alamat
                                    jenisView?.text = faskes.data?.get(position)?.jenis_faskes
                                    telpView?.text = "No telp: " + faskes.data?.get(position)?.telp
                                    kodeView?.text = "KODE: " + faskes.data?.get(position)?.kode

//                    set warna jenis
                                    if (jenisView?.text.toString() == "PUSKESMAS"){
                                        jenisView?.setBackgroundColor(Color.parseColor("#ff80ff"))
                                    }else if (jenisView?.text.toString() == "RUMAH SAKIT"){
                                        jenisView?.setBackgroundColor(Color.parseColor("#00DDFF"))
                                    } else if (jenisView?.text.toString() == "KLINIK"){
                                        jenisView?.setBackgroundColor(Color.parseColor("#FFF800"))
                                    }


//                  set gambar
                                    if (faskes.data?.get(position)?.status.toString() == "Siap Vaksinasi") {
                                        statusImageView?.setImageResource(com.example.perludilindungi.R.drawable.ic_baseline_check_circle_outline_24)
                                    } else {
                                        statusImageView?.setImageResource(com.example.perludilindungi.R.drawable.ic_baseline_cancel_24)
                                    }
                                    statusView?.text = "Fasilitas ini\n" + faskes.data?.get(position)?.status?.uppercase()

                                    val db by lazy { context?.let { BookmarkDB(it) } }

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val cekDB = db?.bookmarkDao()
                                            ?.checkBookmark(faskes.data?.get(position)?.kode.toString())

                                        if (cekDB != null) {
                                            Timber.i("ADA DI DB")
                                            bookmarkBtn?.setBackgroundColor(Color.parseColor("#4d4d4d"))
                                        }else{
                                            bookmarkBtn?.setBackgroundColor(Color.parseColor("#ff80ff"))
                                        }
                                    }
                                    bookmarkBtn?.setOnClickListener{
                                        CoroutineScope(Dispatchers.IO).launch{
                                            val cekDB = db?.bookmarkDao()
                                                ?.checkBookmark(faskes.data?.get(position)?.kode.toString())

                                            if (cekDB != null) {
                                                Timber.i("ADA DI DB")
                                                bookmarkBtn.setBackgroundColor(Color.parseColor("#4d4d4d"))
                                                bookmarkBtn.isActivated = false
                                            }else{
                                                db?.bookmarkDao()?.addBookmark(
                                                    Bookmark(
                                                        0,
                                                        faskes.data?.get(position)?.nama.toString(),
                                                        faskes.data?.get(position)?.alamat.toString(),
                                                        faskes.data?.get(position)?.jenis_faskes.toString(),
                                                        faskes.data?.get(position)?.telp.toString(),
                                                        faskes.data?.get(position)?.kode.toString(),
                                                        faskes.data?.get(position)?.status.toString(),
                                                        faskes.data?.get(position)?.latitude.toString(),
                                                        faskes.data?.get(position)?.longitude .toString()
                                                    ))
                                                Timber.d("BOOKMARK ADDED")
                                                bookmarkBtn.setBackgroundColor(Color.parseColor("#4d4d4d"))
                                                bookmarkBtn.isActivated = false
                                            }

                                        }
                                    }


                                    googleBtn?.setOnClickListener{
                                        Timber.d("GOOGLE CLICKED")
                                        val sourceLatitude = faskes.data?.get(position)?.latitude.toString()
                                        var sourceLongitude = faskes.data?.get(position)?.longitude.toString()
//                        Log.d("sourceLatitude",sourceLatitude)
//                        Log.d("sourceLongitude",sourceLongitude)
                                        val completeLoc = "geo:"+sourceLatitude+","+sourceLongitude+"?q="+sourceLatitude+","+sourceLongitude+"("+faskes.data?.get(position)?.nama.toString()+")"
//                        Log.d("completeLoc", completeLoc)
                                        val gmmIntentUri = Uri.parse(completeLoc)
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        startActivity(mapIntent)
                                    }

                                }
                                override fun onItemLongClick(position: Int, v: View?) {
                                    Timber.i("onItemLongClick pos = $position")
                                }
                            })

                            binding.faskesList.adapter = adapter
                        } else{
                            Timber.i("Location null")
                            Toast.makeText(context, "Last location is null", Toast.LENGTH_SHORT).show();
                        }

                    }
            }



//            val adapter = LokasiAdapter(faskes)
//
//
//            adapter.setOnItemClickListener(object : LokasiAdapter.ClickListener {
//                override fun onItemClick(position: Int, v: View?) {
//                    Timber.i("onItemClick position: $position")
//                }
//                override fun onItemLongClick(position: Int, v: View?) {
//                    Timber.i("onItemLongClick pos = $position")
//                }
//            })
//
//            binding.faskesList.adapter = adapter
        }


//        <--------------------- DETAIL FASKES --------------------->


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(detailFaskesView?.visibility.toString() == "0"){
                    detailFaskesView?.visibility = View.INVISIBLE
                    faskesLayout?.visibility = View.VISIBLE
                    faskesList?.visibility = View.VISIBLE
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })







        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}