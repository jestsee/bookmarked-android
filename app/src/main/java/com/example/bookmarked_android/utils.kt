package com.example.bookmarked_android

fun String.maxCharacters(max: Int): String {
    return if (this.length <= max) this
    else this.take(max - 3).plus("...")
}