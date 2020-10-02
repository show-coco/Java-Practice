package jp.project3.testsumlist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***************************************************************************************************
 * 　TestSumListプログラム：メインクラス
 *
 * 　プログラム名：	TestSumList
 * 　概要：			各生徒を合計点の降順(合計が同じなら名前の昇順)に並び替え、順位、名前、点数、点数、点数
 * 					の順番で出力する。
 * 　作成日付：		2020/9/30
 * 　版数：			1.1版
 * 　作成者(班:PL)：秦　和也(3:秦　和也)
 * 　修正履歴：		・E,Iのメッセージを名前付き定数に追加
 * 　備考：			なし
 * 　課題No：		3
 **************************************************************************************************/
public class TestSumList {
	/*名前付き定数*/
	public static final 	int 			NORMAL			= 0;											//正常終了
	public static final 	int 			ABNORMAL		= -1;											//異常終了
	public static final	int				NEGATIVE		= -1;											//欠席を0点にする
	public static final 	int 			ZERO			= 0;											//欠席科目の点数を置き換えるための変数
	public static final	int				ICHI			= 1;											//数値の1
	public static final	int				NI				= 2;											//数値の2
	public static final	int				SAN				= 3;											//数値の3
	public static final	String			E001			= "入出力エラーが発生しました。";				//エラーメッセージ
	public static final	String			I001			= "[試験成績順位]";								//インフォメーションメッセージ1
	public static final	String			I002			= "[再試験者一覧]";								//インフォメーションメッセージ2
	public static final	String			I003			= "該当者なし";									//インフォメーションメッセージ3
	public static final 	String			REGEX1			= "[\\r\\n]";									//個人データごとに区切るための正規表現
	public static final 	String			REGEX2			= "[,]";										//個人データを種類ごとに区切るための正規表現
	public static final 	String			REGEX3			= ".+(,-1|,0|,[1-9][0-9]|,100){3}";				//点数が正常か判定する
	public static final 	String			INFILE			= "E:/java&システム開発/課題/課題3/testsum.txt";//ファイルのパス
	public static final 	String			CHARCODE 		= "MS932";										//文字コード
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
	public static			List<String>	ReTest			= new ArrayList<>();
	public static			List<String>	Name			= new ArrayList<>();
	public static			List<String>	National		= new ArrayList<>();
	public static			List<String>	Math			= new ArrayList<>();
	public static			List<String>	English			= new ArrayList<>();

	/***************************************************************************************************
	 * 　 メインメソッド：main(String[] args)
	 *
	 * 　メソッド名：	main
	 * 　概要：			各生徒を合計点の降順(合計が同じなら名前の昇順)に並び替え、順位、名前、点数、点数、点数
	 * 					の順で出力する。
	 * 　引数：			String[] args
	 * 　返却値			なし
	 * 　備考：			・データの区切り方が変わった場合はREGEXを変更する
	 * 					・ソート条件を変更、追加する場合はsortedRankingを変更する
	 **************************************************************************************************/
	public static void main(String[] args) {
		String 					File 		= null;
		List<String>			List1 		= new ArrayList<>();
		List<String>			List2		= new ArrayList<>();
		List<Student>			ranking		= new ArrayList<>();
		try {
			File = Files.readString(Path.of(INFILE),Charset.forName(CHARCODE));
		}catch(IOException e) {
			System.out.println(E001);
			System.exit(ABNORMAL);																			//異常終了
		}
		List1 = Stream.of(File.split(REGEX1)).filter(s -> s.matches(REGEX3)).collect(Collectors.toList());//098などの不正データを除外しListに格納
		for(int i=ZERO ; i < List1.size(); i++) {
			List2 = 	 Stream.of(List1.get(i)	.split(REGEX2)).filter(s -> !s.isEmpty())
												.collect(Collectors.toList());								//名前、点1、点2、点3で分割しListに格納
			if(List2.size() > 4) {continue;}
			try {
				judge(List2.get(ZERO), List2.get(ICHI), List2.get(NI), List2.get(SAN));
			}catch(IndexOutOfBoundsException e) {}catch(NumberFormatException e) {}						//不正データは何もしない
		}
		National 	= findMax(National);
		Math		= findMax(Math);
		English		= findMax(English);
		for(int i=ZERO;Name.size()>i;i++) {
			/*成績を持った生徒を生成*/
			Student st = new Student(Name.get(i),National.get(i),Math.get(i),English.get(i));
			ranking.add(st);
		}
		List<Student> sortedRanking = 	ranking.stream().sorted(Comparator.comparing(Student::getScore).reversed()
											   .thenComparing(Student::getName))
											   .collect(Collectors.toList());								//合計点の降順、名前の昇順でソート
		String 	size 			= String.valueOf(sortedRanking.size());
		int		length  		= size.length();
		int 	cnt 			= ICHI;
		int 	rank			= ICHI;
		int		forwardScore 	= ZERO;
		int		maxScore		= ZERO;
		if(sortedRanking != null) {
			System.out.println(I001);
			for(Student st : sortedRanking) {
				String 	Format;
				if(st.getReTest() == false) {ReTest.add(st.getName());}									//再試験者の名前Listに名前を格納
				if(cnt == ICHI) {st.getScore();}															//最高合計点を算出
				if(st.getScore() != forwardScore) {rank = cnt;}												//合計点が上の人と違うときのみ順位を更新する
				if(st.getName().length() < NameLength) {
					int difference 	= NameLength - st.getName().length();
					int Len				= (difference*NI) + st.getName().length();							//文字数の差×全角1文字(半角2文字分)を計算し書式に適応
					Format 	= 	F001+length+F002+F006+F001+F004+Len+F003+F006
							+F001+Length.get(ZERO)+F003+F006+F001+Length.get(ICHI)+F003+F006
							+F001+Length.get(NI)+F003+F001+F005;											//表示用の書式作成
				}else {
					Format 	= 	F001+length+F002+F006+F001+F004+NameLength+F003+F006
							+F001+Length.get(ZERO)+F003+F006+F001+Length.get(ICHI)+F003+F006
							+F001+Length.get(NI)+F003+F001+F005;											//表示用の書式作成
				}
				if(maxScore == st.getScore()) {System.out.printf(Format,rank,st.getName(),st.getNational(),st.getMath(),st.getEnglish());
				}else {System.out.printf(Format,rank,st.getName(),st.getNational(),st.getMath(),st.getEnglish());}
				forwardScore = st.getScore();
				cnt++;
			}
		}
		System.out.println();																				//試験成績順位から1行分改行
		System.out.println(I002);
		if(!ReTest.isEmpty()) {																				//再試験者がいるか？
			ReTest.forEach(list -> System.out.println(list));												//再試験者の名前表示
		}else {System.out.println(I003);}
	}

