package com.myapp.repositorysearcher.ui.common

import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity

data class EditUiState (
    val isEditMode: Boolean = false,
    val selectedIds: Set<Int> = emptySet(),
    val totalCount: Int = 0
)