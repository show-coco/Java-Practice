package domain.models.attend;

import java.util.ArrayList;

import domain.models.employee.EmpId;

public interface IAttendRepo {
	void save(AttendStatus as);
	ArrayList<AttendStatus> get(EmpId empId);
	AttendStatus get(EmpId empId, AttendDate date, int type); 
}
