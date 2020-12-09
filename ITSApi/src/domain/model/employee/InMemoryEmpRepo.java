package domain.model.employee;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InMemoryEmpRepo implements IEmpRepo {
	private static ArrayList<Employee> empList = new ArrayList<>();
	private int currentId = 1;

	@Override
	public Employee save(Employee emp) {
		emp.setEmpId(currentId);

		empList.add(emp);

		currentId++;

		return emp;
	}

	@Override
	public Employee find(int empId) {
		return empList.stream().filter(emp -> emp.getEmpId() == empId).findFirst().get();
	}

	@Override
	public ArrayList<Employee> findAll(int pageNum) {
		return (ArrayList<Employee>) empList.stream().skip((pageNum-1) * 10).limit(pageNum * 10).collect(Collectors.toList());
	}

	@Override
	public ArrayList<Employee> findAll(int pageNum, String name) {
		return null;
	}

	@Override
	public ArrayList<Employee> findAll(int pageNum, int departmentId) {
		return null;
	}
}
