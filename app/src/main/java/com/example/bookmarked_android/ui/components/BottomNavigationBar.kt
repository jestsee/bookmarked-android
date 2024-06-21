package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.data.BottomNavigationItem

@Preview
@Composable
fun BottomNavigationBar() {
    val items = listOf(
        BottomNavigationItem(title = "Home", icon = Icons.Rounded.Home),
        BottomNavigationItem(title = "AddBookmark", icon = Icons.Rounded.BookmarkAdd),
        BottomNavigationItem(title = "Bookmarks", icon = Icons.Rounded.Bookmarks),
        BottomNavigationItem(title = "Profile", icon = Icons.Rounded.Person)
    )

    BottomAppBar(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 20.dp, horizontal = 40.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.08f),
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50)),
        contentPadding = PaddingValues(0.dp),
        tonalElevation = 0.1.dp,
        windowInsets = WindowInsets(0)
    ) {
        Row {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    modifier = Modifier,
                    selected = index == 0,
                    onClick = { /*TODO*/ },
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