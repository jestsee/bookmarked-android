package com.example.bookmarked_android

import com.example.bookmarked_android.model.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.maxCharacters(max: Int): String {
    return if (this.length <= max) this
    else this.take(max - 3).plus("...")
}

fun List<Tag>.toJson(): String {
    val bookmarkTagsListType = object : TypeToken<List<Tag>>() {}.type
    val jsonTags = Gson().toJson(this, bookmarkTagsListType)
    return jsonTags
}