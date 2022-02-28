package com.example.perludilindungi.ui.detail

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perludilindungi.databinding.FragmentDetailFaskesBinding
import com.example.perludilindungi.network.FaskesProperty
import com.example.perludilindungi.network.LocationProperty

import timber.log.Timber

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailFaskesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val detailViewModel =
            ViewModelProvider(this).get(DetailViewModel::class.java)

        _binding = FragmentDetailFaskesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinnerProvinsi: Spinner = binding.spinnerProvinsi
        val spinnerCity : Spinner = binding.spinnerKota

        var currProvinsi = ""
        var currCity = ""

        var province : LocationProperty
        detailViewModel.location.observe(viewLifecycleOwner) {
            province = it
            val provinceArray : MutableList<String> = ArrayList()
            for (item in province.results!!) {
                provinceArray.add(item.value)
            }
            context?.let {
                val  provinceAdapter : ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                    R.layout.simple_spinner_item,provinceArray)
                provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProvinsi.adapter = provinceAdapter
            }

        }
        spinnerProvinsi.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                if (item != null) {
                    detailViewModel.getApiCity(item.toString())
                    currProvinsi = item.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        var city: LocationProperty
        detailViewModel.city.observe(viewLifecycleOwner) {
            city = it
            val cityArray : MutableList<String> = ArrayList()
            for (item in city.results!!) {
                cityArray.add(item.value)
            }
            context?.let{
                val cityAdapter : ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                    R.layout.simple_spinner_item,cityArray)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCity.adapter = cityAdapter
            }
        }


        var faskes: FaskesProperty
        detailViewModel.faskes.observe(viewLifecycleOwner) {
            faskes = it
            Timber.i(faskes.toString())
            val adapter = DetailAdapter(faskes)
            adapter.setOnItemClickListener(object : DetailAdapter.ClickListener {
                override fun onItemClick(position: Int, v: View?) {
                    Timber.i("onItemClick position: $position")
                }
                override fun onItemLongClick(position: Int, v: View?) {
                    Timber.i("onItemLongClick pos = $position")
                }
            })

            binding.faskesList.adapter = adapter
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
            detailViewModel.getApiFaskes(currProvinsi, currCity)

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}