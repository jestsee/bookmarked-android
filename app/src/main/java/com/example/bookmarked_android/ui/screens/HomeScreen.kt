package com.example.bookmarked_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.ui.components.BookmarkCarousel
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.components.RecentBookmarks

@Composable
fun HomeScreen(bookmarkedUiState: BookmarkedUiState) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, innerPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GreetingsText()
//            TODO: search bar
            when (bookmarkedUiState) {
                is BookmarkedUiState.Error -> Text(text = "Error")
                is BookmarkedUiState.Loading -> Text(text = "Loading...")
                is BookmarkedUiState.Success -> {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Spacer(modifier = Modifier.size(16.dp))
                        BookmarkCarousel(bookmarkedUiState.bookmarkedList.items.takeLast(3))
                        Spacer(modifier = Modifier.size(16.dp))
                        RecentBookmarks(bookmarkedUiState.bookmarkedList.items.take(3))
                    }
                }

            }
        }
    }
}

@Composable
fun GreetingsText() {
    Column(Modifier.padding(horizontal = 12.dp)) {
        Text(text = "Hello name", fontSize = 36.sp, fontWeight = FontWeight.Medium)
        Text(text = "You have bookmarked 140 tweets", fontSize = 16.sp)
    }
}

@Composable
fun ResultScreen(photos: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = photos)
    }
}
