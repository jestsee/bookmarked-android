package com.example.bookmarked_android.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.model.Tag

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BookmarkTags(
    tags: List<Tag>,
    bookmarkId: String,
    shouldAnimate: Boolean = false,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            BookmarkTag(
                tag,
                modifier = Modifier.then(
                    if (shouldAnimate) Modifier.sharedElement(
                        state = rememberSharedContentState(key = "${bookmarkId}-${tag.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ) else Modifier
                )
            )
        }
    }
}

@Composable
fun BookmarkTag(
    tag: Tag,
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colorScheme.onSurface.copy(.75f)
    Text(
        tag.name,
        modifier = modifier
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