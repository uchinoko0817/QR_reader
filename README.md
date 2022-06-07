# 概要
KotlinでのAndroid開発を学習するために作成した簡易的なQRコード読取アプリです。

QRコードを読取り、値をURIとして暗黙的なIntentを発行するか、クリップボードに値をコピーするかを選択できます。
また、読み取った値はアプリ内のデータベースに保存され、後から確認することができます。

バーコードスキャン用ライブラリに[zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded)を、データベースにSQLiteを使用しています。