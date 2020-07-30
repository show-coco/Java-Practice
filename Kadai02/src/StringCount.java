import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCount {
	public static final int ABNORMAL = -1;
	public static final int FRONT = 0;
	public static final int BACK = 1;
	public static final String WORD = "広島";
	public static final String FORMAT = "[" + WORD + "]";
	public static final String INFILE = "/Users/sakaishow/workspace/test.txt";
	public static final String E001 = "入出力エラーが発生しました。ファイルが存在しない可能性があります\n";
	public static final String E002 = "ファイルに文字が存在しません";
	public static final String I001 = "プログラムを終了します";
	public static final String REGEX001 = ".{0,5}広島.{0,5}";
	public static final String REGEX002 = ".*[、。]";
	public static final String REGEX003 = "[、。].*";
	public static final String[] SPACES = { " ", "　", "\n" };
	public static final String STRING_INIT = "";
	
	
	public static void main(String[] args) {
		String input = STRING_INIT;
		try {
			input = Files.readString(Path.of(INFILE));
		} catch (IOException e) {
			System.out.printf(E001 + I001, INFILE);
			System.exit(ABNORMAL);
		}
		
		input = removeSpace(input);
		
		if(input.length() == 0) {
			System.out.println(E002 + I001);
			System.exit(ABNORMAL);
		}
		
		List<String[]> matches = getFrontAndBack(input);
		matches.forEach((matche) -> {
			System.out.println(matche[FRONT] + FORMAT + matche[BACK]);
		});
	}
	
	static String removeSpace(String input) {
		String nonSpaceInput = input;
		
		for(String space : SPACES) {
			nonSpaceInput = nonSpaceInput.replaceAll(space, STRING_INIT);
		}
		
		return nonSpaceInput;
	}
	
	static List<String[]> getFrontAndBack(String input) {
		List<String[]> matches = new ArrayList<String[]>();
		Pattern p1 = Pattern.compile(REGEX001);
		
		Matcher m1 = p1.matcher(input);
		while(m1.find()) {
			String[] matche = m1.group().split(WORD);
			matches.add(matche);
		}
		
		for(String[] matche : matches) {
			matche[FRONT] = matche[FRONT].replaceAll(REGEX002, STRING_INIT);
			matche[BACK] = matche[BACK].replaceAll(REGEX003, STRING_INIT);
		}
		
		return matches;
	}
}
