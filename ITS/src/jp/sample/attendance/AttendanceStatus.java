package jp.sample.attendance;

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
	
	public Employee getEmp() {
		return emp;
	}

	public String getDate() {
		return date;
	}

	public int getTypeId() {
		return typeId;
	}

	public String getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return "AttendanceStatus [emp=" + emp.getId().getValue() + ", date=" + date + ", typeId=" + typeId + ", time=" + time + "]";
	}
}
