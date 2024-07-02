package com.example.bookmarked_android.ui.screens.bookmarks

import android.util.Log
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
    data class Success(val bookmarkList: List<BookmarkItem>, val isLoadingMore: Boolean = false) :
        BookmarkListUiState

    object Error : BookmarkListUiState
    object Loading : BookmarkListUiState
}

class BookmarkListViewModel : ViewModel() {
    private val config = Config()

    private val _bookmarkListUiState =
        MutableStateFlow<BookmarkListUiState>(BookmarkListUiState.Loading)
    val bookmarkListUiState: StateFlow<BookmarkListUiState> = _bookmarkListUiState.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val nextCursor = MutableStateFlow<String?>(null)

    init {
        getBookmarks()
    }

    fun getBookmarks() {
        viewModelScope.launch {
            _bookmarkListUiState.value = BookmarkListUiState.Loading
            _bookmarkListUiState.value = try {
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
            _bookmarkListUiState.value = BookmarkListUiState.Success(
                (bookmarkListUiState.value as BookmarkListUiState.Success).bookmarkList,
                isLoadingMore = true
            )

            _bookmarkListUiState.value = try {
                val response = NotionApi.retrofitService.getBookmarks(
                    "Bearer ${config.notionSecret}",
                    config.databaseId,
                    startCursor = nextCursor.value
                )
                _hasMore.value = response.hasMore
                nextCursor.value = response.nextCursor

                BookmarkListUiState.Success((bookmarkListUiState.value as BookmarkListUiState.Success).bookmarkList + response.results)
            } catch (e: Exception) {
                // TODO: display snackbar
                throw e
            }
        }
    }
}