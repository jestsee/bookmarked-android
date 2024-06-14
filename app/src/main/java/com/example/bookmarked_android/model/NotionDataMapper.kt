package com.example.bookmarked_android.model

import androidx.compose.ui.util.fastMap

fun Result.toBookmarkItem() = BookmarkItem(
    id = id,
    title = properties.tweet.title.get(0).plainText,
    url = properties.tweetLink.url,
    author = properties.author.richText.get(0).plainText,
    icon = icon.external.url,
    tags = properties.tags.multiSelect.fastMap { multiSelect ->
        Tag(
            multiSelect.name,
            multiSelect.id,
            multiSelect.color
        )
    },
    createdTime = createdTime,
    lastEditedTime = lastEditedTime
)