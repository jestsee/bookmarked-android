package com.example.bookmarked_android.model

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

data class Tag(
    val id: String,
    val name: String,
    val color: String,
)