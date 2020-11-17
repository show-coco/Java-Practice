package employee;

import java.util.ArrayList;

public interface IEmpRepo {
	public void save(Employee employee);
	public Employee getById(EmpId id);
	public Employee getByIdPw(EmpId id, EmpPassword pw);
	public ArrayList<Employee> getAll();
}
