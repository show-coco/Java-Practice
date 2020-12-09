package service;
import java.util.Date;

import org.junit.jupiter.api.Test;

import domain.model.employee.Employee;
import domain.model.employee.IEmpRepo;
import domain.model.employee.InMemoryEmpRepo;

class TestEmployeeService {
	EmployeeService empService;

	TestEmployeeService() {
		IEmpRepo empRepo = new InMemoryEmpRepo();
		this.empService = new EmployeeService(empRepo);
	}

	@Test
	void test() {
		Employee emp = empService.addEmp(false, 2, 2, 2, 1, new Date(), "password", "sho", "山口県", "山口市", "0120-333-999", "733-0000");
	}

}
