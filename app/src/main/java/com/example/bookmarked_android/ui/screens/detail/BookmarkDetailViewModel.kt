package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.launch

sealed interface BookmarkDetailUiState {
    data class Success(val details: List<BookmarkDetail>) : BookmarkDetailUiState
    object Error : BookmarkDetailUiState
    object Loading : BookmarkDetailUiState
}

class BookmarkDetailViewModel(pageId: String) : ViewModel() {
    var bookmarkDetailUiState: BookmarkDetailUiState by mutableStateOf(BookmarkDetailUiState.Loading)
        private set

    init {
        getBookmarkDetail(pageId)
    }

    fun getBookmarkDetail(pageId: String) {
        viewModelScope.launch {
            bookmarkDetailUiState = try {
                val config = Config()
                val listResult =
                    NotionApi.retrofitService.getBookmarkDetail(
                        "Bearer ${config.notionSecret}",
                        pageId
                    )
                BookmarkDetailUiState.Success(listResult)
            } catch (e: Exception) {
                BookmarkDetailUiState.Error
            }
        }
    }
}

class BookmarkDetailViewModelFactory constructor(val pageId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarkDetailViewModel(pageId) as T
    }
}