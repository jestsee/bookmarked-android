package com.example.bookmarked_android.model


import androidx.compose.runtime.Immutable
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

sealed class Content {
    abstract val type: String
}

@Immutable
data class TextContent(
    override val type: String = "text",
    val id: String,
    val text: String,
    val url: String?,
) : Content()

@Immutable
data class TextsContent(
    override val type: String = "texts",
    val texts: List<TextContent>,
) : Content()

@Immutable
data class ImageContent(
    override val type: String = "image",
    val id: String,
    val url: String
) : Content()

@Immutable
data class CalloutContent(
    override val type: String = "callout",
    val id: String,
    val author: Author,
    @Transient var contents: List<Content>,
    val parentId: String,
) : Content()

fun CalloutContent.toBookmarkDetail(): BookmarkDetail {
    return BookmarkDetail(this.author, this.contents, this.id, "", this.parentId)
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
            "texts" -> Gson().fromJson(json, TextsContent::class.java)
            "image" -> Gson().fromJson(json, ImageContent::class.java)
            "callout" -> {
                val calloutContent = Gson().fromJson(json, CalloutContent::class.java)
                val jsonContents = jsonObject.getAsJsonArray("contents")

                val contents = jsonContents.map { jsonItem ->
                    val itemType = jsonItem.asJsonObject.get("type").asString
                    val itemId = jsonItem.asJsonObject.get("id")?.asString
                    val itemText = jsonItem.asJsonObject.get("text")?.asString
                    val itemTexts = jsonItem.asJsonObject.get("texts")?.asJsonArray
                    val itemUrl = jsonItem.asJsonObject.get("url")?.asString

                    when (itemType) {
                        "image" -> ImageContent(itemId!!, itemType, itemUrl!!)
                        "texts" -> TextsContent(itemType, Gson().fromJson(itemTexts, Array<TextContent>::class.java).toList())
                        else -> TextContent(itemId!!, itemType, itemText!!, itemUrl)
                    }
                }
                calloutContent.contents = contents
                return calloutContent
            }

            else -> throw JsonParseException("Class not found during deserialization")

        }
    }
}