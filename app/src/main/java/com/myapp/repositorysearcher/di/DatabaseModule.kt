package com.myapp.repositorysearcher.di

import android.content.Context
import androidx.room.Room
import com.myapp.repositorysearcher.data.local.AppDatabase
import com.myapp.repositorysearcher.data.local.dao.GitHubDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "github_db"
        ).build()
    }
    @Provides
    fun provideGitHubDao(db: AppDatabase): GitHubDao {
        return db.githubDao()
    }
}