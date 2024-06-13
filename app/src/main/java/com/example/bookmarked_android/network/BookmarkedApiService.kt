package com.example.bookmarked_android.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://api.notion.com/v1/" // TODO save as env

// Logger interceptor
private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

interface BookmarkApiService {
    @POST("databases/{databaseId}/query")
    suspend fun getBookmarks(
        @Header("Authorization") token: String,
        @Path("databaseId") databaseId: String,
        @Header("Notion-Version") version: String = "2022-06-28"
    ): String
}

object BookmarkApi {
    val retrofitService: BookmarkApiService by lazy {
        retrofit.create(BookmarkApiService::class.java)
    }
}