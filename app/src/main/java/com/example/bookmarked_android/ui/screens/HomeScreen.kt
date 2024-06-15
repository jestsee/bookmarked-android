package com.example.bookmarked_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.components.RecentBookmarks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(bookmarkedUiState: BookmarkedUiState) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(12.dp, 40.dp, 12.dp, innerPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GreetingsText()
//            TODO: search bar
//            TODO: cards carousel
//            SectionTitle(title = "Featured")
//            Card(onClick = { /*TODO*/ }) {
//                Text(text = "ini card")
//            }
            Spacer(modifier = Modifier.size(16.dp))
            when (bookmarkedUiState) {
                is BookmarkedUiState.Error -> Text(text = "Error")
                is BookmarkedUiState.Loading -> Text(text = "Loading...")
                is BookmarkedUiState.Success -> {
                    RecentBookmarks(bookmarkedUiState.bookmarkedList.items.take(5))
                }

            }
        }
    }
}

@Composable
fun GreetingsText() {
    Column {
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
