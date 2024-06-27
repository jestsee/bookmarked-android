package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.Screen
import com.example.bookmarked_android.toJson
import com.example.bookmarked_android.ui.components.RecentBookmarkItem
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING

@Composable
fun BookmarksScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    topPadding: Dp,
    bottomPadding: Dp
) {
    when (val bookmarkedUiState = viewModel.bookmarkListUiState) {
        is BookmarkListUiState.Error -> Text(text = "Error")
        is BookmarkListUiState.Loading -> Text(text = "Loading...")
        is BookmarkListUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(HORIZONTAL_PADDING, topPadding, HORIZONTAL_PADDING, bottomPadding)
            ) {

                items(bookmarkedUiState.bookmarkList.items) { item ->
                    val onNavigateToDetail =
                        { id: String, tags: String -> navController.navigate("${Screen.BOOKMARK_DETAIL.name}/$id/$tags") }
                    RecentBookmarkItem(item = item, modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onNavigateToDetail(item.id, item.tags.toJson()) }
                        )
                    })
                }
            }
        }

    }
}