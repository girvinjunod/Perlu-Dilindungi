package com.example.perludilindungi.network
import com.squareup.moshi.Json

data class NewsProperty(
    val success: Boolean,
    val message: String,
    @Json(name = "count_total") val count: Int,
    val results : List<Results>?
)
data class Results(
    val title : String,
    val link: List<String>,
    val pubDate: String,
    @Json(name="enclosure") val image: Enclosure
)

data class Enclosure(
    @Json(name = "_url") val imageUrl : String,
    @Json(name = "_length") val length : String,
    @Json(name = "_type") val type : String
)