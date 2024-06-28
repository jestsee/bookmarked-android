@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookmarked_android.R
import com.example.bookmarked_android.leftBorder
import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.model.CalloutContent
import com.example.bookmarked_android.model.Content
import com.example.bookmarked_android.model.ImageContent
import com.example.bookmarked_android.model.TextsContent
import com.example.bookmarked_android.model.toBookmarkDetail
import com.example.bookmarked_android.navigation.DetailScreenParams
import com.example.bookmarked_android.navigation.Screen
import com.example.bookmarked_android.ui.components.BookmarkTags
import com.example.bookmarked_android.ui.theme.ASYNC_IMAGE_PLACEHOLDER
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Primary
import com.example.bookmarked_android.urlEncoder

/**
 * Implementation
 */
@Composable
fun SharedTransitionScope.DetailScreen(
    navController: NavController,
    pageId: String,
    params: DetailScreenParams,
    topPadding: Dp,
    bottomPadding: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val viewModel: BookmarkDetailViewModel =
        viewModel(factory = remember { BookmarkDetailViewModelFactory(pageId) })
    val bookmarkDetailUiState = viewModel.bookmarkDetailUiState

    DetailScreenUi(
        bookmarkDetailUiState,
        params,
        topPadding,
        bottomPadding,
        animatedVisibilityScope,
        navController
    )
}

@Composable
fun SharedTransitionScope.DetailScreenUi(
    bookmarkDetailUiState: BookmarkDetailUiState,
    params: DetailScreenParams,
    topPadding: Dp,
    bottomPadding: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
) {
    when (bookmarkDetailUiState) {
        is BookmarkDetailUiState.Error -> Text(text = "Error")
        is BookmarkDetailUiState.Loading -> Text(text = "Loading...")
        is BookmarkDetailUiState.Success -> {
            Details(
                bookmarkDetailUiState.details,
                params,
                topPadding,
                bottomPadding,
                animatedVisibilityScope,
                navController
            )
        }
        else -> {}
    }
}

/**
 * UI Logics start here
 */
@Composable
private fun SharedTransitionScope.Details(
    details: List<BookmarkDetail>, params: DetailScreenParams, topPadding: Dp, bottomPadding: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
) {
    LazyColumn(
        modifier = Modifier.padding(
            HORIZONTAL_PADDING,
            topPadding,
            HORIZONTAL_PADDING,
        ),
        contentPadding = PaddingValues(
            top = 32.dp, bottom = bottomPadding
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        itemsIndexed(details) { _, item ->
            val nextItem = details.getOrNull(details.indexOf(item) + 1)
            val nextAuthorUsername = nextItem?.author?.username

            DetailItem(
                detail = item,
                isFirstItem = item == details.first(),
                onImageClick = { imageUrl ->
                    val encodedUrl = urlEncoder(imageUrl)
                    navController.navigate("${Screen.IMAGE_DETAIL.name}/$encodedUrl")
                },
                shouldDisplayAuthor = nextAuthorUsername == null || nextAuthorUsername != item.author.username,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

        if (params.tags.isNotEmpty()) {
            item {
                Column {
                    Text(
                        "TAGS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(.75f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    BookmarkTags(params.tags)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        item {
            Text(
                "OPEN IN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(.75f),
                letterSpacing = 1.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OpenInButton(R.drawable.icon_x, "X", params.tweetUrl)
                OpenInButton(R.drawable.icon_notion, "Notion", params.notionUrl)
            }
        }
    }

}

@Composable
private fun SharedTransitionScope.DetailItem(
    detail: BookmarkDetail,
    isFirstItem: Boolean = false,
    shouldDisplayAuthor: Boolean = true,
    onImageClick: (String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        detail.contents.map {
            ContentItem(
                content = it,
                isFirstContentItem = isFirstItem && it == detail.contents.first(),
                onImageClick = onImageClick,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
        if (shouldDisplayAuthor) AuthorCard(detail.author)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ContentItem(
    content: Content,
    isFirstContentItem: Boolean,
    onImageClick: (String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    if (content is TextsContent) {
        if (isFirstContentItem) {
            // separate the content with its title
            val title = TextsContent(texts = arrayListOf(content.texts.first()))
            DetailTexts(content = title, isTitle = true)

            val restContent = TextsContent(texts = content.texts.drop(1))
            DetailTexts(content = restContent)
            return
        }
        DetailTexts(content = content)
    }

    if (content is ImageContent) {
        val interactionSource = remember { MutableInteractionSource() }
        return AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(content.url)
                .placeholderMemoryCacheKey(content.url).memoryCacheKey(content.url)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .heightIn(0.dp, 240.dp)
                .clip(RoundedCornerShape(8))
                .clickable(interactionSource = interactionSource, indication = null) {
                    onImageClick(content.url)
                }
                .sharedElement(
                    state = rememberSharedContentState(key = content.url),
                    animatedVisibilityScope = animatedVisibilityScope,
//                    boundsTransform = { _, _ -> tween(durationMillis = 1000) }
                ),
            contentDescription = "content image",
            contentScale = ContentScale.Crop,
            placeholder = ASYNC_IMAGE_PLACEHOLDER,
        )
    }
    if (content is CalloutContent) {
        return Row(
            modifier = Modifier.leftBorder(3.dp, Primary),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.width(0.dp))
            DetailItem(
                detail = content.toBookmarkDetail(),
                onImageClick = onImageClick,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}