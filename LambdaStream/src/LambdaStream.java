
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStream {

	public static void main(String[] args) throws Exception {
/**************************************** ラムダ式 ****************************************/
/*	ラムダ式(lambda expression)は、受け渡すことができるコードブロックである。
 *	これは、渡された後、時間的に経過してから1回以上実行すること(これを遅延実行<deferred execution>と呼ぶ)が可能である。
 *	従来(Java7まで)では、このようなコードブロックをどこで使用してきたかというと．．．
 *		(例1)別スレッドで処理をしたい→Runnableインタフェースをimplementsしてrunメソッドで実装していた	*/
  			class Worker implements Runnable {
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) doWork();
				}

				private void doWork() { /* ダミー*/ }
  			}
/*	このスレッドを実行するには、Workerクラスをインスタンス化して、スレッドを開始する。	*/
 		Worker w = new Worker();
 		new Thread(w).start();
/*	ここで重要なのは、runメソッドには、別スレッドで実行したいコードが含まれている(コードブロック)ということである。
 *	また、実際にスレッド処理されるのは、class宣言した時ではなく、スレッドを開始するまで実行が遅延される。
 *
 *		(例2)独自のコンパレータで文字列をソートする：デフォルトの辞書順ではなく長さで文字列を昇順ソートする
 *		オブジェクトの大小はcompare(Object o1, Object o2)の戻り値で決定(o1 < o2 → 負、o1 = o2 → ゼロ、o1 > o2 → 正)*/
			class LenCompa implements Comparator<String> {
				@Override
				public int compare(String first, String second) {
					return Integer.compare(first.length(), second.length());
					// x - yのような演算にしないこと：オーバーフローの危険性：Integer.compareを使用する(Java7)
					// (参考)降順であれば、第1引数と第2引数を逆にする
				}
			}
/*	独自コンパレータが作成できたならば、これをsortメソッドに渡す。	*/
		String[] strings = {"ABC", "HIJK", "XY"};
		Arrays.sort(strings, new LenCompa());		// もし、リスト(List<String>)であれば、Collections.sort(...);
/*	sortメソッドは、compareメソッドを繰り返し呼び出して、配列がソートされるまで順序とおりでない要素を並べ替える。
 *	ここでも、実際にソートを行っているのはcompareメソッドであり、コードブロックである。また、遅延実行である。
 *
 *		(例3)ボタンのコールバック(他のコードの引数として渡されるメソッド)：リスナインタフェースを実装しているクラスの
 *		インスタンスを作成し、ボタンに登録する→通常、匿名クラス(または、無名クラス)の匿名インスタンスを使用する		*/
//			Button button = new Button();		// コメアウトしているのは、JavaFXを実装していないため
//			button.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent event) {
//					System.out.println("OK!");
//				}
//			});
/*	handleメソッド内のコードはボタンがクリックされるごとに実行されるコードである→コードブロックであり、遅延実行である。
 *
 *	先ほどのコンパレータ内のコードである「Integer.compare(first.length(), second.length())」を考えてみる。
 *	これは、一方の文字列が他方の文字列より短いかどうかを検査するコードを渡している。
 *	ここで、firstとsecondは何かというと、文字列である。Javaは型に厳格なので、次のように文字列であることを明示しなければならない。
 *		(String first, String second) -> Integer.compare(first.length(), second.length())
 *	これが、ラムダ式である。ラムダ式は、
 *		パラメタ　矢印(->)　式
 *	で構成される。端的に言えば、ラムダ式とは「パラメタ変数を持つ式」ということになる。
 *
 *	［ラムダ式のルール］
 *	●単一式(上記の例のような式)にはセミコロンは不要である。
 *	●単一式に収まらない計算を行う場合は、メソッド記述のように{}で囲む(ブロックで囲むのでセミコロンは必須である)。
 *		(String first, String second) -> {
 *			if 			(first.length() < second.length())	return -1;
 *			else if 	(first.length() > second.length())	return 1;
 *			else															return 0;
 *		}
 *	●ラムダ式がパラメタを持たない場合は、パラメタなしメソッド記述のように、空のかっこを書く。
 *		() -> { for (int i = 0; i < 1000; i++) dowork(); }		// 例1のラムダ式
 *	●ラムダ式のパラメタ型が推論可能であれば、省略できる。たとえば、例2のラムダ式は、次のようになる。
 *		Comparator<String> comp = (first, second) -> Integer.compare(first.length(), second.length());
 *	　これは、ラムダ式が文字列のコンパレータ(Comparator<String>)へ代入されているので、firstとsecondは文字列と推論される。
 *	●推論された型を持つ単一(1つ)のパラメタのみをメソッドが持つのであれば(例3のような)、型やかっこを省略できる。
 *		EventHandler<ActionEvent> listener = event -> System.out.println("OK!");		// 本来は、「(ActionEvent event) -> ・・・」
 *	●ラムダ式の結果の型は、決して明示しない。それは、文脈から常に推論されるからである。
 *	　たとえば、(String first, String second) -> Integer.compare(first.length(), second.length())は結果の型として
 *	　intが期待されている文脈で使用できる。
 *	●分岐によって値を返し、他の分岐では値を返さないラムダ式は不正である。
 *		(int x) -> { if (x >= 0) return 1; }		// elseがないため
 *
 *	［関数型インタフェース］
 *	RunnableやComparatorといったコードブロックをカプセル化する、単一(1つ)の抽象メソッドを持つインタフェースはたくさんある。
 *	このようなインタフェースのオブジェクトが期待される場所にラムダ式が使用できる。このインタフェースを関数型インタフェースと呼ぶ。
 *
 *	関数型インタフェースへの変換を示すために、例2のArrays.sortを考えてみると、その第2パラメタは、単一メソッドを持つ
 *	インタフェースであるComparatorのインスタンスを要求している。単純に、次のようにラムダ式を渡す。
 *		Arrays.sort(strings, (first, second) -> Integer.compare(first.length(), second.length()));
 *	内部で行われているのは、Arrays.sortメソッドはComparator<String>を実装している何らかのクラス(例2ではLenCompa)の
 *	オブジェクトを受け取る。そのオブジェクトのcompareの呼び出しにより、ラムダ式の本体が実行される。
 *	このように、従来記述してきた内部クラスを使用するよりも効率的なCDが可能である(例2のようなLenCompaクラスは不要になる)。
 *	ラムダ式はオブジェクトではなく関数であると考え、関数型インタフェースにも変換できるということである。
 *
 *	例3にラムダ式を適用すると、次のようになる。
 *		button.setOnAction(event -> System.out.println("OK!"));	// 6行が1行で記述できて効率的で、かつ、可読性が上がる
 *
 *	関数型インタフェースへの変換が、Javaにおいてラムダ式でできる唯一の行為である。
 *	関数型インタフェースには、@FunctionalInterfaceアノテーションを付加できる。Javadocでは、そのインタフェースが
 *	関数型インタフェースであることを示している(例1のRunnableのJavadocで確認：F3キー)ので、どのインタフェースでラムダ式が使用
 *	できるか判断できる。
 *
 *	［メソッド参照］
 *	例3において、ボタンがクリックされたときにイベントオブジェクト(ActionEvent event)を単に表示したいと考えると、
 *		button.setOnAction(event -> System.out.println(event));
 *	と記述できる。このとき、setOnActionメソッドにprintlnメソッドを渡すのみでよい(引数<パラメタ>をそのまま渡しているので)ので、
 *	次のように簡略化できる。
 *		button.setOnAction(System.out::println);		// (注)例3は「OK!」と定数表示しているので簡略化できない
 *	これを、メソッド参照(method reference)と呼ぶ。
 *	メソッド参照は、::演算子でオブジェクト名またはクラス名とメソッド名を区切り、次の3つの組み合わせがある。
 *		(1) オブジェクト名 :: インスタンスメソッド
 *		(2) クラス名 :: クラスメソッド(staticメソッド)
 *		(3) クラス名 :: インスタンスメソッド
 *	(1)と(2)のメソッド参照は、メソッドのパラメタを提供するラムダ式に相当する。
 *	例として、(1)System.out::printlnは、x -> System.out.println(x)に相当し、(2)Math::powは、(x, y) -> Math.pow(x, y)に相当する。
 *	(3)のメソッド参照は、最初のパラメタがメソッドを呼び出すオブジェクトとなる。
 *	例として、String::compareToIgnoreCaseは、(x, y) -> x.compareToIgnoreCase(y)に相当する。
 *	覚え方として、「::」を「の」とするとわかりやすい(System.out「の」printlnをメソッド参照する)。
 *
 *	オブジェクト名またはクラス名の箇所に、thisやsuperを使用できる。
 *	次の例は、スレッドが開始した際に、対象スレッドのRunnableが呼び出される。そして、super::greetが実行されて、
 *	スーパークラスのgreetメソッドが呼び出される。*/
		class Greeter {
			public void greet() { System.out.println("こんにちは"); }
		}
		class SubGreeter extends Greeter {
			@Override
			public void greet() {
				Thread th = new Thread(super::greet);		// () -> super.greet()
				th.start();
			}
		}
 /*
  *	［コンストラクタ参照］
  * コンストラクタ参照(constructor reference)は、メソッド参照における「::」の後ろのメソッド名が「new」であることである。
  * 例として、Button::newとは、Buttonクラスのコンストラクタへの参照である。Buttonクラスのコンストラクタは3つある。
  * どのコンストラクタを呼び出しているかというと、それはコードの文脈から適切なコンストラクタが呼ばれる。
  * 次の例は、文字列のリストから、そのリストの各文字列に対してコンストラクタを呼び出して、ボタンのリストを作成している。
  * すなわち、2番目のコンストラクタである「public Button(String text) {...}」が呼ばれることになる。	*/
