package com.myapp.repositorysearcher.ui.search

import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Error(val message: String) : SearchUiState

    data object Empty : SearchUiState
    data class Success(
        val repositories: List<GitHubRepositoryEntity> = emptyList(),
        val selectedIds: Set<Int> = emptySet(),
        val isEditMode: Boolean = false,
    ) : SearchUiState
}