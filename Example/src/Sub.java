public class Sub {

	public static final 	String FORMAT = ",";
	private 				String Name;
	private 				String English;
	private 				String National;
	private 				String Math;
	private					String Score;

	public String[] PersonData(String name , String national , String math , String english) {
		String[] person = new String[4];
		person[0] = name;
		person[1] = national;
		person[2] = math;
		person[3] = english;
		return person;
	}

	public void setPerson(String name , String national , String math , String english){
		this.Name 		= name;
		this.English	= english;
		this.National	= national;
		this.Math		= math;
	}

	public void setScore(String score) {
		this.Score = score;
	}

	public Integer	getScore() {
		int score = Integer.parseInt(Score);
		return score;
	}

	public String	getName() {return Name;}

	public String	getEnglish() {return English;}

	public String	getNational() {return National;}

	public String	getMath() {return Math;}

	public String 	toString() {return Name + "　" + National + "　" + Math + "　" + English;}
}