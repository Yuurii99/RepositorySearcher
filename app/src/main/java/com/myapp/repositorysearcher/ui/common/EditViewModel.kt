package com.myapp.repositorysearcher.ui.common

import android.util.Log
import androidx.lifecycle.ViewModel
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject


abstract class EditViewModel : ViewModel() {
    private val _editUiState = MutableStateFlow<EditUiState>(EditUiState())
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
        println("Select Repo Id : $id")
    }

    fun clearEditUiState() {
        _editUiState.value = EditUiState()
    }
}