//		List<String> labels = new ArrayList<>(Arrays.asList("スタート", "終了", "リセット"));	// 配列のリスト化
//																									// Arrays.asListのみだと固定長リスト(不変リスト)になるので注意
//																									// Arrays.asListの代わりに「List.of」でも可(Java9)：以後はJava9仕様
//		Stream<Button> st = labels.stream().map(Button::new);		// ラベルをストリーム化し、ボタンを3つ生成してストリーム化
//		List<Button> bt = st.collect(Collectors.toList());						// ボタンのリストにストリームからリスト化
/*	もちろん、1つしかコンストラクタを持っていなければ、それが参照される。
 *
 *	［スコープ］
 *	ラムダ式内で、エンクロージングメソッドまたはエンクロージングクラス(エンクロージングとは「囲む」という意味なので、ラムダ式の
 *	外側のメソッドまたはクラスのこと)の変数へアクセスしたい場合がある。たとえば、次のような場合である。
 *		public static void loopMes (int cnt, String txt) {
 *			Runnable r = () -> {
 *				for (int i = 0; i < cnt; i++) {
 *					System.out.println(txt);
 *					Thread.yield();					// スレッドを一時停止して、他のスレッドを実行する
 *				}};
 *			new Thread(r).start();
 *		}
 *	このメソッドを、loopMes(50, "OK");(スレッドで「OK」を50回表示する)で呼び出すことを考える。
 *	このとき、ラムダ式内のcnt、txtはラムダ式内では定義されていない。これらは、loopMesメソッドのパラメタである。
 *	ラムダ式のコードはloopMesメソッドに対する呼び出しから戻って、パラメタ変数が消滅した後に実行されるかもしれない。
 *	とすれば、このcntとtxtの値をラムダ式が保持していないと困る。
 *
 *	ラムダ式は次の3つの構成要素を持っている。
 *		・コードブロック
 *		・パラメタ
 *		・自由(free)変数値(パラメタでもなく、コード内定義変数でもない値：上記例の「i」はコード内定義変数)
 *
 *	例では、cntとtxtは自由変数である。ラムダ式はこれらの自由変数の値(50と”OK")を保持する。
 *	これを、「ラムダ式によりキャプチャ(capture)された」と呼ぶ。一般的に、キャプチャとはインナーからアウターの変数を操作する
 *	ことである。このキャプチャにより、上記例は正常動作することになる。
 *
 *	ここで、自由変数とペアになっているコードブロック(上記例のような)を、クロージャ(closure)と呼ぶ(他言語では「高階関数」ともいう)。
 *	Java7までは、内部クラスがクロージャであったが、8からはラムダ式もクロージャとなり、効率的なCDが可能となった。
 *
 *	キャプチャの制約としては、「ラムダ式内では値が変更されない変数のみを参照できる」ということである。
 *	たとえば、上記例でcntをインクリ(インクリメントのこと：cnt++)するようなコードは不正である。
 *	これは、ラムダ式内での変数の修正は、スレッドセーフ(複数のスレッドが同時並行実行しても問題が発生しないこと)ではないからである。
 *
 *	［デフォルトメソッド］
 *	コレクション(複数の要素の集まり：List, ArrayList, Mapなど)ライブラリとラムダ式が一体となったコードがjava8から記述できる。
 *	ループ処理などがその典型である。たとえば、次のようなコードがある。	*/
		System.out.println("-- デフォルトメソッド：Java7までのループ処理 --");
		List<String> list_dummy = List.of("ABC", "HI");		// 不変リストとする
		for (int i = 0; i < list_dummy.size(); i++) System.out.println(list_dummy.get(i));
/*	これにラムダ式を利用すると、次のように簡略化できる。	*/
		System.out.println("-- デフォルトメソッド：Java8のラムダ式処理 --");
		list_dummy.forEach(System.out::println);
/*	コレクションライブラリを新規に作成するのはよいが、既存のコレクションライブラリの場合は問題が生じる。
 *	ListインタフェースはCollectionインタフェースを継承しているが、CollectionインタフェースにforEachメソッドが追加されると、
 *	Collectionを実装している既存の独自クラスを利用しているプログラムは、forEachメソッドを実装しない限り動作しない。
 *	これでは困るので、Java8から、具体的な実装を持つメソッドをインタフェースに書けるようになった。
 *	これを、デフォルトメソッド(default method)と呼ぶ。デフォルトメソッドは、既存インタフェースに安全に追加できる。
 *	上記のforEachメソッドは、Collectionインタフェースのスーパーインタフェースであるlterableインタフェースにデフォルトメソッド
 *	として追加されている(※Iterableは「繰り返し可能な」という意味である)。
 *
 *	次の例を考える。
 *		interface Person {
 *			long getId();															// 抽象メソッド：public abstractが省略されている
 *			default String getName() { return "日本太郎"; }		// デフォルトメソッド
 *		}
 *	このインタフェースは、抽象メソッドとデフォルトメソッドの2つのメソッドを持っている。
 *	このインタフェースを実装する具象クラス(完全に実装されたクラスのこと：端的に言えばインスタンス化できるクラス)は、
 *	getIdメソッドは必ず実装しなければならない。
 *	※抽象クラスは(先頭にabstractが付いたクラス)インスタンス化できない：通常、スーパークラスに付与し、サブクラスをインスタンス化する
 *	しかし、デフォルトメソッドであるgetNameメソッドは、そのまま利用するか、オーバーライドするか、どちらかを選択できる。
 *	便利な機能であるが、何でもOKというわけにはいかない。次の同じシグニチャ(メソッド名、パラメタの型と数)に関する制約がある。
 *
 *	(1)インタフェースでは衝突を起こす。
 *		あるインタフェースがデフォルトメソッドを持っていて、もう1つのインタフェースが同じシグニチャを持つメソッドを
 *		持っているとき(デフォルトメソッドであるかは問わない)、PGはそのメソッドをオーバーライドして衝突を回避しなければならない。
 *	(2)スーパークラスが優先される。
 *		スーパークラスが具象メソッドを持っていれば、同じシグニチャを持つデフォルトメソッドは無視される。
 *
 *	(1)の説明：インタフェースの衝突
 *		interface Person {
 *			long getId();															// 抽象メソッド：public abstractが省略されている
 *			default String getName() { return "日本太郎"; }		// デフォルトメソッド
 *		}
 *		interface Names {
 *			default String getName() { return "安芸次郎"; }		// デフォルトメソッド
 *		}
 *	この2つのインタフェース(PersonとNames)を実装するクラスを記述した場合に、どのようになるか？
 *		class Student implements Person, Names {
 *			・・・
 *		}
 *	いずれのインタフェースも同じシグニチャである「getName」メソッドを持っている。このとき、どちらを優先することなく、
 *	コンパイルエラーになる。これを回避するには、getNameメソッドをオーバーライドし、次のように記述する。
 *		class Job implements Person, Names {
 *			@Override
 *			public String getName() {
 *				return Person.super.getName();		// PersonのgetNameメソッドを呼び出す(.super.でつなぐ)
 *			}
 *			・・・
 *		}
 *
 *	(2)の説明：スーパークラスが優先
 *		class NamesClass {
 *			String getName() { return "東京三郎"; }
 *		}
 *		interface Names {
 *			default String getName() { return "安芸次郎"; }		// デフォルトメソッド
 *		}
 *	NamesClassクラスとNamesインタフェースがある場合に、次のクラスを宣言したとする。
 *		class Sclass extends NamesClass implements Names { ・・・ }
 *	この場合は、NamesClassスーパークラスのgetNameメソッドが優先され、Namesインタフェースのデフォルトメソッドは無視される。
 *	当然、Sclass内からNamesClassスーパークラスのgetNameメソッドを呼び出すには、super.getName()となる。
 *	この規則により、Java7との互換性が保証される。
 *
 *	［インタフェースのstaticメソッド］
 *	インタフェースにstaticメソッドを持てるようになった。
 *	Java7までは、インタフェースにはstaticメソッドを持てなかったため、対象インタフェースをimplementsしたクラスに
 *	staticメソッドを持って、操作していた。複数のクラスが、あるインタフェースをimplementsしていた場合に、
 *	オーバーライドが複数のクラスで発生することになる。これが、インタフェースにstaticを持てるということは不要になる。
 *	今後は、独自コンパニオンクラス(インタフェースと対になるクラス)を作成する必要がなくなり、コードが簡素化する。
 *
 */
/**************************************** ストリーム ****************************************/
/*	ストリームは、「値の集まり」を処理するために何を行うかを記述することによって、操作のスケジューリングをライブラリに任せる。
 *
 *	［イテレート(反復)からストリームへの進化］
 *	イテレータは、要素を特定の順序で反復走査することであり、効率的な並行実行を妨げる。
 *	コレクションを処理する場合は、java7までは要素をイテレート(反復)し、その個々の要素に対して何らかの処理をしていた。
 *	次の例を見てみよう。
 *	まずは、テキストファイル(シフトJIS)から文字列を読み込み、単語を取り出してリスト化している。	*/
		String txt = new String(Files.readAllBytes(Path.of("D:\\tmp\\test.txt")), Charset.forName("MS932"));
		// readAllBytesは、try-with-resources構文(Java7)で記述されている(F3キーで確認)ので、読み込み後、ファイルは自動クローズされることに留意する
		// Path.of()はJava11仕様、Paths.get()はJava7仕様
		List<String> words = List.of(txt.split("[　\\s]+"));			// 不変リストとする
		// 単語に分割：[\\s]では全角空白が残る：\sは空白文字：[\t\n\x0B\f\r]と等価
/*	次に、長さが13以上の総数をカウントする。これがイテレートである。	*/
		int cnt = 0;
		for (String str : words) if (str.length() > 12) cnt++;
/*	これは先頭から末尾までの順序で繰り返し処理している。これを大量操作するストリームで記述すると、次のようになる。	*/
		long streamcnt = words.stream().filter(s -> s.length() > 12).count();
/*	streamメソッドは、リスト(words)に対するストリーム(stream)を生成する。
 *	filterメソッドは、長さが13文字以上の単語のみを含む、別のストリームを返す。
 *	countメソッド(リダクション<還元>メソッド)は、filterメソッドで返されたストリームをリデュース(reduce：集約)して(今回はカウント)結果を返す。
 *	その結果をstreamcntに代入する。
 *
 *	ストリームは、コレクションに似ているが、データを変換したり、取り出したりできる。ただし、次の相違点がある。
 *		・ストリームは、その要素を保存しない。文字とおり「流す」のみである。
 *		・ストリーム操作は、その元データ(上記例であれば「words」)を変更しない。その代わりに、新たなストリームを生成する。
 *		・ストリーム操作は、可能な場合は遅延(結果が必要になるまで操作を行わない)される。例として、上記はすべての単語を
 *		　操作しているが、5つのみ結果が欲しいという場合には、filterメソッドは5つ目の一致した単語を検出した後は、フィルタリングを
 *		　停止する。
 *
 *	ストリームの式は、対応するループ式より可読性があり、簡素化される。さらに、並列化も可能である。次の例は、先ほどの例を
 *	並列化して処理している。	*/
		long pcnt = words.parallelStream().filter(s -> s.length() > 12).count();
