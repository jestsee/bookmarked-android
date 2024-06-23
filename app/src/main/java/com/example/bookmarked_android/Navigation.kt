package com.example.bookmarked_android

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.screens.BookmarkListViewModel
import com.example.bookmarked_android.ui.screens.BookmarksScreen
import com.example.bookmarked_android.ui.screens.DetailScreen
import com.example.bookmarked_android.ui.screens.HomeScreen
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING

enum class Screen {
    HOME, BOOKMARK_LIST, BOOKMARK_DETAIL,
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object BookmarkList : NavigationItem(Screen.BOOKMARK_LIST.name)
    data object BookmarkDetail : NavigationItem(Screen.BOOKMARK_DETAIL.name + "/{bookmarkId}")
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
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
    var selectedIndex by rememberSaveable { mutableStateOf<Int>(0) }

    Scaffold(
        Modifier.nestedScroll(nestedScrollConnection),
        bottomBar = {
            BottomNavigationBar(
                showBottomBar.value,
                navController,
                selectedIndex
            ) { index -> selectedIndex = index }
        }) { innerPadding ->
        NavHost(
            modifier = modifier, navController = navController, startDestination = startDestination
        ) {
            composable(NavigationItem.Home.route) {
                HomeScreen(
                    navController = navController,
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = BOTTOM_PADDING,
                    viewModel = bookmarkListViewModel,
                )
            }
            composable(NavigationItem.BookmarkList.route) {
                BookmarksScreen(
                    navController = navController,
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = BOTTOM_PADDING,
                    viewModel = bookmarkListViewModel
                )
            }
            composable(NavigationItem.BookmarkDetail.route) { backStackEntry ->
                val bookmarkId = backStackEntry.arguments?.getString("bookmarkId")
                DetailScreen(
                    navController = navController,
                    topPadding = innerPadding.calculateTopPadding(),
                    bottomPadding = BOTTOM_PADDING,
                    pageId = bookmarkId!!
                )
            }
        }
    }
}