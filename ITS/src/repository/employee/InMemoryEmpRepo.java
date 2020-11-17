package repository.employee;

import java.util.ArrayList;

import domain.employee.EmpId;
import domain.employee.EmpPassword;
import domain.employee.Employee;

public class InMemoryEmpRepo implements IEmpRepo {
	private ArrayList<Employee> employees = new ArrayList<>();

	@Override
	public Employee getById(EmpId id) {
		return employees.stream().filter(emp -> emp.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public void save(Employee employee) {
		employees.add(employee);
	}

	@Override
	public ArrayList<Employee> getAll() {
		return employees;
	}

	@Override
	public Employee getByIdPw(EmpId id, EmpPassword pw) {
		return employees.stream().filter(emp -> emp.getId().equals(id) && emp.getPassword().equals(pw)).findFirst().orElse(null);
	}

}
