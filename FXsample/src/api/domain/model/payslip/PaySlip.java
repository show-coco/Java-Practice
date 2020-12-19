package api.domain.model.payslip;

public class PaySlip {
	private int empId;
	private int month;
	private int basicSalary;
	private int OvertimeAllowance;
	private int vacationAllowance;
	private int nightAlowance;
	private int officerAllowance;

	public PaySlip(int empId, int month, int basicSalary, int overtimeAllowance, int vacationAllowance,
			int nightAlowance, int officerAllowance) {
		super();
		this.empId = empId;
		this.month = month;
		this.basicSalary = basicSalary;
		OvertimeAllowance = overtimeAllowance;
		this.vacationAllowance = vacationAllowance;
		this.nightAlowance = nightAlowance;
		this.officerAllowance = officerAllowance;
	}
}
