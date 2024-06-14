package com.example.bookmarked_android.network

import com.example.bookmarked_android.model.NotionData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://api.notion.com/v1/" // TODO save as env

// Logger interceptor
private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
        .build()

interface NotionApiService {
    @POST("databases/{databaseId}/query")
    suspend fun getNotionData(
        @Header("Authorization") token: String,
        @Path("databaseId") databaseId: String,
        @Header("Notion-Version") version: String = "2022-06-28"
    ): NotionData
}

object NotionApi {
    val retrofitService: NotionApiService by lazy {
        retrofit.create(NotionApiService::class.java)
    }
}