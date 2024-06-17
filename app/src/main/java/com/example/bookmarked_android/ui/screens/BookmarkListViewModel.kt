package com.example.bookmarked_android.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkList
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.launch

sealed interface BookmarkListUiState {
    data class Success(val bookmarkList: BookmarkList) : BookmarkListUiState
    object Error : BookmarkListUiState
    object Loading : BookmarkListUiState
}

class BookmarkListViewModel() : ViewModel() {
    var bookmarkListUiState: BookmarkListUiState by mutableStateOf(BookmarkListUiState.Loading)
        private set

    init {
        getBookmarks()
    }

    fun getBookmarks() {
        viewModelScope.launch {
            bookmarkListUiState = try {
                val config = Config()
                val listResult =
                    NotionApi.retrofitService.getBookmarks(
                        "Bearer ${config.notionSecret}",
                        config.databaseId
                    )
                BookmarkListUiState.Success(BookmarkList(items = listResult))
            } catch (e: Exception) {
                BookmarkListUiState.Error
            }
        }
    }
}