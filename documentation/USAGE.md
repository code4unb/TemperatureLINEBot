# 使い方
## 友達追加後にすること
1.  プライバシーポリシーを読む  
 準備中
1. ユーザー登録
    1. `登録`と入力して画面の指示に従ってください。  
        入力例)`3 1 8 畝傍 太郎`  
       スペースで区切ることを忘れないでください。
    1. `登録情報`と入力して表示される内容が正しいことを確認してください。
    1. 間違いがあった場合再度 1 からやり直してください。  
<img src="https://user-images.githubusercontent.com/38522336/124346548-66fc4000-dc1a-11eb-8e4f-5749907faaad.png" width=100>


## 検温を送信する
一部クラスの検温フォームでGoogleアカウントへのログインが必須となっているようです。  
該当するクラスは以下のスクリーンショットと少し挙動が異なりますが画面の指示に従ってください。  
**該当クラス(現時点)**: 1年1組  2年 2,7組

### その時の日付、午前/午後で入力する場合
1. 体温を数値で入力します。  
入力例)`36.2`
1. 表示される内容を確認して送信または修正して下さい。  
この作業を行った時の日付や午前午後がそのまま検温フォームに送信されます。
<img src="https://user-images.githubusercontent.com/38522336/124346553-754a5c00-dc1a-11eb-8112-68db28ea428f.png" width=100>  

### 日付や午前午後を指定して入力する  
1. `検温`と入力します。
1. 体温を入力します。  
入力例) `36.2`
1. 午前/午後を選択します。  
   スマートフォンの場合画面下部に選択肢が表示されます。  
   表示されない場合は直接入力してください。  
   入力例) `AM`
1. 日付を選択します。  
   スマートフォンの場合画面下部に選択肢が表示されます。
   表示されない場合は直接入力してください。(MM-DD形式)  
   入力例) `06-29`
1. 表示される内容を確認して送信または修正して下さい。  
   <img src="https://user-images.githubusercontent.com/38522336/124346554-77acb600-dc1a-11eb-816f-a92a72c701b7.png" width=100>

## Q&A
1. コマンドの途中で終了したい  
    →"終了"と入力するとセッションを終了することができます
2. 登録がうまくできない  
    →クラス組番号氏名がスペースで区切られていることを確認してください。

## コマンド一覧
|コマンド|説明|使い方|別名|
|-----|-----|-----|-----|
|コマンド|コマンド一覧を表示する| |commands|
|登録|ユーザー情報の登録、変更| |register|
|登録情報|登録済みのユーザー情報を確認| | |
|クラス|対応しているクラスを表示| |対応ホームルーム一覧,対応,対応クラス,ホームルーム|
|バージョン|LineBotのバージョンを確認(開発者向け)| | version |
| 検温 | 体温を入力| 画面の指示 | 入力、体温入力、体温、検温入力|
| - | 体温を入力| 例)36.2 AM 06-29| |

