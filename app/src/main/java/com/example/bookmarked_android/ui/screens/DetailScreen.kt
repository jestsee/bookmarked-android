package com.example.bookmarked_android.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookmarked_android.mock.mockBookmarkDetails
import com.example.bookmarked_android.model.Author
import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.model.CalloutContent
import com.example.bookmarked_android.model.Content
import com.example.bookmarked_android.model.ImageContent
import com.example.bookmarked_android.model.TextContent
import com.example.bookmarked_android.model.toBookmarkDetail
import com.example.bookmarked_android.ui.components.ImageDialog
import com.example.bookmarked_android.ui.components.TextUrl
import com.example.bookmarked_android.ui.theme.ASYNC_IMAGE_PLACEHOLDER
import com.example.bookmarked_android.ui.theme.BookmarkedandroidTheme
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING

/**
 * Preview
 */
class DetailScreenPreviewParameterProvider : PreviewParameterProvider<List<BookmarkDetail>> {
    override val values = sequenceOf(mockBookmarkDetails)
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview(
    @PreviewParameter(DetailScreenPreviewParameterProvider::class) bookmarks: List<BookmarkDetail>
) {
    BookmarkedandroidTheme {
        DetailScreenUi(BookmarkDetailUiState.Success(bookmarks), 0.dp, 0.dp)
    }
}

/**
 * Implementation
 */
@Composable
fun DetailScreen(navController: NavController, pageId: String, topPadding: Dp, bottomPadding: Dp) {
    val viewModel: BookmarkDetailViewModel =
        viewModel(factory = remember { BookmarkDetailViewModelFactory(pageId) })
    val bookmarkDetailUiState = viewModel.bookmarkDetailUiState

    DetailScreenUi(bookmarkDetailUiState, topPadding, bottomPadding)
}

@Composable
fun DetailScreenUi(
    bookmarkDetailUiState: BookmarkDetailUiState,
    topPadding: Dp,
    bottomPadding: Dp
) {
    when (bookmarkDetailUiState) {
        is BookmarkDetailUiState.Error -> Text(text = "Error")
        is BookmarkDetailUiState.Loading -> Text(text = "Loading...")
        is BookmarkDetailUiState.Success -> {
            Details(bookmarkDetailUiState.details, topPadding, bottomPadding)
        }
    }
}


@Composable
private fun Details(
    details: List<BookmarkDetail>, topPadding: Dp, bottomPadding: Dp,
) {
    var selectedImageUrl by rememberSaveable { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.padding(
            HORIZONTAL_PADDING,
            topPadding,
            HORIZONTAL_PADDING,
        ),
        contentPadding = PaddingValues(
            top = 32.dp,
            bottom = bottomPadding
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(details) { _, item ->
            DetailItem(
                item,
                // TODO prevent to show author if redundant
                item == details.first(),
                onImageClick = { imageUrl -> selectedImageUrl = imageUrl }
            )
        }
        // See in Notion
        // See in twitter
        // Bookmarked at
    }

    if (selectedImageUrl != null) {
        ImageDialog(url = selectedImageUrl!!, onDismissRequest = { selectedImageUrl = null })
    }

}

@Composable
private fun DetailItem(
    detail: BookmarkDetail,
    isFirstItem: Boolean = false,
    shouldDisplayAuthor: Boolean = true,
    onImageClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        detail.contents.map {
            ContentItem(it, isFirstItem && it == detail.contents.first(), onImageClick)
        }
        if (shouldDisplayAuthor) AuthorCard(detail.author)
    }
}

@Composable
private fun AuthorCard(author: Author) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            text = author.username,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.8f)
        )
        Text(
            text = author.name, fontSize = 14.sp, fontWeight = FontWeight.Medium
        )
        AsyncImage(
            modifier = Modifier
                .size(32.dp)
                .clip(shape = RoundedCornerShape(50)),
            model = author.avatar,
            contentDescription = "Author's avatar",
            placeholder = ASYNC_IMAGE_PLACEHOLDER,
        )
    }
}

@Composable
private fun ContentItem(
    content: Content,
    isFirstContentItem: Boolean,
    onImageClick: (String) -> Unit
) {
    if (isFirstContentItem && content is TextContent) {
        return Text(
            text = content.text,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 36.sp
        )
    }
    if (content is TextContent) {
        if (content.url != null) return TextUrl(
            text = content.text, url = content.url
        )
        return Text(
            text = content.text, fontSize = 18.sp, lineHeight = 28.sp
        )
    }
    if (content is ImageContent) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, 240.dp)
                .clip(RoundedCornerShape(8))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onImageClick(content.url) })
                },
            model = content.url,
            contentDescription = "content image",
            contentScale = ContentScale.Crop,
            placeholder = ASYNC_IMAGE_PLACEHOLDER,
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    if (content is CalloutContent) {
        Row(
            modifier = Modifier.leftBorder(4.dp, Color(0xFF7A70FF)),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.width(0.dp))
            DetailItem(detail = content.toBookmarkDetail(), onImageClick = onImageClick)
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.leftBorder(strokeWidth: Dp, color: Color) = composed(factory = {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    Modifier.drawBehind {
        val height = size.height
        val strokeWidthHalf = strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = strokeWidthHalf, y = 24.dp.toPx()),
            end = Offset(x = strokeWidthHalf, y = height - 16.dp.toPx()),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round // This sets the tip to be rounded
        )

        drawCircle(
            color = color,
            radius = strokeWidthPx * 1.25f,
            center = Offset(x = strokeWidthHalf, y = 12.dp.toPx())
        )
    }
})