/*	streamをparallelStreamに変更するのみで、フィルタリングとカウントを並列化する。
 *
 *	ポイントとして、ストリームは「方法ではなく何を処理するか」が原則である(例では「長い単語を集めて個数を数える」)。
 *	処理が、どの「順序」で、どの「スレッド」で行われるべきかなどとは指定していない。これに対し、最初のイテレート例は
 *	どのように計算するかを明確に記述しており、最適化の可能性を断念している。
 *
 *	ストリームは、次の3段階で操作のパイプライン(処理要素を直列に連結し、ある要素の出力が次の要素の入力となるようにして、
 *	並行<必ずしも並列とは限らない>に処理させる)を設定する。
 *		(1)ストリームを作成する。
 *		(2)最初のストリームを他のストリームへ、1つ以上の手順で変換するための中間操作(intermediate operation)を指定する。
 *		(3)結果を生成するために終端操作(terminal operation)を適用する。終端操作は、それより前の遅延操作の実行を強制する。
 *		　 その後は、ストリームは利用できない。
 *	上記例では、streamメソッドとparallelStreamメソッドが(1)、filterメソッドが(2)、countメソッドが(3)に相当する。
 *
 *	ストリームは、要素に対する操作をストリームに対して呼び出された順序では実行しない。上記例では、countメソッドが
 *	呼び出されるまで何も起きない。countメソッドが最初の要素を要求すると、filterメソッドが開始され、長さが13以上である
 *	要素を見つけるまで要求し続ける。見つかると、countメソッドが再度要求する。これを繰り返す。
 *
 *	［ストリームの生成］
 *	ストリームは、コレクション、配列、ジェネレータ、イテレータから生成できる。
 *	先ほどの例は、Collectionインタフェースのstreamメソッドを使用することで、コレクションをストリームに変換した。
 *
 *	配列の場合は、その代わりに、staticのStream.ofメソッドを使用する。このStreamはインタフェースである。	*/
		Stream<String> wordstream = Stream.of(txt.split("[　\\s]+"));
/*	ofメソッドは、可変個数のパラメタを持つ順序付けされた順次ストリームを返す。可変個数なので、任意個数の引数からも
 *	ストリームを生成できる。	*/
		Stream<String> shopName = Stream.of("デパート", "スーパー", "八百屋");
/*	配列の一部からストリームを生成するには、staticのArrays.stream(array, from, to)を使用する(to：終了idx+1を指定)。
 *
 *	何も要素を持たないストリームを生成するには、次のようにstaticのStream.emptyメソッドを使用する。	*/
		Stream<String> nothing = Stream.empty();
/*
 *	Streamインタフェースには、無限ストリームを生成する2つのstaticメソッドがある。
 *	(1)generateメソッド：順次付けなし
 *		このメソッドは、引数を持たない関数を引数に指定する(Supplier<T>のオブジェクトを引数にする)。
 *		ストリームの値が必要になった時点で、値を生成するためにその関数が呼び出される。
 *		次の例は、定数値(OK)のストリームを生成している。	*/
			Stream<String> oks = Stream.generate(() -> "OK");
/* 	また、乱数の無限ストリームを生成するには、次のようにする。	*/
			Stream<Double> randoms = Stream.generate(Math::random);
/*
 *	(2)iterateメソッド：順次付けあり
 *		このメソッドは、0 1 2 3 ... のような無限数列を生成する。
 *		このメソッドは、シード(最初の設定値)と関数(UnaryOperation<T>)を引数に指定し、直前の結果にその関数を繰り返し適用する。
 *		次の例は、無限数列を生成している。	*/
			Stream<BigInteger> ints = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.valueOf(2)));
/*		BigIntegerは、変更が不可能な任意精度の整数である(通常、longを超えるときに使用される)。
 *		数列の最初の値はシードの1である。次の値はシードに2を加算するので3である。次の値は、直前の結果(3)に関数を適用するので、
 *		3+2=5となる。その後も同じ動作になるため、1 3 5 7 ... のような無限数列が生成される。
 *
 *	Patternクラス(コンパイル済の正規表現)に、正規表現によってCharSequenceを分割するsplitAsStreamメソッドが追加された。
 *	このメソッドを利用すると、文字列を単語に分割したストリームを生成できる。	*/
		Stream<String> pstream = Pattern.compile("[　\\s]+").splitAsStream(txt);
/*
 *	staticのFiles.linesメソッドは、ファイル内の全行のストリームを生成する。
 *	ここでのポイントは、Streamインタフェースは、スーパーインタフェースにAutoCloseableを持っていることである。
 *	これが意味するのは、Files.linesメソッドを使用してストリームを生成し(生成時点ではファイルはオープン状態)、このストリームに
 *	対してcloseメソッドを発行すると、同時にファイルもクローズされる。確実性を高めるのであれば、try-with-resources構文を併用する。*/
		try (Stream<String> lstream = Files.lines(Path.of("D:\\tmp\\test.txt"), Charset.forName("MS932"))) {
			// lstreamの処理
		}		// ここではcatchもfinallyも記述していないが、当然、記述は可能である。
/*	ストリームが扱っている実際のファイル(上記例では"text.txt")は、tryブロックが正常終了しても例外をスローしても、クローズされる。
 *
 *	［中間操作メソッド］
 *	中間動作は、最初のストリームを他のストリームへ、1つ以上の手順で変換する。
 *
 *	●filterメソッド
 *		ある条件に一致したすべての要素を持つ新たなストリームを生成するメソッドである。	*/
			Stream<String> wds = words.stream();								// 前述のwordsをストリーム化
			Stream<String> lwds = wds.filter(s -> s.length() > 12);	// 長さが13以上の単語を持つストリームに変換
/*		filterメソッドの引数は、Predicate<T>である(Predicate：述語)ので、boolean値関数(結果がtrueかfalseとなる関数)を意味する。
 *
 *	●mapメソッド
 *		要素を別な要素に変換して新たなストリームを生成するメソッドである。	*/
			Stream<String> lowerWds = words.stream().map(String::toLowerCase);	// すべての単語を小文字化に変換
			Stream<Character> fchar = words.stream().map(s -> s.charAt(0));			// すべての単語の先頭文字のストリーム化
/*
 *	●flatMapメソッド
 *		ストリームをフラット化して新たなストリームを生成するメソッドである。
 *			public static Stream<Character> chStream (String s) {	// 文字列をばらして文字化したストリームを返す独自メソッド
 *				List<Character> r = new ArrayList<>();
 *				for (char c : s.toCharArray()) r.add(c);						// 文字列を文字配列に変換し、各文字をリストにつなげる
 *				return r.stream();														// リストをストリーム化して返却
 *			}
 *		上記のメソッドに対して、chStream("abc"); を呼び出すと、'a' 'b' 'c' のストリームを返却する。
 *		単独で利用するには、何ら問題はないが、中間操作であるmapメソッドを使用すると、ストリームをストリーム化することになる。
 *			Stream<Stream<Character>> r = words.stream().map(s -> chStream(s));
 *		単語が「abc」「de」とすると、[ ['a' 'b' 'c'] ['d' 'e'] ]のストリームが返却される([ ]はストリームを示す)。
 *		これをフラット化するのが、flatMapである。
 *			Stream<Character> r = words.stream().flatMap(s -> chStream(s));
 *		これにより、'a' 'b' 'c' 'd' 'e' のフラット化されたストリームが得られる。
 *
 *	●limitメソッド
 *		n個の要素の後(元のストリームがn個に満たない場合はストリームが終了した時)に終了する新たなストリームを生成するメソッドである。
 *		効果があるのは、無限ストリームに制限を与えることである。	*/
			Stream<Double> rdms = Stream.generate(Math::random).limit(50);
/*		この例は、先ほどの乱数の無限ストリームのうち、要素を50個持つストリームを生成する。
 *
 *	●skipメソッド
 *		limitメソッドの逆で、最初のn個の要素を破棄する。
 *
 *	●concatメソッド
 *		Streamクラスのstaticメソッドである。このメソッドは、2つのストリームを連結(concat)する。
 *			Stream<Character> rr = Stream.concat(chStream("abc"), chStream("de"));
 *		この例では、先ほどのflatMapメソッドを使用した結果と同じになる。
 *
 *	●peekメソッド
 *		元のストリームと同じ要素を持つ別のストリームを生成する。
 *		次の例のように、デバッグ時に利用される(peekは「覗く」という意味があることから効果は予想できる)。	*/
			Stream<Double> dbl = Stream.iterate(1.0, p -> p * 2).peek(s -> System.out.println("要素：" + s)).limit(5);
/*		2倍(1.0 2.0 4.0 8.0 ...)の無限ストリームの先頭から5つの要素(iterateは順次付けあり)をストリーム化し、要素が実際にアクセス
 *		された際にメッセージを表示する。この他にも、遅延処理の確認にもデバッグ使用される。
 *
 *	［状態を持つ中間操作］
 *	ここまで説明してきた中間操作メソッドは、「状態を持たない」ものであった。状態を持たないとは、中間操作の結果は、それ以前の
 *	要素には依存していないということである(入ってきたものを操作後そのまま流す)。
 *	では、「状態を持つ」とは何だろう？
 *
 *	●distinctメソッド
 *		元のストリームと同じ順序で要素を生成するが、重複している要素は取り除かれる(distinct：「区別」という意味)。	*/
			Stream<String> uwds = Stream.of("abc", "de", "abc", "xy", "abc").distinct();
/*		結果のストリームは、"abc" "de" "xy" となり、重複要素である"abc"(2回)は取り除かれる。
 *		重複要素を取り除くということは、それまでに出現している要素を記憶していなければならない。
 *		これを、「状態を持つ」と呼ぶ。
 *
 *	●sortedメソッド
 *		指定された方法でソートした結果のストリームを生成する(ただし、無限ストリームはソートできない)。
 *		このメソッドも「状態を持つ」メソッドである。いくつか例を挙げる。
 *		・リスト化された数値の降順ソートストリームを生成(結果：94 78 67 51)	*/
			List<Integer> pt = List.of(67, 94, 51, 78);			// 不変リストとする
			Stream<Integer> pts = pt.stream().sorted((x, y) -> Integer.compare(y, x));
/* 	・ストリーム化された数値の昇順ソートストリームを生成(結果：51 67 78 94)	*/
			Stream<Integer> pts2 = Stream.of(67, 94, 51, 78).sorted((x, y) -> Integer.compare(x, y));
/*		・ストリーム化された文字列の文字列長の降順ソートストリームを生成(結果："ありがとう" "日本" "海")	*/
			Stream<String> stg = Stream.of("海", "ありがとう", "日本").sorted(Comparator.comparing(String::length).reversed());
