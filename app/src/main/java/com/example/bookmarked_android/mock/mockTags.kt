package com.example.bookmarked_android.mock

import com.example.bookmarked_android.model.Tag
import com.example.bookmarked_android.network.customTypeAdapter
import com.google.gson.reflect.TypeToken

private val mockTags = """
    [
            {
                "id": "557617fb-1a5c-440b-9f59-b6844763c11c",
                "name": "UI",
                "color": "gray"
            },
            {
                "id": "dae51839-c8c2-4680-be3a-5b908c3a23d9",
                "name": "Component",
                "color": "yellow"
            }
        ]
""".trimIndent()

private val bookmarkTagsListType = object : TypeToken<List<Tag>>() {}.type

val mockBookmarTags = customTypeAdapter.fromJson<List<Tag>>(
    mockTags,
    bookmarkTagsListType
)