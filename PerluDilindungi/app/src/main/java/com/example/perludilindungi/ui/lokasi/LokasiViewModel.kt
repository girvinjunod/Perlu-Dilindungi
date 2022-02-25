package com.example.perludilindungi.ui.lokasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LokasiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Lokasi Fragment"
    }
    val text: LiveData<String> = _text
}