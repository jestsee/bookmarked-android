@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.ui.components.RecentBookmarkItem
import com.example.bookmarked_android.ui.components.ScrollToTop
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Purple
import com.example.bookmarked_android.utils.isReachedTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SharedTransitionScope.BookmarksScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val bookmarksScope = remember {
        BookmarksScreenImpl(
            navController,
            viewModel,
            animatedVisibilityScope,
            this
        )
    }

    bookmarksScope.BookmarksListContainer()
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BookmarksScreenImpl.BookmarksListContainer() {
    val bookmarkList by viewModel.bookmarkList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val error by viewModel.error.collectAsState()

    PullToRefreshBox(
        isRefreshing = isLoading, onRefresh = viewModel::fetchBookmarks
    ) {
        if (error != null) return@PullToRefreshBox Text(text = "Error")

        this@BookmarksListContainer.BookmarkList(
            bookmarkList, isLoading, isLoadingMore
        )
    }
}

internal fun LazyListState.isReachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}

@Composable
private fun BookmarksScreenImpl.BookmarkList(
    bookmarkList: List<BookmarkItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    listState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    tagsViewModel: FilterTagsViewModel = viewModel(),
    typeViewModel: FilterTypeViewModel = viewModel(),
) {
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val isReachedBottom by remember { derivedStateOf { listState.isReachedBottom() } }
    val hasMoreData = viewModel.hasMore.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.injectFilterViewModel(typeViewModel, tagsViewModel)
    }

    LaunchedEffect(isReachedBottom, hasMoreData) {
        if (isReachedBottom && hasMoreData) viewModel.fetchMoreBookmarks()
    }

    Box(Modifier.padding(horizontal = HORIZONTAL_PADDING)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = BOTTOM_PADDING)
        ) {
//            stickyHeader {
//                AnimatedVisibility(
//                    visible = isScrollingUp || listState.isReachedTop(),
//                    enter = slideInVertically(initialOffssetY = { -500 }),
//                    exit = slideOutVertically(targetOffsetY = { -500 }),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .height(84.dp)
//                            .padding(top = 12.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        SearchBar(value = viewModel.searchQuery.collectAsState().value,
//                            onChange = viewModel::setSearch,
//                            onClear = { viewModel.setSearch("") },
//                            trailing = {
//                                IconButton(onClick = { showFilterBottomSheet = true }) {
//                                    Icon(
//                                        modifier = Modifier.size(28.dp),
//                                        painter = painterResource(id = R.drawable.icon_filter),
//                                        contentDescription = "Filter"
//                                    )
//                                }
//                            })
//                    }
//                }
//            }

            if (isLoading) item { Text("Loading...") }

            if (!isLoading) bookmarkListComposable(bookmarkList, this@BookmarkList)

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

        ScrollToTop(modifier = Modifier.align(Alignment.BottomEnd),
            buttonModifier = Modifier.padding(bottom = BOTTOM_PADDING * 1.5f),
            visible = !listState.isScrollInProgress && !listState.isReachedTop(),
            onClick = {
                coroutineScope.launch {
                    listState.scrollToItem(index = 0)
                }
            })
//
//        AnimatedVisibility(
//            visible = isScrollingUp || listState.isReachedTop(),
//            enter = slideInVertically(initialOffsetY = { -500 }),
//            exit = slideOutVertically(targetOffsetY = { -500 }),
//        ) {
//            Box(
//                modifier = Modifier
//                    .height(84.dp)
//                    .padding(top = 12.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                SearchBar(value = viewModel.searchQuery.collectAsState().value,
//                    onChange = viewModel::setSearch,
//                    onClear = { viewModel.setSearch("") },
//                    trailing = {
//                        IconButton(onClick = { showFilterBottomSheet = true }) {
//                            Icon(
//                                modifier = Modifier.size(28.dp),
//                                painter = painterResource(id = R.drawable.icon_filter),
//                                contentDescription = "Filter"
//                            )
//                        }
//                    })
//            }
//        }

    }
    if (showFilterBottomSheet) {
        FilterBottomSheet(
            tagsViewModel = tagsViewModel,
            filterTypeViewModel = typeViewModel,
            onDismissRequest = { showFilterBottomSheet = false },
            onApply = {
                viewModel.fetchBookmarks()
                showFilterBottomSheet = false
            }
        )
    }
}

private fun LazyListScope.bookmarkListComposable(
    bookmarkList: List<BookmarkItem>, bookmarksScreenImpl: BookmarksScreenImpl
) {
    items(bookmarkList) { item ->
        bookmarksScreenImpl.RecentBookmarkItem(item = item,
            animatedVisibilityScope = bookmarksScreenImpl.animatedVisibilityScope,
            shouldAnimate = true,
            modifier = Modifier.clickable {
                bookmarksScreenImpl.onNavigateToDetail(item)
            })
    }
}