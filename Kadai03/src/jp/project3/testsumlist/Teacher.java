package jp.project3.testsumlist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Teacher {
	StudentList ranking;
	StudentList retesters;
	
	public void createStudentList(String filePath) {    
		List<String[]> lines = new ArrayList<String[]>();
		ranking = new StudentList();
		retesters = new StudentList();

		// ファイルを読み込み「,」で区切り文字列配列のリストを生成
		try(Stream<String[]> str = Files.lines(Path.of(filePath), Charset.forName(TestSumList.CHAR_CODE))
				.filter(s -> s.matches("[^,]*(,-1|,0|,[1-9][0-9]|,100){3}"))
				.map(s -> s.split(",")); ) {
			lines = str.collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println(e);
			System.exit(TestSumList.ABNORMAL);
		}

		// 順位リストと、再試験者リストに生徒を格納
		for (String line[] : lines) {
			// 成績情報を持った生徒を生成
			if(!TestSumList.validation(line)) continue; // ゼロから始まる数字がある行はスキップ
			Student st = new Student(line[TestSumList.NAME_INDEX],
					Integer.parseInt(line[TestSumList.JAPANESE_INDEX]),
					Integer.parseInt(line[TestSumList.MATH_INDEX]),
					Integer.parseInt(line[TestSumList.ENGLISH_INDEX])
					);
			if (st.isRetest()) { 	// 再試験がtrueだったら
				retesters.add(st); 	// 再試験者リストへ格納
			}
			ranking.add(st);  // 順位リストへ格納
		}
		
		ranking.sort();
	}
	
	public void outRanking() {		
		int nameMax = ranking.getNameMax();
		int japaneseMax = ranking.getJapaneseMax();
		int mathMax = ranking.getMathMax();
		int englishMax = ranking.getEnglishMax();
		String Format = TestSumList.F001 + String.valueOf(ranking.size()).length() + TestSumList.F002 + TestSumList.SPACE +
				TestSumList.F003 + nameMax + TestSumList.F004 +
				TestSumList.F006 + TestSumList.F001 + String.valueOf(japaneseMax).length() + TestSumList.F002 +
				TestSumList.F006 + TestSumList.F001 + String.valueOf(mathMax).length()+ TestSumList.F002 +
				TestSumList.F006 + TestSumList.F001 + String.valueOf(englishMax).length() + TestSumList.F002 + TestSumList.F005;
		
		if(ranking.size() != 0) {
			System.out.println(TestSumList.TITLE1);
			String japaneseAsta =TestSumList. SPACE;
			String mathAsta = TestSumList.SPACE;
			String englishAsta = TestSumList.SPACE;
			int preSum = 0;
			int rank = 0;
			int preRank = 0;
			
			Iterator<Student> itr = ranking.iterator();
			while(itr.hasNext()) {
				Student st = itr.next();
				if(st.getJapanese() == japaneseMax) japaneseAsta = TestSumList.ASTA;
				if(st.getMath() == mathMax) 		mathAsta = TestSumList.ASTA;
				if(st.getEnglish() == englishMax) 	englishAsta = TestSumList.ASTA;
				rank++;
				if(preSum != st.getSum()) {
					preRank = rank;
					System.out.printf(Format, rank, st.getName(), japaneseAsta, st.getJapanese(), mathAsta, st.getMath(),englishAsta, st.getEnglish());
				} else {
					System.out.printf(Format, preRank, st.getName(), japaneseAsta, st.getJapanese(), mathAsta, st.getMath(),englishAsta, st.getEnglish());
				}
				preSum = st.getSum();
				japaneseAsta = TestSumList.SPACE;
				mathAsta = TestSumList.SPACE;
				englishAsta = TestSumList.SPACE;
			}
		}
	}
	
	public void outRetertes() {
		System.out.println("");
		System.out.println(TestSumList.TITLE2);
		StringBuilder msg = new StringBuilder();
		retesters.stream()
		  .map(Student::getName)
		  .forEach(name -> {
		    msg.append(name);
		    msg.append("\n");
		  });

		String msgStr = msg.length() == 0 ? "該当者なし" : msg.toString();

		System.out.println(msgStr);
	}
}
