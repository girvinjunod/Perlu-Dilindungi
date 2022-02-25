package com.example.perludilindungi.network

import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://perludilindungi.herokuapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("api/get-news")
    fun getNews():
            Call<NewsProperty>
}

object PerluDilindungiApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}

