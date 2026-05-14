package com.myapp.repositorysearcher.ui.search

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.myapp.repositorysearcher.R
import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showSortDialog by remember { mutableStateOf(false) }
    val isEditMode = (uiState as? SearchUiState.Success)?.isEditMode ?: false
    val isFavoriteMode = viewModel.isFavoriteMode.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.saveOrDeleteEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    BackHandler(enabled = isEditMode) {
        viewModel.clearEditUiState()
    }
    Scaffold(
        topBar = {
            if (isEditMode) {
                TopAppBar(
                    title = { Text(
                        stringResource(
                            R.string.select_item_count,
                            (uiState as SearchUiState.Success).selectedIds.size
                        )) },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.clearEditUiState() }) {
                            Icon(
                                painterResource(R.drawable.check),
                                contentDescription = stringResource(R.string.edit_mode_cancel),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        },
        floatingActionButton = {
            when {
                isEditMode && isFavoriteMode -> { // DELETE
                    FloatingActionButton(onClick = {
                        viewModel.deleteSelectedItems(
                            (uiState as SearchUiState.Success).repositories
                        )
                    }
                    ) {
                        Icon(
                            painterResource(R.drawable.delete),
                            contentDescription = stringResource(R.string.delete_favorite)
                        )
                    }
                }
                isEditMode -> { // SAVE
                    FloatingActionButton(onClick = {
                        viewModel.saveSelectedItems(
                            (uiState as SearchUiState.Success).repositories
                        )
                    }
                    ) {
                        Icon(
                            painterResource(R.drawable.favorite),
                            contentDescription = stringResource(R.string.save_favorite)
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val currentUiState = uiState
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(all = 16.dp)
            ) {
                item {
                    Icon(
                        painterResource(R.drawable.octocat),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .size(120.dp)
                            .align(Alignment.Center)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isFavoriteMode) MaterialTheme.colorScheme.primaryContainer
                                    else Color.Transparent
                                )
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.toggleFavoriteMode()
                                }
                            ) {
                                Icon(
                                    painterResource(R.drawable.favorite),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        IconButton(
                            onClick = { showSortDialog = true },
                        ) {
                            Icon(
                                painterResource(R.drawable.sort_icon),
                                contentDescription = "Sort",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                stickyHeader {
                    AnimatedVisibility(
                        visible = !isEditMode && !isFavoriteMode,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            color = MaterialTheme.colorScheme.background,
                            tonalElevation = 2.dp
                        ) {
                            QueryInputField(
                                onSearchClick = { viewModel.repositorySearch(it) }
                            )
                        }
                    }
                }

                if (currentUiState is SearchUiState.Success) { // Lazy入れ子回避のためここでitems
                    if (currentUiState.repositories.isEmpty()) {

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.nothing_favorite_items))
                            }
                        }
                    } else {
                        items(currentUiState.repositories, key = { it.id }) { repo ->
                            RepositoryItem(
                                repoItem = repo,
                                isSelected = currentUiState.selectedIds.contains(repo.id),
                                onItemClick = {
                                    if (currentUiState.isEditMode) {
                                        viewModel.toggleSelection(repo.id)
                                    } else {
                                        onItemClick(repo.repoUrl) // repoURLに遷移
                                    }
                                },
                                onLongClick = {
                                    viewModel.toggleSelection(repo.id)
                                }
                            )
                        }
                    }
                } else {
                    item {
                        SearchContentView(currentUiState, isFavoriteMode)
                    }
                }
            }
        }
        if (showSortDialog) {
            AlertDialog(
                onDismissRequest = { showSortDialog = false },
                confirmButton = {
                    TextButton(onClick = { showSortDialog = false }) { Text(stringResource(R.string.close)) }
                },
                title = { Text(stringResource(R.string.select_sort_order)) },
                text = {
                    Column {
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.sort_order_starsDESC)) },
                            leadingContent = {
                                Icon(
                                    painterResource(R.drawable.arrow_downward),
                                    null
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.updateSortOrder(SearchViewModel.SortOrder.STARS_DESC)
                                showSortDialog = false
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            )
                        )
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.sort_order_stars_ASC)) },
                            leadingContent = {
                                Icon(
                                    painterResource(R.drawable.arrow_upward),
                                    null
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.updateSortOrder(SearchViewModel.SortOrder.STARS_ASC)
                                showSortDialog = false
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SearchContentView(
    uiState: SearchUiState,
    isFavoriteMode: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when (uiState) {
            is SearchUiState.Empty -> Text(
                if (isFavoriteMode) stringResource(R.string.nothing_favorite_items)
                else stringResource(R.string.nothing_search_result)
            )
            is SearchUiState.Loading -> CircularProgressIndicator()
            is SearchUiState.Error -> Text(stringResource(R.string.error, uiState.message), color = Color.Red)
            is SearchUiState.Idle -> {Text(stringResource(R.string.idle_query))}
            is SearchUiState.Success -> {} // LazyColumn入れ子できない
        }
    }
}

@Composable
fun QueryInputField(
    onSearchClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { Text(stringResource(R.string.query_placeholder)) },
        trailingIcon = {
            IconButton(onClick = {
                onSearchClick(query)
                keyboardController?.hide()
            }) {
                Icon(painterResource(R.drawable.search), contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchClick(query)
            keyboardController?.hide()
        })
    )
}

@Composable
fun RepositoryItem(
    repoItem: GitHubRepositoryEntity,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .combinedClickable(
                    onClick = onItemClick,
                    onLongClick = onLongClick,
                ),
            colors = CardDefaults.cardColors(
                containerColor =
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
            ),
            border = if (isSelected) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                null
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                AsyncImage(
                    model = repoItem.avatarUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    error = painterResource(R.drawable.no_avatar),
                    placeholder = painterResource(R.drawable.no_avatar),
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = repoItem.name,
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(
                            text = repoItem.ownerName,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = repoItem.language,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(
                            text = "★ ${repoItem.stars}",
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}