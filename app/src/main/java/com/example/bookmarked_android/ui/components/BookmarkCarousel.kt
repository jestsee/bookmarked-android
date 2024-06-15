package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.model.BookmarkItem

@Composable
fun BookmarkCarousel(items: List<BookmarkItem>) {
    SectionTitle(title = "Continue reading")
    Spacer(modifier = Modifier.size(40.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(56.dp)) {
        items.forEachIndexed { index, item ->
            val scaleModifier = if (index == 1) Modifier.scale(1.5f) else Modifier
            item {
                BookmarkCarouselItem(
                    item = item,
                    modifier = scaleModifier
                )
            }
        }
    }
    Spacer(modifier = Modifier.size(32.dp))
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
                .size(160.dp, 80.dp)
                .background(
                    brush =
                    Brush.linearGradient(
                        listOf(
                            Color(color = 0xFFD5D5D5),
                            Color(color = 0xFF949494),
                        )
                    )
                ),
        ) {}
        Text(
            text = item.title,
            maxLines = 1
        )
        Text(text = item.author)
    }
}