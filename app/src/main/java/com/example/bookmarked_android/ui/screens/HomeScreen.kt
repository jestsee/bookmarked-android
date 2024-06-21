package com.example.bookmarked_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookmarked_android.Screen
import com.example.bookmarked_android.ui.components.BookmarkCarousel
import com.example.bookmarked_android.ui.components.RecentBookmarks

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: BookmarkListViewModel = viewModel(),
    topPadding: Dp,
    bottomPadding: Dp
) {
    val bookmarkedUiState = viewModel.bookmarkListUiState

    Column(
        modifier = Modifier
            .padding(top = topPadding)
            .verticalScroll(rememberScrollState())
            .padding(bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        GreetingsText()
//            TODO: search bar
        when (bookmarkedUiState) {
            is BookmarkListUiState.Error -> Text(text = "Error")
            is BookmarkListUiState.Loading -> Text(text = "Loading...")
            is BookmarkListUiState.Success -> {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Spacer(modifier = Modifier.size(12.dp))
                    BookmarkCarousel(bookmarkedUiState.bookmarkList.items.takeLast(3))
                    Spacer(modifier = Modifier.size(16.dp))

                    val onNavigateToDetail =
                        { id: String -> navController.navigate("${Screen.BOOKMARK_DETAIL.name}/$id") }
                    RecentBookmarks(
                        bookmarkedUiState.bookmarkList.items.take(5),
                        onNavigateToDetail,
                    )
                }
            }

        }
    }
}

@Composable
fun GreetingsText() {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = "Welcome back,",
                color = MaterialTheme.colorScheme.onBackground.copy(0.9f)
            )
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            )
            Text(text = "Olivia Ong!", fontSize = 40.sp, fontWeight = FontWeight.Medium)
        }
    }
}