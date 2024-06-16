package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.model.BookmarkItem

@Composable
fun BookmarkCarousel(items: List<BookmarkItem>) {
    SectionTitle(title = "Recently viewed")
    Spacer(modifier = Modifier.size(16.dp))
    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
        items.forEach { item ->
            // TODO
            BookmarkCarouselItem(
                item = item,
                modifier = Modifier
            )

        }
    }
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun BookmarkCarouselItem(item: BookmarkItem, modifier: Modifier) {
    Card(
        modifier = modifier.width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF7A70FF)),
        )
        Text(
            modifier = Modifier.padding(12.dp),
            text = item.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}