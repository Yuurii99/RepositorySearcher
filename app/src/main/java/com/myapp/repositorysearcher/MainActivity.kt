package com.myapp.repositorysearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myapp.repositorysearcher.ui.detail.DetailScreen
import com.myapp.repositorysearcher.ui.search.SearchScreen
import com.myapp.repositorysearcher.ui.theme.RepositorySearcherTheme
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepositorySearcherTheme {
                RepositorySearcherApp(
                )
            }
        }
    }
}


@Composable
fun RepositorySearcherApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            SearchScreen(
                onItemClick = { repositoryUrl ->
                    val encodeUrl = URLEncoder.encode(repositoryUrl, "UTF-8")
                    navController.navigate("detail/$encodeUrl")
                }
            )
        }
        composable(
            "detail/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            DetailScreen(
                url = url,
                goBackSearchScreen = { navController.popBackStack() }
            )
        }
    }
}