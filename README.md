# 課題について
## 一覧画面・詳細画面
- 要件定義された機能は全て実装しました。
- 詳細画面については、WebViewを使用しており、GitHubドメイン内であれば外部ブラウザで開かずにそのまま表示します。
- それ以外のリンクについてはAlertDialogを出し、同意を得た際に外部ブラウザでリンクを開きます。

## 自由実装について
### スター数による昇順・降順ソート機能
- APIから取得したリポジトリのスター数によって昇順・降順にソート機能を実装。  
- クエリバー右上にソートボタンを配置し、押すとダイアログが表示され、昇順・降順を選べます。

### ローカルDBによるリポジトリ保存機能
- APIから取得したリポジトリをお気に入りとして保存する機能を実装。
- リポジトリを長押しすると編集モードに入り、右下のアクションボタンでタップしたリポジトリの保存を行います。
- 保存したリポジトリは、クエリバー左上の♡ボタンでトグルするお気に入りモードで表示します。
- お気に入りモード中、編集モードに入ると右下のアクションボタンが削除ボタンになり、お気に入りから削除できます。
- APIで取得した物と同様、ソートが可能です。

### その他
- リポジトリの作者アバターURLを受け取り、Coilライブラリで表示しています。
- 詳細画面で戻るボタンを押すとネイティブアプリまで戻ってしまいますが、View内で戻れる履歴がある場合は一つ前のディレクトリに戻れるようにしています。
- Google公式アプリに寄せた見た目にしたく思い、MaterialThemeを多用しています。
- Google推奨の開発手法で進めてみようと思い、MVVMアーキテクチャを採用しました。

## 選定ライブラリ
### [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation?hl=ja)
- 用途: UIフレームワーク
- 選定理由: Google推奨UIフレームワークのため・Stateに応じた動的表示のため

### [Room](https://developer.android.com/training/data-storage/room?hl=ja)
- 用途: ローカルDB (SQLite)
- 選定理由: リポジトリのローカル保存機能（お気に入り）実装のため

### [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja)
- 用途: 依存性注入による各クラスの疎結合化
- 選定理由: 以前作成したポートフォリオにてモックデータの注入がし辛いと感じたため導入しました。
- ただし、MVVMアーキテクチャを含め、活用が出来ていないなと個人的に感じました...。

### [Coil](https://coil-kt.github.io/coil/)
- 用途: 非同期画像表示
- 選定理由: 非同期によるアバター画像表示のため

### [Retrofit2/OkHttp3](https://square.github.io/retrofit/)
- 用途: Httpクライアント作成
- 選定理由: Android標準なHTTPクライアントかつ、シリアライゼーション併用による型安全なレスポンス取得のため

### [Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md)
- 用途: JSONデシリアライズ
- 選定理由: APIから受け取ったJSONのデシリアライズのため
  
