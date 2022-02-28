package com.example.perludilindungi.network

data class FaskesProperty(
    val success: Boolean,
    val message: String,
    val count_total: Int,
    val data : List<FaskesResults>?
)

data class FaskesResults(
    val kode : String?,
    val nama : String?,
    val kota : String?,
    val provinsi : String?,
    val alamat : String?,
    val latitude: String,
    val longitude : String,
    val telp : String?,
    val jenis_faskes : String?,
    val kelas_rs: String?,
    val status: String,
)

