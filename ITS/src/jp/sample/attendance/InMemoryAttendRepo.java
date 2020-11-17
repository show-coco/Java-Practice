package jp.sample.attendance;

import java.util.ArrayList;
import java.util.stream.Collectors;

import jp.sample.employee.Employee;

public class InMemoryAttendRepo implements IAttendRepo {
	private ArrayList<AttendanceStatus> AttendStatuses = new ArrayList<>();

	@Override
	public void save(AttendanceStatus as) {
		AttendStatuses.add(as);
	}

	@Override
	public ArrayList<AttendanceStatus> get(Employee emp) {
		return (ArrayList<AttendanceStatus>) AttendStatuses.stream().filter(as -> as.getEmp().getId().equals(emp.getId())).collect(Collectors.toList());
	}

}
