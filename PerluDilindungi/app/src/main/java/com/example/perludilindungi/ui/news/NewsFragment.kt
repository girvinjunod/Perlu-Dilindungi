package com.example.perludilindungi.ui.news

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.databinding.FragmentNewsBinding
import com.example.perludilindungi.network.NewsProperty
import timber.log.Timber


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val newsViewModel =
            ViewModelProvider(this).get(NewsViewModel::class.java)

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var obj : NewsProperty
//        val textView: TextView = binding.textNews
        val myWebView: WebView = binding.web

        newsViewModel.response.observe(viewLifecycleOwner) {
            obj = it
            val adapter = NewsAdapter(obj)
            adapter.setOnItemClickListener(object : NewsAdapter.ClickListener {
                override fun onItemClick(position: Int, v: View?) {
                    Timber.i("onItemClick position: $position")
                    myWebView.visibility = View.VISIBLE
                    myWebView.loadUrl(obj.results?.get(position)?.link?.get(0) ?: "")
                }

                override fun onItemLongClick(position: Int, v: View?) {
                    Timber.i("onItemLongClick pos = $position")
                }
            })
            binding.newsList.adapter = adapter
        }


        Timber.i("onCreateView called")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.i("onDestroyView called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }
    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }
    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }
    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }
    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }
}