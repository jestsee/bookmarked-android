package com.example.bookmarked_android.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName


@Immutable
data class BookmarkListResponse(
    val results: List<BookmarkItem>,
    val nextCursor: String?,
    val hasMore: Boolean = false
)

@Immutable
data class BookmarkItem(
    @SerializedName("author")
    val author: Author,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("icon")
    val type: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isLiked")
    val isLiked: Boolean,
    @SerializedName("notionUrl")
    val notionUrl: String,
    @SerializedName("publicUrl")
    val publicUrl: String,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("title")
    val title: String,
    @SerializedName("tweetUrl")
    val tweetUrl: String,
    @SerializedName("tweetedTime")
    val tweetedTime: String,
    @SerializedName("updatedTime")
    val updatedTime: String
)

@Immutable
data class Tag(
    @SerializedName("color")
    val color: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

@Immutable
data class TagListResponse(
    val results: List<Tag>
)