package com.myapp.repositorysearcher.ui.search

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.myapp.repositorysearcher.R
import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.data.repository.GitHubLocalRepository
import com.myapp.repositorysearcher.data.repository.GitHubRepository
import com.myapp.repositorysearcher.ui.common.EditViewModel
import com.myapp.repositorysearcher.ui.common.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val gitHubRepository: GitHubRepository,

    private val localRepository: GitHubLocalRepository
) : EditViewModel() {
    private val _rawSearchResult =
        MutableStateFlow<List<GitHubRepositoryEntity>>(emptyList())
    private val _status = MutableStateFlow<Status>(Status.Idle)
    private val _sortOrder = MutableStateFlow<SortOrder>(SortOrder.STARS_DESC)
    private val _saveOrDeleteEvent = Channel<String>()
    val saveOrDeleteEvent = _saveOrDeleteEvent.receiveAsFlow()

    val favoriteRepositories: StateFlow<List<GitHubRepositoryEntity>> =
        localRepository.allRepositories
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _isFavoriteMode = MutableStateFlow(false)
    val isFavoriteMode = _isFavoriteMode.asStateFlow()

    val currentList: StateFlow<List<GitHubRepositoryEntity>> = combine(
        _rawSearchResult,
        favoriteRepositories,
        _isFavoriteMode
    ) { result, favorite, isFavMode ->
        when (isFavMode) {
            true -> favorite
            false -> result
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<SearchUiState> = combine(
        currentList,
        _status,
        _sortOrder,
        editUiState,
        _isFavoriteMode
    ) { currentList, status, order, editUiState, isFavMode ->
        if (isFavMode) {
            val sortedFavorites = when (order) {
                SortOrder.STARS_DESC -> currentList.sortedByDescending { it.stars }
                SortOrder.STARS_ASC -> currentList.sortedBy { it.stars }

            }
            return@combine SearchUiState.Success(
                repositories = sortedFavorites,
                selectedIds = editUiState.selectedIds,
                isEditMode = editUiState.isEditMode,
            )
        }
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
            _rawSearchResult.update { emptyList() }
            return
        }

        viewModelScope.launch {
            _status.update { Status.Loading }
            val result = gitHubRepository.searchRepositories(query)

            result.fold(
                onSuccess = { repositories ->
//                    _rawSearchResult.update { mockRepos }
                    _rawSearchResult.update { repositories }
                    _status.update { Status.Success }
                },
                onFailure = { error ->
                    _status.update {
                        Status.Error(
                            error.message ?: context.getString(R.string.error, error.message)
                        )
                    }
                }
            )
        }
    }

    fun saveSelectedItems(repositories: List<GitHubRepositoryEntity>) {
        viewModelScope.launch {
            _status.update { Status.Loading }
            try {
                val itemsToSave = repositories.filter { repository ->
                    editUiState.value.selectedIds.contains(repository.id)
                }

                if (itemsToSave.isNotEmpty()) {
                    localRepository.saveAll(itemsToSave)
                    val count = itemsToSave.size
                    val msg = context.getString(R.string.save_item_count, count)
                    _saveOrDeleteEvent.send(msg)
                    clearEditUiState()
                    _status.update { Status.Success }
                }
            } catch (e: Exception) {
                val errorMsg = context.getString(R.string.error, e.message)
                _saveOrDeleteEvent.send(errorMsg)
            }
        }
    }

    fun deleteSelectedItems(repositories: List<GitHubRepositoryEntity>) {
        viewModelScope.launch {
            try {
                val itemsToDelete = repositories.filter { repository ->
                    editUiState.value.selectedIds.contains(repository.id)
                }

                if (itemsToDelete.isNotEmpty()) {
                    val count = itemsToDelete.size
                    _saveOrDeleteEvent.send(context.getString(R.string.delete_item_count, count))
                    localRepository.deleteRepositories(itemsToDelete.map { it.id })
                    clearEditUiState()
                }
            } catch (e: Exception) {
                _saveOrDeleteEvent.send(context.getString(R.string.failure_delete))
            }
        }
    }
}

//private val mockRepos = List(20) {
//    GitHubRepositoryEntity(
//        id = it,
//        name = "Kotlin",
//        ownerName = "JetBrains",
//        avatarUrl = "https://...",
//        language = "The Kotlin Language",
//        stars = it + 100,
//        repoUrl = "https://...${it + 100}"
//    )
//}