/*		そもそもソート処理は、ストリームを使用しないで、コレクションをソートしてもよい(Java7までの方法で)。
 *		しかし、ソート処理が、ストリームの中間操作の一部であるのであれば(ソート結果をさらに流す)、sortedメソッドは効率的である。
 *		※Collections.sortメソッドは、渡されたコレクション自体をソートする(変更される)が、ストリームであればソート後のストリームを
 *		　新たに生成するのみなので、元のコレクションは変更されないことに留意する。
 *
 *	［終端操作：単純なリダクション(還元)］
 *	終端操作の目的は、中間操作までのストリームデータから最終的な結果を得ることである。
 *	ここで説明するメソッド群をリダクション(還元)と呼び、ストリームをプログラム内で使用可能な値へとリデュース(集約)する。
 *	よって、終端操作は「リダクション」である。忘れてはいけないのは、終端操作を実行した後は、それまでのストリームは使用できなくなる。
 *
 *	ここまでの説明でリダクションメソッドとして、countメソッドがあった(文字列長が13文字以上の総数を取得)。
 *	他の単純なリダクションを次に挙げる。
 *
 *	●max/minメソッド
 *		最大値、最小値を返す。
 *		これらは、Optional<T>値を返す(Optional：「任意」という意味)。Optional<T>値は、最終的な結果があるか、ストリームが空っぽ
 *		であったために結果がないか、いずれかを表現する。Java7までは、結果がない場合にはnull値を返すのが常であったが、これは、
 *		テスト工程時(まだプログラムがフィールドに出せる状態ではない未完成)に異常が発生すると、「NullPointerException」例外を
 *		スローする可能性があり、テスト効率が悪くなる。
 *		Optional型は、存在しない返却値を示す最良の方法である。	*/
			System.out.println("-- 終端操作：単純なリダクション(還元)：max/minメソッド --");
			Stream<String> stg2 = Stream.of("AB", "XYZ", "HIJKLMN");
			Optional<String> largestg2 = stg2.max(String::compareToIgnoreCase);
			if (largestg2.isPresent()) System.out.println("ストリームの最大値：" + largestg2.get());	// "XYZ"と表示
/*		上記の例は、文字列ストリームを大文字・小文字の区別なしで辞書順に比較し、その最大値を表示している。
 *		if文の「isPresent()」は、Optional値があるかないかを判断している。上記例は明らかにストリームが空っぽではなく、最大値が
 *		largestg2に格納されている。
 *		存在していれば、getメソッドで値を取得できる(存在していない場合にget()を発行すると、NoSuchElementException例外をスローする)。
 *
 *	●findFirstメソッド
 *		空っぽではないコレクションの最初の値を返す。
 *		通常はfilterメソッドと併用される。	*/
			System.out.println("-- 終端操作：単純なリダクション(還元)：findFirstメソッド --");
			Stream<String> stg3 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			Optional<String> startH = stg3.filter(s -> s.startsWith("H")).findFirst();
			String rstartH = startH.orElse("Nothing"); System.out.println("文字Hで始まる最初の単語：" + rstartH);	// "HIJKLMN"と表示
//			System.out.print("文字Hで始まる最初の単語：");
//			if (startH.isPresent())	System.out.println(startH.get());
//			else							System.out.println("Nothing");
/*		上記の例は、文字列ストリームの文字Hで始まる最初の文字列(単語)を表示している。
 *		orElseメソッドは、Optional値が存在すればその値を、存在しなければ引数値を返すメソッドである。したがって、該当する単語が
 *		あれば(存在することは明白ではあるが)その値("HIJKLMN")を返し(startHはOptional<String>なのでString型で返す)、なければ
 *		文字列"Nothing"を返すことになる。もし、orElseメソッドを使用しなければ、コメント行のようになる。
 *		このように、orElseメソッドを使用すると、Optionalを意識せずに慣れ親しんだ型でCDできる。
 *
 *	●findAnyメソッド
 *		findFirstメソッドが最初の値を返すが、このメソッドは一致さえしていれば、その値を返す。
 *		一致さえしていればよいということは、findFirstのような順次性(直列)は問われないので、並列化(parallelメソッド適用)すると
 *		効率的である。効率的という意味は、並列化のいずれかで発見すれば、その時点で操作が終了するからである。
 *		欠点といえば、このメソッドを並列化すると、実行のたびに結果が異なることである。	*/
			System.out.println("-- 終端操作：単純なリダクション(還元)：findAnyメソッド --");
			Stream<String> stg4 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			Optional<String> startH2 = stg4.parallel().filter(s -> s.startsWith("H")).findAny();
			if (startH2.isPresent()) System.out.println("文字Hで始まる単語：" + startH2.get());	// "HIJKLMN"または"HIT"と表示
/*		※逆に並列化をやめて直列化したい場合は、sequential()を中間操作に加える
 *
 *	●anyMatchメソッド
 *		一致さえしていればtrueを返し、それ以外(一致している要素がない)はfalseを返す。
 *		findAnyメソッド同様、並列化すると効率的である。	*/
			System.out.println("-- 終端操作：単純なリダクション(還元)：anyMatchメソッド --");
			Stream<String> stg5 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			if (stg5.parallel().anyMatch(s -> s.startsWith("H"))) 	System.out.println("一致あり");
			else																			System.out.println("一致なし");
/*		このメソッドの引数は、Predicate(述語：boolean値関数)なので、filterメソッドを使用する必要がない。
 *
 *	●allMatch/noneMatchメソッド
 *		anyMatchメソッドと同類のメソッドとして、次のものがある。
 *			・allMatchメソッド：すべての要素が述語に一致しているときtrueを返す。
 *			・noneMatchメソッド：すべての要素が一致していないときtrueを返す。
 *		いずれのメソッドも、並列化すると効率的である。
 *
 *	［Optional型とは］
 *	Optional<T>は、T型のオブジェクトに対するラッパー、または、何もオブジェクトがないラッパーである。
 *	nullを扱うより安全ではあるが、正しく使用しない限りその保証はされない。
 *
 *	getメソッド、isPresentメソッド、orElseメソッドは、前述した。他のメソッドを次に挙げる。
 *	●ifPresentメソッド
 *		orElseメソッドは、値が存在しない場合に引数値を返すのに対し、このメソッドは、値が存在する場合に引数の関数を
 *		実行する。値が存在しなければ、何も起こらない。if文の代替として使用される。	*/
			System.out.println("-- Optional型：ifPresentメソッド --");
			Stream<String> stg6 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			Optional<String> startH3 = stg6.filter(s -> s.startsWith("X")).findFirst();
			startH3.ifPresent(System.out::println);		// "XYZ"と表示
/*
 *	●isEmptyメソッドである(Java11)
 *		値が存在しない場合はtrueを返し、存在する場合にはfalseを返す。
 *		isPresentメソッドの逆である。	*/
			System.out.println("-- Optional型：isEmptyメソッド(Java11) --");
			var op1 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT").filter(s -> s.startsWith("M")).findFirst();
			if (op1.isEmpty()) System.out.println("「M」から始まる文字列はありません");
/*
 *	●ifPresentOrElseメソッド(Java9)
 *		ifPresentは、値が存在する場合に引数の関数を実行するのに対し、このメソッドは、値が存在する場合は
 *		第1引数の関数を、値が存在しない場合には第2引数の関数を実行する。		*/
			System.out.println("-- Optional型：ifPresentOrElseメソッド(Java9) --");
			Optional<String> opEmpty = Optional.empty();
			opEmpty.ifPresentOrElse(System.out::println, () -> System.out.println("値なし"));	// "値なし"と表示
/*
 *	●orメソッド(Java9)
 *		値が存在する場合は同じOptionalを、存在しない場合は引数が作成する新しいOptionalを返す。
 *		このメソッドは、常にOptionalがあることを保証したい場合に便利である。		*/
			System.out.println("-- Optional型：orメソッド(Java9) --");
			Optional<String> opEmpty2 = Optional.empty();
			Optional<String> ans = opEmpty2.or(() -> Optional.of("正解です"));
			ans.ifPresent(System.out::println);					// "正解です"と表示
/*
 *	●orElseGetメソッド
 *		このメソッドは、値が存在しない場合に引数の関数を実行する。値が存在すれば、そのままOptional値を返す。
 *		用途としては、デフォルト値の設定などである。	*/
			System.out.println("-- Optional型：orElseGetメソッド --");
			Optional<String> opl = Optional.empty();												// 例題動確のため、強制的に空にする
			String rdir = opl.orElseGet(() -> System.getProperty("user.home"));		// C:\Users\xxxx(xxxx：ユーザ名)を設定
/*
 *	●orElseThrowメソッド(Java8/Java10)
 *		このメソッドは、値が存在しない場合に引数の例外をスローする。値が存在すれば、そのままOptional値を返す。
 *		用途としては、内部エラー検出などである。
 *		引数を1つ取る仕様がJava8、引数なしがjava10の仕様である	*/
			System.out.println("-- Optional型：orElseThrowメソッド(Java8/10) --");
			try {
				Optional<String> opl2 = Optional.empty();											// 例題動確のため、強制的に空にする
				String rstg = opl2.orElseThrow(NoSuchElementException::new);			// java10では引数不要：同じ結果になる
			} catch (NoSuchElementException e) {
				System.out.println("例外到達");															// 表示される
			}
/*
 *	●streamメソッド(Java9)
 *		値が存在する場合は、その値のみを含む1個のストリームを返し、それ以外の場合は空(0個)のストリームを返す。
 *		このメソッドは、任意の要素のストリームを、現在の型要素のストリームに変換する場合に使用する。	*/
			System.out.println("-- Optional型：streamメソッド(Java9) --");
			// nullを含むリストからOptional<String>ストリームを生成する(null時はempty値に変換する)
			Stream<Optional<String>> opt =
					Arrays.asList("A", null, "B", null, "C").stream().<Optional<String>>map(Optional::ofNullable);
			// empty値以外のOptional<String>要素をStringストリームに変換する
			// empty値の場合は空(0個)のストリームを返すということは、ストリームを流さない->除外することになる
			Stream<String> goodOpt = opt.flatMap(Optional::stream);
			goodOpt.forEach(System.out::println);			// "A""B""C"と表示
