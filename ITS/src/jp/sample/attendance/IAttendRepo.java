package jp.sample.attendance;

import java.util.ArrayList;

import jp.sample.employee.Employee;

public interface IAttendRepo {
	void save(AttendanceStatus as);
	ArrayList<AttendanceStatus> get(Employee emp); 
}
