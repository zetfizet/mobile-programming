package com.example.news_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.news_app.ui.screens.DetailScreen
import com.example.news_app.ui.screens.HomeScreen
import com.example.news_app.viewmodel.NewsViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail")
}

@Composable
fun AppNavGraph(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onArticleClick = { article ->
                    viewModel.selectArticle(article)
                    navController.navigate(Screen.Detail.route)
                }
            )
        }
        composable(Screen.Detail.route) {
            val selectedArticle by viewModel.selectedArticle.collectAsState()
            selectedArticle?.let { article ->
                DetailScreen(
                    article = article,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } ?: run {
                // Safe fallback if selected article becomes null
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}