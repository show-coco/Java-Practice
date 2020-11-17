package domain.attend;

import domain.employee.Employee;

public class AttendStatus {
	private Employee emp;
	private String date;
	private int typeId;
	private String time;

	public AttendStatus(Employee emp, String date, int typeId, String time) {
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
	
	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "AttendanceStatus [emp=" + emp.getId().getValue() + ", date=" + date + ", typeId=" + typeId + ", time=" + time + "]";
	}
}
