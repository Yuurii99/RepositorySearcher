package com.myapp.repositorysearcher.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.ui.search.SearchContentView
import com.myapp.repositorysearcher.ui.search.SearchUiState
import com.myapp.repositorysearcher.ui.theme.RepositorySearcherTheme

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen_Loading() {
    RepositorySearcherTheme() {
        SearchContentView(
            uiState = SearchUiState.Loading,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen_Success() {
    val mockRepos = listOf(
        GitHubRepositoryEntity(
            name = "tinyTetrisC++",
            ownerName = "JetBrains",
            avatarUrl = "https://...",
            language = "The Kotlin Language",
            stars = 4500,
            repoUrl = "https://..."
        ),
        GitHubRepositoryEntity(
            name = "React-tetris",
            ownerName = "Kevin",
            avatarUrl = "https://...",
            language = "Kotlin",
            stars = 3000,
            repoUrl = "https://...",
        )
    )

    RepositorySearcherTheme() {
        SearchContentView(
            uiState = SearchUiState.Success(mockRepos),
            onItemClick = {}
        )
    }
}