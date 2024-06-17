package com.example.bookmarked_android.model


import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Author(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String
)