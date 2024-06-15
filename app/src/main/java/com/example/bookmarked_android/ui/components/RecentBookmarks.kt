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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmarked_android.model.BookmarkItem

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
                .padding(18.dp, 12.dp)
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
//
//class RecentBookmarksProvider : PreviewParameterProvider<List<BookmarkItem>> {
//    private val jsonString = """
//        "id": "1fbc60b6-2652-4d7e-8d15-0c1cf749d35e",
//        "createdTime": "2024-06-14T08:44:00.000Z",
//        "updatedTime": "2024-06-14T08:44:00.000Z",
//        "icon": "https://pbs.twimg.com/profile_images/1737436487912718336/FGWDhP1X_normal.jpg",
//        "isLiked": false,
//        "author": "Supabase (@supabase)",
//        "tags": [
//            {
//                "id": "969fe237-9703-4070-8d20-8e81e65777a9",
//                "name": "Offline first",
//                "color": "default"
//            },
//            {
//                "id": "d670ae0c-e32b-40ef-a108-2eb411e50090",
//                "name": "Local first",
//                "color": "default"
//            },
//            {
//                "id": "fb89f1f4-4929-4be3-8338-255997b9be6d",
//                "name": "Database",
//                "color": "default"
//            },
//            {
//                "id": "303a8535-5b6d-4425-a4c8-dc919fa98bd4",
//                "name": "Supabase",
//                "color": "default"
//            },
//            {
//                "id": "f8b708b8-2f21-4ea8-aec5-b4148f056e9b",
//                "name": "Libraries",
//                "color": "brown"
//            }
//        ],
//        "tweetedTime": "2024-06-14T02:50:00.000+00:00",
//        "title": "Building Local-First apps?",
//        "tweetUrl": "https://x.com/supabase/status/1801447031388705225",
//        "notionUrl": "https://www.notion.so/Building-Local-First-apps-1fbc60b626524d7e8d150c1cf749d35e",
//        "publicUrl": "https://jestsee.notion.site/Building-Local-First-apps-1fbc60b626524d7e8d150c1cf749d35e"
//    """
//    override val values = sequenceOf(
//        listOf(
//            Gson().fromJson(jsonString, BookmarkItem::class.java)
//        )
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun RecentBookmarksPreview(@PreviewParameter(RecentBookmarksProvider::class) items: List<BookmarkItem>) {
//    RecentBookmarks(items = items)
//}