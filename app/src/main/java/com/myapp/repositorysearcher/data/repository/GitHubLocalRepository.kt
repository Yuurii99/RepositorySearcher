package com.myapp.repositorysearcher.data.repository

import com.myapp.repositorysearcher.data.local.dao.GitHubDao
import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity
import javax.inject.Inject

class GitHubLocalRepository @Inject constructor(
    private val dao: GitHubDao
) {
    val allRepositories = dao.getAllRepositories()

//    suspend fun save(repository: GitHubRepositoryEntity) {
//        dao.insertRepository(repository)
//    }

    suspend fun saveAll(repositories: List<GitHubRepositoryEntity>) {
        dao.insertAll(repositories)
    }

    suspend fun deleteRepositories(ids: List<Int>) {
        dao.deleteMultiple(ids)
    }
}