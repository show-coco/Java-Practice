package iterator;

public class MyTeacher extends Teacher {
	MyStudentList studentList;
	
	public void createStudentList() {
		studentList = new MyStudentList(5);
		studentList.add(new Student("赤井亮太", 1));
		studentList.add(new Student("赤羽里美", 0));
		studentList.add(new Student("岡田美央", 0));
		studentList.add(new Student("西森俊介", 1));
		studentList.add(new Student("中ノ森玲菜", 0));
	}
	
	public void callStudents() {
		Iterator itr = studentList.iterator();
		while(itr.hasNext()) {
			System.out.println(((Student)itr.next()).getName());
		}
	}
}
