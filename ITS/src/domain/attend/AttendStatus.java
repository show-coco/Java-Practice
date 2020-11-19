package domain.attend;

import domain.employee.EmpId;

public class AttendStatus {
	private EmpId empId;
	private AttendDate date;
	private AttendTime time;
	private int typeId;

	public AttendStatus(EmpId empId, AttendDate date, AttendTime time, int typeId) {
		this.empId = empId;
		this.date = date;
		this.time = time;
		this.typeId = typeId;
	}
	
	public EmpId getEmpId() {
		return empId;
	}

	public AttendDate getDate() {
		return date;
	}

	public int getTypeId() {
		return typeId;
	}

	public AttendTime getTime() {
		return time;
	} 
	
	public void setDate(AttendDate date) {
		this.date = date;
	}

	public void setTime(AttendTime time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "AttendanceStatus [emp=" + empId.getValue() + ", date=" + date.toString() + ", typeId=" + typeId + "]";
	}
}
