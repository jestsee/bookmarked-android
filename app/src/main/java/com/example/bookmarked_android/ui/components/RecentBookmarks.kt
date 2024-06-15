package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.model.Tag

@Composable
fun RecentBookmarks(items: List<BookmarkItem>) {
    SectionTitle(title = "Recently added")
    Spacer(modifier = Modifier.size(4.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items.forEach { item ->
            item { RecentBookmarkItem(item = item) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentBookmarkItem(item: BookmarkItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
        ),
        onClick = { /*TODO*/ }) {
        Row(
            modifier = Modifier
                .padding(16.dp, 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(shape = RoundedCornerShape(20)),
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
            Spacer(modifier = Modifier.size(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.author,
                    maxLines = 1,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun RecentBookmarkItemOld(item: BookmarkItem) {
    androidx.compose.material3.ListItem(
        headlineContent = {
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = item.author, maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = RoundedCornerShape(20)),
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

class RecentBookmarksProvider : PreviewParameterProvider<List<BookmarkItem>> {
    override val values = sequenceOf(
        listOf(
            BookmarkItem(
                "1fbc60b6-2652-4d7e-8d15-0c1cf749d35e",
                "ur2024-06-14T08:44:00.000Zl",
                "2024-06-14T08:44:00.000Z",
                "https://pbs.twimg.com/profile_images/1737436487912718336/FGWDhP1X_normal.jpg",
                "Supabase (@supabase)",
                listOf(Tag("1", "Database", "Green")),
                "https://x.com/supabase/status/1801447031388705225",
                "Building Local-First apps?"
            ),
            BookmarkItem(
                "1fbc60b6-2652-4d7e-8d15-0c1cf749d35e",
                "ur2024-06-14T08:44:00.000Zl",
                "2024-06-14T08:44:00.000Z",
                "https://pbs.twimg.com/profile_images/1737436487912718336/FGWDhP1X_normal.jpg",
                "Supabase (@supabase)",
                listOf(Tag("1", "Database", "Green")),
                "https://x.com/supabase/status/1801447031388705225",
                "PGVECTOR IS NOW FASTER THAN PINECONE. And 75% cheaper thanks to a new open-source extension – introducing pgvectorscale."
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun RecentBookmarksPreview(@PreviewParameter(RecentBookmarksProvider::class) items: List<BookmarkItem>) {
    RecentBookmarks(items = items)
}