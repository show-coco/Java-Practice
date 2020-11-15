package jp.sample.accounting;

public class EmployeeService {
	IEmployeeRepo employeeRepo; 
	
	public EmployeeService(IEmployeeRepo employeeRepo) {
		super();
		this.employeeRepo = employeeRepo;
	}



	public boolean exists(Employee employee) {
		EmpId targetId = employee.getId();

		return employeeRepo.find(targetId);
	}
}
