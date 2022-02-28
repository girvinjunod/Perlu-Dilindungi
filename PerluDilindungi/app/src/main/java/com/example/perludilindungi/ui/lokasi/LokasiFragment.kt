package com.example.perludilindungi.ui.lokasi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perludilindungi.databinding.FragmentLokasiBinding
import com.example.perludilindungi.network.FaskesProperty
import com.example.perludilindungi.network.FaskesResults
import com.example.perludilindungi.network.LocationProperty
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


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
                val adapter = LokasiAdapter(faskes)

                adapter.setOnItemClickListener(object : LokasiAdapter.ClickListener {
                    override fun onItemClick(position: Int, v: View?) {
                        Timber.i("onItemClick position: $position")
                    }
                    override fun onItemLongClick(position: Int, v: View?) {
                        Timber.i("onItemLongClick pos = $position")
                    }
                })

                binding.faskesList.adapter = adapter
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
                                }
                                override fun onItemLongClick(position: Int, v: View?) {
                                    Timber.i("onItemLongClick pos = $position")
                                }
                            })

                            binding.faskesList.adapter = adapter
                        } else{
                            Timber.i("Location null")
                            val adapter = LokasiAdapter(faskes)


                            adapter.setOnItemClickListener(object : LokasiAdapter.ClickListener {
                                override fun onItemClick(position: Int, v: View?) {
                                    Timber.i("onItemClick position: $position")
                                }
                                override fun onItemLongClick(position: Int, v: View?) {
                                    Timber.i("onItemLongClick pos = $position")
                                }
                            })

                            binding.faskesList.adapter = adapter
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







        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}