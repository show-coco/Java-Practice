package api.domain.model.attendance;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
	public static final int ATTEND_TYPE = 1;
	public static final int LEAVE_TYPE = 2;

	private int empId;
	private int type;
	private LocalDate date;
	private LocalTime time;

	public Attendance(int empId, int type, LocalDate date, LocalTime time) {
		super();
		this.empId = empId;
		this.type = type;
		this.date = date;
		this.time = time;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public int getEmpId() {
		return empId;
	}

	public int getType() {
		return type;
	}

	public LocalDate getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "Attendance [empId=" + empId + ", type=" + type + ", " + (date != null ? "date=" + date + ", " : "")
				+ (time != null ? "time=" + time : "") + "]";
	}
}
