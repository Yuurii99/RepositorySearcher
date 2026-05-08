package com.myapp.repositorysearcher.domain.model

// 「検索結果リスト」の要素には、各リポジトリの「リポジトリ名」・「作者/組織名」・「言語名」・「スター数」が表示されていること。
data class GitHubRepositoryEntity(
    // アプリ内専用のモデル（ドメイン層）
    val name: String, // リポジトリ名
    val ownerName: String, // 作者名
    val avatarUrl: String, // アバター画像URL
    val language: String, // プログラミング言語名
    val stars: Int, // スター数
    val repoUrl: String // 詳細画面への遷移先URL
)