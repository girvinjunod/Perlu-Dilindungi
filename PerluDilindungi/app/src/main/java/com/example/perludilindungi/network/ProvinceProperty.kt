package com.example.perludilindungi.network

data class LocationProperty(
    val curr_val: String,
    val message: String,
    val results : List<LocationResults>?
)

data class LocationResults(
    val key: String,
    val value: String
)

