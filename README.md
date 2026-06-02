# ポートフォリオについて
- GitHub公式API search repositoriesを活用し、アプリ内で入力したクエリにて検索を行い、表示するアプリです。
- リポジトリを表示する一覧画面、リポジトリをタップして遷移する詳細画面で構成されています。
- Google公式アプリを意識したUIデザインを狙い、MaterialThemeを使用しています。
- Google推奨の開発手法を学ぶため、MVVMアーキテクチャを採用しています。

## 一覧画面について
- クエリによって取得したリポジトリをリスト形式で表示します。
- リポジトリを長押しすると編集モードに入る事ができます。
- 編集モードに入ると、リポジトリをタップで選択後、画面右下のボタンで保存する事ができます。
- 保存したリポジトリは、画面左上の♡ボタンでお気に入りモードに入り表示します。
- お気に入りモード中に編集モードに入ると、保存と同様の操作で削除を行うことが出来ます。
- 画面右上のソートボタンを押すと、スター数を基準として昇順・降順どちらでソートするか選択できます。

## 詳細画面について
- リポジトリのURLをWebViewで表示します。
- GitHubドメイン内であればアプリ内でそのままリポジトリを閲覧できます。
- WebView内で戻る動作をした際、戻る事が出来る場合は一つ前のページに戻り、無い場合は一覧画面へ戻ります。
- 外部リンクをタップした際は、警告を出した後、ユーザーの了解を得た場合は外部ブラウザでリンク先を表示します。

## 選定ライブラリ
### [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation?hl=ja)
- 用途: UIフレームワーク
- 選定理由: Google推奨UIフレームワークのため・Stateに応じた動的表示のため

### [Room](https://developer.android.com/training/data-storage/room?hl=ja)
- 用途: ローカルDB (SQLite)
- 選定理由: リポジトリのローカル保存機能（お気に入り）実装のため

### [Kotlin Coroutines / Flow](https://developer.android.com/kotlin/flow?hl=ja)
- 用途: API通信・ローカルDBとの非同期処理
- 選定理由: Flowを活用したホットストリームによるリポジトリリストの動的表示を狙いました。

### [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja)
- 用途: 依存性注入による各クラスの疎結合化
- 選定理由: 実務に即した設計を学ぶために導入しました。

### [Coil](https://coil-kt.github.io/coil/)
- 用途: 非同期画像表示
- 選定理由: 非同期によるアバター画像表示のため

### [Retrofit2/OkHttp3](https://square.github.io/retrofit/)
- 用途: Httpクライアント作成
- 選定理由: Android標準なHTTPクライアントかつ、シリアライゼーション併用による型安全なレスポンス取得のため

### [Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md)
- 用途: JSONデシリアライズ
- 選定理由: APIから受け取ったJSONのデシリアライズのため
  
