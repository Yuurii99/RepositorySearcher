package com.myapp.repositorysearcher.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.domain.repository.GitHubRepository
import com.myapp.repositorysearcher.ui.common.EditUiState
import com.myapp.repositorysearcher.ui.common.EditViewModel
import com.myapp.repositorysearcher.ui.common.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections.list
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : EditViewModel() {
    private val _rawSearchResult =
        MutableStateFlow<List<GitHubRepositoryEntity>>(emptyList())
    private val _status = MutableStateFlow<Status>(Status.Idle)
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    private val _sortOrder = MutableStateFlow<SortOrder>(SortOrder.STARS_DESC)

    private val _isFavoriteMode = MutableStateFlow(false)
    val isFavoriteMode = _isFavoriteMode.asStateFlow()

    private val favoriteMockRepos = List(20) {
        GitHubRepositoryEntity(
            id = it,
            name = "favorite repo",
            ownerName = "JetBrains",
            avatarUrl = "https://...",
            language = "The Kotlin Language",
            stars = it + 100,
            repoUrl = "https://...${it + 100}"
        )
    }

    val uiState: StateFlow<SearchUiState> = combine(
        _rawSearchResult,
        _status,
        _sortOrder,
        editUiState,
        _isFavoriteMode
    ) { result, status, order, editUiState, isFavMode ->
        val currentList = if (isFavMode) favoriteMockRepos else result // mockReposはlocal DBから持ってくる物と置換

        when (status) {
            is Status.Loading -> SearchUiState.Loading
            is Status.Error -> SearchUiState.Error(status.message)
            is Status.Success -> {
                val sortedList = when (order) {
                    SortOrder.STARS_DESC -> currentList.sortedByDescending { it.stars }
                    SortOrder.STARS_ASC -> currentList.sortedBy { it.stars }
                }
                SearchUiState.Success(
                    repositories = sortedList,
                    selectedIds = editUiState.selectedIds,
                    isEditMode = editUiState.isEditMode,
                    isFavoriteMode = isFavMode
                )
            }

            is Status.Idle -> SearchUiState.Idle
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SearchUiState.Idle
    )

    enum class SortOrder {
        STARS_DESC,
        STARS_ASC
    }

    fun updateSortOrder(order: SortOrder) {
        _sortOrder.update { order }
    }

    fun toggleFavoriteMode() {
        _isFavoriteMode.update { !it }
    }

    fun repositorySearch(query: String) {
        if (query.isBlank()) {
            _status.update { Status.Idle }
            return
        }

        viewModelScope.launch {
            _status.update { Status.Loading }
//            val result = gitHubRepository.searchRepositories(query)
//            _uiState.update {
//                result.fold(
//                    onSuccess = { repositories ->
//                        if (repositories.isEmpty()) {
//                            SearchUiState.Error("該当するリポジトリが見つかりませんでした")
//                        } else {
//                            SearchUiState.Success(repositories)
//                        }
//                    },
//                    onFailure = { error ->
//                         _status.update { Status.Error(error.message ?: "リポジトリの取得に失敗しました") }
//                        SearchUiState.Error(error.message ?: "リポジトリの取得に失敗しました")
//                    }
//                )
//            }
            _rawSearchResult.update { mockRepos }
            _status.update { Status.Success }
        }
    }

    fun saveSelectedItems() {
        val selectedIds = editUiState.value.selectedIds
        viewModelScope.launch {
            // TODO room保存logic
            clearEditUiState()
        }
    }
}

private val mockRepos = List(20) {
    GitHubRepositoryEntity(
        id = it,
        name = "tinyTetrisC++",
        ownerName = "JetBrains",
        avatarUrl = "https://...",
        language = "The Kotlin Language",
        stars = it + 100,
        repoUrl = "https://...${it + 100}"
    )
}
