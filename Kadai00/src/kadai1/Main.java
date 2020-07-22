package kadai1;

public class Main {

	public static void main(String[] args) throws Exception {
		String fromPath = "/Users/sakaishow/workspace/test.txt";
		String toPath = "/Users/sakaishow/workspace/edit.txt";
		String toPath2 = "/Users/sakaishow/workspace/edit2.txt";
		
		FileTextReplaceRobot robot1 = new FileTextReplaceRobot(fromPath, toPath, "12", "xy");
		robot1.run();
		FileTextReplaceRobot robot2 = new FileTextReplaceRobot(fromPath, toPath2, "12", "zp");
		robot2.run();
	}
}
