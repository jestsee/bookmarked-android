package com.example.bookmarked_android.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkList
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.launch

sealed interface BookmarkedUiState {
    data class Success(val bookmarkedList: BookmarkList) : BookmarkedUiState
    object Error : BookmarkedUiState
    object Loading : BookmarkedUiState
}

class BookmarkedViewModel() : ViewModel() {
    var bookmarkUiState: BookmarkedUiState by mutableStateOf(BookmarkedUiState.Loading)
        private set

    init {
        getBookmarks()
    }

    fun getBookmarks() {
        viewModelScope.launch {
            bookmarkUiState = try {
                val config = Config()
                val listResult =
                    NotionApi.retrofitService.getNotionData(
                        "Bearer ${config.notionSecret}",
                        config.databaseId
                    )
                BookmarkedUiState.Success(BookmarkList(items = listResult))
            } catch (e: Exception) {
                Log.d("BookmarkedViewModel", "getBookmarks: ${e}")
                BookmarkedUiState.Error
            }
        }
    }
}