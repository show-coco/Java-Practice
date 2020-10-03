package jp.project3.testsumlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentList {
	private List<Student> studentList = new ArrayList<Student>();
	
	public void add(Student st) {
		studentList.add(st);
	}
	
	public void sort() {
		studentList = studentList.stream()
				.sorted(Comparator.comparing(Student::getSum).reversed()
						.thenComparing(Student::getName))
				.collect(Collectors.toList());
	}
	
	public Iterator<Student> iterator() {
		return studentList.iterator();
	}
	
	public  Stream<Student> stream() {
		return studentList.stream();
	}
	
	public int size() {
		return studentList.size();
	}
	
	public int getJapaneseMax() {
		return studentList.stream().mapToInt(Student::getJapanese).max().getAsInt();
	}
	
	public int getMathMax() { 
		return studentList.stream().mapToInt(Student::getMath).max().getAsInt();
	}
	
	public int getEnglishMax() { 
		return studentList.stream().mapToInt(Student::getEnglish).max().getAsInt();
	}
	
	public int getNameMax() {
		return studentList.stream().mapToInt(st -> st.getName().length()).max().getAsInt();
	}
}
