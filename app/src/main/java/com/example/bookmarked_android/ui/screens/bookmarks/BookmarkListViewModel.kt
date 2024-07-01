package com.example.bookmarked_android.ui.screens.bookmarks

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BookmarkListUiState {
    data class Success(val bookmarkList: List<BookmarkItem>) : BookmarkListUiState
    object Error : BookmarkListUiState
    object Loading : BookmarkListUiState
}

class BookmarkListViewModel() : ViewModel() {
    var bookmarkListUiState: BookmarkListUiState by mutableStateOf(BookmarkListUiState.Loading)
        private set

    private val config = Config()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val nextCursor = MutableStateFlow<String?>(null)

    init {
        getBookmarks()
    }

    fun getBookmarks() {
        viewModelScope.launch {
            bookmarkListUiState = BookmarkListUiState.Loading
            bookmarkListUiState = try {
                val response =
                    NotionApi.retrofitService.getBookmarks(
                        "Bearer ${config.notionSecret}",
                        config.databaseId
                    )

                _hasMore.value = response.hasMore
                nextCursor.value = response.nextCursor

                BookmarkListUiState.Success(response.results)
            } catch (e: Exception) {
                Log.d("TAG", "getBookmarks: $e")
                BookmarkListUiState.Error
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            _isLoadingMore.value = true
            bookmarkListUiState = try {
                val response = NotionApi.retrofitService.getBookmarks(
                    "Bearer ${config.notionSecret}",
                    config.databaseId,
                    startCursor = nextCursor.value
                )

                BookmarkListUiState.Success((bookmarkListUiState as BookmarkListUiState.Success).bookmarkList + response.results)
            } catch (e: Exception) {
                // TODO: display snackbar
                throw e
            }
            _isLoadingMore.value = false
        }
    }
}