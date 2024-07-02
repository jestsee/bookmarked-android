package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.navigation.DetailScreenParams
import com.example.bookmarked_android.navigation.Screen
import com.example.bookmarked_android.navigation.toJson

interface BookmarksScreenScope {
    fun onNavigateToDetail(item: BookmarkItem);
}

@OptIn(ExperimentalSharedTransitionApi::class)
class BookmarksScreenImpl(
    private val navController: NavController,
    val viewModel: BookmarkListViewModel,
    val topPadding: Dp,
    val animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : BookmarksScreenScope,
    SharedTransitionScope by sharedTransitionScope {

    override fun onNavigateToDetail(item: BookmarkItem) {
        val params = DetailScreenParams(
            item.id,
            item.title,
            item.tags,
            item.tweetUrl,
            item.notionUrl,
            item.publicUrl
        )
        navController.navigate("${Screen.BOOKMARK_DETAIL.name}/${item.id}/${params.toJson()}")
    }
}