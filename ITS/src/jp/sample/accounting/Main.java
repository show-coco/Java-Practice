package jp.sample.accounting;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		InMemoryEmpRepo employeeRepo = new InMemoryEmpRepo();
		EmployeeService employeeService = new EmployeeService(employeeRepo);

		EmployeeApplicationService empApp = new EmployeeApplicationService(employeeRepo, employeeService);
		empApp.register("ABCDE123", "Sho Sakai", "show@example.com", 1);
		ArrayList<Employee> employees = empApp.getAll();
		employees.forEach(emp -> System.out.println(emp.getId().getValue()));
	}

}
