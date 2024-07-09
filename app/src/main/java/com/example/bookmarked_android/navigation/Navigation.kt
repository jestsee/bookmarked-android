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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookmarked_android.ui.components.ImageDialog
import com.example.bookmarked_android.ui.components.app_bar.BarContent
import com.example.bookmarked_android.ui.components.app_bar.BottomBarViewModel
import com.example.bookmarked_android.ui.components.app_bar.BottomNavigationBar
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarkListViewModel
import com.example.bookmarked_android.ui.screens.bookmarks.BookmarksScreen
import com.example.bookmarked_android.ui.screens.detail.DetailScreen
import com.example.bookmarked_android.ui.screens.home.HomeScreen
import com.google.gson.Gson

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Home.route
) {
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    /**
     * View model
     */
    val bookmarkListViewModel: BookmarkListViewModel = viewModel()
    val bottomBarViewModel: BottomBarViewModel = viewModel()

    /**
     * Adjust bottom bar visibility
     */
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        bottomBarViewModel.adjustBottomBarByRoute(navBackStackEntry?.destination?.route)
    }

    Scaffold(
        Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
//            .then(
//                if (topBarViewModel.topBarContent is BarContent.CustomBar)
//                    Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection) else Modifier
//            ),
//        topBar = {
//            when (val topBarContent = topBarViewModel.topBarContent) {
//                is BarContent.CustomBar -> {
//                    CustomTopAppBar(scrollBehavior = topBarScrollBehavior, content = {
//                        topBarContent.content()
//                    })
//                }
//
//                else -> {}
//            }
//        }
        ,
        bottomBar = {
            when (val bottomBarContent = bottomBarViewModel.bottomBarContent) {
                is BarContent.ShowBar -> {
                    BottomNavigationBar(navController, scrollBehavior)
                }

                is BarContent.CustomBar -> {
                    bottomBarContent.content()
                }

                BarContent.Hidden -> {}
            }
        }
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
                        animatedVisibilityScope = this,
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