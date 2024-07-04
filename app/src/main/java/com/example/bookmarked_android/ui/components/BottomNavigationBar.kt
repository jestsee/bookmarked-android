package com.example.bookmarked_android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.bookmarked_android.ui.theme.Primary

@Composable
fun BottomNavigationBar(
    visible: Boolean,
    navController: NavController,
) {
    val items = listOf(
        BottomNavigationItem(title = "Home", iconId = R.drawable.icon_home, Screen.HOME.name),
        BottomNavigationItem(
            title = "Bookmarks",
            iconId = R.drawable.icon_bookmark,
            Screen.BOOKMARK_LIST.name
        ),
        BottomNavigationItem(title = "Profile", iconId = R.drawable.icon_profile, Screen.PROFILE.name)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        CustomAppBar {
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

@Composable
fun CustomAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Surface(
            tonalElevation = 0.dp,
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .align(Alignment.Center)
        ) {
            Row(
                Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    20.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}