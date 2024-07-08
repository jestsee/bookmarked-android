package com.example.bookmarked_android.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.components.ImageDialog
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarkListViewModel
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarksScreen
import com.example.bookmarked_android.ui.screens.detail.DetailScreen
import com.example.bookmarked_android.ui.screens.home.HomeScreen
import com.google.gson.Gson

enum class Screen {
    HOME, BOOKMARK_LIST, BOOKMARK_DETAIL, IMAGE_DETAIL, PROFILE
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object BookmarkList : NavigationItem(Screen.BOOKMARK_LIST.name)
    data object BookmarkDetail :
        NavigationItem(Screen.BOOKMARK_DETAIL.name + "/{bookmarkId}/{params}")

    data object ImageDetail :
        NavigationItem(Screen.IMAGE_DETAIL.name + "/{imageUrl}")

    data object Profile : NavigationItem(Screen.PROFILE.name)
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Home.route
) {
    val isScrollingUp = remember { mutableStateOf(false) } // TODO delete later
    val showBottomBar = remember { mutableStateOf(true) }

    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val bookmarkListViewModel: BookmarkListViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    /**
     * Control bottom bar visibility
     */
    when (navBackStackEntry?.destination?.route) {
        NavigationItem.ImageDetail.route -> {
            showBottomBar.value = false
        }

        NavigationItem.BookmarkDetail.route -> {
            showBottomBar.value = false
        }

        else -> {
            showBottomBar.value = true
        }
    }

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = { if (showBottomBar.value) BottomNavigationBar(navController, scrollBehavior) }
    ) { _ ->
        SharedTransitionLayout {
            NavHost(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                startDestination = startDestination,
                enterTransition = { slideInHorizontally { it } + fadeIn() },
                exitTransition = { slideOutHorizontally { -it } + fadeOut() },
                popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
                popExitTransition = { slideOutHorizontally { it } + fadeOut() },
            ) {
                composable(NavigationItem.Home.route) {
                    HomeScreen(
                        navController = navController,
                        viewModel = bookmarkListViewModel,
                        animatedVisibilityScope = this
                    )
                }
                composable(NavigationItem.BookmarkList.route) {
                    BookmarksScreen(
                        navController = navController,
                        viewModel = bookmarkListViewModel,
                        isScrollingUp = isScrollingUp.value,
                        animatedVisibilityScope = this
                    )
                }
                composable(NavigationItem.BookmarkDetail.route) { backStackEntry ->
                    val bookmarkId = backStackEntry.arguments?.getString("bookmarkId")
                    val params = backStackEntry.arguments?.getString("params")

                    val parsedParams = Gson().fromJson(params, DetailScreenParams::class.java)

                    DetailScreen(
                        navController = navController,
                        pageId = bookmarkId!!,
                        params = parsedParams,
                        isScrollingUp = isScrollingUp.value,
                        animatedVisibilityScope = this
                    )
                }
                composable(
                    NavigationItem.ImageDetail.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }) {
                    val imageUrl = it.arguments?.getString("imageUrl")

                    ImageDialog(url = imageUrl!!, animatedVisibilityScope = this)
                }
                composable(
                    NavigationItem.Profile.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }) {

                    Text("TODO")
                }
            }
        }
    }
}