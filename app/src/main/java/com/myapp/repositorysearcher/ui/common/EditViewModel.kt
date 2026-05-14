package com.myapp.repositorysearcher.ui.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class EditViewModel : ViewModel() {
    private val _editUiState = MutableStateFlow(EditUiState())
    val editUiState: StateFlow<EditUiState> = _editUiState.asStateFlow()

    fun toggleSelection(id: Int) {
        val currentSelected = _editUiState.value.selectedIds
        val newSelected =
            if (currentSelected.contains(id)) {
                currentSelected - id
            } else {
                currentSelected + id
            }
        _editUiState.value = _editUiState.value.copy(
            selectedIds = newSelected,
            isEditMode = newSelected.isNotEmpty()
        )
    }

    fun clearEditUiState() {
        _editUiState.value = EditUiState()
    }
}
