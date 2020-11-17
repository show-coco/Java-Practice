package jp.sample.accounting;

import java.util.ArrayList;

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
