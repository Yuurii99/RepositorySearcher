package com.myapp.repositorysearcher.ui.search

import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Error(val message: String) : SearchUiState
    data class Success(
        val repositories: List<GitHubRepositoryEntity>,
        val selectedIds: Set<String> = emptySet(),
        val isEditMode: Boolean = false
    ) : SearchUiState
}