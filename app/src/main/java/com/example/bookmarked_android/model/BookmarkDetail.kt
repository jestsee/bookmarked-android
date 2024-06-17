package com.example.bookmarked_android.model


import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class BookmarkDetail(
    @SerializedName("author")
    val author: Author,
    @SerializedName("contents")
    val contents: List<Content>,
    @SerializedName("id")
    val id: String,
    @SerializedName("parentId")
    val parentId: String,
    @SerializedName("tweetUrl")
    val tweetUrl: String
)