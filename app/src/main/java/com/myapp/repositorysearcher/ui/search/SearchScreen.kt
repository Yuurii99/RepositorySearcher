package com.myapp.repositorysearcher.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.myapp.repositorysearcher.R
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("リポジトリ検索ワード") },
            trailingIcon = {
                IconButton(onClick = { viewModel.repositorySearch(query) }) {
                    Icon(painterResource(R.drawable.search), contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = uiState) {
            is SearchUiState.Idle -> Text("検索ワードを入力してください")
            is SearchUiState.Loading -> CircularProgressIndicator()
            is SearchUiState.Error -> Text("エラー: ${state.message}", color = Color.Red)
            is SearchUiState.Success -> {
                LazyColumn {
                    items(state.repositories) { repository ->
                        RepositoryItem(
                            repoItem = repository,
                            onClick = {
                                onItemClick(
                                    repository.repoUrl
                                )
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun RepositoryItem(
    repoItem: GitHubRepositoryEntity,
    onClick: () -> Unit
    // onItemClick 詳細画面遷移
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            AsyncImage(
                model = repoItem.avatarUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = repoItem.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "★ ${repoItem.stars}", style= MaterialTheme.typography.bodySmall)
            }
        }
    }
}