/*
 *	●empty/ofメソッド
 *		・emptyメソッド：空のOptional値を生成するstaticメソッド
 *		・ofメソッド：ある値からOptional値を生成するstaticメソッド
 *				public static Optional<Double> inv (Double x) {
 *					return x == 0 ? Optional.empty() : Optional.of(1 / x);
 *				}
 *		この例は、数値の逆数を求めるメソッドである。xが0であれば空のOptional値を、0以外であればxの逆数のOptional値を返す。
 *
 *	●ofNullableメソッド
 *		このメソッドは、引数のオブジェクトがnullでなければ Optional.of(obj) を返し、nullであれば Optional.empty() を返す。
 *
 *	●flatMapメソッド
 *		このメソッドは、複数のOptional関数を合成する。
 *		次のメソッドは、平方根を求めるメソッドである。
 *			public static Optional<Double> sqRoot (Double x) {
 *				return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
 *			}
 *		先ほどのinvメソッドとsqRootメソッドを合成すると、次のようになる。
 *			Optional<Double> rInvSqrt = inv(0.5).flatMap(Test::sqRoot);		// TestはsqRootメソッドを保有するクラス名
 *		これで、0.5の逆数(=2)の平方根が得られる。invメソッドの返却型はOptional<Double>であり、sqRootの引数はDoubleである。
 *		この場合は合成可能であるが、型が異なるとエラーとなる。
 *		次の例は、上記例と同等である。
 *			Optional<Double> rInvSqrt = Optional.of(0.5).flatMap(Test::inv).flatMap(Test::sqRoot);
 *		(参考)
 *		前述の［中間操作メソッド］で説明したflatMapメソッドも、ストリームを生成する2つのメソッドを合成するために使用していた。
 *
 *	［終端操作：リダクション(還元)操作：reduceメソッド］
 *	ここではreduceメソッドの3つの形式を説明する(reduce：「集約」という意味)。
 *
 *	(1)引数が1つ：Optional<T> reduce(BinaryOperator<T> accumulator)
 *		この形式は、二項関数(BinaryOperator<T>)を引数にして、最初に2つの要素から始めて、その関数を適用し続ける。
 *		ストリームが空っぽの場合は結果が存在しないので、返却型をOptional<T>としている(Optional.empty()を返す)。
 *		例として、合計計算したいと考える。	*/
			Stream<Integer> i1 = Stream.of(10, 20, 30, 40);
			Optional<Integer> i1sum = i1.reduce((x, y) -> x + y);
/*		この場合は、v0 + v1 + v2 + ... を計算する。この時の vi (i = 0, 1, 2, ...)は、ストリームの要素である。
 *		注意する点は、上記例で 10 20 30 40 の順番で加算されているわけではないということである。
 *		すなわち、reduceメソッドは並列化して処理するため、二項関数が結合的でなければ使用できないことを意味する。
 *		ここで結合的とは、(x op y ) op z = x op (y op z)であることを指す(opは演算子：上記例であれば「+」)。
 *		加算、乗算、文字列結合、最大、最小のこれらには、このメソッドを適用できる。しかし、減算は結合的ではないため適用できない。
 *		<理由>(10 - 2) - 5 != 10 - (2 - 5)
 *
 *		先ほどの例と同等な文は、次のとおりである。
 *			Optional<Integer> i1sum = i1.reduce(Integer::sum);		// Integer.sum(x, y)はx + yを計算する：Java8
 *
 *	(2)引数が2つ：T reduce(T identity, BinaryOperator<T> accumulator)
 *		x op e = x のとき、eを恒等(identity)であるという。加算であれば0、乗算であれば1が恒等値である。
 *		このメソッドは、この恒等値を第1引数に指定し、第2引数は二項関数である。
 *		先ほどの合計計算をこの形式に修正すると、次のようになる	*/
			Stream<Integer> i2 = Stream.of(10, 20, 30, 40);
			int i2sum = i2.reduce(0, (x, y) -> x + y);		// 0 + v0 + v1 + v2 + ... を計算
/*		ストリームが空っぽであれば、恒等値を返す(0を返却)、これが意味するのは、(1)のようにOptional<T>を返す必要がない。
 *		よって、Optional値を必要としないのであれば、この形式の方が効率的である。もちろん、この形式も結合的である必要がある。
 *
 *	(3)引数が3つ：<U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)
 *		文字列のストリームがあり、それらの文字列長の総和を求めたいと考える。このとき、(1)や(2)の形式は使用できない。
 *		2つの形式は、二項関数の型と結果の型が同じである。しかし、この例では、文字列の型(String)と文字列長の型(int)は異なる。
 *		この形式は、このような異なる型を解決する。
 *		第2引数にアキュムレータ(演算結果を累積すること)関数である BiFunction を指定する。BiFunctionは異なる型を引数に持つ
 *		関数なので、(total, word) -> total + word.lenght() となる。
 *		次に、第2引数のアキュームレータ関数は型が異なるので結合的ではない((1)と(2)は結合的)。これらが並列化されると、
 *		最終的な総和を求めることが必要になる。これを第3引数に指定する(並列化されたものを結合するコンバイナ(結合)関数)。	*/
			Stream<String> s0 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			int r0 = s0.reduce(0, (total,  word) -> total + word.length(), (x, y) -> x + y);	// コンバイナは「Integer::sum」でも可
/*
 *		3つの形式を説明したが、現場で使用されることはあまりない。それは、合計、最大、最小、平均値など、プログラム上頻度が高い
 *		要件専用のストリームとメソッドが用意されている。後述するが、(3)の形式の例を別解すると、次のようになる。
 *			int r0 = s0.mapToInt(String::length).sum();
 *		この方が、ボクシング(基本データ型⇒ラッパークラス)が発生しないため、効率的である。
 *		※(3)の例は、Stream<T>を流している。intは流せないので、ラッパーであるIntegerで流している⇒ボクシングが発生している。
 *
 *	［終端操作：結果をコレクト(収集)する］
 *	ここまでは、結果を1つにリデュース(集約)してきたが、デバッグ等でストリームの中身を見たい場合がある。
 *
 *	●iteratorメソッド	*/
			System.out.println("-- 終端操作：iteratorメソッド --");
			Stream<String> s1 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			for (Iterator<String> itr = s1.iterator(); itr.hasNext();) System.out.println(itr.next());
/*		iteratorメソッドを使用して、古い形式のイテレータを生成する。あとは、従来とおり要素を参照する。
 *
 *	●toArrayメソッド	*/
			System.out.println("-- 終端操作：toArrayメソッド --");
			Stream<String> s2 = Stream.of("AB", "XYZ", "HIJKLMN", "HIT");
			String[] r2 = s2.toArray(String[]::new);
			for (String rr2 : r2) System.out.println(rr2);
/*		toArrayメソッドを使用して、配列を生成する。何の配列にすべきかは、引数に対象配列のコンストラクタ参照を指定する。
 *
 *	●collectメソッド
 *		ストリーム値をHashSet(重複を許さず格納順は問わない集合構造)に、結果収集したいと考える。
 *		ハッシュセットは、スレッドセーフではないため、並列化が前提のreduceメソッドは使用できない：要素を直接格納できない。
 *		リデュース対象が、HashSetやArrayListのような可変の結果コンテナ(ArrayListのようなコレクションクラスの総称)の場合は、
 *		collectメソッドで可変リダクション操作をする。
 *
 *		(1)引数を3つ取る形式
 *			第1引数：	対象オブジェクトの新たなインスタンスを作成するためのサプライヤ(supplier)。
 *							この例では、HashSetのコンストラクタを指定する。
 *			第2引数：	対象オブジェクトに要素を追加するアキュームレータ(accumulator)。
 *							この例では、HashSet用のaddメソッドを指定する。
 *			第3引数：	2つのオブジェクトを1つにマージするコンバイナ(combiner)。
 *							この例では、HashSet用のaddAllメソッドを指定する。	*/
				System.out.println("-- 終端操作：collectメソッド --");
				Stream<String> s3 = Stream.of("A0", "X0", "HIJ0", "HI0", "A0");
				HashSet<String> r3 = s3.collect(HashSet::new, HashSet::add, HashSet::addAll);	// "A0"は1つのみ格納される
				r3.forEach(System.out::println);
/*
 *		(2)引数を1つ取る形式
 *			現場では、(1)の形式を使用することはなく、3つの引数関数をカプセル化したCollectorインタフェースがあり、それを利用した
 *			Collectorsクラスのメソッドを引数にあてる方法が簡素で可読性もよく、多用される。
 *
 *			・toListメソッド
 *				ストリーム値をリストに結果収集するメソッドである。	*/
					System.out.println("-- 終端操作：toListメソッド --");
					Stream<String> s41 = Stream.of("A1", "X1", "HIJ1", "HI1", "A1");
					List<String> r41 = s41.collect(Collectors.toList());		// "A1"は2度格納される
					r41.forEach(System.out::println);
/*
 *			・toSetメソッド(Setインタフェース：重複を許さないコレクション：Set<String> hs = new HashSet<>(); List-ArrayListと同じ関係)
 *				ストリーム値をセットに結果収集するメソッドである。	*/
					System.out.println("-- 終端操作：toSetメソッド --");
 					Stream<String> s42 = Stream.of("A2", "X2", "HIJ2", "HI2", "A2");
 					Set<String> r42 = s42.collect(Collectors.toSet());			// "A2"は1つのみ格納される
					r42.forEach(System.out::println);
/*
 *			・toCollectionメソッド
 *				どの種類のリストやセットに結果収集するかを指定するメソッドである。引数に対象のコンストラクタ参照を指定する。
 *				<ArrayListクラス：Listインタフェースのサイズ変更可能な配列の実装>	*/
					System.out.println("-- 終端操作：toCollectionメソッド<ArrayList> --");
					Stream<String> s43 = Stream.of("A3", "X3", "HIJ3", "HI3", "A3");
					ArrayList<String> r43 = s43.collect(Collectors.toCollection(ArrayList::new));		// "A3"は2度格納される
					r43.forEach(System.out::println);
/*				<HashSetクラス：ハッシュ表に連動し、Setインタフェースを実装。null要素を許容>：(1)の別解	*/
					System.out.println("-- 終端操作：toCollectionメソッド<HashSet> --");
					Stream<String> s44 = Stream.of("A4", "X4", "HIJ4", "HI4", "A4");
					HashSet<String> r44 = s44.collect(Collectors.toCollection(HashSet::new));		// "A4"は1つのみ格納される
					r44.forEach(System.out::println);
/*				<TreeSetクラス：基本的にはHashSetと同等だが、自動的に要素がソートされる、null要素は許容されないという違い>	*/
					System.out.println("-- 終端操作：toCollectionメソッド<TreeSet> --");
					Stream<String> s45 = Stream.of("A5", "X5", "HIJ5", "HI5", "A5");
					TreeSet<String> r45 = s45.collect(Collectors.toCollection(TreeSet::new));		// "A5"は1つのみ格納される
					r45.forEach(System.out::println);
/*
 *	●joiningメソッド
 *		すべての文字列を検出順に連結するメソッドである。	*/
			System.out.println("-- 終端操作：joiningメソッド(連結) --");
			Stream<String> s5 = Stream.of("A5", "X", "HIJ", "HI");
			String r5 = s5.collect(Collectors.joining());
			System.out.println(r5);
/*		引数なしであると、ストリーム値をそのまま連結する。もし、要素間にデリミッタ(区切り文字)が必要であれば、引数に指定する。	*/
			System.out.println("-- 終端操作：joiningメソッド(デリミッタ) --");
			Stream<String> s6 = Stream.of("A6", "X", "HIJ", "HI");
			String r6 = s6.collect(Collectors.joining(", "));
			System.out.println(r6);
