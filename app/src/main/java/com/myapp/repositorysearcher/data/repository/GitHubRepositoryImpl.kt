package com.myapp.repositorysearcher.data.repository

import com.myapp.repositorysearcher.data.api.GitHubApi
import com.myapp.repositorysearcher.data.model.RepositoryDto
import com.myapp.repositorysearcher.domain.model.GitHubRepositoryEntity
import com.myapp.repositorysearcher.domain.repository.GitHubRepository
import javax.inject.Inject


class GitHubRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi
) : GitHubRepository {

    // toDomainで実データ(json)からドメイン層に変換
    override suspend fun searchRepositories(query: String): Result<List<GitHubRepositoryEntity>> {
        return runCatching {
            val response = gitHubApi.searchRepositories(query)
            response.items.map { it.toDomain() } // DTOからDomainモデルへ変換
        }
    }

    private fun RepositoryDto.toDomain(): GitHubRepositoryEntity {
        return GitHubRepositoryEntity(
            name = this.name,
            ownerName = this.owner.login,
            avatarUrl = this.owner.avatarUrl,
            language = this.language ?: "",
            stars = this.stargazersCount,
            repoUrl = this.htmlUrl
        )
    }
}