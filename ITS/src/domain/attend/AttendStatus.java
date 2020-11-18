package domain.attend;

import domain.employee.EmpId;

public class AttendStatus {
	private EmpId empId;
	private String date;
	private int typeId;
	private String time;

	public AttendStatus(EmpId empId, String date, int typeId, String time) {
		this.empId = empId;
		this.date = date;
		this.typeId = typeId;
		this.time = time;
	}
	
	public EmpId getEmpId() {
		return empId;
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
		return "AttendanceStatus [emp=" + empId.getValue() + ", date=" + date + ", typeId=" + typeId + ", time=" + time + "]";
	}
}
