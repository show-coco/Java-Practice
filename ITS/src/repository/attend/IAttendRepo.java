package repository.attend;

import java.util.ArrayList;

import domain.attend.AttendStatus;
import domain.employee.Employee;

public interface IAttendRepo {
	void save(AttendStatus as);
	ArrayList<AttendStatus> get(Employee emp); 
	AttendStatus get(Employee emp, String time, int type); 
}
