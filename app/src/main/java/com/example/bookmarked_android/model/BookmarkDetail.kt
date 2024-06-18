package com.example.bookmarked_android.model


import androidx.compose.runtime.Immutable

@Immutable
data class BookmarkDetail(
    val author: Author,
    val contents: List<Content>,
    val id: String,
    val parentId: String,
    val tweetUrl: String
)