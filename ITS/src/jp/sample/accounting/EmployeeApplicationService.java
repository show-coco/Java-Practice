package jp.sample.accounting;

import java.util.ArrayList;

public class EmployeeApplicationService {
	private IEmployeeRepo employeeRepo;
	private EmployeeService employeeService;

	public EmployeeApplicationService(IEmployeeRepo employeeRepo, EmployeeService employeeService) {
		this.employeeRepo = employeeRepo;
		this.employeeService = employeeService;
	}
	
	public void register(String id, String name, String mailAddress, int genderId) {
		Employee employee = new Employee(new EmpId(id), new EmpName(name), new MailAddress(mailAddress), new Gender(genderId));
		
		if (employeeService.exists(employee)) {
			throw new IllegalArgumentException(employee.getId() + "は既に存在しています");
		}

		employeeRepo.save(employee);
	}
	
	public ArrayList<Employee> getAll() {
		return employeeRepo.getAll(); 
	}
}
