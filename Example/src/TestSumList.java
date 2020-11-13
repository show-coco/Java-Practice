package jp.project3.testsumlist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/***************************************************************************************************
 * 　TestSumListプログラム：メインクラス
 *
 * 　プログラム名：	TestSumList
 * 　概要：			試験の成績順位と再試験車を表示
 * 　作成日付：		2020/10/08
 * 　版数：			1.0版
 * 　作成者(班:PL)：中道翔太(1:秦和也)
 * 　修正履歴：		なし	※履歴ありの場合は「1.1版	名前付き定数漏れ」等を記述し、ソース上に修正版数をコメント化
 * 　備考：			なし
 * 　課題No：		1
 **************************************************************************************************/

public class TestSumList {

	public static final String CHARSET ="MS932";
	public static final String INFILE = "F:/Testdate/課題３/testsum.txt";
	public static final int ABNORMAL = -1;
	public static final String E001 = "IOエラーが発生しました";
	public static final String SPLITER = ",";
	public static final String FORMAT_1 = "%";
	public static final String FORMAT_2 = "d";
	public static final String FORMAT_3 = "s";
	public static final String FORMAT_4 = "-";
	public static final String FORMAT_5 = "n";
	public static final String FORMAT_6 = "　";
	public static final String TITLE_1 = "【試験成績順位】";
	public static final String TITLE_2 = "【再試験者】";
	public static final String TITLE_3 = "該当者なし";
	public static final String MAX_MARK = "*";
	public static final String SPACE = " ";
	public static final int ZERO = 0;

	public static void main(String[] args) {
		List<Student> students = new ArrayList<Student>();
		List<Student> retest = new ArrayList<Student> ();
		List<String[]> personresults = null;
		/*ファイルからデータを抽出*/
		try {
			personresults = Files.lines(Path.of(INFILE) , Charset.forName(CHARSET))
					.filter(s -> s.matches("[^,]*(,-1|,0|,[1-9][0-9]|,100){3}"))
					.map(s ->s.split(SPLITER))
					.collect(Collectors.toList());
		}catch(IOException e){
			System.out.println(E001);
			System.exit(ABNORMAL);

		}
		for(String[] str :personresults) {
			Student s = new Student(str[0],Integer.parseInt(str[1]),Integer.parseInt(str[2]),Integer.parseInt(str[3]));
			if(s.isRetest()) {
				retest.add(s);
			}
			students.add(s);

		}
//		for(Student st:students) {System.out.println(st.getName());}

		/*名前の最大長*/
		int namelen = students.stream().max((s1, s2) -> s1.getName().length() - s2.getName().length()).get().getName().length();
//		System.out.println(namelen);

		/*点数の最大値*/
		int sum_max = students.stream().max((s1 , s2) -> s1.getSum() - s2.getSum()).get().getSum();
		int japanese_max = students.stream().max((s1, s2) -> s1.getjapanese()- s2.getjapanese()).get().getjapanese();
		int math_max = students.stream().max((s1, s2) -> s1.getMath()- s2.getMath()).get().getMath();
		int english_max = students.stream().max((s1, s2) -> s1.getEnglish()- s2.getEnglish()).get().getEnglish();
//		System.out.println(japanese_max);
//		System.out.println(math_max);
//		System.out.println(english_max);

		/*合計点の降順、名前の昇順でソート*/
		List<Student> sorted = new ArrayList<Student>();
		sorted = students.stream()
				.sorted(Comparator.comparing(Student::getSum)
				.reversed()
				.thenComparing(Student::getName))
				.collect(Collectors.toList());
//		for(Student s : sorted) {System.out.println(s.getName() + "　" + s.getSum() );}

		/*フォーマットして出力*/
		System.out.println(TITLE_1);
		for(Student s : sorted) {

			/* 合計点が最高の人の場合 */
			if(s.getSum() == sum_max) {
				System.out.printf( MAX_MARK + FORMAT_1 + FORMAT_4 + namelen + FORMAT_3
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2 + FORMAT_1 + FORMAT_5
						,s.getName(),s.getjapanese(),s.getMath(),s.getEnglish());

			/* 国語が最高点だった人の場合 */
			}else if (s.getjapanese() == japanese_max) {
				System.out.printf( FORMAT_1 + FORMAT_4 + namelen + FORMAT_3
						+ FORMAT_6 + MAX_MARK + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2 + FORMAT_1 + FORMAT_5
						,s.getName(),s.getjapanese(),s.getMath(),s.getEnglish());

			/* 数学が最高点だった人の場合 */
			}else if(s.getMath() == math_max) {
				System.out.printf( FORMAT_1 + FORMAT_4 + namelen + FORMAT_3
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + MAX_MARK + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2 + FORMAT_1 + FORMAT_5
						,s.getName(),s.getjapanese(),s.getMath(),s.getEnglish());

			/* 英語が最高点だった人の場合 */
			}else if(s.getEnglish() == english_max) {
				System.out.printf( FORMAT_1 + FORMAT_4 + namelen + FORMAT_3
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + MAX_MARK + FORMAT_1 + FORMAT_2 + FORMAT_1 + FORMAT_5
						,s.getName(),s.getjapanese(),s.getMath(),s.getEnglish());
			/* 最高点を取ってない人 */
			}else {
				System.out.printf( FORMAT_1 + FORMAT_4 + namelen + FORMAT_3
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2
						+ FORMAT_6 + SPACE + FORMAT_1 + FORMAT_2 + FORMAT_1 + FORMAT_5
						,s.getName(),s.getjapanese(),s.getMath(),s.getEnglish());
			}
		}
		
		/* 再試験者の表示 */
		System.out.println(TITLE_2);
		if(retest.isEmpty()) {
			System.out.println(TITLE_3);
		}else {
			retest.forEach(s -> System.out.println(s.getName()));
		}
		
	}
}
