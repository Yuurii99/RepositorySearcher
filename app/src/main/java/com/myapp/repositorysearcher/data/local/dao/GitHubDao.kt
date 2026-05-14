package com.myapp.repositorysearcher.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GitHubDao {
    @Query("SELECT * FROM favorite_repositories")
    fun getAllRepositories(): Flow<List<GitHubRepositoryEntity>>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertRepository(repository: GitHubRepositoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<GitHubRepositoryEntity>)

    @Query("DELETE FROM favorite_repositories WHERE id IN (:ids)")
    suspend fun deleteMultiple(ids: List<Int>)
}