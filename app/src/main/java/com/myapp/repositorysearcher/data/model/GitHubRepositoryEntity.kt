package com.myapp.repositorysearcher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 「検索結果リスト」の要素には、各リポジトリの「リポジトリ名」・「作者/組織名」・「言語名」・「スター数」が表示されていること。
@Entity(tableName = "favorite_repositories")
data class GitHubRepositoryEntity(
    // アプリ内専用のモデル（ドメイン層）
    @PrimaryKey val id: Int, // リポID
    val name: String, // リポジトリ名
    val ownerName: String, // 作者名
    val avatarUrl: String, // アバター画像URL
    val language: String, // プログラミング言語名
    val stars: Int, // スター数
    val repoUrl: String // 詳細画面への遷移先URL
)