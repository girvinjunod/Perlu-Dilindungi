package com.example.perludilindungi.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perludilindungi.databinding.FragmentBookmarkBinding
import com.example.perludilindungi.network.FaskesProperty
import com.example.perludilindungi.room.Bookmark
import com.example.perludilindungi.room.BookmarkDB
import com.example.perludilindungi.ui.lokasi.LokasiAdapter
import com.example.perludilindungi.ui.lokasi.LokasiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lokasiViewModel =
            ViewModelProvider(this).get(LokasiViewModel::class.java)

        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val root: View = binding.root





        val db by lazy { context?.let { BookmarkDB(it) } }
        CoroutineScope(Dispatchers.IO).launch{
            val bookmarks = db?.bookmarkDao()?.getBookmark()
            if (bookmarks != null) {
                for(item in bookmarks){
                    Timber.i("GETBOOKMARKS: $item")
                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}