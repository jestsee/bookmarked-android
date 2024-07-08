package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookmarked_android.R
import com.example.bookmarked_android.data.BottomNavigationItem
import com.example.bookmarked_android.navigation.Screen
import com.example.bookmarked_android.ui.components.bottomNavigationBar.CustomBottomAppBar
import com.example.bookmarked_android.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavController,
    scrollBehavior: BottomAppBarScrollBehavior
) {
    val items = listOf(
        BottomNavigationItem(title = "Home", iconId = R.drawable.icon_home, Screen.HOME.name),
        BottomNavigationItem(
            title = "Bookmarks",
            iconId = R.drawable.icon_bookmark,
            Screen.BOOKMARK_LIST.name
        ),
        BottomNavigationItem(
            title = "Profile",
            iconId = R.drawable.icon_profile,
            Screen.PROFILE.name
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    CustomBottomAppBar(scrollBehavior = scrollBehavior) {
        items.forEach { item ->
            CustomNavigationItem(item = item, selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

    }
}


@Composable
private fun CustomNavigationItem(
    item: BottomNavigationItem,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier =
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .then(if (selected) Modifier.background(Primary) else Modifier)
            .clickable(onClick = onClick)
    )
    {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp),
            painter = painterResource(id = item.iconId),
            contentDescription = item.title,
            tint = MaterialTheme.colorScheme.onSurface
//                        tint = if (selectedIndex == index) Primary else MaterialTheme.colorScheme.onSurface
        )
    }
}