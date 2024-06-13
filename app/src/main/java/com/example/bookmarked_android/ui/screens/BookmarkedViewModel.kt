package com.example.bookmarked_android.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.network.BookmarkApi
import kotlinx.coroutines.launch

class BookmarkedViewModel() : ViewModel() {
    var bookmarkUiState: String by mutableStateOf("")
        private set

    init {
        getBookmarks()
    }

    fun getBookmarks() {
        viewModelScope.launch {
            try {
                val config = Config()
                val listResult =
                    BookmarkApi.retrofitService.getBookmarks(
                        "Bearer ${config.notionSecret}",
                        config.databaseId
                    )
                bookmarkUiState = listResult
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}