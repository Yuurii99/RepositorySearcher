package com.myapp.repositorysearcher.domain.repository

import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity

interface GitHubRepository {
    // ドメイン層だから内部実装は気にしないようにする
    suspend fun searchRepositories(query: String): Result<List<GitHubRepositoryEntity>>
    suspend fun getRepositoryDetails(htmlUrl: String)
}