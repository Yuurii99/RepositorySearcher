package com.myapp.repositorysearcher.data.repository

import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity

interface GitHubRepository {
    suspend fun searchRepositories(query: String): Result<List<GitHubRepositoryEntity>>
}