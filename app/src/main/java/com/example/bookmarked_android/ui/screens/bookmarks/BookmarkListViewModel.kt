package com.example.bookmarked_android.ui.screens.bookmarks

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

class BookmarkListViewModel : ViewModel() {
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

    private val filterViewModel = MutableStateFlow<FilterViewModel?>(null)

    init {
        fetchBookmarks()
        observeQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(timeoutMillis = 1000).collectLatest {
                fetchBookmarks()
            }
        }
    }

    private fun fetchBookmarkList(
        handleLoading: (Boolean) -> Unit,
        handleError: (String) -> BookmarkListErrorState,
        handleData: (List<BookmarkItem>) -> List<BookmarkItem> = { it }
    ) {
        viewModelScope.launch {
            _error.value = null
            handleLoading(true)

            try {
                val filter = filterViewModel.value?.appliedFilter?.value

                val response = NotionApi.retrofitService.getBookmarks(
                    "Bearer ${config.notionSecret}",
                    config.databaseId,
                    startCursor = nextCursor.value,
                    search = _searchQuery.value,
                    type = filter?.type,
                    tags = filter?.tags
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

    fun fetchBookmarks() {
        nextCursor.value = null
        fetchBookmarkList(
            handleLoading = { _isLoading.value = it },
            handleError = { BookmarkListErrorState.FetchError(it) })
    }

    fun fetchMoreBookmarks() {
        if (isLoading.value || !hasMore.value) return
        fetchBookmarkList(
            handleLoading = { _isLoadingMore.value = it },
            handleError = { BookmarkListErrorState.FetchMoreError(it) },
            handleData = { bookmarkList.value + it })
    }

    private val pendingDeletions = mutableMapOf<String, Pair<Int, Job>>()

    fun deleteBookmark(pageId: String, snackbarHostState: SnackbarHostState) {
        val index = bookmarkList.value.indexOfFirst { it.id == pageId }
        val bookmark = bookmarkList.value.find { it.id == pageId }
        if (index == -1 || bookmark == null) return

        _bookmarkList.value = bookmarkList.value.filter { it.id != pageId }
        viewModelScope.launch {
            val result = snackbarHostState.showSnackbar(
                "Bookmark deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Indefinite
            )
            if (result == SnackbarResult.ActionPerformed) {
                return@launch undoDeleteBookmark(bookmark)
            }
        }

        val job =
            viewModelScope.launch {
                delay(3000)
                snackbarHostState.currentSnackbarData?.dismiss()
                try {
                    Log.d("TAG", "deleteBookmark: called")
                    NotionApi.retrofitService.deleteBookmark(
                        "Bearer ${config.notionSecret}",
                        pageId
                    )
                    pendingDeletions.remove(pageId)
                } catch (e: Exception) {
                    Log.d("ERROR", "deleteBookmark: $e")
                    undoDeleteBookmark(bookmark)
                    snackbarHostState.showSnackbar("Failed to delete bookmark")
                }
            }
        pendingDeletions[pageId] = Pair(index, job)
    }

    fun undoDeleteBookmark(bookmark: BookmarkItem) {
        pendingDeletions[bookmark.id]?.let { (index, job) ->
            job.cancel()
            pendingDeletions.remove(bookmark.id)
            _bookmarkList.value = bookmarkList.value.toMutableList().apply {
                add(index, bookmark)
            }
        }
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
    }

    fun injectFilterViewModel(
        typeViewModel: FilterViewModel,
    ) {
        filterViewModel.value = typeViewModel
    }
}