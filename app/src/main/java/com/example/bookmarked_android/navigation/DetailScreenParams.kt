package com.example.bookmarked_android.navigation

import com.example.bookmarked_android.model.Tag
import com.example.bookmarked_android.urlEncoder
import com.google.gson.Gson

data class DetailScreenParams(
    val id: String,
    val title: String,
    val tags: List<Tag>,
    val tweetUrl: String,
    val notionUrl: String,
    val siteUrl: String?
)

fun DetailScreenParams.toJson(): String {
    val encoded = this.copy(
        title = urlEncoder(this.title),
        tweetUrl = urlEncoder(this.tweetUrl),
        notionUrl = urlEncoder(this.notionUrl),
        siteUrl = null // TODO
    )
    return Gson().toJson(encoded, DetailScreenParams::class.java)
}