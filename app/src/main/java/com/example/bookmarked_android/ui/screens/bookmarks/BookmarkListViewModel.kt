package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

sealed interface BookmarkListErrorState {
    data class FetchError(val message: String) : BookmarkListErrorState
    data class FetchMoreError(val message: String) : BookmarkListErrorState
}

class BookmarkListViewModel() : ViewModel() {
    private val config = Config()

    /**
     * Bookmark list state
     */
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

    /**
     * Bookmark list filter
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetch()
        observeFilter()
    }

    @OptIn(FlowPreview::class)
    private fun observeFilter() {
        viewModelScope.launch {
            _searchQuery.debounce(timeoutMillis = 500).collectLatest {
                fetch()
            }
        }
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
                    startCursor = nextCursor.value,
                    search = _searchQuery.value
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

    fun fetch() {
        nextCursor.value = null
        fetchBookmarks(
            handleLoading = { _isLoading.value = it },
            handleError = { BookmarkListErrorState.FetchError(it) })
    }

    fun fetchMore() {
        if (isLoading.value || !hasMore.value) return
        fetchBookmarks(
            handleLoading = { _isLoadingMore.value = it },
            handleError = { BookmarkListErrorState.FetchMoreError(it) },
            handleData = { bookmarkList.value + it })
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
    }
}