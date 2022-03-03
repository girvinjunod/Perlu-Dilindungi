package com.example.perludilindungi.ui.bookmark

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.databinding.FragmentBookmarkBinding
import com.example.perludilindungi.room.Bookmark
import com.example.perludilindungi.room.BookmarkDB
import com.example.perludilindungi.ui.lokasi.LokasiViewModel
import com.example.perludilindungi.ui.news.NewsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var bookmarkAdapter: BookmarkAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bookmarkViewModel =
            ViewModelProvider(this).get(BookmarkViewModel::class.java)

        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var obj : List<Bookmark>



        val db by lazy { context?.let { BookmarkDB(it) } }
        db?.let { bookmarkViewModel.getDatabaseBookmark(it) }
        Timber.i("$db")
//        <--------------------- DETAIL FASKES --------------------->
        val detailFaskesView : ConstraintLayout = binding.detailFakses
        val namaFaskesDetail : TextView = binding.namaFaskesDetail
        val alamatView: TextView = binding.alamatDetail
        val jenisView: TextView = binding.jenisFaskesDetail
        val telpView: TextView = binding.noTelpDetail
        val kodeView: TextView = binding.kodeFaskesDetail
        val statusView: TextView = binding.statusFaskesDetail
        val statusImageView : ImageView = binding.statusImage
        val bookmarkBtn : Button = binding.bookmarkButton
        val googleBtn : Button = binding.googleButton
        val textBookmark : TextView = binding.textViewBookmark
        val bookmarkList : RecyclerView = binding.bookmarkList

        bookmarkViewModel.response.observe(viewLifecycleOwner) {
            obj = it
            val adapter = BookmarkAdapter(obj)
            adapter.setOnItemClickListener(object : BookmarkAdapter.ClickListener {
                override fun onItemClick(position: Int, v: View?) {
                detailFaskesView.visibility = View.VISIBLE
                textBookmark.visibility = View.INVISIBLE
                bookmarkList.visibility = View.INVISIBLE

                namaFaskesDetail.text = obj[position].namaFaskes
                alamatView.text = obj[position].alamatFaskes
                jenisView.text = obj[position].jenisFaskes
                telpView.text = "No telp: " + obj[position].telpFaskes
                kodeView.text = "KODE: " + obj[position].kodeFaskes

//                    set warna jenis
                if (jenisView.text.toString() == "PUSKESMAS"){
                    jenisView.setBackgroundColor(Color.parseColor("#ff80ff"))
                }else{
                    jenisView.setBackgroundColor(Color.parseColor("#8533ff"))
                }

//                  set gambar
                if (obj[position].statusFaskes.toString() == "Siap Vaksinasi") {
                    statusImageView.setImageResource(com.example.perludilindungi.R.drawable.ic_baseline_check_circle_outline_24)
                } else {
                    statusImageView.setImageResource(com.example.perludilindungi.R.drawable.ic_baseline_cancel_24)
                }
                statusView.text = "Fasilitas ini\n" + obj[position].statusFaskes.uppercase()

                bookmarkBtn.setOnClickListener{

//                    TODO: DELETE DB
                    Timber.d("DELETE FASKES %S", namaFaskesDetail?.text.toString())
//                    CoroutineScope(Dispatchers.IO).launch{
//                        db?.bookmarkDao()?.addBookmark(
//                            Bookmark(
//                                0,
//                                obj[position].namaFaskes.toString(),
//                                obj[position].alamatFaskes.toString(),
//                                obj[position].jenisFaskes.toString(),
//                                obj[position].telpFaskes.toString(),
//                                obj[position].kodeFaskes.toString(),
//                                obj[position].statusFaskes.toString()
//                            ))
//                    }
                    Timber.d("BOOKMARK DELETED")
                }

                googleBtn?.setOnClickListener{
                    Timber.d("GOOGLE CLICKED")
                    val sourceLatitude = obj[position].latitude.toString()
                    var sourceLongitude = obj[position].longitude.toString()
//                        Log.d("sourceLatitude",sourceLatitude)
//                        Log.d("sourceLongitude",sourceLongitude)
                    val completeLoc = "geo:"+sourceLatitude+","+sourceLongitude+"?q="+sourceLatitude+","+sourceLongitude+"("+obj[position].namaFaskes.toString()+")"
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
            binding.bookmarkList.adapter = adapter
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(detailFaskesView?.visibility.toString() == "0"){
                    detailFaskesView?.visibility = View.INVISIBLE
                    textBookmark?.visibility = View.VISIBLE
                    bookmarkList?.visibility = View.VISIBLE
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })

        return root
    }

    private fun setupRecyclerView(){
        val bookmark_list = binding.bookmarkList
        bookmarkAdapter = BookmarkAdapter(arrayListOf())
        bookmark_list.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = bookmarkAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}