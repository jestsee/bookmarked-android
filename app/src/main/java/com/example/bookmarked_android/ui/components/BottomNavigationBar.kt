package com.example.bookmarked_android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookmarked_android.Screen
import com.example.bookmarked_android.data.BottomNavigationItem

@Composable
fun BottomNavigationBar(
    visible: Boolean,
    navController: NavController,
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit
) {
    val items = listOf(
        BottomNavigationItem(title = "Home", icon = Icons.Rounded.Home) {
            navController.navigate(
                Screen.HOME.name
            )
        },
        BottomNavigationItem(title = "Bookmarks", icon = Icons.Rounded.Bookmarks) {
            navController.navigate(
                Screen.BOOKMARK_LIST.name
            )
        },
        BottomNavigationItem(title = "Profile", icon = Icons.Rounded.Person) {
            navController.navigate(
                Screen.HOME.name // TODO
            )
        }
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomAppBar(
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 68.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.08f),
                    shape = RoundedCornerShape(50)
                )
                .clip(RoundedCornerShape(50)),
            contentPadding = PaddingValues(4.dp),
            tonalElevation = 0.1.dp,
            windowInsets = WindowInsets(0)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = Modifier,
                        selected = index == selectedIndex,
                        onClick = {
                            item.onClick()
                            onIndexChange(index)
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.padding(vertical = 16.dp),
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                    )
                }
            }

        }
    }
}