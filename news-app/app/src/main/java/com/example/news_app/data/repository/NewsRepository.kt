package com.example.news_app.data.repository

import com.example.news_app.data.model.Article
import com.example.news_app.data.remote.ApiService
import com.example.news_app.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(private val apiService: ApiService = RetrofitClient.apiService) {
    suspend fun getTopHeadlines(query: String? = null): List<Article> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getTopHeadlines(
                country = "us",
                query = query,
                apiKey = RetrofitClient.API_KEY
            )
            response.articles ?: emptyList()
        }
    }
}