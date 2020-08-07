import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCount {
	public static final int NORMAL = 0;
	public static final int ABNORMAL = -1;	
	public static final int FRONT = 0;	
	public static final int BACK = 1;
	public static final String INFILE = "/Users/sakaishow/workspace/test.txt";
	public static final String SEARCH_WORD = "広島";
	public static final String CHAR_CODE = "MS932";
	public static final String REMOVE_REGEX = "[\\p{C} 　]";
	public static final String WORD_REGEX = "(.{0,5}" + SEARCH_WORD + ").{0,5}";
	public static final String W001 = "入出力エラーが発生しました。%sにファイルが存在しない可能性があります。\n";
	public static final String I001 = "プログラムを終了します";
	public static final String NANMONAI = "";
	public static final String FORMAT= "[" + SEARCH_WORD + "]";	
	
	public static void main(String[] args) {
		String all = NANMONAI;
		try {
			all = Files.readString(Path.of(INFILE));
		} catch (IOException e) {
			System.out.printf(W001 + I001, INFILE);
			System.exit(ABNORMAL);
		}
		all = all.replaceAll(REMOVE_REGEX, NANMONAI);
		List<String> results = new ArrayList<String>();
		
		// Streamを使うパターン
//		Integer count = 0;
//		Pattern.compile(WORD_REGEX)
//			.matcher(all)
//			.results()
//			.forEach((MatchResult result) -> {
//				results.add(result.group());
//			});
		
		// Whileを使うパターン
		int end = 0;
		Pattern p1 = Pattern.compile(WORD_REGEX);
		Matcher m1 = p1.matcher(all);
		while(m1.find(end)) {
			results.add(m1.group());
			end = m1.end(1);
		}
		
		results.forEach(result -> {
			System.out.println(result);
		});
	}
}
