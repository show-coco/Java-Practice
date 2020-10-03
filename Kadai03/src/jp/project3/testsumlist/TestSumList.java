package jp.project3.testsumlist;

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
	public static final String PATH = "/Users/sakaishow/workspace/testsum.txt";
	public static final String CHAR_CODE = "MS932";
	public static final String REMOVE_REGEX = "[\\s　]";
	public static final String F001 = "%";
	public static final String F002 = "d";
	public static final String F003 = "%-";
	public static final String F004 = "s";
	public static final String F005 = "\n";
	public static final String F006 = "　%s";

	public static void main(String[] args) {
		Teacher teacher = new Teacher();
		teacher.createStudentList(PATH);
		teacher.outRanking();
		teacher.outRetertes();
	}

	static boolean validation(String line[]) {
		for(String w : line) {
			if(w.matches("^0.+")) return false;
		}
		return true;
	}
}
