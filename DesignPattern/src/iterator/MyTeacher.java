package iterator;

public class MyTeacher extends Teacher {
	StudentList studentList;
	
	public void createStudentList() {
		studentList = new StudentList(5);
		studentList.add(new Student("赤井亮太", 1));
		studentList.add(new Student("赤羽里美", 0));
		studentList.add(new Student("岡田美央", 0));
		studentList.add(new Student("西森俊介", 1));
		studentList.add(new Student("中ノ森玲菜", 0));
	}
	
	public void callStudents() {
		int last = studentList.getLastNum();
		for(int i=0; i<last; i++) {
			System.out.println(studentList.getStudentAt(i).getName());
		}
	}
}
