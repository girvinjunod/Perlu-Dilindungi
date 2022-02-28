package com.example.perludilindungi.ui.lokasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perludilindungi.databinding.FragmentLokasiBinding
import com.example.perludilindungi.network.FaskesProperty
import com.example.perludilindungi.network.LocationProperty
import com.example.perludilindungi.ui.news.NewsAdapter
import timber.log.Timber

class LokasiFragment : Fragment() {

    private var _binding: FragmentLokasiBinding? = null

    private val binding get() = _binding!!

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
        lokasiViewModel.faskes.observe(viewLifecycleOwner) {
            faskes = it
            val adapter = LokasiAdapter(faskes)
            Timber.i(faskes.toString())
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






        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}