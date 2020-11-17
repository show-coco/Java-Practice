# 設計について

設計手法として、ドメイン駆動設計を採用しています。<br>
参考: [ドメイン駆動設計入門 ボトムアップでわかる! ドメイン駆動設計の基本](https://www.amazon.co.jp/%E3%83%89%E3%83%A1%E3%82%A4%E3%83%B3%E9%A7%86%E5%8B%95%E8%A8%AD%E8%A8%88%E5%85%A5%E9%96%80-%E3%83%9C%E3%83%88%E3%83%A0%E3%82%A2%E3%83%83%E3%83%97%E3%81%A7%E3%82%8F%E3%81%8B%E3%82%8B-%E3%83%89%E3%83%A1%E3%82%A4%E3%83%B3%E9%A7%86%E5%8B%95%E8%A8%AD%E8%A8%88%E3%81%AE%E5%9F%BA%E6%9C%AC-%E6%88%90%E7%80%AC-%E5%85%81%E5%AE%A3/dp/479815072X/ref=sr_1_1?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&dchild=1&keywords=%E3%83%89%E3%83%A1%E3%82%A4%E3%83%B3%E9%A7%86%E5%8B%95%E8%A8%AD%E8%A8%88&qid=1605611496&sr=8-1)

# ドメインオブジェクト 

[ディレクトリ](https://github.com/show-coco/Java-Practice/tree/master/ITS/src/domain)<br>
ドメインオブジェクトには**値オブジェクト**と**エンティティ**が存在します。
作成する対象物の知識(ドメイン知識)をドメインオブジェクトとしてプログラム化します。

## 値オブジェクト

値オブジェクトは値をオブジェクトとして表したものです。StringやIntegerのラッパークラスも値オブジェクトに当たります。

例えばこのような仕様があったとします。
- メールアドレスは必須項目でメールアドレスの形式であること(「@」や「.」が含まれている) 
- パスワードは必須項目で8文字以上であること

これらはそれぞれ次のようなコードになります。
- [メールアドレス](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/domain/employee/MailAddress.java)
- [パスワード](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/domain/employee/EmpPassword.java)

こうすることで、仕様変更に強くなります。
なぜ仕様変更に強くなるのかを説明するために、パスワードを例にあげてみます。
パスワードの仕様が関係すると予想される場所は社員作成と社員情報変更の部分です。CRUDのC(作成)U(更新)に当たります。パスワードの強度をもっと上げるために、小文字、大文字、数字を含むように変更するとします。このとき本来であれば、それぞれの実装部分(作成と更新)で判定処理をしている場合、2箇所変更する必要があります。あるいは更に変更部分があるかもしれないため探さなければなりません。もし全て見つけ出せなければバグが混入します。

値オブジェクトとして定義していれば、生成する際に判定されるので一箇所変更すれば良いことになります。

## エンティティ

エンティティは値オブジェクトで構成されます。エンティティはライフサイクルを持っていて可変です。社員を例にあげて説明してみます。

社員はパスワードや社員ID, 性別などの値(値オブジェクト)を保持しています。そして、社員は生成、変更、削除のライフサイクルがあり、「変更」の部分で値を変更することができます。

例:
- [社員エンティティ](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/domain/employee/Employee.java)
- [出退勤状況エンティティ](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/domain/attend/AttendStatus.java)

# ドメインサービス

ドメインサービスはドメインオブジェクトに関する処理の不自然さを解消します。

たとえば、同じ社員IDが既に存在するかを確認する処理を実装するとします。これが社員エンティティ自体に存在すると不自然です。
なぜなら、生成した社員エンティティ自身に既に存在するかを聞くことになるからです。これは多くの開発者を混乱させます。

そのため、これをドメインのためのサービスとして外に出してあげます。外に出されたものをドメインサービスとして定義します。
こうすることで不自然さを解消できます

例: [社員サービス](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/domain/employee/EmpService.java)

# リポジトリ

リポジトリはエンティティの永続化を担当します。永続化処理をビジネスロジックと一緒に記述してしまうとビジネスロジックがぼやけ、理解し難くなってしまいます。永続化の処理を外に出してあげることで、ビジネスロジックを明確化します

例: 
- [社員リポジトリ](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/repository/employee/InMemoryEmpRepo.java)
- [出退勤状況リポジトリ](https://github.com/show-coco/Java-Practice/blob/master/ITS/src/repository/attend/InMemoryAttendRepo.java)

