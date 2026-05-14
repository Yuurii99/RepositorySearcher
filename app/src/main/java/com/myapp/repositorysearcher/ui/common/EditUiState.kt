package com.myapp.repositorysearcher.ui.common

data class EditUiState (
    val isEditMode: Boolean = false,
    val selectedIds: Set<Int> = emptySet(),
    val totalCount: Int = 0
)