package com.myapp.repositorysearcher.di

import com.myapp.repositorysearcher.data.api.GitHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true // 知らないキーは無視
        coerceInputValues = true // nullの場合はデフォルト値が入る
        isLenient = true // ""で囲われてない値も許可される？
    }
    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }
}