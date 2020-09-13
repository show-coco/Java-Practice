import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringCount {
	public static final int 	NORMAL 		= 0;										// システム正常終了用の数値
	public static final int		ABNORMAL 	= -1;										// システム異常終了用数値
	public static final int		LENGTH 		= 5;										// システム異常終了用数値
	public static final String 	INFILE 		= "/Users/sakaishow/workspace/test.txt";	// 使用するファイルのパスを文字列で宣言
	public static final String 	SEARCH_WORD = "広島";									// 検索する文字
	public static final String 	SEARCH_REGEX= "(?<TYPE1>[^、。]{0," + LENGTH + "})(?<TYPE2>" + SEARCH_WORD + ")(?<TYPE3>[^、。]{0," + LENGTH + "})";	// SEARCH_WORDの前後の文字を含んだ文を検索するための正規表現
	public static final String 	CHAR_CODE 	= "UTF-8";									// 使用するファイルの文字コード
	public static final String 	REMOVE_REGEX= "[\\s　]";									// ファイルから削除する文字の正規表現
	public static final String 	E001 		= "入出力エラーが発生しました。" + INFILE + "にファイルが存在しない可能性があります。\n";	// 入出力エラーメッセージ
	public static final String 	I001 		= "プログラムを終了します";						// プログラムを終了メッセージ
	public static final String 	I002 		= SEARCH_WORD + "がファイル" + INFILE + "に存在しません";	// ファイルにSEARCH_WORDが存在しない時のメッセージ
	public static final String 	FORMAT 		= "[" + SEARCH_WORD + "]";
	public static final String 	TYPE1 		= "TYPE1";
	public static final String 	TYPE2 		= "TYPE2";
	public static final String 	TYPE3 		= "TYPE3";
	
	public static void main(String[] args) {
		String all = "";
		
		long startTime = System.currentTimeMillis();
		
		try {
			all = Files.readString(Path.of(INFILE), Charset.forName(CHAR_CODE));
		} catch (IOException e) {
			System.out.println(E001 + I001);
			System.exit(ABNORMAL);
		}
		
		String formattedAll = Stream.of(all.split(REMOVE_REGEX)).collect(Collectors.joining());
		System.out.println(formattedAll);
		List<String> results = new ArrayList<String>();
		
		Stream.of(for)
		
		if(formattedAll.contains(SEARCH_WORD)) {
			Matcher m1 = Pattern.compile(SEARCH_REGEX).matcher(formattedAll);
			int nextStart = 0;
			while(m1.find(nextStart)) {
				String sentence = m1.group(TYPE1) + FORMAT + m1.group(TYPE3);
				results.add(sentence);
				nextStart = m1.start(TYPE3);
			}
			results.forEach(s -> System.out.println(s));
		} else {
			System.out.println(I002);
			System.exit(NORMAL);
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime + "ms");
	}
}
