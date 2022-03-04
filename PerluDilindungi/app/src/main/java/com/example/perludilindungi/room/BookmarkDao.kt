package com.example.perludilindungi.room

import androidx.room.*

@Dao
interface BookmarkDao {
    @Insert
    suspend fun addBookmark(bookmark: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmark")
    fun getBookmark(): List<Bookmark>

    @Query("SELECT * FROM bookmark WHERE kodeFaskes IN (:bookmarkID)")
    fun checkBookmark(bookmarkID : String): Bookmark
}