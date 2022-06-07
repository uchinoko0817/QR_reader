# 概要
KotlinでのAndroid開発を学習するために作成した簡易的なQRコード読取アプリです。

QRコードを読取り、値をURIとして暗黙的なIntentを発行するか、クリップボードに値をコピーするかを選択できます。
また、読み取った値はアプリ内のデータベースに保存され、後から確認することができます。

# 使用ライブラリ等
- バーコードスキャンライブラリ：[zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded)
- OSSライセンス表示ライブラリ：[oss-licenses-plugin](https://github.com/google/play-services-plugins/tree/master/oss-licenses-plugin)
- データベース：SQLite