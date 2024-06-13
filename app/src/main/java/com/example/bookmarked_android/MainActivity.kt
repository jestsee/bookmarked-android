package com.example.bookmarked_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmarked_android.ui.screens.BookmarkedViewModel
import com.example.bookmarked_android.ui.screens.HomeScreen
import com.example.bookmarked_android.ui.theme.BookmarkedandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val bookmarkedViewModel: BookmarkedViewModel = viewModel()
            BookmarkedandroidTheme {
                HomeScreen(
                    bookmarkedUiState = bookmarkedViewModel.bookmarkUiState
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
