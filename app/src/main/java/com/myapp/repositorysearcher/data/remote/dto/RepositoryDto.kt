package com.myapp.repositorysearcher.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositorySearchResponse(
    @SerialName("items") val items: List<RepositoryDto>
)

// 「検索結果リスト」の要素には、各リポジトリの「リポジトリ名」・「作者/組織名」・「言語名」・「スター数」が表示されていること。
@Serializable
data class RepositoryDto(
    val id: Int, // リポID
    val name: String, // リポジトリ名
    val owner: OwnerDto, // 作者/組織名 + アバター画像URL
    val language: String?, // プログラミング言語名
    @SerialName("html_url") val htmlUrl: String, // 詳細画面での遷移先URL
    @SerialName("stargazers_count") val stargazersCount: Int // スター数
)

@Serializable
data class OwnerDto(
    val login: String, // 作者名?
    @SerialName("avatar_url") val avatarUrl: String // アバター画像
)