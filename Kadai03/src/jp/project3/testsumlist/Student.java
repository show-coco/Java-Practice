package jp.project3.testsumlist;

public class Student {
	private String 	name;		// 名前
	private int 	japanese;	// 国語の点数
	private int 	math;		// 数学の点数
	private int 	english;	// 英語の点数
	private int		sum;		// 合計点数
	private boolean	isRetest;	// 再試験か
	
	public Student(String name, int japanese, int math, int english) {
		this.name = name;
		this.japanese = japanese;
		this.math = math;
		this.english = english;
		this.sum = calcSum(japanese, math, english);
		this.isRetest = judjeIsRetest(japanese, math, english);
	}
	
	public String getName() {
		return name;
	}
	public int getJapanese() {
		return japanese;
	}
	public int getMath() {
		return math;
	}
	public int getEnglish() {
		return english;
	}
	public int getSum() {
		return sum;
	}
	public boolean isRetest() {
		return isRetest;
	}
	
	int calcSum(int... scores) {
		// 版数1.3
		int sum = TestSumList.ZERO;
		for (int score : scores) {
			sum += score == TestSumList.NEGATIVE ? TestSumList.ZERO : score;
		}
		return sum; 
	}
	
	boolean judjeIsRetest(int... scores) {
		// 版数1.3
		int count = TestSumList.ZERO;
		for (int score : scores) {
			count += score <= TestSumList.RED_SCORE ? TestSumList.ONE : TestSumList.ZERO;
		}
		return count > TestSumList.ZERO; 
	}
}
