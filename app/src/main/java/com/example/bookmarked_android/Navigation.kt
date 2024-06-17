package com.example.bookmarked_android

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookmarked_android.ui.screens.DetailScreen
import com.example.bookmarked_android.ui.screens.HomeScreen

enum class Screen {
    HOME,
    BOOKMARK_LIST,
    BOOKMARK_DETAIL,
}

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object BookmarkList : NavigationItem(Screen.BOOKMARK_LIST.name)
    object BookmarkDetail : NavigationItem(Screen.BOOKMARK_DETAIL.name + "/{bookmarkId}")
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(NavigationItem.BookmarkList.route) {
//        TODO
        }
        composable(NavigationItem.BookmarkDetail.route) { backStackEntry ->
            val bookmarkId = backStackEntry.arguments?.getString("bookmarkId")
            DetailScreen(navController, bookmarkId!!)
        }
    }
}