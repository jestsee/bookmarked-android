package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.model.Tag

@Composable
fun RecentBookmarks() {
}

@Composable
fun RecentBookmarkListItem(item: BookmarkItem) {
    androidx.compose.material3.ListItem(
        headlineContent = { Text(text = item.title) },
        supportingContent = { Text(text = item.author) },
        leadingContent = {
            AsyncImage(
                modifier = Modifier.size(48.dp).clip(shape = RoundedCornerShape(20)),
                model = item.icon, contentDescription = null,
                placeholder = BrushPainter(
                    Brush.linearGradient(
                        listOf(
                            Color(color = 0xFFD5D5D5),
                            Color(color = 0xFF949494),
                        )
                    )
                ),
            )
        },
    )
}

class RecentBookmarkListItemProvider : PreviewParameterProvider<BookmarkItem> {
    override val values = sequenceOf(
        BookmarkItem(
            "1fbc60b6-2652-4d7e-8d15-0c1cf749d35e",
            "ur2024-06-14T08:44:00.000Zl",
            "2024-06-14T08:44:00.000Z",
            "https://pbs.twimg.com/profile_images/1737436487912718336/FGWDhP1X_normal.jpg",
            "Supabase (@supabase)",
            listOf(Tag("1", "Database", "Green")),
            "https://x.com/supabase/status/1801447031388705225",
            "Building Local-First apps?"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun RecentBookmarkListItemPreview(@PreviewParameter(RecentBookmarkListItemProvider::class) item: BookmarkItem) {
    RecentBookmarkListItem(item = item)
}