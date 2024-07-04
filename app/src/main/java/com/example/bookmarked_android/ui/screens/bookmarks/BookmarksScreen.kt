@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.isReachedTop
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.ui.components.RecentBookmarkItem
import com.example.bookmarked_android.ui.components.SearchBar
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Purple

@Composable
fun SharedTransitionScope.BookmarksScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    topPadding: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isScrollingUp: Boolean = false,
) {
    val bookmarksScope = remember {
        BookmarksScreenImpl(
            navController,
            viewModel,
            topPadding,
            animatedVisibilityScope,
            this
        )
    }

    bookmarksScope.BookmarksListContainer(isScrollingUp)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BookmarksScreenImpl.BookmarksListContainer(isScrollingUp: Boolean) {
    val bookmarkList by viewModel.bookmarkList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val error by viewModel.error.collectAsState()

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = viewModel::refresh
    ) {
        if (isLoading) return@PullToRefreshBox Text(text = "Loading...")

        if (error != null) return@PullToRefreshBox Text(text = "Error")

        this@BookmarksListContainer.BookmarkList(bookmarkList, isLoadingMore, isScrollingUp)
    }
}

internal fun LazyListState.isReachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}

@Composable
private fun BookmarksScreenImpl.BookmarkList(
    bookmarkList: List<BookmarkItem>,
    isLoadingMore: Boolean,
    isScrollingUp: Boolean,
    listState: LazyListState = rememberLazyListState(),
) {
    val isReachedBottom by remember { derivedStateOf { listState.isReachedBottom() } }
    LaunchedEffect(isReachedBottom) {
        if (isReachedBottom) viewModel.fetchMore()
    }

    Box(Modifier.padding(start = HORIZONTAL_PADDING, end = HORIZONTAL_PADDING)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = topPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = BOTTOM_PADDING)
        ) {
            item {
                Spacer(modifier = Modifier.height(84.dp))
            }

            bookmarkListComposable(bookmarkList, this@BookmarkList)

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

        AnimatedVisibility(
            visible = isScrollingUp || listState.isReachedTop(),
            enter = slideInVertically(initialOffsetY = { -500 }),
            exit = slideOutVertically(targetOffsetY = { -500 }),
        ) {
            SearchBar(Modifier.padding(top = topPadding + 20.dp))
        }
    }
}

private fun LazyListScope.bookmarkListComposable(
    bookmarkList: List<BookmarkItem>,
    bookmarksScreenImpl: BookmarksScreenImpl
) {
    items(bookmarkList) { item ->
        bookmarksScreenImpl.RecentBookmarkItem(
            item = item,
            animatedVisibilityScope = bookmarksScreenImpl.animatedVisibilityScope,
            shouldAnimate = true,
            modifier = Modifier.clickable {
                bookmarksScreenImpl.onNavigateToDetail(item)
            }
        )
    }
}