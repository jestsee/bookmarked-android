package com.example.bookmarked_android.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.components.TextUrl
import com.example.bookmarked_android.ui.theme.ASYNC_IMAGE_PLACEHOLDER
import com.example.bookmarked_android.ui.theme.BookmarkedandroidTheme
import com.example.bookmarked_android.ui.theme.PADDING

class DetailScreenPreviewParameterProvider : PreviewParameterProvider<List<BookmarkDetail>> {
    override val values = sequenceOf(mockBookmarkDetails)
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview(
    @PreviewParameter(DetailScreenPreviewParameterProvider::class)
    bookmarks: List<BookmarkDetail>
) {
    BookmarkedandroidTheme {
        DetailScreenUi(BookmarkDetailUiState.Success(bookmarks))
    }
}

@Composable
fun DetailScreen(navController: NavController, pageId: String) {
    val viewModel: BookmarkDetailViewModel =
        viewModel(factory = remember { BookmarkDetailViewModelFactory(pageId) })
    val bookmarkDetailUiState = viewModel.bookmarkDetailUiState

    DetailScreenUi(bookmarkDetailUiState)
}

@Composable
fun DetailScreenUi(bookmarkDetailUiState: BookmarkDetailUiState) {
    Scaffold(bottomBar = { BottomNavigationBar() }) { innerPadding ->
        when (bookmarkDetailUiState) {
            is BookmarkDetailUiState.Error -> Text(text = "Error")
            is BookmarkDetailUiState.Loading -> Text(text = "Loading...")
            is BookmarkDetailUiState.Success -> {
                Details(innerPadding, bookmarkDetailUiState)
            }
        }
    }
}

@Composable
private fun Details(
    innerPadding: PaddingValues, bookmarkDetailUiState: BookmarkDetailUiState.Success
) {
    LazyColumn(
        modifier = Modifier.padding(
            PADDING,
            innerPadding.calculateTopPadding(),
            PADDING,
            innerPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { Spacer(modifier = Modifier.height(24.dp)) }
        items(bookmarkDetailUiState.details) {
            DetailItem(it, it == bookmarkDetailUiState.details.first())
        }
        // See in Notion
        // See in twitter
        // Bookmarked at
    }
}

@Composable
private fun DetailItem(detail: BookmarkDetail, isFirstItem: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        detail.contents.map {
            ContentItem(it, isFirstItem && it == detail.contents.first())
        }
    }
    AuthorCard(detail.author)
}

@Composable
private fun AuthorCard(author: Author) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = author.username,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.8f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = author.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))
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
private fun ContentItem(content: Content, isFirstContentItem: Boolean) {
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
            text = content.text,
            url = content.url
        )
        return Text(
            text = content.text,
            fontSize = 18.sp,
            lineHeight = 28.sp
        )
    }
    if (content is ImageContent) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
//                .height(160.dp)
                .clip(RoundedCornerShape(2)), // TODO
            model = content.url,
            contentDescription = "content image",
            placeholder = ASYNC_IMAGE_PLACEHOLDER
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
    if (content is CalloutContent) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp),
                color = Color(
                    0xFF8E85FF
                ),
            )
            DetailItem(detail = content.toBookmarkDetail())
        }
    }
}