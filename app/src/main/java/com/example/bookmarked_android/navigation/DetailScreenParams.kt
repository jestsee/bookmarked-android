package com.example.bookmarked_android.navigation

import com.example.bookmarked_android.model.Tag

data class DetailScreenParams(
    val title: String,
    val tags: List<Tag>,
    val tweetUrl: String,
    val notionUrl: String,
    val siteUrl: String?
)