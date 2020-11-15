package jp.sample.accounting;

import java.util.ArrayList;

public interface IEmployeeRepo {
	public boolean find(EmpId id);
	public void save(Employee employee);
	public ArrayList<Employee> getAll();
}
