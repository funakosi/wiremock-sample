# wiremock-sample
sample for wiremock (with google-http-java-client)

JavaのHTTPクライアントクラス作ったとき、Wiremockでmockサーバ作れば最強に便利じゃんと知ったのでサンプル書いた。

`MyHttpClient#fetchStatus(String endpoint)`

endpoint (URL) にGETでアクセスし、HTTPステータスコードの5倍の値を返すHTTPクライアントメソッドです。

`MyHttpClientTest`

このテストクラスの中で、`http://localhost:8089/200`や`http://localhost:8089/302`等を用意し、テストクラスの中だけでmockサーバの起動とテストを完結させている。
これまではローカルホストで別途mockサーバ立ち上げるか、コンテナとか用意しないといけないと思ってたので便利だと思う
