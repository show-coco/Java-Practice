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
	public static final String PATH = "/Users/sakaishow/workspace/test.txt";
	public static final String CHAR_CODE = "UTF-8";
	
	public static void main(String[] args) { 
		List<String[]> lines = new ArrayList<String[]>();
		List<Student> ranking = new ArrayList<Student>();
		List<Student> retesters = new ArrayList<Student>();
		
		try {
			// ファイルを読み込み「,」で区切り文字列配列のリストを生成
			lines = Files.lines(Path.of(PATH), Charset.forName(CHAR_CODE))
					.map(s -> s.replaceAll("[\\s　]", ""))
					.map(s -> s.split(","))
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.exit(-1);
		}
		
		// 順位リストと、再試験者リストに生徒を格納
		for (String line[] : lines) {
			try {
				// 成績情報を持った生徒を生成
				Student st = new Student(line[0], Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]));
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
		Optional<Integer> japaneseMax = sortedRanking.stream().map(st -> st.getName().length()).max(Comparator.comparingInt(a -> a));
		int mathMax= sortedRanking.stream().max(Comparator.comparing(Student::getMath)).get().getMath();
		int englishMax = sortedRanking.stream().max(Comparator.comparing(Student::getEnglish)).get().getEnglish();
		
		japaneseMax.ifPresent(System.out::println);
		System.out.println(mathMax);
		System.out.println(englishMax);
//		System.out.println(nameMaxDegit);
		
		System.out.println("[順位]");
		for(Student st : sortedRanking) {
			System.out.print(st.getName() + ", ");
			System.out.print(st.getSum() + ", ");
			System.out.println(st.isRetest());
		}
		
		System.out.println("[再試験者]");
		for(Student st : retesters) {
			System.out.print(st.getName() + ", ");
			System.out.print(st.getSum() + ", ");
			System.out.println(st.isRetest());
		}
		
	}
}
