package com.example.perludilindungi.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perludilindungi.room.Bookmark
import com.example.perludilindungi.room.BookmarkDB
import kotlinx.coroutines.launch
import timber.log.Timber

class BookmarkViewModel : ViewModel() {

    fun getDatabaseBookmark(bookmarkDB: BookmarkDB) {
        viewModelScope.launch {
            try {
                val result = bookmarkDB.bookmarkDao().getBookmark()

                _response.value = result

            } catch (e: Exception) {
                Timber.i(e)
            }

        }
    }
    val response: LiveData<List<Bookmark>>
        get() = _response

    private val _response = MutableLiveData<List<Bookmark>>()
}