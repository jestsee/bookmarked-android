package com.example.bookmarked_android.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookmarked_android.mock.mockBookmarkDetails
import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.model.Content
import com.example.bookmarked_android.model.ImageContent
import com.example.bookmarked_android.model.TextContent
import com.example.bookmarked_android.ui.components.BottomNavigationBar
import com.example.bookmarked_android.ui.components.TextUrl
import com.example.bookmarked_android.ui.theme.BookmarkedandroidTheme
import com.example.bookmarked_android.ui.theme.PADDING

class DetailScreenPreviewParameterProvider : PreviewParameterProvider<List<BookmarkDetail>> {
    override val values = sequenceOf(mockBookmarkDetails)
}

@PreviewLightDark
@Preview(showBackground = true)
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
    }
}

@Composable
private fun DetailItem(detail: BookmarkDetail, isFirstItem: Boolean) {
    Log.d("Detail", "DetailItem: ${detail}")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        detail.contents.map {
            ContentItem(it, isFirstItem && it == detail.contents.first())
        }
    }
    Text(text = "${detail.author.name} ${detail.author.username}")
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
        val textStyle = TextStyle(fontSize = 18.sp, lineHeight = 28.sp)
        if (content.url != null) return TextUrl(text = content.text, url = content.url)
        return Text(text = content.text, style = textStyle)
    }
    if (content is ImageContent) {
        return AsyncImage(model = content.url, contentDescription = "content image")
    }
}