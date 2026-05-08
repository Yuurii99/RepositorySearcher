package com.myapp.repositorysearcher.ui.search

import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val repositories: List<GitHubRepositoryEntity>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}