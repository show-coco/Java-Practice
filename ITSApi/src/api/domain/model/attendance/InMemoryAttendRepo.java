package api.domain.model.attendance;

import java.time.LocalDate;
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
		int preMonth = LocalDate.of(year, month, 21).minusMonths(1).getMonthValue();
		int currentMonth = month;
		
		return (ArrayList<Attendance>) attendList.stream().filter(attend -> {
			return attend.getDate().getYear() == year && 
					(attend.getDate().getMonthValue() == preMonth && attend.getDate().getDayOfMonth() >= 21) || 
					(attend.getDate().getMonthValue() == currentMonth && attend.getDate().getDayOfMonth() < 21);
		}).collect(Collectors.toList());
	}

}
