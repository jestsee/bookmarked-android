package com.example.bookmarked_android.ui.components.app_bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookmarked_android.navigation.NavigationItem

sealed class BarContent {
    data object Hidden : BarContent()
    data object ShowBar : BarContent()
    data class CustomBar(val content: @Composable () -> Unit) : BarContent()
}

class BottomBarViewModel: ViewModel() {
    var bottomBarContent by mutableStateOf<BarContent>(BarContent.ShowBar)
        private set

    private fun showBottomBar() {
        bottomBarContent = BarContent.ShowBar
    }

    private fun hideBottomBar() {
        bottomBarContent = BarContent.Hidden
    }

    fun adjustBottomBarByRoute(route: String?) {
        when (route) {
            NavigationItem.BookmarkDetail.route -> hideBottomBar()
            NavigationItem.ImageDetail.route -> hideBottomBar()
            else -> showBottomBar()
        }
    }
}