@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.navigation.DetailScreenParams
import com.example.bookmarked_android.ui.components.RecentBookmarkItem
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Purple

@Composable
fun SharedTransitionScope.BookmarksScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    topPadding: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val bookmarksScope = remember {
        BookmarksScreenImpl(
            viewModel,
            navController,
            topPadding,
            animatedVisibilityScope,
            this
        )
    }

    bookmarksScope.BookmarksListContainer()
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BookmarksScreenImpl.BookmarksListContainer() {
    PullToRefreshBox(
        isRefreshing = uiState is BookmarkListUiState.Loading,
        onRefresh = this@BookmarksListContainer::onRefresh
    ) {
        when (uiState) {
            is BookmarkListUiState.Error -> Text(text = "Error")
            is BookmarkListUiState.Loading -> Text(text = "Loading...")
            is BookmarkListUiState.Success -> {
                this@BookmarksListContainer.BookmarkList(uiState, isLoadingMore.collectAsState().value)
            }
        }

    }
}

@Composable
private fun BookmarksScreenImpl.BookmarkList(
    successUiState: BookmarkListUiState.Success,
    isLoadingMore: Boolean = true,
) {
    val listState = rememberLazyListState()
    val buffer = 1
    val isReachedBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - buffer
        }
    }

    LaunchedEffect(isReachedBottom) {
        if (isReachedBottom) onLoadMore()
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = topPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            horizontal = HORIZONTAL_PADDING,
            vertical = BOTTOM_PADDING,
        )
    ) {
        items(successUiState.bookmarkList) { item ->
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

        if (isLoadingMore) {
            item {
                Box(Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        strokeWidth = 3.5.dp,
                        color = Purple,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}