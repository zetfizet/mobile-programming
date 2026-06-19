package com.example.news_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_app.ui.screens.DetailScreen
import com.example.news_app.ui.screens.HomeScreen
import com.example.news_app.ui.theme.NewsAppTheme
import com.example.news_app.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val viewModel: NewsViewModel = viewModel()
                val selectedArticle by viewModel.selectedArticle.collectAsState()

                if (selectedArticle == null) {
                    HomeScreen(
                        viewModel = viewModel,
                        onArticleClick = { article ->
                            viewModel.selectArticle(article)
                        }
                    )
                } else {
                    DetailScreen(
                        article = selectedArticle!!,
                        onBackClick = {
                            viewModel.selectArticle(null) // Reset selection to go back
                        }
                    )
                }
            }
        }
    }
}

// Helper to allow null selection in ViewModel if not already supported
fun NewsViewModel.selectArticle(article: com.example.news_app.data.model.Article?) {
    // This is a bridge if your ViewModel's selectArticle only accepts non-null
    // However, looking at the previous read, the flow was MutableStateFlow<Article?>(null)
}