/*		上記例は、デリミッタとして「,△」(△は半角空白)を付加している。csvファイル作成に利用できる。
 *		ストリーム値が文字列であれば問題はないが、文字列以外の場合にはmapメソッドで文字列化してからjoiningする。	*/
			System.out.println("-- 終端操作：joiningメソッド(数値変換) --");
			Stream<Integer> s7 = Stream.of(10, 20, 30, 40);
			String r7 = s7.map(Object::toString).collect(Collectors.joining());
			System.out.println(r7);
/*
 *	●summarizingInt/summarizingLong/summarizingDoubleメソッド
 *		ストリーム値のカウント、合計、最大、最小、平均を結果収集するメソッドである。
 *		それぞれのメソッドは、引数の型で使い分ける。また、それぞれの返却型は、IntSummaryStatistics/LongSummaryStatistics/
 *		DoubleSummaryStatisticsクラスである。これらのクラスには、カウント、合計、最大、最小、平均を求めるメソッドがある
 *		ため、それらを利用する。
 *		文字列のストリームから、文字列長のカウント、合計、最大、最小、平均を求めると、次のようになる。*/
			System.out.println("-- 終端操作：summarizingIntメソッド(文字列) --");
			Stream<String> s8 = Stream.of("A8", "X", "HIJ", "HI");
			IntSummaryStatistics ss8 = s8.collect(Collectors.summarizingInt(String::length));
			long r81		= ss8.getCount();		System.out.println("count(slen)=" + r81);
			long r82		= ss8.getSum();		System.out.println("sum(slen)=" + r82);
			int r83			= ss8.getMax();			System.out.println("max(slen)=" + r83);
			int r84			= ss8.getMin();			System.out.println("min(slen)=" + r84);
			double r85	= ss8.getAverage();	System.out.println("ave(slen)=" + r85);
/*		整数のカウント、合計、最大、最小、平均を求めると、次のようになる。	*/
			System.out.println("-- 終端操作：summarizingIntメソッド(整数) --");
			Stream<Integer> i9 = Stream.of(10, 20, 30, 40);
			IntSummaryStatistics ii9 = i9.collect(Collectors.summarizingInt(x -> x));
			long r91		= ii9.getCount();		System.out.println("count(i)=" + r91);
			long r92		= ii9.getSum();			System.out.println("sum(i)=" + r92);
			int r93			= ii9.getMax();			System.out.println("max(i)=" + r93);
			int r94			= ii9.getMin();			System.out.println("min(i)=" + r94);
			double r95	= ii9.getAverage();	System.out.println("ave(i)=" + r95);
/*
 *	●forEach/forEachOrderedメソッド
 *		(中間操作までの)ストリーム要素をすべて処理するメソッドである(たとえば、次の例のような表示など)。	*/
			System.out.println("-- 終端操作：forEach/forEachOrderedメソッド --");
			Stream<Integer> i10 = Stream.of(10, 20, 30, 40);
			i10.forEach(System.out::println);		// この例は中間操作なし
/*		並列ストリームは、要素がどんな順番で処理されるか不明である。forEachメソッドは、到達した順番で実行する。
 *		それに対し、forEachOrderedメソッドは、ストリームの順序とおりに処理する。これは、並列化されないことを意味する。
 *		これらのメソッドは終端操作であるため、この後のストリームは利用できない。もし、全要素の処理の後にストリームを
 *		利用したいのであれば、［中間操作メソッド］で説明したpeekメソッドを使用する。
 *
 *	［終端操作：マップへ結果をコレクト(収集)する：toMapメソッド］
 *	マップは、キーとそれに対応する値をペアにして管理するオブジェクトである(キーの重複は許されない)。
 *	現場では、一時格納や検索用として多用される。
 *
 *	いま、従業員のリストがある。これをストリームを使用してマップ化(社員IDと氏名)し、IDで氏名を検索したいと考える。	*/
		System.out.println("-- 終端操作：toMapメソッド --");
		class Employee {
			private int id;	private String name;															// フィールド
			public Employee(int id, String name) {this.id = id; this.name = name;}		// コンストラクタ
			public int getId() {return id;	}																	// アクセサ
			public String getName() {return name;	}													// アクセサ
		}
		List<Employee> lst = List.of(new Employee(1, "日本太郎"), new Employee(2, "安芸次郎"));	// 不変リストとする
		Map<Integer, String> idName = lst.stream().collect(Collectors.toMap(Employee::getId, Employee::getName));
		idName.forEach((k, v) -> System.out.println(k + " = " + v));							// 表示
//		for(Integer key : idName.keySet()) System.out.println(key + " = " + idName.get(key));	// 別解
/*	リストなので可変リダクション操作であるcollectメソッドを使用する。
 *	toMapメソッドは、第1引数にキー生成する関数を、第2引数に値を生成する関数を指定し、ストリーム値をマップ化する。
 *	これで、社員IDで氏名を検索するマップが完成する。
 *
 *	次に、社員IDを検索キーとし、対応するクラスのインスタンスそのものを値とするマップを作成したいとすると、	*/
		System.out.println("-- 終端操作：toMapメソッド(クラスのインスタンス) --");
		Map<Integer, Employee> idClass = lst.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
		idClass.forEach((k, v) -> System.out.println(k + " = " + v.getName()));							// 表示
//		for(Integer key : idClass.keySet()) System.out.println(key + " = " + idClass.get(key).getName());	// 別解
/*	ここで、Function.identity()は、常に入力関数を返す関数である(入力はストリーム値=Employeeクラスのインスタンス)。
 *	もちろん、この代わりに x -> x でも可である。
 *
 *	これら2つのマップ化は、IDに重複がないことが前提である。もし、重複があると、IllegalStateException例外がスローされる。
 *	リストの要素はセットではないので重複可である。とすれば、いつ例外をスローしてもおかしくない。これではプログラム化できない
 *	ため、キーに対する値を決定する振る舞いを決める関数を第3引数に指定すると、この問題が解決する。
 *	方法は3つ。最初に出現した値を採用するか、最後に出現した値を採用するか、出現する値を組み合わせた値を採用するかである。	*/
		List<Employee> lst2 = List.of(new Employee(1, "name1"), new Employee(2, "name2"),		// 不変リストとする
																	new Employee(3, "name3"), new Employee(1, "name1重複")); 	// ID=1 が重複
/*	・最初に出現した値を採用するケース	*/
		Map<Integer, String> idName2 = lst2.stream().collect(Collectors.toMap(Employee::getId, Employee::getName, (e, n) -> e));
/*	・最後に出現した値を採用するケース	*/
		Map<Integer, String> idName3 = lst2.stream().collect(Collectors.toMap(Employee::getId, Employee::getName, (e, n) -> n));
/*	・出現する値を組み合わせた値を採用するケース：値を「,」で連結	*/
		Map<Integer, String> idName4 = lst2.stream().collect(Collectors.toMap(Employee::getId, Employee::getName, (e, n) -> e + "," + n));
/*
 *	TreeMapを得たいのであれば、第4引数にTreeMapのコンストラクタを渡す。	*/
		Map<Integer, Employee> idClass2 = lst.stream().collect(Collectors.toMap(Employee::getId, Function.identity(),
				(e, n) -> {throw new IllegalStateException();}, TreeMap::new));	// 重複時は例外をスロー
/*
 *	もし、結果収集されるマップが要素順序を問わないのであれば並列化した方が効率的である。この場合は、toMapメソッドの代わりに
 *	toConcurrentMapメソッドを使用する。使用方法はtoMapと同じである。コンカレントとは「並行して」「同時」という意味である。
 *
 *	［終端操作：結果収集のグループ分けと分割：groupingBy/partitioningByメソッド］
 *	ここで、Locale(ロケール)について触れておく。Localeは、特定の地域を様々なフィールド値で管理されているオブジェクトである。
 *	一般的なのは、国とその国で使用される言語や通貨である。たとえば、スイスは、Localeに登録されている言語がフランス語、ドイツ語、
 *	イタリア語の3語ある。
 *	このように、1つの国に複数の言語があると、先ほどの例のようにマップで管理できなくなる(国をキーにして言語を値にすると、キーが
 *	重複してしまうので)。そこで、この問題を解決するのが、グループ分けという機能である。
 *
 *	●groupingByメソッド
 *		このメソッドは、分類する関数(クラシファイア関数)をキーとしてグループ分けをし、リストに値を収集する。*/
			System.out.println("-- 終端操作：groupingByメソッド --");
			Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());	// すべてのロケールの配列をストリーム化
			Map<String, List<Locale>> countryLocale = locales.collect(Collectors.groupingBy(Locale::getCountry));
			List<Locale> ch = countryLocale.get("CH");											// スイスの言語をList化
			ch.forEach(System.out::println);															// 3つのロケールを表示
/*		分類関数は、国コードを取得するLocaleが持つgetCountryメソッドである。これをキーとして、重複する国コードを
 *		リスト化する。スイスの国コードは「CH」なので、これをキーとしてリストを取得し、表示すると、3つのロケールが表示される。
 *		ロケールfr_CHは、最初が言語コード、末尾が国コードなので、「スイスでのフランス語」ということになる。
 *		※groupingByConcurrentメソッドで並列化される。toMapとtoConcurrentMapの関係と同等。
 *
 *	●partitioningByメソッド
 *		もし、分類関数が述語関数(boolean値を返す関数)であれば、true/falseの2つに分類される。
 *		この場合には、partitioningBy(分割)メソッドを利用する。目安として、男女など世の中的に2つの場合はこのメソッドが効率的である。	*/
			System.out.println("-- 終端操作：partitioningByメソッド --");
			Stream<Locale> locales2 = Stream.of(Locale.getAvailableLocales());
			Map<Boolean, List<Locale>> eng = locales2.collect(Collectors.partitioningBy(l -> l.getLanguage().equals("en")));
			List<Locale> englocales = eng.get(true);	// 単に表示のみでよいなら、eng.get(true).forEach(System.out::println);でよい
			englocales.forEach(System.out::println);
