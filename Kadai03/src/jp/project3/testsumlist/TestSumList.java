/***************************************************************************************************
 * 　TestSumListプログラム：メインクラス
 *
 * 　プログラム名：	TestSumList
 * 　概要：			各生徒を合計点の降順(合計が同じなら名前の昇順)に並び替え、順位、名前、点数、点数、点数
 * 					の順番で出力する。
 * 　作成日付：		2020/9/30
 * 　版数：			1.2版
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
	public static final int NORMAL = 1;
	public static final int ABNORMAL = -1;
	public static final int NAME_INDEX = 0;
	public static final int JAPANESE_INDEX = 1;
	public static final int MATH_INDEX = 2;
	public static final int ENGLISH_INDEX = 3;
	public static final String E001 = "入出力エラーが発生しました";
	public static final String I001 = "[試験成績順位]";
	public static final String I002 = "[再試験者]";
	public static final String I003 = "該当者なし";
	public static final String PATH = "/Users/sakaishow/workspace/testsum.txt";
	public static final String CHAR_CODE = "MS932";
	public static final String ASTA = "*";
	public static final String SPACE = " ";	
	public static final String REMOVE_REGEX = "[\\s　]";
	public static final String SPLIT_REGEX = ",";
	public static final String VALID_REGEX = "[^,]*(,-1|,0|,[1-9][0-9]|,100){3}";
	public static final String F001 = "%";
	public static final String F002 = "d";
	public static final String F003 = "%-";
	public static final String F004 = "s";
	public static final String F005 = "\n";
	public static final String F006 = "　%s";
	
	static List<Student> ranking = new ArrayList<Student>();
	static List<Student> retesters = new ArrayList<Student>();

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
			for(String w : line) {
				if(w.matches("^0.+")) continue;   // ゼロから始まる点数がある行はスキップ
			}
			Student st = new Student(line[NAME_INDEX], // 成績情報を持った生徒を生成
					Integer.parseInt(line[JAPANESE_INDEX]),
					Integer.parseInt(line[MATH_INDEX]),
					Integer.parseInt(line[ENGLISH_INDEX])
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
			String japaneseAsta = SPACE;
			String mathAsta = SPACE;
			String englishAsta = SPACE;
			int preSum = 0;
			int rank = 0;
			int preRank = 0;
			for(Student st : sortedRanking) {
				if(st.getJapanese() == japaneseMax) japaneseAsta = ASTA;
				if(st.getMath() == mathMax) 		mathAsta = ASTA;
				if(st.getEnglish() == englishMax) 	englishAsta = ASTA;
				rank++;
				if(preSum != st.getSum()) {
					preRank = rank;
					System.out.printf(Format, rank, st.getName(), japaneseAsta, st.getJapanese(), mathAsta, st.getMath(),englishAsta, st.getEnglish());
				} else {
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
