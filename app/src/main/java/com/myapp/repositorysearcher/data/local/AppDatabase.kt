package com.myapp.repositorysearcher.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.repositorysearcher.data.local.dao.GitHubDao
import com.myapp.repositorysearcher.data.model.GitHubRepositoryEntity

@Database(entities = [GitHubRepositoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubDao(): GitHubDao
}