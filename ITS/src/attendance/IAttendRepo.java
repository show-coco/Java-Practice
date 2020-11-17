package attendance;

import java.util.ArrayList;

import employee.Employee;

public interface IAttendRepo {
	void save(AttendStatus as);
	ArrayList<AttendStatus> get(Employee emp); 
	AttendStatus get(Employee emp, String time, int type); 
}