/*		この例は、英語("en")言語を使用するロケールと、そうでないロケールに分割し、英語言語を使用するロケールを取得している。
 *
 *	［終端操作：ダウンストリーム・コレクタ］
 *	groupingByメソッドはリストを結果収集するメソッドである。リストではなくセットを取得したい場合がある。
 *	この場合は、ダウンストリーム・コレクタ(下流リダクションを実装したコレクタ：分類した後に何らかの処理をするコレクタ)
 *	にCollectors.toSetをgroupingByメソッドの第2引数に指定する。
 *	●toSetメソッド*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：toSetメソッド --");
			Stream<Locale> locales3 = Stream.of(Locale.getAvailableLocales());
			Map<String, Set<Locale>> countryLocaleSets = locales3.collect(Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));
/*		この例は、リストの代わりにセットにキーに対する値を結果収集している。
 *
 *	その他の、ダウンストリーム・コレクタを次に挙げる。
 *	●countingメソッド
 *		結果収集された要素数を生成する。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：countingメソッド --");
			Stream<Locale> locales4 = Stream.of(Locale.getAvailableLocales());
			Map<String, Long> countryToLocaleCounts = locales4.collect(Collectors.groupingBy(Locale::getCountry, Collectors.counting()));
			System.out.println("counting：" + countryToLocaleCounts);
/*		この例は、各国ごとのロケール数を生成する。
 *
 *	●summingInt/summingLong/summingDoubleメソッド
 *		引数に関数を指定し、その関数をダウンストリームの各要素に適用して、合計を生成する。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：summingIntメソッド --");
			class City {
				private int population;/*人口*/ private String pref;/*都道府県*/ private String area;/*市町村*/
				public City(int population, String state, String area) {this.population = population; this.pref = state; this.area = area;}
				public int getPopulation() {return population;}
				public String getPref() {return pref;}
				public String getArea() {return area;}
			}
			List<City> c1 = List.of(new City(100, "東京", "渋谷区"), new City(90, "大阪", "堺市"),		// 不変リストとする
				new City(50, "広島", "広島市"), new City(200, "東京", "中野区"), new City(80, "大阪", "高槻市"), new City(40, "広島", "尾道市"));

			Map<String, Integer> prefToPop = c1.stream().collect(Collectors.groupingBy(City::getPref, Collectors.summingInt(City::getPopulation)));
			prefToPop.forEach((k, v) -> System.out.println(k + " = " + v));
/*		この例は、Cityクラスインスタンスのリスト作成から、都道府県別(キー)に対する人口合計(値)のマップを結果収集している。
 *
 *	●maxBy/minByメソッド
 *		引数にコンパレータを指定し、その関数をダウンストリームの各要素に適用して、最大値/最小値を生成する。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：maxBy/minByメソッド --");
			Map<String, Optional<City>> mCity = c1.stream().collect(Collectors.groupingBy(City::getPref, Collectors.maxBy(Comparator.comparing(City::getPopulation))));
			mCity.forEach((k, v) -> System.out.println(k + " = " + v.get().getArea()));
/*		この例は、都道府県別(キー)に対する最大人口の市町村名のマップを結果収集している。
 *		maxByメソッドは、指定されたコンパレータ(人口比較)にしたがってOptional<T>(例ではOptional<City>)として記述された
 *		最大要素を生成するコレクタを返す(minByメソッドは最大要素を最小要素に読み替える)。
 *		指定されているコンパレータは、「Comparator.comparing(City::getPopulation)」となっている。
 *		これは、分類関数をキーとしてグループ分けした各都道府県の中で、人口をcomparing(Cityクラスから比較するキー<人口>を
 *		受け取り、そのキーで比較するコンパレータを返す)している。東京であれば、東京のキーで2つのインスタンスが該当し、
 *		それぞれの人口である100と200を比較するコンパレータから最大値(200)のCityクラスインスタンスである[200, "東京", "中野区"]を
 *		返すことになる。したがって、結果表示は、東京が中野区、大阪が堺市、広島が広島市となる。
 *
 *	●summarizingInt/summarizingLong/summarizingDoubleメソッド
 *		［終端操作：結果をコレクト(収集)する］で説明したsummarizingInt/summarizingLong/summarizingDoubleメソッドを指定
 *		すると、サマリ統計オブジェクト(IntSummaryStatistics/LongSummaryStatistics/DoubleSummaryStatistics)を結果収集
 *		できる。これらのメソッドを使用すると、カウント、合計、最大、最小、平均を一気に取得できる。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：summarizingIntメソッド --");
			Map<String, IntSummaryStatistics> statc1 = c1.stream().collect(Collectors.groupingBy(City::getPref, Collectors.summarizingInt(City::getPopulation)));
			statc1.forEach((k, v) -> System.out.println(k + "(合計) = " + v.getSum()));
			statc1.forEach((k, v)-> System.out.println(k + "(数) = " + v.getCount()));
			statc1.forEach((k, v) -> System.out.println(k + "(平均) = " + v.getAverage()));
/*		最大、最小のみを求めるのであれば、maxBy/minByメソッドでよいが、複数統計を求めるのであれば、このメソッドが効率的である。
 *
 *	●mappingメソッド
 *		ダウンストリームの結果に対して関数を適用(マッピング)する。第2引数にその関数を指定する。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：mappingメソッド1 --");
			Map<String, Optional<String>> maxL = c1.stream().collect(Collectors.groupingBy(City::getPref, Collectors.mapping(City::getArea, Collectors.maxBy(Comparator.comparing(String::length)))));
			maxL.forEach((k, v) -> System.out.println(k + " = " + v.get()));
/*		この例は、都道府県(キー)でグループ分けをして、都道府県別の市町村の名前を生成し、最大文字列長の市町村名を取得している。
 *		2段階で処理が行われると考えるとわかりやすい。
 *		東京や広島はすべて同じ文字列長である。この場合は、最初に適用した要素が収集される。
 *		このメソッドを使用すると、前述のロケールの処理で、各国のすべての言語を収集するセットを取得することが簡単に可能になる。
 *		※前述の例では、各国のロケールを結果収集することが目的であったが、さらに関数を適用できるのでピンポイントで取得できる。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：mappingメソッド2 --");
			Stream<Locale> locales5 = Stream.of(Locale.getAvailableLocales());
			Map<String, Set<String>> countryLanguageSets2 = locales5.collect(Collectors.groupingBy(Locale::getDisplayCountry, Collectors.mapping(Locale::getDisplayLanguage, Collectors.toSet())));
			countryLanguageSets2.forEach((k, v) -> System.out.println(k + " = " + v));
/*
 *	●reducingメソッド
 *		ダウンストリームの結果に対して通常のリダクション(還元)を適用する。
 *		このメソッドは3つの形式がある(［終端操作：リダクション(還元)操作：reduceメソッド］のStream.reduceメソッドの形式
 *		のように：ただし、各形式で仕様が異なる)。
 *		3つ目の形式で例を挙げる(1つ目、2つ目は割愛)。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：reducingメソッド1 --");
			Map<String, String> aName = c1.stream().collect(Collectors.groupingBy(City::getPref, Collectors.reducing("", City::getArea, (s, t) -> s.length() == 0 ? t : s + ", " + t)));
			aName.forEach((k, v) -> System.out.println(k + " = " + v));
/*		この例は、都道府県(キー)でグループ分けをして、都道府県別の市町村名を名前を生成し、それらを「, 」で連結している。
 *		Stream.reduceメソッドと同じように、現場では使用されないだろう。このメソッドの代わりに、1つ前のmappingメソッドを
 *		使用した方が簡単に実現できる。	*/
			System.out.println("-- 終端操作：ダウンストリーム・コレクタ：reducingメソッド2 --");
			Map<String, String> aName2 = c1.stream().collect(Collectors.groupingBy(City::getPref, Collectors.mapping(City::getArea, Collectors.joining(", "))));
			aName2.forEach((k, v) -> System.out.println(k + "(別解) = " + v));
/*
 *	［基本データ型ストリーム］
 *	ここまで、整数のストリームをStream<Integer>で収集してきた。Integerはintのラッパークラスであるため、ボクシングが発生
 *	してしまう。これは非効率的である。
 *	そこで、ラッパークラスを使用せずに基本データ型を扱う特別な型がある。これが基本データ型ストリームである。
 *		IntStream：int, short, char, byte, booleanが対象
 *		LongStream：longが対象
 *		DoubleStream：float, doubleが対象
 *
 *	●IntStream作成(LongStream/DoubleStreamは同じ内容なので割愛)
 *		前述の［ストリームの生成］で説明した、staticのofメソッド/Array.streamメソッドで作成できる。	*/
			System.out.println("-- 基本データ型ストリーム：IntStream作成 --");
			IntStream ints1 = IntStream.of(1, 3, 4, 12);			// 可変個
			ints1.forEach(System.out::println);
			int[] ints2ary = {1, 3, 4, 12};								// 配列
			IntStream ints2 = Arrays.stream(ints2ary, 0, 2);	// 全件投入するなら、Arrays.stream(int2ary)でもIntStream.of(int2ary)でもよい
			ints2.forEach(System.out::println);
/*
 *	●range/rangeClosedメソッド
 *		IntStreamとLongStreamには、staticのrange/rangeClosedメソッドがある。これらは、1ずつ増加する整数範囲を生成する。
 *		rangeメソッドは第2引数より1小さい数まで、rangeClosedメソッドは第2引数まで生成する。	*/
			System.out.println("-- 基本データ型ストリーム：rangeメソッド --");
			IntStream ints3 = IntStream.range(0, 10);			// 0～(10-1)を作成
			System.out.println(ints3.mapToObj(String::valueOf).collect(Collectors.joining(", ")));
			System.out.println("-- 基本データ型ストリーム：rangeClosedメソッド --");
			IntStream ints4 = IntStream.rangeClosed(0, 10);	// 0～10を作成
//			System.out.println(ints4.mapToObj(Integer::toString).collect(Collectors.joining(", ")));
			System.out.println(ints4.mapToObj(String::valueOf).collect(Collectors.joining(", ")));
/*		ここで、mapToObjメソッドはint->Stringに変換する。逆はmapToIntメソッドである。
 *		次に、mapToxxxの変換例を挙げる。
 *		・String->int
 *				Stream<String> h1 = Stream.of("1", "4");
 *				IntStream h10 = h1.mapToInt(Integer::parseInt);
 *		・String->int:文字列長
 *				IntStream h11 = h1.mapToInt(String::length);
 *		・int->String
 *				IntStream h2 = IntStream.of(1, 4);
 *				Stream<String> h20 = h2.mapToObj(String::valueOf);
 *				Stream<String> h21 = h2.mapToObj(Integer::toString);
 *		・int->Integer
 *				Stream<Integer> h22 = h2.mapToObj(n -> n);	// ボクシング適用
 *				Stream<Integer> h23 = h2.mapToObj(Integer::valueOf);
 *				Stream<Integer> h24 = h2.boxed();					// 明示的にボクシングするboxedメソッド適用
 *		・Integer->int
 *				Stream<Integer> h3 = Stream.of(1, 4);
 *				IntStream h31 = h3.mapToInt(n -> n);				// アンボクシング適用
 *				IntStream h32 = h3.mapToInt(Integer::intValue);
 *		・int->long
 *				LongStream h41 = h2.mapToLong(n -> n);
 *				LongStream h42 = h2.asLongStream();
 *
 *	［基本データ型ストリーム：オブジェクトストリーム(Stream<String>など)と異なるメソッドの特徴］
 *	●toArrayメソッド
 *		基本データ型の配列を返す。引数なし。	*/
			System.out.println("-- 基本データ型ストリーム：toArrayメソッド --");
			IntStream ints5 = IntStream.of(100, 200, 300);
			int[] intary = ints5.toArray();			// オブジェクトストリームのtoArrayは配列の型のコンストラクタ参照が必要
			for (int rint : intary) System.out.println(rint);