	/***************************************************************************************************
	 * 　 メインメソッド：judge(String name,String a,String b,String c)
	 *
	 * 　メソッド名：	judge
	 * 　概要：			各生徒を合計点の降順(合計が同じなら名前の昇順)に並び替え、順位、名前、点数、点数、点数
	 * 					の順で出力する。
	 * 　引数：			String name,String a,String b,String c
	 * 　返却値			なし
	 * 　備考：			・再試験を表す文字列が変更する場合replaceの第一引数を変更する
	 **************************************************************************************************/
/*再試験の-1を0点に変更し各科目点数をListに格納*/
	public static void judge(String name,String a,String b,String c) {
		int 	ten1	= Integer.parseInt(a);																//再試験判定用
		int 	ten2	= Integer.parseInt(b);																//再試験判定用
		int 	ten3	= Integer.parseInt(c);																//再試験判定用
		if(ten1==NEGATIVE||ten2==NEGATIVE||ten3==NEGATIVE) {
			a = a.replace("-1","0");																		//欠席の-1を0点に置換する
			b = b.replace("-1","0");																		//欠席の-1を0点に置換する
			c = c.replace("-1","0");																		//欠席の-1を0点に置換する
		}
		Name.add(Normalizer.normalize(name, Normalizer.Form.NFKC));
		National.add(Normalizer.normalize(a, Normalizer.Form.NFKC));
		Math.add(Normalizer.normalize(b, Normalizer.Form.NFKC));
		English.add(Normalizer.normalize(c, Normalizer.Form.NFKC));
		if(name.length() > NameLength) {NameLength = name.length();}										//名前の最大文字数を格納(最高点者に*をつけるため+1する)
	}

	/***************************************************************************************************
	 * 　 メインメソッド：findMax(List<String> data)
	 *
	 * 　メソッド名：	findMax
	 * 　概要：			各生徒を合計点の降順(合計が同じなら名前の昇順)に並び替え、順位、名前、点数、点数、点数
	 * 					の順で出力する。
	 * 　引数：			List<String> data
	 * 　返却値			なし
	 * 　備考：			・最高点者とそれ以外を表す文字列を変更する場合ASTAとSPACEを変更する
	 **************************************************************************************************/
/*各科目の最高点者に*を付ける*/
	public static List<String> findMax(List<String> data) {
		OptionalInt	num		= data.stream().mapToInt(s -> Integer.parseInt(s)).max();						//科目ごとの最大点を算出
		String		strMax	= String.valueOf(num.orElse(ZERO));											//Optionalから最大値を取り出しString化
		Length.add(strMax.length()+ICHI);																	//各科目の最大点の長さを格納(+1は"*"または" "の長さ)
		for(int i=ZERO ; i < data.size(); i++) {
			if(data.get(i).equals(strMax)) {data.set(i , ASTA + data.get(i));								//最大点に"*"を付加
			}else {data.set(i , SPACE + data.get(i));}														//最大点以外に" "を付加
		}
		return data;
	}
}
