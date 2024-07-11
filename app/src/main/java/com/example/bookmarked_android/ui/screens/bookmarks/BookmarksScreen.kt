@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.bookmarks

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.R
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.ui.components.RecentBookmarkItem
import com.example.bookmarked_android.ui.components.ScrollToTop
import com.example.bookmarked_android.ui.components.SearchBar
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Primary
import com.example.bookmarked_android.ui.theme.Purple
import com.example.bookmarked_android.utils.isReachedTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class)
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

@SuppressLint("AutoboxingStateCreation")
@Composable
private fun BookmarksScreenImpl.BookmarkList(
    bookmarkList: List<BookmarkItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    listState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    tagsViewModel: FilterTagsViewModel = viewModel(),
) {
    val filterViewModel: FilterViewModel =
        viewModel(factory = remember { FilterViewModelFactory(tagsViewModel) })

    var showFilterBottomSheet by remember { mutableStateOf(false) }
    var showTagsFilterBottomSheet by remember { mutableStateOf(false) }
    val isReachedBottom by remember { derivedStateOf { listState.isReachedBottom() } }
    val hasMoreData = viewModel.hasMore.collectAsState().value

    val toolbarHeight = 96.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val focusManager = LocalFocusManager.current

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta

                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                focusManager.clearFocus()

                return Offset.Zero
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.injectFilterViewModel(filterViewModel)
    }

    LaunchedEffect(isReachedBottom, hasMoreData) {
        if (isReachedBottom && hasMoreData) viewModel.fetchMoreBookmarks()
    }

    // Determine if the LazyColumn is scrollable
    val isScrollable by remember {
        derivedStateOf {
            listState.canScrollForward || listState.canScrollBackward
        }
    }

    Box(modifier = Modifier.then(if (isScrollable) Modifier.nestedScroll(nestedScrollConnection) else Modifier)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = toolbarHeight,
                bottom = BOTTOM_PADDING,
                start = HORIZONTAL_PADDING,
                end = HORIZONTAL_PADDING
            )
        ) {
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
            buttonModifier = Modifier.padding(
                bottom = BOTTOM_PADDING * 1.5f,
                end = HORIZONTAL_PADDING
            ),
            visible = !listState.isScrollInProgress && !listState.isReachedTop(),
            onClick = {
                coroutineScope.launch {
                    toolbarOffsetHeightPx.value = 0f
                    listState.scrollToItem(index = 0)
                }
            })

        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }
                .padding(
                    top = BOTTOM_PADDING,
                    start = HORIZONTAL_PADDING,
                    end = HORIZONTAL_PADDING
                ),
            contentAlignment = Alignment.Center
        ) {
            val appliedFilter by filterViewModel.appliedFilter.collectAsState()
            val searchValue by viewModel.searchQuery.collectAsState()

            SearchBar(value = searchValue,
                onChange = viewModel::setSearch,
                onClear = { viewModel.setSearch("") },
                trailing = {
                    val appliedFilterCounter = appliedFilter.count()
                    BadgedBox(badge = {
                        if (appliedFilterCounter > 0) {
                            Badge(
                                containerColor = Primary,
                                contentColor = Color.White
                            ) {
                                Text(appliedFilterCounter.toString())
                            }
                        }
                    }) {
                        IconButton(onClick = { showFilterBottomSheet = true }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(id = R.drawable.icon_filter),
                                contentDescription = "Filter"
                            )
                        }
                    }
                })
        }


    }
    if (showFilterBottomSheet) {
        FilterBottomSheet(
            viewModel = filterViewModel,
            onDismissRequest = { showFilterBottomSheet = false },
            onApply = {
                viewModel.fetchBookmarks()
                showFilterBottomSheet = false
            },
            onClickAddTag = {
                showTagsFilterBottomSheet = true
            }
        )
    }

    if (showTagsFilterBottomSheet) {
        TagsFilterBottomSheet(tagViewModel = filterViewModel.tagViewModel, onDismissRequest = {
            showTagsFilterBottomSheet = false
        })
    }
}

private fun LazyListScope.bookmarkListComposable(
    bookmarkList: List<BookmarkItem>, bookmarksScreenImpl: BookmarksScreenImpl
) {
    items(bookmarkList, key = { it.id }) { item ->
        bookmarksScreenImpl.RecentBookmarkItem(item = item,
            animatedVisibilityScope = bookmarksScreenImpl.animatedVisibilityScope,
            shouldAnimate = true,
            modifier = Modifier.clickable {
                bookmarksScreenImpl.onNavigateToDetail(item)
            })
    }
}