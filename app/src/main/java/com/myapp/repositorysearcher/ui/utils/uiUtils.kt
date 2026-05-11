package com.myapp.repositorysearcher.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.repositorysearcher.R
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.ui.search.QueryInputField
import com.myapp.repositorysearcher.ui.search.RepositoryItem
import com.myapp.repositorysearcher.ui.search.SearchContentView
import com.myapp.repositorysearcher.ui.search.SearchUiState

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewSearchScreen_Success() {
    val mockRepos = List(20) {
        GitHubRepositoryEntity(
            name = "tinyTetrisC++",
            ownerName = "JetBrains",
            avatarUrl = "https://...",
            language = "The Kotlin Language",
            stars = 4500,
            repoUrl = "https://..."
        )
    }

    val uiState = SearchUiState.Success(mockRepos)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {  }) { Text("閉じる") }
            },
            title = { Text("ソート順を選択") },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("スター数：多い順") },
                        leadingContent = { Icon(painterResource(R.drawable.arrow_upward), null) },
                        modifier = Modifier.clickable {
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                    ListItem(
                        headlineContent = { Text("スター数：少ない順") },
                        leadingContent = { Icon(painterResource(R.drawable.arrow_downward), null) },
                        modifier = Modifier.clickable {
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }
        )
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
                    Spacer(modifier = Modifier.height(60.dp))
                    Icon(
                        painterResource(R.drawable.octocat),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .size(120.dp)
                            .align(Alignment.Center)
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        IconButton(
                            onClick = { },
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
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background,
                        tonalElevation = 2.dp
                    ) {
                        QueryInputField(
                            onSearchClick = { }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                }
                if (uiState is SearchUiState.Success) {
                    items(uiState.repositories) { repository ->
                        RepositoryItem(
                            repoItem = repository,
                            onItemClick = {}
                        )
                    }
                } else {
                    item {
                        SearchContentView(uiState)
                    }
                }
            }
        }
    }
}