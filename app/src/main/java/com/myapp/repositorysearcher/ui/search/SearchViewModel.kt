package com.myapp.repositorysearcher.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.repositorysearcher.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow() // 読み取り専用

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