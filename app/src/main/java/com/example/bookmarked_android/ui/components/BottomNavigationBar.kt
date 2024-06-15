package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.data.BottomNavigationItem

val items = listOf(
    BottomNavigationItem(title = "Home", icon = Icons.Rounded.Home),
    BottomNavigationItem(title = "Bookmarks", icon = Icons.Rounded.List),
    BottomNavigationItem(title = "Profile", icon = Icons.Rounded.Person)
)
@Preview
@Composable
fun BottomNavigationBar() {
    NavigationBar(modifier = Modifier.height(96.dp)) {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == 0,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
//                    label = {
//                        Text(
//                            text = item.title,
//                            color = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
                )
            }
        }
    }
}