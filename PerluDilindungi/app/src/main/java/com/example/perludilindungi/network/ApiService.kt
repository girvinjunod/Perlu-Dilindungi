package com.example.perludilindungi.network

import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
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
    suspend fun getNews(): NewsProperty

    @GET("api/get-province")
    suspend fun getProvince() : LocationProperty

    @GET("api/get-city")
    suspend fun getCity(@Query("start_id") province : String) : LocationProperty

    @GET("api/get-faskes-vaksinasi")
    suspend fun getFaskes(@Query("province") province : String, @Query("city") city: String) : FaskesProperty

    @POST("/check-in")
    suspend fun getStatus(@Body requestBody: RequestBody): Response<ResponseBody>
}

object PerluDilindungiApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}

