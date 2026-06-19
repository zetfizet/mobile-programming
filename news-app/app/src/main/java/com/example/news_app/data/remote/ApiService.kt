package com.example.news_app.data.remote

import com.example.news_app.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("q") query: String? = null,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}