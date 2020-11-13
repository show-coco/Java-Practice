/***************************************************************************************************
 * 　TestSumListプログラム：メインクラス
 *
 * 　プログラム名：	TestSumList
 * 　概要：			各生徒を合計点の降順(合計が同じなら名前の昇順)に並び替え、順位、名前、点数、点数、点数
 * 					の順番で出力する。
 * 　作成日付：		2020/9/30
 * 　版数：			1.3版
 * 　作成者(班:PL)：坂井 晶(3:秦　和也)
 * 　修正履歴：		1.1版 最大値取得の効率向上
 * 　備考：			なし
 * 　修正履歴： 		1.2版 再試験者表示の効率向上
 * 　備考：			StringBuilderを使用
 * 　課題No：			3
 **************************************************************************************************/
package jp.project3.testsumlist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestSumList {
	public static final int NORMAL = 1;		// 正常終了
	public static final int ABNORMAL = -1;	// 異常終了
	public static final int NEGATIVE = -1;	// マイナス1
	public static final int ZERO = 	0;	// ゼロ
	public static final int ONE = 	1;	// マイナス1
	public static final int TWO = 	2;	// 名前が格納されるINDEX
	public static final int THREE = 3; // 国語の点数が格納されるINDEX
	public static final int RED_SCORE = 25;	// この点数以下が欠点になる
	public static final String E001 = "入出力エラーが発生しました";	// IOExceptionのエラメッセージ
	public static final String I001 = "[試験成績順位]";		// 試験成績順位者を表示する時のタイトル
	public static final String I002 = "[再試験者]";			// 再試験者を表示する時のタイトル
	public static final String I003 = "該当者なし";			// 再試験者がいなかった場合に出力する文字列
	public static final String PATH = "/Users/sakaishow/workspace/testsum2.txt";	// ファイルのパス
	public static final String CHAR_CODE = "MS932";	// ファイルの文字コード
	public static final String ASTA = "*";	// 最高点につける文字
	public static final String SPACE = " ";		// 最高点ではなかった場合につける文字
	public static final String SPLIT_REGEX = ",";	// CSV形式のファイルを区切る文字
	public static final String VALID_REGEX = "[^,]*(,-1|,0|,[1-9][0-9]|,100){3}";	// ファイルの列が正しい入力値の場合にマッチする正規表現のための文字列
	public static final String F001 = "%";	// 表示用書式1
	public static final String F002 = "d";	// 表示用書式2
	public static final String F003 = "%-";	// 表示用書式3
	public static final String F004 = "s";	// 表示用書式4
	public static final String F005 = "\n";	// 表示用書式5
	public static final String F006 = "　%s";	// 表示用書式6
	
	static List<Student> ranking = new ArrayList<Student>(); 	// 試験成績順位に表示する生徒を格納するリスト
	static List<Student> retesters = new ArrayList<Student>();	// 再試験者を格納するリスト

	public static void main(String[] args) {
		List<String[]> lines = new ArrayList<String[]>();

		try {
			// ファイルを読み込み「,」で区切り文字列配列のリストを生成
			lines = Files.lines(Path.of(PATH), Charset.forName(CHAR_CODE))
					.filter(s -> s.matches(VALID_REGEX))
					.map(s -> s.split(SPLIT_REGEX))
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println(E001);
			System.exit(ABNORMAL);
		}

		// 順位リストと、再試験者リストに生徒を格納
		for (String line[] : lines) {
			Student st = new Student(line[ZERO], // 成績情報を持った生徒を生成
					Integer.parseInt(line[ONE]),
					Integer.parseInt(line[TWO]),
					Integer.parseInt(line[THREE])
					);
			if (st.isRetest()) { 	// 再試験がtrueだったら
				retesters.add(st); 	// 再試験者リストへ格納
			}
			ranking.add(st);  // 順位リストへ格納(再試験者も)
		}

		// 合計点でソートする。合計点が一緒だったら名前でソート
		List<Student> sortedRanking = ranking.stream()
									.sorted(Comparator.comparing(Student::getSum).reversed()
											.thenComparing(Student::getName))
									.collect(Collectors.toList());

		int nameMax = sortedRanking.stream().mapToInt(s -> s.getName().length()).max().getAsInt();	// 名前の最大桁取得(1.1版)
		int japaneseMax = sortedRanking.stream().mapToInt(Student::getJapanese).max().getAsInt();  	// 国語の最大得点取得(1.1版)
		int mathMax= sortedRanking.stream().mapToInt(Student::getMath).max().getAsInt();			// 数学の最大得点取得(1.1版)
		int englishMax = sortedRanking.stream().mapToInt(Student::getEnglish).max().getAsInt();		// 英語の最大得点取得(1.1版)

		// 試験成績順位出力のためのFORMATを定義
		String Format = F001 + String.valueOf(sortedRanking.size()).length() + F002 + SPACE +
						F003 + nameMax + F004 +
						F006 + F001 + String.valueOf(japaneseMax).length() + F002 +
						F006 + F001 + String.valueOf(mathMax).length()+ F002 +
						F006 + F001 + String.valueOf(englishMax).length() + F002 + F005;

		// 試験成績順位を表示
		if(sortedRanking.size() != 0) {
			System.out.println(I001);
			String japaneseAsta = SPACE, mathAsta = SPACE, englishAsta = SPACE;
			int preSum = ZERO, rank = ZERO, preRank = ZERO;
			
			for(Student st : sortedRanking) {
				// 生徒の各点数が最大点か判定
				if(st.getJapanese() == japaneseMax) japaneseAsta = ASTA;
				if(st.getMath() == mathMax) 		mathAsta = ASTA;
				if(st.getEnglish() == englishMax) 	englishAsta = ASTA;
				rank++;
				if(preSum != st.getSum()) { // 前に表示した生徒と点数が違う場合
					preRank = rank;
					System.out.printf(Format, rank, st.getName(), japaneseAsta, st.getJapanese(), mathAsta, st.getMath(),englishAsta, st.getEnglish());
				} else { // 前に表示した生徒と点数が同じ場合
					System.out.printf(Format, preRank, st.getName(), japaneseAsta, st.getJapanese(), mathAsta, st.getMath(),englishAsta, st.getEnglish());
				}
				preSum = st.getSum();
				japaneseAsta = SPACE;
				mathAsta = SPACE;
				englishAsta = SPACE;
			}
		}

		System.out.println(); // 改行
		System.out.println(I002); // [再試験者]と出力
		// 再試験者を表示(1.2版)
		StringBuilder msg = new StringBuilder();
		retesters.stream()
		  .map(Student::getName)
		  .forEach(name -> {
		    msg.append(name);
		    msg.append("\n");
		  });
		String msgStr = msg.length() == 0 ? I003 : msg.toString();
		System.out.println(msgStr);
	}
}
