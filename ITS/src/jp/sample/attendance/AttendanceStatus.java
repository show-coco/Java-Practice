package jp.sample.attendance;

import java.util.Date;

import jp.sample.employee.Employee;

public class AttendanceStatus {
	private Employee emp;
	private String date;
	private int typeId;
	private String time;

	public AttendanceStatus(Employee emp, String date, int typeId, String time) {
		this.emp = emp;
		this.date = date;
		this.typeId = typeId;
		this.time = time;
	}	
}