/*
 *	●sum/average/max/minメソッド
 *		オブジェクトストリームにはない、合計、平均、最大、最小値を求めるメソッドがある。	*/
			System.out.println("-- 基本データ型ストリーム：sum/average/max/minメソッド --");
			IntStream ints6 = IntStream.of(10, 20, 25);
			int intsum = ints6.sum();
			System.out.println(intsum);
/*
 *	●OptionalInt/OptionalLong/OptionalDouble型
 *		これらの型を返すメソッドには、findAny, findFirst, max, min, reduceメソッドがある。	*/
			System.out.println("-- 基本データ型ストリーム：OptionalInt/OptionalLong/OptionalDoubleメソッド --");
			IntStream ints7 = IntStream.of(10, 20, 25);
			OptionalInt intmax = ints7.max();
			if (intmax.isPresent()) System.out.println(intmax.getAsInt());
/*		OptionalInt値を取得するには、Optional値のgetメソッドではなく、getAsIntメソッドを使用する。
 *
 *	●summaryStatisticsメソッド
 *		前述したIntSummaryStatistics/LongSummaryStatistics/DoubleSummaryStatisticsクラスのオブジェクトを返す。
 *		これで、カウント、合計、最大、最小、平均を一気に取得できる。	*/
			System.out.println("-- 基本データ型ストリーム：summaryStatisticsメソッド --");
			IntStream ints8 = IntStream.of(10, 20, 25);
			IntSummaryStatistics intstat = ints8.summaryStatistics();
			long scnt = intstat.getCount(); System.out.println("カウント = " + scnt);
/*
 *	●ints/longs/doublesメソッド
 *		乱数を基本データ型ストリームに返すメソッドである。	*/
			System.out.println("-- 基本データ型ストリーム：ints/longs/doublesメソッド --");
			IntStream ints9 = new Random().ints();		// generateやiterateメソッドではないが、事実上の無限ストリーム
			ints9.limit(5).forEach(System.out::println);		// 5つに限定
/*
 *	［関数パラメタの<? super T>ジェネリックスの意味］
 *	Stream.filterメソッドのJavadocを見てみると、Stream<T> filter(Predicate<? super T> predicate) と記述がある。
 *	Predicate<? super T> predicate の<? super T>は、filterメソッドに限らず、関数パラメタでよく見かけるジェネリックスである。
 *	今、class Ko extends Oya の関係があるとき、Stream<Ko>のストリームがあるとする(TがKo)。このストリームに、
 *	filterをかけるとき、引数には、Predicate<Ko>、Predicate<Oya>、Predicate<Object>の3つの型を指定できる。
 *	これが、<? super T>ジェネリックスの意味である。継承関係が何層もある場合はすべてのクラスを指定できる(?のワイルドカード)。
 *	なぜ、このような仕様になっているかというと、継承関係であれば、KoはOyaのメソッドは無条件に参照できる。
 *	そこで、predicate関数としてOyaの「メソッド参照」したいと考えるのが普通である。
 *	もし、Predicate<T>のみとなると、せっかく継承関係があるのに、利用できないということになる。
 *	※この説明は引数型にsuperを指定した場合であるが、戻り値型形式には「extends」を使用する。考え方は同様である。
 *
 *	［StreamとCollectorsクラスのメソッドパラメタで使用される関数型インタフェース］
 *
 *	関数型インタフェース		パラメータ型	戻り値型	関数メソッド									説明
 *	--------------------------------------------------------------------------------------------------------------------
 *	Supplier<T>					なし				T				get()												T型の値を供給する
 *	--------------------------------------------------------------------------------------------------------------------
 *	Consumer<T>				T					void			accept(Object)								T型の値を消費する
 *	--------------------------------------------------------------------------------------------------------------------
 *	BiConsumer<T, U>		T, U				void			accept(Object, Object)					T型とU型の値を消費する
 *	--------------------------------------------------------------------------------------------------------------------
 *	Predicate<T>				T					boolean		test(Object)									ブール値関数
 *	--------------------------------------------------------------------------------------------------------------------
 *	ToIntFunction<T>			T					int				applyAsInt(Object)							int値関数
 *	ToLongFunction<T>		T					long			applyAsLong(Object)						long値関数
 *	ToDoubleFunction<T>	T					double		applyAsDouble(Objrct)					double値関数
 *	--------------------------------------------------------------------------------------------------------------------
 *	IntFunction<R>				int					R				apply(int)										int型の引数を持つ関数
 *	LongFunction<R>			long				R				apply(long)									long型の引数を持つ関数
 *	DoubleFunction<R>		double			R				apply(double)								double型の引数を持つ関数
 *	--------------------------------------------------------------------------------------------------------------------
 *	Function<T, R>				T					R				apply(Object)								T型の引数を持つ関数
 *	--------------------------------------------------------------------------------------------------------------------
 *	BiFunction<T, U, R>		T, U				R				apply(Object, Object)						T型とU型の引数を持つ関数
 *	--------------------------------------------------------------------------------------------------------------------
 *	UnaryOperator<T>		T					T				Function.apply(Object)					T型に対する単項演算
 *	--------------------------------------------------------------------------------------------------------------------
 *	BinaryOperator<T>		T, T				T				BiFunction.apply(Object, Object)	T型に対する二項演算
 *	--------------------------------------------------------------------------------------------------------------------
 *
 *	前述の［終端操作：リダクション(還元)操作：reduceメソッド］説明でもわかるように、通常は、ラムダ式またはメソッド参照を
 *	適用する。
 *	前述例と上記表の意味が分かれば、次の例が理解できる。
 *	●Supplier<T>の例：パラメタ型なし(ラムダ式で引数なし)、戻り値型Integer(sup::lengthの戻り値型)：Integer値を供給	*/
		System.out.println("-- 関数型インタフェース：Supplier<T> --");
		String sup = new String("長さはいくつ？");
		Supplier<Integer> suplen = sup::length;				// () -> sup.length() でも可
		System.out.println(suplen.get());							// 7 と表示
/*	●Consumer<T>の例：パラメタ型String(ラムダ式で引数sが1つ)、戻り値型void(System.out::printlnは何も返さない)：String値を消費	*/
		System.out.println("-- 関数型インタフェース：Consumer<T> --");
		Consumer<String> condis = System.out::println;	// s -> System.out.println(s) でも可
		condis.accept("Stringの値を消費した");					// "Stringの値を消費した"と表示
																					// もし、int値1を指定するとエラーになる：パラメタ型がStingなので
/*	●BinaryOperator<T>の例：パラメタ型Integer, Integer(ラムダ式で引数xとyの2つ)、戻り値型Integer(Math::minの戻り値型)：二項演算	*/
		System.out.println("-- 関数型インタフェース：BinaryOperator<T> --");
		BinaryOperator<Integer> bimin = Math::min;		// (x, y) -> Math.min(x, y) でも可
		System.out.println(bimin.apply(50, 100));				// 50 と表示
/*	●Function<T, R>の例：	パラメタ型Integer(ラムダ式で引数nが1つ)、戻り値型Func(Func::newの戻り値型)：Integer引数を持つ関数*/
		System.out.println("-- 関数型インタフェース：Function<T, R> --");
		class Func {
			private int pop;													// フィールド
			Func (int pop) { this.pop = pop; }						// コンストラクタ
			public int getPop() {return pop; }						// アクセサ
		}
		Function<Integer, Func> funcpop = Func::new;	// n -> new Func(n) でも可
		Func func = funcpop.apply(90);								// Func func = new Func(90); と等価
		System.out.println(func.getPop());							// 90 と表示
/*
 *	ここでは、ストリームに関する関数型インタフェースを説明したが、Javaにはたくさんの関数型インタフェースがある。
 *	ラムダ式の説明で例として挙げた Runnable も関数型インタフェースであるし、また、上記の関数型インタフェースの例題で
 *	気になったことだろうが、どうしても基本データ型を使用すると、ボクシングが発生してしまう。
 *	これを解決するために、たとえば、Supplier<T>の代わりにIntSupplier(<T>は付かない：戻り型はintで決定しているので)があり、
 *	intに特化した関数型インタフェースが	ある。これは、int、long、doubleにそれぞれに存在する。
 *	上記のSupplier<T>の例の正解は次のようになる(ボクシングが発生しない)。
 *		IntSupplier isuplen = sup::length;
 *		System.out.println(isuplen.getAsInt());
 *
 *	次の表は、基本データ型に特化した関数型インタフェースを示す。
 *	※関数メソッドは関数名のみとし、「説明」は前述の表と同じであるため割愛する。
 *	※△と□は、int、long、double。▲と■は、Int、Long、Double
 *
 *	関数型インタフェース		パラメータ型	戻り値型	関数メソッド
 *	--------------------------------------------------------------------------------------------------------------------
 *	BooleanSupplier				なし				boolean		getAsBoolean
 *	▲Supplier						なし				△				getAs▲
 *	--------------------------------------------------------------------------------------------------------------------
 *	▲Consumer					△					void			accept
 *	--------------------------------------------------------------------------------------------------------------------
 *	Obj▲Consumer<T>		T, △				void			accept					(注)Bi▲Consumerではない
 *	--------------------------------------------------------------------------------------------------------------------
 *	▲Predicate					△					boolean		test
 *	--------------------------------------------------------------------------------------------------------------------
 *	▲Function<T>				△					T				apply						※前表と重複
 *	--------------------------------------------------------------------------------------------------------------------
 *	▲To■Function				△					□				applyAs■
 *	--------------------------------------------------------------------------------------------------------------------
 *	To▲Function<T>			T					△				applyAs▲				※前表と重複
 *	--------------------------------------------------------------------------------------------------------------------
 *	To▲BiFunction<T, U>	T, U				△				applyAs▲
 *	--------------------------------------------------------------------------------------------------------------------
 *	▲UnaryOperator			△					△				applyAs▲
 *	--------------------------------------------------------------------------------------------------------------------
 *	▲BinaryOperator			△, △			△				applyAs▲
 *	--------------------------------------------------------------------------------------------------------------------
 */

	}

/*********************************** 説明で使用するためのメソッド ***********************************/
	// optional値の合成用メソッド
	public static Optional<Double> inverse (Double x) {
		return x == 0 ? Optional.empty() : Optional.of(1 / x);
	}
	public static Optional<Double> squareRoot (Double x) {
		return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
	}
/* 参考文献：Java SE8実践プログラミング */
}