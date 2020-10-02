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
	public static final int ABNORMAL = -1;
	public static final int NORMAL = 1;
	public static final int NAME_INDEX = 0;
	public static final int JAPANESE_INDEX = 1;
	public static final int MATH_INDEX = 2;
	public static final int ENGLISH_INDEX = 3;
	public static final String ASTA = "*";
	public static final String SPACE = " ";	
	public static final String TITLE1 = "【試験成績順位】";
	public static final String TITLE2 = "【再試験者】";
	public static final String PATH = "/Users/sakaishow/workspace/testsum2.txt";
	public static final String CHAR_CODE = "MS932";
	public static final String REMOVE_REGEX = "[\\s　]";
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
					.filter(s -> s.matches("[^,]*(,-1|,0|,[1-9][0-9]|,100){3}"))
					.map(s -> s.split(","))
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.exit(ABNORMAL);
		}

		// 順位リストと、再試験者リストに生徒を格納
		for (String line[] : lines) {
			// 成績情報を持った生徒を生成
			if(!validation(line)) continue; // ゼロから始まる数字がある行はスキップ
			Student st = new Student(line[NAME_INDEX],
					Integer.parseInt(line[JAPANESE_INDEX]),
					Integer.parseInt(line[MATH_INDEX]),
					Integer.parseInt(line[ENGLISH_INDEX])
					);
			if (st.isRetest()) { 	// 再試験がtrueだったら
				retesters.add(st); 	// 再試験者リストへ格納
			}
			ranking.add(st);  // 順位リストへ格納
		}

		// 合計点でソートする。合計点が一緒だったら名前でソート
		List<Student> sortedRanking = ranking.stream()
									.sorted(Comparator.comparing(Student::getSum).reversed()
											.thenComparing(Student::getName))
									.collect(Collectors.toList());

		// 名前の最大桁取得
		int nameMax = sortedRanking.stream().map(Student::getName).max(Comparator.comparing(String::length)).get().length();
		// 各教科の最大得点取得
		int japaneseMax = sortedRanking.stream().max(Comparator.comparing(Student::getJapanese)).get().getJapanese();
		int mathMax= sortedRanking.stream().max(Comparator.comparing(Student::getMath)).get().getMath();
		int englishMax = sortedRanking.stream().max(Comparator.comparing(Student::getEnglish)).get().getEnglish();

		// 試験成績順位出力のためのFORMATを定義
		String Format = F001 + String.valueOf(sortedRanking.size()).length() + F002 + SPACE +
						F003 + nameMax + F004 +
						F006 + F001 + String.valueOf(japaneseMax).length() + F002 +
						F006 + F001 + String.valueOf(mathMax).length()+ F002 +
						F006 + F001 + String.valueOf(englishMax).length() + F002 + F005;

		// 試験成績順位を表示
		if(sortedRanking.size() != 0) {
			System.out.println(TITLE1);
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

		System.out.println("");
		System.out.println(TITLE2);
		// 再試験者を表示
		if(retesters.size() != 0) {
			for(Student st : retesters) {
				System.out.println(st.getName());
			}
		} else {
			System.out.println("該当者なし");
		}
	}

	static boolean validation(String line[]) {
		for(String w : line) {
			if(w.matches("^0.+")) return false;
		}
		return true;
	}
}
