package jp.ac.hsc.java7special;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Java7Special {

	public static void main(String[] args) {
/*********************************** 重要なJava7機能 ***********************************/
/*	［try-with-resources構文］
 *	Java6までは、リソース(ファイルなど)のオープン、クローズ処理を、次のように記述していた(catch処理を除く)。
 *		// リソースオープン
 *		try {
 *				// リソース処理
 *		} finally {
 *				// リソース解放(クローズ)
 *		}
 *	クローズ処理を失念すると、メモリリークやファイル競合など、悲惨な結果が待っている。
 *	Java7は、クローズ処理を記述しなくても確実にリソースを解放する「try-with-resources構文」を提供した。
 *	構文は、次のとおりである。従来の「try」と「{」の間に、リソースオープンを「()」でくくる。
 *		<Java7文法>											<java9文法>
 *		try (リソースの型 res = ・・・) {				リソースの型 res = ・・・
 *				// resの処理									try (res) {
 *		}																	// resの処理
 *																	}
 *
 *	もう面倒なクローズ処理もfinallyブロックも不要である。
 *	挙動としては、tryブロックが終了するときに、res.close()が自動的に発行される。
 *	注意点は、これが効くのは、リソースがAutoCloseableインタフェースを持っていなければならない。
 *	通常のファイル操作やストリーム操作であればAutoCloseableインタフェースを持っているので、有効である(使用前に調査要)。
 *	※下記の「Stream<String>」にカーソルを入れてF3キーで確認		*/
		System.out.println("-- try-with-resources構文 --");
		try (Stream<String> lstream = Files.lines(Paths.get("D:\\tmp\\test.txt"), Charset.forName("MS932"))) {
			lstream.forEach(System.out::println);
		} catch (IOException e) { }
/*	上記であれば、tryブロックが無事に終了、または、例外がスローされると、従来のfinallyブロックでクローズ処理したように、
 *	lstream.close()メソッドが自動的に呼び出される。
 *
 *	さらに、try-with-resources構文でのリソース再割り当てがJava9で不要になった。リソース割り当てをtryの外でできるので、
 *	tryが簡素になり、柔軟なCDが可能となる(Java9afterプロジェクト参照)。
 *
 *	複数のリソースの場合に適用したい場合には、「try ()」のかっこの中に「;」で区切って列挙することで実現する。
 *		try (リソースの型 res1 = ・・・; リソースの型 res2 = ・・・) {
 *			// res1、res2の処理
 *		}
 *	ただし、多くのリソースをかっこの中に埋め込むと、デバッグ時の特定が困難になる点に留意する。
 *	また、この構文は、catchブロック、finallyブロックを記述できる。catchブロック、finallyブロックは、リソース解放の後に実行
 *	されるので注意すること(リソースは利用不可)。
 *
 *	［抑制された例外：getSuppressedメソッド］
 *	入出力を扱う場合は、例外がつきまとう。今、IOException例外が発生したと想定してみる。
 *	すると、このリソース解放のcloseメソッドでも別の例外をスローする可能性がある。
 *	Java6までは、finallyブロックでリソース解放(クローズ処理)をしていた。Javaでは、finallyブロックでスローされた例外は、
 *	その前に発生した例外を破棄することになっている。これでは、最後のリソース解放の例外のみ知らされ、本質的な最初の例外は
 *	闇の中である。これでは困る。
 *	そこで、try-with-resources構文のお出ましである。try-with-resources構文は、この挙動を逆にして、リソース解放時の
 *	closeメソッド(当然、AutoCloseableインタフェースを実装しているリソースの)で例外がスローされた場合には、元々発生した
 *	例外を再スローし、closeメソッドの例外は再スローされる例外の中に保持される(情報が包まれる)。
 *	これを、「抑制された例外」という。最後の例外(closeメソッドでの例外)は抑制(suppressed)されたことを意味する。
 *	では、抑制された例外はどうやって取得するのか。そのためのgetSuppressedメソッドがJava7で提供された。
 *	getSuppressedメソッドは主となる例外(最初のIOException例外)に保持されている抑制された例外を取得する。
 *	次の例は、故意に、ここでの説明を再現してみる。	*/
		System.out.println("-- 抑制された例外：getSuppressedメソッド --");
		InputStream instm = new InputStream() {		// 独自のInputStreamを生成
			@Override
			public int read() throws IOException {									// read()本体
				throw new IOException("最初の例外だよ：read()");						// 強制的にIOException例外をスロー
			}
			@Override
			public void close() throws IOException {								// close()本体
				throw new IOException("抑制された例外だよ：close()");				// 強制的にIOException例外をスロー
			}
		};
		try (instm) {																				// Java9記述
			System.out.println(instm.read());											// read()の呼び出し：これで例外発生
		} catch (IOException e) {										// このブロックの直前にclose()が走る
			System.out.println(e);										// 最初の例外を表示
			Throwable[] supExs = e.getSuppressed();			// 抑制された例外を取得(今回は1つのみ：本来は、抑制された分)
			System.out.println(Arrays.toString(supExs));		// 抑制された例外を表示
		}
/*	例外は派生するものであるから、この手法は有用である。派生する抑制された例外は、1つとは限らないので、そのすべてを主となる
 *	例外に保持される。それらを取得するので、必然的に配列で受け取ることになる。
 *
 *	抑制された例外は、try-with-resources構文使用を想定しているが、通常箇所でも使用可能である。が、果たしてそのような場面が
 *	あるかは疑問である。仮に、自分で主たる例外に保持させたいのであれば、
 *		e.addSuppressed(追加したい例外);
 *	で追加する。
 *
 *	［複数の例外をcatchする］
 *	1つのcatchで複数の例外をキャッチできるようになった(「|」使用)。
 *		try {
 *				// 例外をスローする可能性のある処理
 *		} catch (FileNotFoundException e) {
 *				// ファイルが存在しないときの処理
 *		} catch (IOException | SQLException e) {
 *				// 他のI/OエラーとDBに関するエラー時の処理
 *		}
 *	例外処理が同じロジックであれば、共通化できるので便利である。
 *	ただし、上記例であれば、FileNotFoundExceptionとIOExceptionを列挙できない。FileNotFoundExceptionは、IOExceptionの
 *	サブクラスである。これは、コンパイルエラーになる。したがって、複数指定できるための条件は、互いにどちらのサブクラスでは
 *	ないことである。
 *	もちろん、FileNotFoundExceptionとIOExceptionの順序を逆にしてはいけないことは周知である(最初にIOExceptionを書いて
 *	しまうとその配下の例外をすべてキャッチしてしまうので)。
 *
 *	［ファイル操作］
 *	Java7では、ファイル操作に関する機能が多く提供された。その根本となるのが、Java7で提供された、Pathインタフェースである。
 *
 *	●パス
 *		Pathは、複数のディレクトリ名の連続であり、末尾にファイル名が含まれていることもある。
 *		パスの最初の要素が、ルート要素(Windowsであれば、C:\など)であれば絶対パスであり、ルート要素でなければ
 *		相対パスである。
 *			・絶対パス指定	*/
					System.out.println("-- ファイル操作：パス：絶対パス(Paths.getメソッド) --");
					Path absPath = Paths.get("D:\\", "tmp", "test.txt");
					System.out.println(absPath);												// D:\tmp\test.txt
/*			・相対パス指定	*/
					System.out.println("-- ファイル操作：パス：相対パス(Paths.getメソッド) --");
					Path relPath = Paths.get("tmp", "test.txt");
					System.out.println(relPath);												// tmp\test.txt
/*		staticのPaths.getメソッドは、1つ以上の文字列を受け取って、対象のファイルシステムのパス区切り(Unixは「/」、
 *		Windowsは「\」)で連結したパス(Path)を生成する。
 *		※Java11から「Path.ofメソッド」の仕様となる。以後はJava11仕様とする。
 *		もし、指定した文字列を結合したパス文字列をPathに変換できなければ(無効なパス)、InvalidPathException例外
 *		をスローする(パス文字列は、実際に実機に存在している必要はない)。
 *		もちろん、絶対パスまたは相対パスが既に形成されている場合は、単にそれを指定すればよい。
 *			Path directPath = Paths.get("D:\\tmp\\test.txt");
 *
 *	●パスの解決
 *		パス同士を連結・解析するには、resolveメソッド(インスタンスメソッド)を使用する。
 *			orgPath.resolve(otherPath)
 *		resolveメソッドの規則は、次のとおりである。
 *			・otherPathが絶対パスであれば、結果はotherPathである。すなわち、orgPathは無視される。
 *			・otherPathが相対パスであればディレクトリとみなして、orgPathとotherPathを連結したパスを返す。	*/
					System.out.println("-- ファイル操作：パスの解決(resolveメソッド) --");
					Path orgPath = Path.of("D:\\");
					Path otherPath1 = Path.of("D:\\tmp\\text.txt");
					Path otherPath2 = Path.of("tmp\\text.txt");
					System.out.println(orgPath.resolve(otherPath1));		// otherpath1を返却
					System.out.println(orgPath.resolve(otherPath2));		// orgpathとotherpathを連結して返却
/*
 *	●パスのシブリング生成
 *		パスの親(元)を最初に解決して、シブリング(sibling：兄弟)パスを生成するには、resolveSiblingメソッドを
 *		使用する。これは、ファイル名を別のファイル名に置き換える必要がある場合に役立つ。	*/
			System.out.println("-- ファイル操作：パスのシブリング生成(resolveSiblingメソッド) --");
			Path hpath = Path.of("D:\\tmp\\dummy");		// hpathの親パスは1つ上のD:\tmp
			Path tpath = hpath.resolveSibling("org");			// 親パスにorgを連結する：dummyディレクトリを置換
			System.out.println(tpath);
/*		引数が絶対パスの場合は、その引数を返す。
 *
 *	●パスの相対化
 *		resolveメソッド(解決)の逆の効果(相対化)があるのは、relativizeメソッドである。
 *		mPath.relativize(spath)は、mPathからsPathへの経路を示す相対パスを生成する。	*/
			System.out.println("-- ファイル操作：パスの相対化(relativizeメソッド) --");
			Path mPath  = Path.of("D:\\tmp\\org");
			Path sPath = Path.of("D:\\tmp\\dummy");
			System.out.println(mPath.relativize(sPath));	// 1つ上がってからの相対となる
/*
 *	●パスの正規化
 *		normalizeメソッドは、「.」と「..」要素を取り除く。解決や相対化によって生成されたパスを正規化する。
 *		正規化とは、単純に取り除くわけではなく、相対が考慮される。	*/
			System.out.println("-- ファイル操作：パスの正規化(normalizeメソッド) --");
			Path makedPath = Path.of("D:\\tmp\\.\\dummy\\..\\org");
			System.out.println(makedPath.normalize());	// D:\tmp\org
/*
 *	●パスの絶対化
 *		toAbsolutePathメソッドは、指定されたパスの絶対パスを生成する。
 *		指定されたパスが絶対パスでない場合は、ユーザディレクトリ(VMが起動されたディレクトリ)に対して解決される。	*/
			System.out.println("-- ファイル操作：パスの絶対化(toAbsolutePathメソッド) --");
			Path jobPath = Path.of("job");
			System.out.println(jobPath.toAbsolutePath());	// プロジェクトフォルダ\jobとなる
/*
 *	●その他のメソッド
 *		親パスを取得するgetParentメソッド、パスの最後の要素を取得するgetFileNameメソッド、ルート要素を取得する
 *		getRootメソッドなどがある。	*/
			System.out.println("-- ファイル操作：getParent、getFileName、getRootメソッド --");
			Path tmpPath = Path.of("D:\\tmp", "org", "original.txt");
			System.out.println("パス->" + tmpPath);											// D:\tmp\org\original.txt
			Path parent = tmpPath.getParent();
			System.out.println("親パスの取得->" + parent);									// D:\tmp\org
			Path filename = tmpPath.getFileName();
			System.out.println("最後の要素->" + filename);									// original.txt
			Path root = tmpPath.getRoot();
			System.out.println("ルート要素->" + root);											// D:\
			System.out.println("相対パスのルート要素->" + filename.getRoot());	// null
/*
 *	［ファイルアクセス］
 *	ファイルアクセスに関する便利なメソッドが多くJava7で提供された。それが、Filesクラスである。
 *
 *	●ファイル全体をバイトとして読み込む
 *		staticのreadAllBytesメソッドは、パスで示されたファイルからすべてのバイトを読み取る(返却はバイト配列)。
 *		また、内部ではtry-with-resources構文で記述されているので、読み込み後、ファイルは自動クローズされる。
 *		※サイズの大きなファイルを想定していない。
 *
 *		通常、バイト配列より文字列として処理するために、readAllBytesメソッドとStringの組み合わせで使用される。
 *		Java8からは、その後の文字列処理する際に、ストリーム化するのが一般的である。	*/
			System.out.println("-- ファイルアクセス：readAllBytesメソッド --");
			try {
				String txt = new String(Files.readAllBytes(Path.of("D:\\tmp\\test.txt")), Charset.forName("MS932"));
				System.out.println(txt);
			} catch (IOException e) { }
/*		バイト配列から文字列化する場合には、第2引数にCharsetを指定してデコード処理をうながす。
 *		このテキストファイルはシフトJISエンコードなので、そのCharsetである「MS932」を指定する(指定がなければ、UTF-8)。
 *		※文字列を直接読み込む別解として、readStringメソッドがある(Java11)。
 *				String str = Files.readString(Path.of("D:\\tmp\\testAfter.txt"), Charset.forName("MS932"));
 *
 *	●ファイルの行を読み込む
 *		staticのreadAllLinesメソッドは、パスで示されたファイルからすべての行を読み取る(返却はStringのリスト)。
 *		第2引数には、エンコードを指定する(指定がなければ、UTF-8)。
 *		このメソッドも、読み込み後、ファイルは自動クローズされる。
 *		※サイズの大きなファイルを想定していない。	*/
			System.out.println("-- ファイルアクセス：readAllLinesメソッド --");
			try {
				List<String> lines = Files.readAllLines(Path.of("D:\\tmp\\test.txt"), Charset.forName("MS932"));
				System.out.println(lines.get(lines.size() - 1));				// 最後の行のみ表示
			} catch (IOException e) { }
/*
 *	●文字列をファイルに書き込む
 *		staticのwriteメソッドは、第1引数のパスで示されたファイルに、第2引数のバイトを書き込む。
 *		既存ファイルがなければ新規ファイルとして、あれば上書きファイルとして書き込まれる。
 *		第2引数のgetBytesメソッドは、エンコードを指定して、Stringをバイト配列化している。
 *		このメソッドも、書き込み後、ファイルは自動クローズされる。	*/
			System.out.println("-- ファイルアクセス：writeメソッド：バイト使用 --");
			try {
				String txt1 = new String(Files.readAllBytes(Path.of("D:\\tmp\\test.txt")), Charset.forName("MS932"));
				txt1 = txt1.replaceAll("2", "x");									// txt1中の文字2を文字xにすべて置換
				Files.write(Path.of("D:\\tmp\\test2.txt"), txt1.getBytes(Charset.forName("MS932")));
				System.out.println(new String(Files.readAllBytes(Path.of("D:\\tmp\\test2.txt")), Charset.forName("MS932")));
			} catch (IOException e) { }
/*		上記例は、バイトを書き込んだが、リストも書き込める。
 *		第3引数にエンコードを指定する。	*/
			System.out.println("-- ファイルアクセス：writeメソッド：リスト使用 --");
			try {
				List<String> lines = Files.readAllLines(Path.of("D:\\tmp\\test.txt"), Charset.forName("MS932"));
				Files.write(Path.of("D:\\tmp\\test3.txt"), lines, Charset.forName("MS932"));
				System.out.println(Files.readAllLines(Path.of("D:\\tmp\\test3.txt"), Charset.forName("MS932")).get(lines.size() - 1));
			} catch (IOException e) { }
/*		ここまでの2つの例は、上書きモードで書き込んできた。
 *		もし、追加モードで書き込みたいのであれば、バイト使用であれば第3引数に、リスト使用であれば第4引数に、
 *			StandardOpenOption.APPEND
 *		と記述し、追加モードを明示する(オプションの指定方法は、下記「●オプション」の項を参照)。
 *		※文字列を直接書き込む別解として、writeStringメソッドがある(Java11)。
 *			Files.writeString(Path.of("D:\\tmp\\testAfter.txt"), txt1, Charset.forName("MS932"));
 *
 *	●ストリーム・リーダー・ライターに関するメソッド
 *		Java6までは、ファイル操作をしたい場合には、次のようなコードを書いていた。
 *			BufferedReader in = new BufferedReader(new InputStreamReader(
 *																			new FileInputStream("D:\\tmp\\test.txt"), Charset.forName("MS932")));
 *		または
 *			BufferedReader in = new BufferedReader(new FileReader("D:\\tmp\\test.txt"));		// ただし、エンコード指定できない
 *		これが、staticのnewBufferedReaderメソッドを使用すると、次のようになる。
 *			BufferedReader in = Files.newBufferedReader(Path.of("D:\\tmp\\test.txt"), Charset.forName("MS932"));
 *		パスを引数にして、途中の経緯(コード変換：バイトストリーム⇄文字ストリーム)を省略できる。
 *		他にも、newInputStream/newOutputStream/newBufferedWriterメソッドなどがある。
 *		※FileReader/FileWriterメソッドで文字コード指定が可能になった(Java11)。
 *
 *	［空ファイルと空ディレクトリの作成］
 *	●空ディレクトリの作成
 *		staticのcreateDirectoryメソッドは、ディレクトリのパスを指定して、空ディレクトリを作成する。
 *		既に指定したディレクトリが存在していたり、親パスが存在していなかったりする場合は、例外をスローする。	*/
			System.out.println("-- ディレクトリの作成：createDirectoryメソッド --");
			try {
				Files.createDirectory(Path.of("D:\\tmp\\demo"));							// 1回のみ成功する
				System.out.println("成功しました！");
			} catch (IOException e) {System.out.println("既に存在している可能性がある");}
/*
 *	●複数の空ディレクトリの作成
 *		staticのcreateDirectoriesメソッドは、ディレクトリのパスを指定して、空ディレクトリを作成するが、createDirectory
 *		メソッドと異なる点は、createDirectoryメソッドは単一のディレクトリを作成するのに対し、createDirectoriesメソッドは、
 *		途中のディレクトリも同時に作成することである。かつ、既に指定したディレクトリが存在していても、例外をスローしない
 *		(ただし、指定したパスの末尾がディレクトリ以外(ファイルなど)の場合は例外がスローされる)。
 *		今、D:\tmp(親パス)があり、最終的にD:\tmp\sample\dataを作成したいとすると、createDirectoryメソッドは2回実行
 *		しなければならないが、createDirectoriesメソッドであれば、1回でよい。	*/
			System.out.println("-- ディレクトリの作成：createDirectoriesメソッド --");
			try {
				Files.createDirectories(Path.of("D:\\tmp\\sample\\data"));			// 何度でも成功する
				System.out.println("成功しました！");
			} catch (IOException e) {System.out.println("最後の要素がディレクトリではない可能性がある");}
/*
 *	●空ファイルの作成
 *		staticのcreateFileメソッドは、	ファイルのパスを指定して、空ファイルを作成する。
 *		既に指定したファイルが存在していたり、親パスが存在していなかったりする場合は、例外をスローする。	*/
			System.out.println("-- ファイルの作成：createFileメソッド --");
			try {
				Files.createFile(Path.of("D:\\tmp\\nodata.txt"));							// 1回のみ成功する
				System.out.println("成功しました！");
			} catch (IOException e) {System.out.println("既に存在している可能性がある");}
/*
 *	［ファイルの存在確認］
 *		staticのexistsメソッドは、ファイルのパスを指定して、存在確認する。	*/
			System.out.println("-- ファイルの存在確認：existメソッド --");
			if (Files.exists(Path.of("D:\\tmp\\test.txt"))) System.out.println("ファイルは存在している");
/*
 *	［一時ファイルと一時ディレクトリ］
 *		Files.existsメソッドで存在確認したとしても、メソッドから戻ってきた後には、そのファイルは削除されてしまっている
 *		かもしれない。固定ファイルである必要がなく、一時的にしか利用しないのであれば、テンポラリファイルを使用する。
 *		staticのcreateTempFileメソッドは、パスや文字列を指定して、一時ファイルを作成する。
 *		staticのcreateTempDirectoryメソッドは、パスや文字列を指定して、一時ディレクトリを作成する	*/
			System.out.println("-- 一時ファイルと一時ディレクトリ：createTempFile、createTempDirectoryメソッド --");
			try {
				Path tempFile1 = Files.createTempFile(Path.of("D:\\tmp"), "temp", ".txt");
				System.out.println("一時ファイル(dir, prefix, suffix)->" + tempFile1);	// 指定したディレクトリに作成
				Path tempFile2 = Files.createTempFile("temp", ".txt");						// C:\Users\xxx\AppData\Local\Temp直下に作成
				System.out.println("一時ファイル(prefix, suffix)->" + tempFile2);
				Path tempDir1 = Files.createTempDirectory(Path.of("D:\\tmp"), "temp");
				System.out.println("一時ディレクトリ(dir, suffix)->" + tempDir1);		// 指定したディレクトリに作成
				Path tempDir2 = Files.createTempDirectory("temp");
				System.out.println("一時ディレクトリ(suffix)->" + tempDir2);				// C:\Users\xxx\AppData\Local\Temp直下に作成
				Files.delete(tempFile1); Files.delete(tempFile2); Files.delete(tempDir1); Files.delete(tempDir2);
			} catch (IOException e) { }
/*		prefixやsuffixはnull指定でも可である。
 *		これら一時ファイルは、利用後は二度と使用しないはずである。そのため、Java8のFiles.listやFiles.walkメソッドで一覧を取得し、
 *		削除するのがよいであろう。単独であれば、後述するFiles.deleteやFiles.deleteIfExistsメソッドを利用して削除する。
 *
 *	［ファイルのコピー、移動、削除］
 *	●ファイルのコピー
 *		あるディレクトリから別のディレクトリへファイルをコピーするには、staticのcopyメソッドを使用する。
 *		第1引数から第2引数へコピーする。
 *		既にファイルが存在している場合には、例外をスローする。	*/
			System.out.println("-- ファイルのコピー：copyメソッド --");
			try {
				Files.copy(Path.of("D:\\tmp\\test.txt"), Path.of("D:\\tmp\\demo\\test.txt"));			// tmp->demoへtext.txtをコピー
				System.out.println("成功しました！");
			} catch (IOException e) {System.out.println("既に存在している可能性がある");}
/*
 *	●ファイルの移動
 *		ファイルを移動するには、staticのmoveメソッドを使用する。
 *		第1引数から第2引数へ移動する。
 *		既にファイルが存在している場合には、例外をスローする。	*/
			System.out.println("-- ファイルの移動：moveメソッド --");
			try {
				Files.move(Path.of("D:\\tmp\\demo\\test.txt"), Path.of("D:\\tmp\\move.txt"));		// demo->tmpへ移動(ファイル名変更して)
				System.out.println("成功しました！");
			} catch (IOException e) {System.out.println("既に存在している可能性がある");}
/*
 *	●オプション
 *		ファイルのコピー・移動は、コピー先・移動先が存在していると失敗する。失敗させずに、上書きしたいのであれば、
 *		オプションREPLACE_EXISTINGを指定する。また、ファイル属性のすべてをコピーしたいのであれば、オプション
 *		COPY_ATTRIBUTESを指定する。これらのオプションは、複数指定可能である。	*/
			System.out.println("-- ファイルのコピー・移動：オプション：REPLACE_EXISTING、COPY_ATTRIBUTES --");
			try {
				Files.copy(Path.of("D:\\tmp\\test.txt"), Path.of("D:\\tmp\\demo\\test.txt"),
						StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
				System.out.println("成功しました！");
			} catch (IOException e) { }
/*		移動の際に、移動が完全に成功したことを保証する、または、移動が失敗した場合には、移動元が必ず存在することを保証する
 *		には、オプションATOMIC_MOVEを指定する。	*/
			System.out.println("-- ファイルのコピー・移動：オプション：ATOMIC_MOVE --");
			try {
				Files.move(Path.of("D:\\tmp\\demo\\test.txt"), Path.of("D:\\tmp\\move.txt"),
						StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
				System.out.println("成功しました！");
			} catch (IOException e) { }
/*	●ファイルの削除
 *		ファイルを削除するには、staticのdeleteメソッドを使用する。
 *		ファイルが存在していない、または、パスが空でないディレクトリを指定すると、例外をスローする。	*/
			System.out.println("-- ファイルの削除：deleteメソッド --");
			try {
				Files.delete(Path.of("D:\\tmp\\move.txt"));
				System.out.println("成功しました！");
			} catch (IOException e) {System.out.println("存在していない可能性がある");}
/*		存在していない場合には、例外をスローさせずに削除しない場合には、staticのdeleteIfExistsメソッドを使用する。	*/
			System.out.println("-- ファイルの削除：deleteIfExistsメソッド --");
			try {
				Files.deleteIfExists(Path.of("D:\\tmp\\move.txt"));			// move.txtは存在していないが、例外はスローされない
				System.out.println("成功しました！");
			} catch (IOException e) { }
/*		自分が作成して削除する分には、deleteIfExistsメソッドは不要であるように思われるが(自分が知っているので)、誰が削除する
 *		かはわからない。よって、削除の際には、このメソッドは有用である。
 *		また、空ディレクトリを削除するメソッドはないため、Java8のFiles.listやFiles.walkメソッドで一覧を取得し、削除していくなどの
 *		工夫が必要である。
 *
 *	［nullの安全な同値検査］
 *	今、Stringのaとbがあるとし、これらの比較を行うことを想定する。
 *	従来で確実な同値検査をするのであれば、まず、aとbのnull検査をしてから、aとbの同値検査を行う。つまり、	*/
		String a, b; a = new String("ABC"); b = new String("XYZ");
		if (((a == null) && (b == null)) || ((a != null) && a.equals(b))) {/*同値処理*/}
/*	このコードは、冗長であり、null検査がなされていない場合も考えられる。
 *	Java7は、nullの安全な同値検査として、staticのObjects.equalsメソッドを提供した。
 *	上記の例は、次と等価である。	*/
		if (Objects.equals(a, b)) {/*同値処理*/}
/*	すなわち、aとbが両方ともnullであればtrueを返し、aがnullであればfalseを返し、aがnullでなければa.equals(b)の結果を返す。
 *	コードが簡略化し、可読性も向上する。
 *
 *	［ハッシュコードの計算］
 *	ハッシュコードとは、データを一意に識別する固定長の数値である。
 *	今、あるクラスがあり、そのクラスのハッシュコードを実装する場合を考える。
 *	最も簡単に実装するならば、クラスが所有しているフィールド値を利用して、それらのハッシュコードの和を返すことである。
 *	※staticのObjects.hashCodeメソッドは、引数を1つ取り、引数がnullの場合は0を返す。	*/
		class Sample {
			private String a; private String b;
			/*・・・ */
			public int hashCode() {
				return Objects.hashCode(a) + Objects.hashCode(b);
				//					return Objects.hash(a, b);														// 上記と同等
			}
		}
/*	多くのフィールドを所有している場合には、このコードは冗長である。これを解決するために、1つしか引数を指定できない
 *	Objects.hashCodeの代わりにObjects.hashメソッドを提供した。このメソッドは、可変個の引数を指定できるため、コードが
 *	簡素になる。
 *
 *	［数値型の比較］
 *	Java6までは、コンパレータで整数比較(成績点数やお金を比較するなど)を行う場合に、2つの整数の差を返して比較していた。
 *	しかし、x - yを返そうとして、xがint値の限界値に近い数値で、yが負であると、この差は、オーバーフローを発生させる危険性を
 *	はらんでいる。
 *	この問題を解決するために、staticのInteger.compareメソッドが提供された(Integer.compare(x, y))。
 *	他のラッパークラスである、Long、Short、Byte、Booleanにもcompareメソッドが提供されている。
 *	※FloatとDoubleのラッパーには、従来からcompareクラスは存在していた。
 *
 *	［引数のnull確認］
 *	メソッドやコンストラクタの引数は、通常、nullを期待していない。期待していないということは、チェックなしで使用しているのが
 *	現状である。
 *	これは、かなり危険なことであり、null値が他へ派生し、予期しない結果を招くことになるであろう。
 *	では、毎回、nullチェックのif文を入れるかとなると冗長であり、敬遠されることに違いない。そこで、複数文にならずに、通常の
 *	代入のように使用でき、nullチェックを行い、もし、nullであれば例外をスローするメソッドObjects.requireNonNullが提供された。	*/
		System.out.println("-- 引数のnull確認：requireNonNullメソッド --");
		class Jump {
			private int hop; private String step;
			public Jump(int hop, String step) {
				try {
					this.hop = Objects.requireNonNull(hop);																// nullであれば単に例外に飛ばす
					this.step = Objects.requireNonNull(step, "第2引数stepはnullであってはならない");	// nulllであればメッセージ付きで飛ばす
				} catch (NullPointerException e) {System.out.println(e.getMessage());}
			}
		//	・・・
		}
		Jump jump = new Jump(12, null);
/*	引数を1つのみ指定した場合は、単に例外に飛ばす。これは、他にも多くの引数があり、まとめて例外時に処理したいときに使用する。
 *	引数を2つ指定した場合には、第2引数のメッセージを例外時に利用できる。これは、ピンポイントで例外処理をしたいときに役立つ。
 *	Java9で、第1引数がnullの場合はnull以外の第2引数を返す、requireNonNullElseメソッドが追加された。
 *
 *	［BitSet］
 *	BitSetは、ビット列として実装された整数の集合である。ビットは0から開始される(BitSet自身は従来から実装されている)。
 *	Java7で、このBitSetを生成するためのメソッドvalueOf、その逆のtoByteArray、toLongArrayメソッドが追加された。	*/
		System.out.println("-- BitSet：valueOf、toByteArrayメソッド --");
		byte[] bits = {(byte)3};									// 3-> 00000011
		BitSet data = BitSet.valueOf(bits);					// byte配列をBitSet化
		System.out.println(data);									// ビットが立っている0オリジン表示：0と1が表示される
		byte[] result = data.toByteArray();					// BitSetをbyte配列化(valueOfの逆の効果)
		for (byte c : result) {System.out.print(c);}
	}
/* 参考文献：Java SE8実践プログラミング */
}