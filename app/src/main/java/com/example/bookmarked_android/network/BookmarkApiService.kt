package com.example.bookmarked_android.network

import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.model.BookmarkItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

private val baseUrl = Config().baseUrl

// Logger interceptor
private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

interface NotionApiService {

    @GET("bookmarks/{databaseId}")
    suspend fun getBookmarks(
        @Header("Authorization") token: String,
        @Path("databaseId") databaseId: String,
    ): List<BookmarkItem>

    @GET("bookmarks/{pageId}/detail")
    suspend fun getBookmarkDetail(
        @Header("Authorization") token: String,
        @Path("pageId") pageId: String,
    ): List<BookmarkDetail>
}

object NotionApi {
    val retrofitService: NotionApiService by lazy {
        retrofit.create(NotionApiService::class.java)
    }
}