package attendance;

import java.util.ArrayList;
import java.util.stream.Collectors;

import employee.Employee;

public class InMemoryAttendRepo implements IAttendRepo {
	private ArrayList<AttendStatus> AttendStatuses = new ArrayList<>();

	@Override
	public void save(AttendStatus as) {
		AttendStatuses.add(as);
	}

	@Override
	public ArrayList<AttendStatus> get(Employee emp) {
		return (ArrayList<AttendStatus>) AttendStatuses.stream().filter(as -> as.getEmp().getId().equals(emp.getId())).collect(Collectors.toList());
	}

	@Override
	public AttendStatus get(Employee emp, String date, int type) {
		return AttendStatuses.stream()
				.filter(as -> as.getEmp().getId().equals(emp.getId()) && as.getDate().equals(date) && as.getTypeId() == type)
				.findFirst()
				.orElse(null);
	}
	
}
