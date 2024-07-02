package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BookmarkListErrorState {
    data class FetchError(val message: String) : BookmarkListErrorState
    data class FetchMoreError(val message: String) : BookmarkListErrorState
}

class BookmarkListViewModel() : ViewModel() {
    private val config = Config()

    private val _bookmarkList = MutableStateFlow<List<BookmarkItem>>(emptyList())
    val bookmarkList: StateFlow<List<BookmarkItem>> = _bookmarkList.asStateFlow()

    private val _error = MutableStateFlow<BookmarkListErrorState?>(null)
    val error: StateFlow<BookmarkListErrorState?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow<Boolean>(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val nextCursor = MutableStateFlow<String?>(null)

    init {
        refresh()
    }

    private fun fetchBookmarks(
        handleLoading: (Boolean) -> Unit,
        handleError: (String) -> BookmarkListErrorState,
        handleData: (List<BookmarkItem>) -> List<BookmarkItem> = { it }
    ) {
        viewModelScope.launch {
            _error.value = null
            handleLoading(true)
            try {
                val response = NotionApi.retrofitService.getBookmarks(
                    "Bearer ${config.notionSecret}",
                    config.databaseId,
                    startCursor = nextCursor.value
                )
                _hasMore.value = response.hasMore
                nextCursor.value = response.nextCursor

                _bookmarkList.value = handleData(response.results)
            } catch (e: Exception) {
                _error.value = handleError(e.message ?: "Unknown error")
            }
            handleLoading(false)
        }
    }

    fun refresh() {
        nextCursor.value = null
        fetchBookmarks(
            handleLoading = { _isLoading.value = it },
            handleError = { BookmarkListErrorState.FetchError(it) })
    }

    fun fetchMore() {
        fetchBookmarks(
            handleLoading = { _isLoadingMore.value = it },
            handleError = { BookmarkListErrorState.FetchMoreError(it) },
            handleData = { bookmarkList.value + it })
    }
}