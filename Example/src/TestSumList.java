package jp.project3.testsumlist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestSumList {
	/*名前付き定数*/
	public static final 	int 			NORMAL			= 0;											//正常終了
	public static final 	int 			ABNORMAL		= -1;											//異常終了
	public static final 	int 			ZERO			= 0;											//欠席科目の点数を置き換えるための変数
	public static final	int				ICHI			= 1;											//数値の1
	public static final	int				NI				= 2;											//数値の2
	public static final	int				SAN				= 3;											//数値の3
	public static final	int				RE1				= -1;											//再試験判定用
	public static final	int				RE2				= 25;											//再試験判定用
	public static final 	String			REGEX1			= "[\\r\\n]";									//個人データごとに区切るための正規表現
	public static final 	String			REGEX2			= "[,]";										//個人データを種類ごとに区切るための正規表現
	public static final 	String			REGEX3			= ".+(,-1|,0|,[1-9][0-9]|,100){3}";				//点数が正常か判定する
	public static final 	String			INFILE			= "F:/java&システム開発/課題/課題3/data.txt";	//ファイルのパス
	public static final 	String			CHARCODE 		= "UTF-8";										//文字コード
	public static final	String			ASTA			= "*";											//最高点につけるアスタリスク
	public static final	String			SPACE			= " ";											//最高点以外につける半角空白
	public static final 	String			F001			= "%";											//表示用書式1
	public static final 	String			F002			= "d";											//表示用書式2
	public static final 	String			F003			= "s";											//表示用書式3
	public static final	String			F004			= "-";											//表示用書式4
	public static final	String			F005			= "n";											//表示用書式5
	public static final	String			F006			= " ";											//表示用書式6
	/*クラス変数*/
	public static			int 			NameLength		= ZERO;
	public static 			List<Integer>	Length			= new ArrayList<>();
	public static			List<Integer>	Score			= new ArrayList<>();
	public static			List<String>	ReTest			= new ArrayList<>();
	public static			List<String>	Name			= new ArrayList<>();
	public static			List<String>	National		= new ArrayList<>();
	public static			List<String>	Math			= new ArrayList<>();
	public static			List<String>	English			= new ArrayList<>();

	public static void main(String[] args) {
		Long 					startTime	= System.currentTimeMillis();
		String 					File 		= null;
		List<String>			List1 		= new ArrayList<>();
		List<String>			List2		= new ArrayList<>();
		List<Student>			ranking		= new ArrayList<>();
		try {
			File = Files.readString(Path.of(INFILE),Charset.forName(CHARCODE));
		}catch(IOException e) {
			System.out.println("入出力エラーが発生しました。");
			System.exit(ABNORMAL);
		}
		List1 = Stream.of(File.split(REGEX1)).filter(s -> s.matches(REGEX3)).collect(Collectors.toList());//098などの不正データを除外しListに格納
		for(int i=ZERO ; i < List1.size(); i++) {
			List2 = 	 Stream.of(List1.get(i)	.split(REGEX2)).filter(s -> !s.isEmpty())
												.collect(Collectors.toList());								//名前、点1、点2、点3で分割しListに格納
			try {
				Score.add(sumScore(List2.get(ZERO), List2.get(ICHI), List2.get(NI), List2.get(SAN)));
			}catch(IndexOutOfBoundsException e) {}															//不正データは何もしない
		}
		National 			= findMax(National);
		Math				= findMax(Math);
		English				= findMax(English);
		for(int i=ZERO;Name.size()>i;i++) {
			/*成績を持った生徒を生成*/
			Student st = new Student(Name.get(i),National.get(i),Math.get(i),English.get(i),Score.get(i));
			ranking.add(st);
		}
		List<Student> sortedRanking = 	ranking.stream().sorted(Comparator.comparing(Student::getScore).reversed()
											   .thenComparing(Student::getName))
											   .collect(Collectors.toList());								//合計点の降順、名前の昇順でソート
		Integer maxScore	= Score.stream().mapToInt(v -> v).max().orElse(ZERO);							//最高合計点を算出
		String 	size 		=	String.valueOf(sortedRanking.size());
		int		length  	= 	size.length();
		int rank 			= ICHI;
		int	forwardScore 	= ZERO;
		if(sortedRanking != null) {
			System.out.println("[試験成績順位]");
			for(Student st : sortedRanking) {
				String 	Format;
				if(st.getScore() == forwardScore) {rank--;}
				if(st.getName().length() < NameLength) {
					int difference 	= NameLength - st.getName().length();
					int Len				= (difference*NI) + st.getName().length();							//文字数の差×全角1文字(半角2文字分)を計算し書式に適応
					Format 	= 	F001+length+F002+F006+F001+F004+Len+F003+F006
								+F001+Length.get(ZERO)+F003+F006+F001+Length.get(ICHI)+F003+F006
								+F001+Length.get(NI)+F003+F001+F005;										//表示用の書式作成
				}else {
					Format 	= 	F001+length+F002+F006+F001+F004+NameLength+F003+F006
								+F001+Length.get(ZERO)+F003+F006+F001+Length.get(ICHI)+F003+F006
								+F001+Length.get(NI)+F003+F001+F005;										//表示用の書式作成
				}
				if(maxScore == st.getScore()) {System.out.printf(Format,rank,ASTA+st.getName(),st.getNational(),st.getMath(),st.getEnglish());
				}else {System.out.printf(Format,rank,SPACE+st.getName(),st.getNational(),st.getMath(),st.getEnglish());}
				forwardScore = st.getScore();
				rank++;
			}
		}
		System.out.println();																				//試験成績順位から1行分改行
		if(!ReTest.isEmpty()) {																				//再試験者がいるか？
			System.out.println("[再試験者一覧]");
			ReTest.forEach(list -> System.out.println(list));												//再試験者の名前表示
		}else {System.out.println("該当者なし");}
		Long endTime = System.currentTimeMillis();
		System.out.println("処理時間:" + (endTime - startTime));
	}

/*再試験かどうか判定し、テスト点の合計値を計算して返却*/
	public static Integer sumScore(String name , String a , String b , String c) {
		int 	ten1	= Integer.parseInt(a);																//再試験判定用
		int 	ten2	= Integer.parseInt(b);																//再試験判定用
		int 	ten3	= Integer.parseInt(c);																//再試験判定用
		if(ten1==RE1||ten2==RE1||ten3==RE1||ten1<=RE2||ten2<=RE2||ten3<=RE2) {ReTest.add(name);}
		int sum = Stream.of(a,b,c).mapToInt(s -> Integer.parseInt(s)).sum();								//3科目の合計を算出
		Name.add(name);
		National.add(a);
		Math.add(b);
		English.add(c);
		if(name.length() > NameLength) {NameLength = name.length()+ICHI;}									//名前の最大文字数を格納(最高点者に*をつけるため+1する)
		return sum;
	}

/*各科目の最高点者に*を付ける*/
	public static List<String> findMax(List<String> data) {
		OptionalInt	num			= data.stream().mapToInt(s -> Integer.parseInt(s)).max();					//科目ごとの最大点を算出
		String		strMax		= String.valueOf(num.orElse(ZERO));										//Optionalから最大値を取り出しString化
		Length.add(strMax.length()+ICHI);																	//各科目の最大点の長さを格納(+1は"*"または" "の長さ)
		for(int i=ZERO ; i < data.size(); i++) {
			if(data.get(i).equals(strMax)) {data.set(i , ASTA + data.get(i));								//最大点に"*"を付加
			}else {data.set(i , SPACE + data.get(i));}														//最大点以外に" "を付加
		}
		return data;
	}
}
