@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.navigation.DetailScreenParams
import com.example.bookmarked_android.navigation.Screen
import com.example.bookmarked_android.navigation.toJson
import com.example.bookmarked_android.ui.components.RecentBookmarkItem
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.BookmarksScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    topPadding: Dp,
    bottomPadding: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val bookmarkedUiState = viewModel.bookmarkListUiState

    val refreshScope = rememberCoroutineScope()
    fun refresh() = refreshScope.launch {
        viewModel.getBookmarks()
    }

    PullToRefreshBox(
        isRefreshing = bookmarkedUiState is BookmarkListUiState.Loading,
        onRefresh = { viewModel.getBookmarks() }
    ) {
        when (bookmarkedUiState) {
            is BookmarkListUiState.Error -> Text(text = "Error")
            is BookmarkListUiState.Loading -> Text(text = "Loading...")
            is BookmarkListUiState.Success -> {
                BookmarkList(
                    topPadding,
                    bottomPadding,
                    bookmarkedUiState,
                    navController,
                    animatedVisibilityScope
                )
            }
        }

    }
}

@Composable
private fun SharedTransitionScope.BookmarkList(
    topPadding: Dp,
    bottomPadding: Dp,
    bookmarkedUiState: BookmarkListUiState.Success,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = topPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            horizontal = HORIZONTAL_PADDING,
            vertical = bottomPadding,
        )
    ) {

        items(bookmarkedUiState.bookmarkList.items) { item ->
            val onNavigateToDetail =
                { id: String, params: DetailScreenParams -> navController.navigate("${Screen.BOOKMARK_DETAIL.name}/$id/${params.toJson()}") }
            RecentBookmarkItem(
                item = item,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onNavigateToDetail(
                                item.id,
                                DetailScreenParams(
                                    item.id,
                                    item.title,
                                    item.tags,
                                    item.tweetUrl,
                                    item.notionUrl,
                                    item.publicUrl
                                )
                            )
                        }
                    )
                })
        }
    }
}