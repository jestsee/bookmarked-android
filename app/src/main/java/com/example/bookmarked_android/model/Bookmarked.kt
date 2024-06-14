package com.example.bookmarked_android.model

import com.google.gson.annotations.SerializedName

data class BookmarkedItem(
    val id: String,

//    @SerialName(value = "created_time")
//    val createdTime: String,
//
//    @SerialName(value = "last_edited_time")
//    val lastEditedTime: String,

//    @Serializable(with = IconSerializer::class)
//    val icon: String,
)

data class BookmarkedItems(
    @SerializedName("next_cursor")
    val nextCursor: String,

//    val results: List<BookmarkedItem>
)