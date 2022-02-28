package com.example.perludilindungi.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perludilindungi.network.*
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailViewModel : ViewModel() {

    fun getApiFaskes(province : String, city : String) {
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

    val faskes: LiveData<FaskesProperty>
        get() = _faskes

    private val _faskes = MutableLiveData<FaskesProperty>()



    init {
        getApiFaskes("JAWA TENGAH", "KAB. TEGAL")
    }
}