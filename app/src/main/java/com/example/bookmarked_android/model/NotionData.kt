package com.example.bookmarked_android.model
import com.google.gson.annotations.SerializedName

data class NotionData(
    @SerializedName("developer_survey")
    val developerSurvey: String,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("next_cursor")
    val nextCursor: String,
    @SerializedName("object")
    val objectX: String,
    @SerializedName("page_or_database")
    val pageOrDatabase: PageOrDatabase,
    @SerializedName("request_id")
    val requestId: String,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("type")
    val type: String
)

class PageOrDatabase

data class Result(
    @SerializedName("archived")
    val archived: Boolean,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("created_by")
    val createdBy: CreatedBy,
    @SerializedName("created_time")
    val createdTime: String,
    @SerializedName("icon")
    val icon: Icon,
    @SerializedName("id")
    val id: String,
    @SerializedName("in_trash")
    val inTrash: Boolean,
    @SerializedName("last_edited_by")
    val lastEditedBy: LastEditedBy,
    @SerializedName("last_edited_time")
    val lastEditedTime: String,
    @SerializedName("object")
    val objectX: String,
    @SerializedName("parent")
    val parent: Parent,
    @SerializedName("properties")
    val properties: Properties,
    @SerializedName("public_url")
    val publicUrl: String,
    @SerializedName("url")
    val url: String
)

data class CreatedBy(
    @SerializedName("id")
    val id: String,
    @SerializedName("object")
    val objectX: String
)

data class Icon(
    @SerializedName("external")
    val `external`: External,
    @SerializedName("type")
    val type: String
)

data class LastEditedBy(
    @SerializedName("id")
    val id: String,
    @SerializedName("object")
    val objectX: String
)

data class Parent(
    @SerializedName("database_id")
    val databaseId: String,
    @SerializedName("type")
    val type: String
)

data class Properties(
    @SerializedName("Author")
    val author: Author,
    @SerializedName("Created")
    val created: Created,
    @SerializedName("Liked")
    val liked: Liked,
    @SerializedName("Tags")
    val tags: Tags,
    @SerializedName("Tweet")
    val tweet: Tweet,
    @SerializedName("Tweet Date")
    val tweetDate: TweetDate,
    @SerializedName("Tweet Link")
    val tweetLink: TweetLink,
    @SerializedName("Type")
    val type: Type
)

data class External(
    @SerializedName("url")
    val url: String
)

data class Author(
    @SerializedName("id")
    val id: String,
    @SerializedName("rich_text")
    val richText: List<RichText>,
    @SerializedName("type")
    val type: String
)

data class Created(
    @SerializedName("created_time")
    val createdTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String
)

data class Liked(
    @SerializedName("checkbox")
    val checkbox: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String
)

data class Tags(
    @SerializedName("id")
    val id: String,
    @SerializedName("multi_select")
    val multiSelect: List<MultiSelect>,
    @SerializedName("type")
    val type: String
)

data class Tweet(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: List<Title>,
    @SerializedName("type")
    val type: String
)

data class TweetDate(
    @SerializedName("date")
    val date: Date,
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String
)

data class TweetLink(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)

data class Type(
    @SerializedName("id")
    val id: String,
    @SerializedName("select")
    val select: Select,
    @SerializedName("type")
    val type: String
)

data class RichText(
    @SerializedName("annotations")
    val annotations: Annotations,
    @SerializedName("href")
    val href: Any,
    @SerializedName("plain_text")
    val plainText: String,
    @SerializedName("text")
    val text: Text,
    @SerializedName("type")
    val type: String
)

data class Annotations(
    @SerializedName("bold")
    val bold: Boolean,
    @SerializedName("code")
    val code: Boolean,
    @SerializedName("color")
    val color: String,
    @SerializedName("italic")
    val italic: Boolean,
    @SerializedName("strikethrough")
    val strikethrough: Boolean,
    @SerializedName("underline")
    val underline: Boolean
)

data class Text(
    @SerializedName("content")
    val content: String,
    @SerializedName("link")
    val link: Any
)

data class MultiSelect(
    @SerializedName("color")
    val color: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class Title(
    @SerializedName("annotations")
    val annotations: Annotations,
    @SerializedName("href")
    val href: String,
    @SerializedName("plain_text")
    val plainText: String,
    @SerializedName("text")
    val text: TextX,
    @SerializedName("type")
    val type: String
)

data class TextX(
    @SerializedName("content")
    val content: String,
    @SerializedName("link")
    val link: Link
)

data class Link(
    @SerializedName("url")
    val url: String
)

data class Date(
    @SerializedName("end")
    val end: Any,
    @SerializedName("start")
    val start: String,
    @SerializedName("time_zone")
    val timeZone: Any
)

data class Select(
    @SerializedName("color")
    val color: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)