package api.domain.model.register;

import java.time.LocalDate;

public class BusinessTrip {
	private String empId;
	private LocalDate date;

	public BusinessTrip(String empId, LocalDate date) {
		super();
		this.empId = empId;
		this.date = date;
	}

	@Override
	public String toString() {
		return "BusinessTrip [" + (empId != null ? "empId=" + empId + ", " : "") + (date != null ? "date=" + date : "")
				+ "]";
	}
	
}
