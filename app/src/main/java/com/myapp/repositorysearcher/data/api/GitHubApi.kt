package com.myapp.repositorysearcher.data.api

import com.myapp.repositorysearcher.data.model.RepositorySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    // 一覧
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30 // 最大表示件数 機能追加するかも？
    ): RepositorySearchResponse
}
