package com.example.news_app.viewmodel

import com.example.news_app.data.model.Article

sealed interface NewsUiState {
    data object Loading : NewsUiState
    data class Success(val articles: List<Article>) : NewsUiState
    data class Error(val message: String) : NewsUiState
    data object Empty : NewsUiState
}