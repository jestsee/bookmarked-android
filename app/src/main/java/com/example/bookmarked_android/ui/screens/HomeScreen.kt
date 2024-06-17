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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                .padding(0.dp, 48.dp, 0.dp, innerPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GreetingsText()
//            TODO: search bar
            when (bookmarkedUiState) {
                is BookmarkedUiState.Error -> Text(text = "Error")
                is BookmarkedUiState.Loading -> Text(text = "Loading...")
                is BookmarkedUiState.Success -> {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Spacer(modifier = Modifier.size(12.dp))
                        BookmarkCarousel(bookmarkedUiState.bookmarkedList.items.takeLast(3))
                        Spacer(modifier = Modifier.size(16.dp))
                        RecentBookmarks(bookmarkedUiState.bookmarkedList.items.take(5))
                    }
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
            Spacer(modifier = Modifier
                .height(2.dp)
                .fillMaxWidth())
            Text(text = "Olivia Ong!", fontSize = 40.sp, fontWeight = FontWeight.Medium)
        }
    }
}