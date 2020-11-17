package repository.employee;

import java.util.ArrayList;

import domain.employee.EmpId;
import domain.employee.EmpPassword;
import domain.employee.Employee;

public interface IEmpRepo {
	public void save(Employee employee);
	public Employee getById(EmpId id);
	public Employee getByIdPw(EmpId id, EmpPassword pw);
	public ArrayList<Employee> getAll();
}
