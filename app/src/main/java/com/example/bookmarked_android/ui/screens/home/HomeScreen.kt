@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.navigation.DetailScreenParams
import com.example.bookmarked_android.navigation.Screen
import com.example.bookmarked_android.navigation.toJson
import com.example.bookmarked_android.ui.components.RecentBookmarks
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarkListViewModel
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING

@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val bookmarkList by viewModel.bookmarkList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        GreetingsText()

        if (isLoading) return Text(text = "Loading...")
        if (error != null) return Text(text = "Error")


        Column(modifier = Modifier.fillMaxHeight()) {
//                    Spacer(modifier = Modifier.size(12.dp))
//                    BookmarkCarousel(bookmarkedUiState.bookmarkList.items.takeLast(3))
//                    Spacer(modifier = Modifier.size(16.dp))

            val onNavigateToDetail =
                { id: String, params: DetailScreenParams -> navController.navigate("${Screen.BOOKMARK_DETAIL.name}/$id/${params.toJson()}") }
            RecentBookmarks(
                bookmarkList.take(5),
                onNavigateToDetail,
                animatedVisibilityScope
            )
        }
        Spacer(modifier = Modifier.height(BOTTOM_PADDING))
    }
}

@Composable
fun GreetingsText() {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.padding(horizontal = HORIZONTAL_PADDING)) {
            Text(
                text = "Welcome back,",
                color = MaterialTheme.colorScheme.onBackground.copy(0.8f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Jesica",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}