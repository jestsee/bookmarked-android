package com.example.bookmarked_android.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookmarkList(
    val items: List<BookmarkItem>,
)

@Immutable
data class BookmarkItem(
    val id: String,
    val createdTime: String,
    val lastEditedTime: String,
    val icon: String,
    val author: String,
    val tags: List<Tag>,
    val url: String,
    val title: String,
)

@Immutable
data class Tag(
    val id: String,
    val name: String,
    val color: String,
)