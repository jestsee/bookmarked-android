package com.example.bookmarked_android

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.screens.DetailScreen
import com.example.bookmarked_android.ui.screens.HomeScreen

enum class Screen {
    HOME, BOOKMARK_LIST, BOOKMARK_DETAIL,
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
    Scaffold(bottomBar = { BottomNavigationBar() }) { innerPadding ->
        NavHost(
            modifier = modifier, navController = navController, startDestination = startDestination
        ) {
            composable(NavigationItem.Home.route) {
                HomeScreen(
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = innerPadding.calculateBottomPadding(),
                    navController = navController
                )
            }
            composable(NavigationItem.BookmarkList.route) {
                //        TODO
            }
            composable(NavigationItem.BookmarkDetail.route) { backStackEntry ->
                val bookmarkId = backStackEntry.arguments?.getString("bookmarkId")
                DetailScreen(
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = innerPadding.calculateBottomPadding(),
                    navController = navController,
                    pageId = bookmarkId!!
                )
            }
        }
    }
}