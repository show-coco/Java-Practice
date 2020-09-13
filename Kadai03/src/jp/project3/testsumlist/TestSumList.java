package jp.project3.testsumlist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestSumList {
	public static final int ABNORMAL = -1;
	public static final int NORMAL = 1;
	public static final int NAME_INDEX = 0;
	public static final int JAPANESE_INDEX = 1;
	public static final int MATH_INDEX = 1;
	public static final int ENGLISH_INDEX = 1;
	public static final String TITLE1 = "[順位]";
	public static final String TITLE2 = "[再試験者]";
	public static final String PATH = "/Users/sakaishow/workspace/test.txt";
	public static final String CHAR_CODE = "UTF-8";
	public static final String REMOVE_REGEX = "[\\s　]";
	
	public static void main(String[] args) { 
		List<String[]> lines = new ArrayList<String[]>();
		List<Student> ranking = new ArrayList<Student>();
		List<Student> retesters = new ArrayList<Student>();
		
		try {
			// ファイルを読み込み「,」で区切り文字列配列のリストを生成
			lines = Files.lines(Path.of(PATH), Charset.forName(CHAR_CODE))
					.map(s -> s.replaceAll(REMOVE_REGEX, ""))
					.map(s -> s.split(","))
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.exit(ABNORMAL);
		}
		
		// 順位リストと、再試験者リストに生徒を格納
		for (String line[] : lines) {
			try {
				// 成績情報を持った生徒を生成
				Student st = new Student(
						line[NAME_INDEX], 
						Integer.parseInt(line[JAPANESE_INDEX]), 
						Integer.parseInt(line[MATH_INDEX]), 
						Integer.parseInt(line[ENGLISH_INDEX])
						);
				if (st.isRetest()) { 
					retesters.add(st); // 再試験者リストへ格納
				} else {
					ranking.add(st);  // 順位リストへ格納		
				}
			} catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
				continue; // 不正なデータがあったらスキップ
			}
		}
		
		List<Student> sortedRanking = ranking.stream()
									.sorted(Comparator.comparing(Student::getSum).reversed())
									.collect(Collectors.toList());

		// それぞれの最大得点の桁取得
		Optional<Integer> nameMax = sortedRanking.stream().map(st -> st.getName().length()).max(Comparator.comparingInt(a -> a));
		int japaneseMax = sortedRanking.stream().max(Comparator.comparing(Student::getJapanese)).get().getJapanese();
		int mathMax= sortedRanking.stream().max(Comparator.comparing(Student::getMath)).get().getMath();
		int englishMax = sortedRanking.stream().max(Comparator.comparing(Student::getEnglish)).get().getEnglish();
		
//		japaneseMax.ifPresent(System.out::println);
//		System.out.println(mathMax);
//		System.out.println(englishMax);
//		System.out.println(nameMaxDegit);
		
		System.out.println(TITLE1);
		for(Student st : sortedRanking) {
			System.out.print(st.getName() + ", ");
			System.out.print(st.getSum() + ", ");
			System.out.println(st.isRetest());
		}
		
		System.out.println(TITLE2);
		for(Student st : retesters) {
			System.out.print(st.getName() + ", ");
			System.out.print(st.getSum() + ", ");
			System.out.println(st.isRetest());
		}
		
	}
}
