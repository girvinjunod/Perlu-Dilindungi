package com.example.perludilindungi.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["kodeFaskes"], unique = true)])
data class Bookmark (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val namaFaskes: String,
    val alamatFaskes: String,
    val jenisFaskes: String,
    val telpFaskes: String,
    val kodeFaskes: String,
    val statusFaskes: String
)