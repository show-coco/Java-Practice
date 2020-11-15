package jp.sample.accounting;

import java.util.ArrayList;

public class InMemoryEmpRepo implements IEmployeeRepo {
	private ArrayList<Employee> employees = new ArrayList<>();

	@Override
	public boolean find(EmpId id) {
		return false;
	}

	@Override
	public void save(Employee employee) {
		employees.add(employee);
	}

	@Override
	public ArrayList<Employee> getAll() {
		return employees;
	}

}
