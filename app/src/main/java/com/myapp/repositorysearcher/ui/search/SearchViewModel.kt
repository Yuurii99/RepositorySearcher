package com.myapp.repositorysearcher.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    private val _sortOrder = MutableStateFlow<SortOrder>(SortOrder.STARS_DESC)
    val uiState: StateFlow<SearchUiState> = combine(
        _uiState,
        _sortOrder
    ) { state, order ->
        when (state) {
            is SearchUiState.Loading -> SearchUiState.Loading
            is SearchUiState.Error -> SearchUiState.Error(state.message)
            is SearchUiState.Idle -> SearchUiState.Idle
            is SearchUiState.Success -> {
                val sortedList = when (order) {
                    SortOrder.STARS_DESC -> state.repositories.sortedByDescending { it.stars }
                    SortOrder.STARS_ASC -> state.repositories.sortedBy { it.stars }
                }
                SearchUiState.Success(sortedList)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState.Idle)

    enum class SortOrder {
        STARS_DESC,
        STARS_ASC
    }

    fun updateSortOrder(order: SortOrder) {
        _sortOrder.update { order }
    }

    fun repositorySearch(query: String) {
        if (query.isBlank()) {
            _uiState.update { SearchUiState.Idle }
            return
        }

        viewModelScope.launch {
            _uiState.update { SearchUiState.Loading }
            val result = gitHubRepository.searchRepositories(query)
            _uiState.update {
                result.fold(
                    onSuccess = { repositories ->
                        if (repositories.isEmpty()) {
                            SearchUiState.Error("該当するリポジトリが見つかりませんでした")
                        } else {
                            SearchUiState.Success(repositories)
                        }
                    },
                    onFailure = { error ->
                        SearchUiState.Error(error.message ?: "リポジトリの取得に失敗しました")
                    }
                )
            }
        }
    }
}