package domain.dayOff;

import java.util.Calendar;

import domain.employee.EmpId;

public class DayOff {
	private EmpId empId;
	private DayOffDate date;
	private int id;

	public DayOff(EmpId empId, DayOffDate date, int id) {
		this.empId = empId;
		this.date = date;
		this.id = id;
	}
	
	/**
	 * 週末であればfalseを返却。
	 * @param calendar
	 * @return
	 */
	public static boolean isWeekend(Calendar calendar) {
		return 	calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
				calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

	@Override
	public String toString() {
		return "DayOff [empId=" + empId.getValue() + ", date=" + date.toString() + ", id=" + id + "]";
	}
}
