package domain.model.attendance;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class InMemoryAttendRepo implements IAttendRepo {
	private ArrayList<Attendance> attendList = new ArrayList<>();

	@Override
	public Attendance save(Attendance attend) {
		attendList.add(attend);

		return attend;
	}

	@Override
	public ArrayList<Attendance> findForMonth(int empId, int year, int month) {
		return (ArrayList<Attendance>) attendList.stream().filter(attend -> {
			return attend.getDate().getYear() == year && attend.getDate().getMonthValue() == month;
		}).collect(Collectors.toList());
	}

}
