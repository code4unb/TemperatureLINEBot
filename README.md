# 検温入力 LINE BOT
<a href="https://lin.ee/ZNs4VXX"><img src="https://scdn.line-apps.com/n/line_add_friends/btn/ja.png" alt="友だち追加" height="36" border="0"></a>
## 使い方
## 開発者向け情報
### 環境構築
#### 環境
- Java : `1.8.0`
- Gradle : `7.0.2`
- DB : `h2-database with PostgreSQL`
#### セットアップ
`gradlew setup`   
#### 環境変数
| 変数名 | 値| 説明 |
----|----|----
| LINE_BOT_CHANNEL_SECRET | 任意 | LINEAPI シークレット |
| LINE_BOT_CHANNEL_TOKEN | 任意 | LINEAPI トークン |
| SPRING_PROFILES_ACTIVE | dev or prod | dev を設定すればDBが/h2db/に生成される|

### コーディングルール

### コミットガイドライン
コミットルールは基本的に *[Angular.jsのもの](https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#-git-commit-guidelines)* に準じます。  
また、コミットメッセージは英語を基本とします。