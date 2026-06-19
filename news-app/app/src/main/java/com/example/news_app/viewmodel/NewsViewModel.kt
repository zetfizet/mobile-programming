package com.example.news_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_app.data.model.Article
import com.example.news_app.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository = NewsRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()
    private var allArticles = listOf<Article>()
    
    // Store selected article for detail view
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    init {
        fetchNews()
    }

    fun fetchNews(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _uiState.value = NewsUiState.Loading
            }
            try {
                // Fetch news from repository
                val articles = repository.getTopHeadlines()
                allArticles = articles
                filterArticles()
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Failed to load news"
                if (isRefresh) {
                    _errorEvent.emit(errorMessage)
                } else {
                    _uiState.value = NewsUiState.Error(errorMessage)
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterArticles()
    }

    private fun filterArticles() {
        val query = _searchQuery.value.trim()
        if (allArticles.isEmpty() && _uiState.value !is NewsUiState.Loading) {
            _uiState.value = NewsUiState.Empty
            return
        }
        val filtered = if (query.isEmpty()) {
            allArticles
        } else {
            allArticles.filter {
                it.title?.contains(query, ignoreCase = true) == true ||
                        it.description?.contains(query, ignoreCase = true) == true ||
                        it.author?.contains(query, ignoreCase = true) == true
            }
        }
        if (filtered.isEmpty() && allArticles.isNotEmpty()) {
            _uiState.value = NewsUiState.Empty
        } else if (filtered.isNotEmpty()) {
            _uiState.value = NewsUiState.Success(filtered)
        } else {
            _uiState.value = NewsUiState.Empty
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }
}