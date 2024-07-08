package com.example.bookmarked_android.ui.components.bottom_navigation_bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookmarked_android.navigation.NavigationItem

sealed class BottomBarContent {
    data object Hidden : BottomBarContent()
    data object BottomNavBar : BottomBarContent()
    data class CustomBar(val content: @Composable () -> Unit) : BottomBarContent()
}

class BottomBarViewModel: ViewModel() {
    var bottomBarContent by mutableStateOf<BottomBarContent>(BottomBarContent.BottomNavBar)
        private set

    private fun showBottomBar() {
        bottomBarContent = BottomBarContent.BottomNavBar
    }

    private fun hideBottomBar() {
        bottomBarContent = BottomBarContent.Hidden
    }

    fun adjustBottomBarByRoute(route: String?) {
        when (route) {
            NavigationItem.BookmarkDetail.route -> hideBottomBar()
            NavigationItem.ImageDetail.route -> hideBottomBar()
            else -> showBottomBar()
        }
    }

}