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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.bookmarked_android.ui.screens.home.HomeScreen
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarkListViewModel
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarksScreen
import com.example.bookmarked_android.ui.screens.detail.DetailScreen
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.google.gson.Gson

enum class Screen {
    HOME, BOOKMARK_LIST, BOOKMARK_DETAIL, IMAGE_DETAIL
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object BookmarkList : NavigationItem(Screen.BOOKMARK_LIST.name)
    data object BookmarkDetail :
        NavigationItem(Screen.BOOKMARK_DETAIL.name + "/{bookmarkId}/{params}")

    data object ImageDetail :
        NavigationItem(Screen.IMAGE_DETAIL.name + "/{imageUrl}")
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
    val showBottomBar = remember { mutableStateOf(true) }

// connection to the nested scroll system and listen to the scroll
// happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.floatValue + delta
                bottomBarOffsetHeightPx.floatValue = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                showBottomBar.value = newOffset / 2 >= 0f

                return Offset.Zero
            }
        }
    }

    val bookmarkListViewModel: BookmarkListViewModel = viewModel()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    /**
     * Control bottom bar visibility
     */
    when (navBackStackEntry?.destination?.route) {
        NavigationItem.ImageDetail.route -> {
            showBottomBar.value = false
        }
        else -> {
            showBottomBar.value = true
        }
    }

    Scaffold(
        Modifier.nestedScroll(nestedScrollConnection),
        bottomBar = {
            BottomNavigationBar(
                showBottomBar.value,
                navController,
                selectedIndex
            ) { index -> selectedIndex = index }
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
                        bottomPadding = BOTTOM_PADDING,
                        viewModel = bookmarkListViewModel,
                        animatedVisibilityScope = this
                    )
                }
                composable(NavigationItem.BookmarkList.route) {
                    BookmarksScreen(
                        navController = navController,
                        topPadding = innerPadding.calculateTopPadding(),
                        bottomPadding = BOTTOM_PADDING,
                        viewModel = bookmarkListViewModel,
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
                        bottomPadding = BOTTOM_PADDING,
                        pageId = bookmarkId!!,
                        params = parsedParams,
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
            }
        }
    }
}