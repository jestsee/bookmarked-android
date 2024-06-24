package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmarked_android.R
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING

@Composable
fun RecentBookmarks(
    bookmarks: List<BookmarkItem>,
    onNavigateToDetail: (String) -> Unit,
) {
    var pressedBookmarkId by rememberSaveable { mutableStateOf<String?>(null) }
    val haptics = LocalHapticFeedback.current

//    SectionTitle(title = "Recently bookmarked")
    Spacer(modifier = Modifier.size(16.dp))
    Column(
        modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        bookmarks.forEach { bookmark ->
            RecentBookmarkItem(
                bookmark,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onNavigateToDetail(bookmark.id) },
                        onLongPress = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            pressedBookmarkId = bookmark.id
                        },
                        onPress = {
                            tryAwaitRelease()
                            pressedBookmarkId = null
                        },
                    )
                }
            )
        }

    }

    if (pressedBookmarkId != null) {
        BookmarkDialog(
            bookmark = bookmarks.first { it.id == pressedBookmarkId },
            onDismissRequest = { pressedBookmarkId = null })
    }
}

@Composable
fun RecentBookmarkItem2(item: BookmarkItem, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0f),
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = RoundedCornerShape(32))
                    .background(Color(0xFF7A70FF)),
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(28.dp),
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = "tips",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.author,
                    maxLines = 1,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun RecentBookmarkItem(item: BookmarkItem, modifier: Modifier) {
    Box(Modifier.fillMaxWidth()) {
        CustomShapeBox(
            modifier.matchParentSize(),
            cornerRadius = 28.dp,
            innerRadius = 46.dp,
            filledColor = MaterialTheme.colorScheme.inverseOnSurface.copy(.75f)
        )
        Column(Modifier.padding(24.dp, 20.dp, 24.dp, 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    model = item.icon,
                    contentDescription = "Author's avatar"
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        "Author's name",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "@username",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.size(18.dp))
            BookmarkTags(tags = item.tags)
        }
        IconButton(
            modifier = Modifier
//                .padding(end = 2.dp, top = 2.dp)
//                .background(Primary, shape = CircleShape)
                .size(36.dp)
                .align(Alignment.TopEnd),
            onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.icon_external_link),
                contentDescription = "external link",
            )
        }
    }
}