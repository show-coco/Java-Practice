package jp.project3.testsumlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentList {
	List<Student> studentList = new ArrayList<Student>();
	
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
	
	public int size() {
		return studentList.size();
	}
	
	public int getJapaneseMax() {
		return studentList.stream().max(Comparator.comparing(Student::getJapanese)).get().getJapanese();
	}
	
	public int getMathMax() { 
		return studentList.stream().max(Comparator.comparing(Student::getMath)).get().getMath();
	}
	
	public int getEnglishMax() { 
		return studentList.stream().max(Comparator.comparing(Student::getEnglish)).get().getEnglish();
	}
	
	public int getNameMax() {
		return studentList.stream().map(Student::getName).max(Comparator.comparing(String::length)).get().length();
	}
}
