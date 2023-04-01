# 検温入力 LINE BOT
本番用LINEアカウント   
　<a href="https://lin.ee/Nl4njFS"><img src="https://scdn.line-apps.com/n/line_add_friends/btn/ja.png" alt="友だち追加" height="36" border="0"></a>   

開発用LINEアカウント   
　<a href="https://lin.ee/ZNs4VXX"><img src="https://scdn.line-apps.com/n/line_add_friends/btn/ja.png" alt="友だち追加" height="36" border="0"></a>
 
## [使い方](https://github.com/code4unb/TemperatureLINEBot/blob/document/documentation/USAGE.md#%E4%BD%BF%E3%81%84%E6%96%B9)

## 開発者向け情報
### 環境構築 スタンドアローン
#### 環境
- Java : `11.0.11`
- Gradle : `6.8`
- DB : `dev-stage:h2 prod-stage:Postgresql`
#### セットアップ
`gradlew setup`   
#### 環境変数
| 変数名 | 値| 説明 |
----|----|----
| LINE_BOT_CHANNEL_SECRET | 任意 | LINEAPI シークレット |
| LINE_BOT_CHANNEL_TOKEN | 任意 | LINEAPI トークン |
| SPRING_PROFILES_ACTIVE | dev or prod | dev を設定すればDBが/h2db/に生成される|

### 環境構築 Docker Compose
#### ビルド
```
set SPRING_PROFILES_ACTIVE=prod
./gradlew build
```
#### UP
※注意 ssl通信必須
```
docker-compose -f docker-compose-shared.yml -p "temperaturelinebot_shared" up -d
./gradlew composeUp
```
| 変数名 | 値| 説明 |
----|----|----
| LINE_BOT_CHANNEL_SECRET | 任意 | LINEAPI シークレット |
| LINE_BOT_CHANNEL_TOKEN | 任意 | LINEAPI トークン |
| IMAGE_NAME | 任意 | 作成されるコンテナイメージの名前|
| CONTAINER_NAME | 任意 | 作成されるコンテナの名前|
| POSTGRES_DB | LineBot_Data-xxx | xxxの部分をPRODUCTIONとそろえる|
| PRODUCTION | prod/dev | prodの場合バージョンがSNAPSHOTになる|
| POSTGRES_PASSWORD | 任意 | DBのパスワード|
| PUBLISHED_PORT | 443 | exposeされるポート|
