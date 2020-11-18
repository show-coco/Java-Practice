package repository.attend;

import java.util.ArrayList;

import domain.attend.AttendStatus;
import domain.employee.EmpId;

public interface IAttendRepo {
	void save(AttendStatus as);
	ArrayList<AttendStatus> get(EmpId empId); 
	AttendStatus get(EmpId empId, String time, int type); 
}
