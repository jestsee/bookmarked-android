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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Home.route
) {
    val bottomBarHeight = 64.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val isScrollingUp = remember { mutableStateOf(false) }
    val shouldShowBottomBar = remember { mutableStateOf(true) }
    val showBottomBar = remember { mutableStateOf(true) }

// connection to the nested scroll system and listen to the scroll
// happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.floatValue + delta
                bottomBarOffsetHeightPx.floatValue = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                isScrollingUp.value = newOffset / 2 >= 0f

                if (shouldShowBottomBar.value) showBottomBar.value = isScrollingUp.value

                return Offset.Zero
            }
        }
    }

    val bookmarkListViewModel: BookmarkListViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    /**
     * Control bottom bar visibility
     */
    when (navBackStackEntry?.destination?.route) {
        NavigationItem.ImageDetail.route -> {
            shouldShowBottomBar.value = false
            showBottomBar.value = false
        }

        NavigationItem.BookmarkDetail.route -> {
            shouldShowBottomBar.value = false
            showBottomBar.value = false
        }

        else -> {
            shouldShowBottomBar.value = true
            showBottomBar.value = true
        }
    }

    Scaffold(
        Modifier.nestedScroll(nestedScrollConnection),
        bottomBar = {
            /**
             * TODO
             * if navBackStackEntry?.destination?.route == NavigationItem.BookmarkList.route
             * then show the search bar
             */
            BottomNavigationBar(
                showBottomBar.value,
                navController,
            )
        }) { innerPadding ->
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
                        topPadding = innerPadding.calculateTopPadding(),
                        viewModel = bookmarkListViewModel,
                        animatedVisibilityScope = this
                    )
                }
                composable(NavigationItem.BookmarkList.route) {
                    BookmarksScreen(
                        navController = navController,
                        topPadding = innerPadding.calculateTopPadding(),
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
                        topPadding = innerPadding.calculateTopPadding(),
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