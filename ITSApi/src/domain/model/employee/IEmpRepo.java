package domain.model.employee;
import java.util.ArrayList;

public interface IEmpRepo {
	public Employee save(Employee emp);
	public Employee find(int empId);
	public ArrayList<Employee> findAll(int pageNum);
	public ArrayList<Employee> findAll(int pageNum, String name);
	public ArrayList<Employee> findAll(int pageNum, int departmentId);
}
