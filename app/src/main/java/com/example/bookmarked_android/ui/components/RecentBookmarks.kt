package com.example.bookmarked_android.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.R
import com.example.bookmarked_android.model.BookmarkItem
import com.example.bookmarked_android.navigation.DetailScreenParams
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Primary
import com.example.bookmarked_android.utils.maxCharacters

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecentBookmarks(
    bookmarks: List<BookmarkItem>,
    onNavigateToDetail: (String, DetailScreenParams) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        modifier = Modifier
            .padding(horizontal = HORIZONTAL_PADDING)
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        bookmarks.forEach { bookmark ->
            RecentBookmarkItem(
                bookmark,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onNavigateToDetail(
                                bookmark.id,
                                DetailScreenParams(
                                    bookmark.id,
                                    bookmark.title,
                                    bookmark.tags,
                                    bookmark.tweetUrl,
                                    bookmark.notionUrl,
                                    bookmark.publicUrl
                                )
                            )
                        },
                    )
                },
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecentBookmarkItem(
    item: BookmarkItem,
    modifier: Modifier,
    shouldAnimate: Boolean = false,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRemove: (() -> Unit)? = null
) {
    if (onRemove != null) {
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = {
                when (it) {
                    SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
                    else -> {
                        onRemove()
                        return@rememberSwipeToDismissBoxState true
                    }
                }
            },
            // positional threshold of 25%
            positionalThreshold = { it * .25f }
        )

        return SwipeToDismissBox(
            state = dismissState,
            modifier = modifier
                .clip(RoundedCornerShape(28.dp, 20.dp, 28.dp, 28.dp)),
            backgroundContent = { DismissBackground(dismissState) },
            content = {
                RecentBookmarkItemContent(item, modifier, shouldAnimate, animatedVisibilityScope)
            })
    }

    return RecentBookmarkItemContent(item, modifier, shouldAnimate, animatedVisibilityScope)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecentBookmarkItemContent(
    item: BookmarkItem,
    modifier: Modifier,
    shouldAnimate: Boolean = false,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val uriHandler = LocalUriHandler.current

    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp, 20.dp, 28.dp, 28.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
//        CustomShapeBox(
//            modifier.matchParentSize(),
//            cornerRadius = 28.dp,
//            innerRadius = 48.dp,
//            filledColor = MaterialTheme.colorScheme.inverseOnSurface.copy(.75f)
//        )
        Column(
            Modifier.padding(24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        item.author.name.maxCharacters(25),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        item.author.username.maxCharacters(25),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = item.title,
                maxLines = 4, overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .then(
                        if (shouldAnimate)
                            Modifier.sharedBounds(
                                rememberSharedContentState(key = "title-${item.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) else Modifier
                    )
                    .wrapContentWidth()
            )
            Spacer(modifier = Modifier.size(24.dp))
            BookmarkTags(
                tags = item.tags,
                bookmarkId = item.id,
                shouldAnimate = shouldAnimate,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
        IconButton(
            modifier = Modifier
                .padding(20.dp)
                .size(36.dp)
                .align(Alignment.TopEnd),
            onClick = {
                uriHandler.openUri(item.tweetUrl)
            }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.icon_external_link),
                contentDescription = "external link",
                tint = Primary
            )
        }
    }
}