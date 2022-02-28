package com.example.perludilindungi.ui.lokasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perludilindungi.network.*
import kotlinx.coroutines.launch
import timber.log.Timber

class LokasiViewModel : ViewModel() {

    private fun getApiProvince() {
        viewModelScope.launch {
            try {
                val result = PerluDilindungiApi.retrofitService.getProvince()

                _location.value = result

            } catch (e: Exception) {
                val temp = LocationProperty("", "", null)
                _location.value = temp
            }

        }
    }

    private fun getApiCity(province : String) {
        viewModelScope.launch {
            try {
                val result = PerluDilindungiApi.retrofitService.getCity(province)

                _city.value = result

            } catch (e: Exception) {
                val temp = LocationProperty("", "", null)
                _city.value = temp
            }

        }
    }

    private fun getApiFaskes(province : String, city : String) {
        viewModelScope.launch {
            try {
                val result = PerluDilindungiApi.retrofitService.getFaskes(province, city)

                _faskes.value = result

            } catch (e: Exception) {
                Timber.e(e)
                val temp = FaskesProperty(false, "", 0, null)
                _faskes.value = temp
            }

        }
    }

    val location: LiveData<LocationProperty>
        get() = _location

    private val _location = MutableLiveData<LocationProperty>()

    val city: LiveData<LocationProperty>
        get() = _city

    private val _city = MutableLiveData<LocationProperty>()

    val faskes: LiveData<FaskesProperty>
        get() = _faskes

    private val _faskes = MutableLiveData<FaskesProperty>()



    init {
        getApiProvince()
        getApiCity("JAWA TENGAH")
        getApiFaskes("JAWA TENGAH", "KAB. TEGAL")
    }
}