package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.model.Tag

@Composable
fun BookmarkDialog(bookmark: BookmarkItem, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(24.dp, 20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(shape = RoundedCornerShape(50)),
                        model = bookmark.author.avatar,
                        contentDescription = "user's twitter avatar"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = bookmark.author.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookmark.author.username,
                            fontSize = 16.sp,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = bookmark.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                BookmarkTags(tags = bookmark.tags)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookmarkTags(tags: List<Tag>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            BookmarkTag(tag)
        }
    }
}

@Composable
fun BookmarkTag(tag: Tag) {
    val color = MaterialTheme.colorScheme.onSurface.copy(.75f)
    Text(
        tag.name,
        modifier = Modifier
            .border(
                width = 0.5.dp,
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        fontSize = 12.sp,
        color = color
    )
}