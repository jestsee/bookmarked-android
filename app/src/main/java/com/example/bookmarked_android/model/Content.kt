package com.example.bookmarked_android.model


import androidx.compose.runtime.Immutable
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

sealed class Content {
    abstract val id: String
    abstract val type: String
}

@Immutable
data class TextContent(
    override val id: String,
    override val type: String = "text",
    val text: String,
    val url: String?,
) : Content()

@Immutable
data class ImageContent(
    override val id: String,
    override val type: String = "image",
    val url: String
) : Content()

@Immutable
data class CalloutContent(
    override val id: String,
    override val type: String = "callout",
    val author: Author,
    @Transient var contents: List<Content>,
    val parentId: String,
) : Content()

fun CalloutContent.toBookmarkDetail(): BookmarkDetail {
    return BookmarkDetail(this.author, this.contents, this.id, "", this.parentId )
}

class ContentTypeAdapter : JsonDeserializer<Content> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Content {
        val jsonObject = json.getAsJsonObject()
        val typeName = jsonObject.get("type").asString

        return when (typeName) {
            "text" -> Gson().fromJson(json, TextContent::class.java)
            "image" -> Gson().fromJson(json, ImageContent::class.java)
            "callout" -> {
                var calloutContent = Gson().fromJson(json, CalloutContent::class.java)
                val jsonContents = jsonObject.getAsJsonArray("contents")

                val contents = jsonContents.map { jsonItem ->
                    val itemType = jsonItem.asJsonObject.get("type").asString
                    val itemId = jsonItem.asJsonObject.get("id").asString
                    val itemText = jsonItem.asJsonObject.get("text")?.asString
                    val itemUrl = jsonItem.asJsonObject.get("url")?.asString
                    when (itemType) {
                        "image" -> ImageContent(itemId, itemType, itemUrl!!)
                        else -> TextContent(itemId, itemType, itemText!!, itemUrl)
                    }
                }
                calloutContent.contents = contents
                return calloutContent
            }

            else -> throw JsonParseException("Class not found during deserialization")

        }
    }
}