package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.model.TextsContent
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

sealed interface BookmarkDetailUiState {
    data class Success(val details: List<BookmarkDetail>) : BookmarkDetailUiState
    data object Error : BookmarkDetailUiState
    data object Loading : BookmarkDetailUiState
}

class BookmarkDetailViewModel(pageId: String) : ViewModel() {
    var bookmarkDetailUiState: BookmarkDetailUiState by mutableStateOf(BookmarkDetailUiState.Loading)
        private set

    init {
        getBookmarkDetail(pageId)
    }

    private fun getBookmarkDetail(pageId: String) {
        viewModelScope.launch {
            bookmarkDetailUiState = try {
                val config = Config()
                val listResult =
                    NotionApi.retrofitService.getBookmarkDetail(
                        "Bearer ${config.notionSecret}",
                        pageId
                    ).toMutableList()

                /**
                 * Remove the title
                 */
                listResult[0] = listResult.first().let { item ->
                    item.copy(
                        contents = item.contents.mapIndexed { index, content ->
                            if (index == 0 && content is TextsContent) {
                                content.copy(texts = content.texts.drop(1))
                            } else {
                                content
                            }
                        }
                    )
                }

                BookmarkDetailUiState.Success(listResult.toImmutableList())
            } catch (e: Exception) {
                BookmarkDetailUiState.Error
            }
        }
    }
}

class BookmarkDetailViewModelFactory(private val pageId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarkDetailViewModel(pageId) as T
    }
}