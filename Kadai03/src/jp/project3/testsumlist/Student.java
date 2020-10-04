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
	
	int calcSum(int japanese, int math, int english) {
		int sum = 0;
		if(japanese == -1) {
			sum += 0;
		} else {
			sum += japanese;
		}
		
		if(math == -1) {
			sum += 0;
		} else {
			sum += math;
		}
		
		if(english == -1) {
			sum += 0;
		} else {
			sum += english;
		}
		
		return sum; 
	}
	
	boolean judjeIsRetest(int japanese, int math, int english) {
		if(japanese <= 25 || math <= 25 || english <= 25) return true;
		return false;
	}
}
