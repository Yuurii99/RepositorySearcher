package com.myapp.repositorysearcher.di

import com.myapp.repositorysearcher.data.repository.GitHubRepositoryImpl
import com.myapp.repositorysearcher.domain.repository.GitHubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGitHubRepository(
        impl: GitHubRepositoryImpl
    ): GitHubRepository
}
