package jp.project3.testsumlist;

public class Student {
	String 	name;
	int 	japanese;
	int 	math;
	int 	english;
	int		sum;
	int 	rank;
	boolean	isRetest;
	
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
	
	public void setRank(int rank) {
		this.rank = rank;
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
