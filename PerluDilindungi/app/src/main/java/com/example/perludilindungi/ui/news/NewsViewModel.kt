package com.example.perludilindungi.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perludilindungi.network.NewsProperty
import retrofit2.Callback
import com.example.perludilindungi.network.PerluDilindungiApi
import retrofit2.Call
import retrofit2.Response


class NewsViewModel : ViewModel() {

    private fun getApiNews() {
        PerluDilindungiApi.retrofitService.getNews().enqueue(
            object: Callback<NewsProperty> {
                override fun onResponse(call: Call<NewsProperty>, response: Response<NewsProperty>) {

                    _response.value = "Success: ${response.body()?.count} news retrieved"
                }

                override fun onFailure(call: Call<NewsProperty>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                }
            }
        )
    }
    val response: LiveData<String>
        get() = _response

    private val _response = MutableLiveData<String>()

    init {
        getApiNews()
    }
}