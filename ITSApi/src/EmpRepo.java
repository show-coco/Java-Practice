import java.util.ArrayList;

public class EmpRepo {
	private static ArrayList<Employee> empList = new ArrayList<>();

	public static Employee save(Employee emp) {
		empList.add(emp);

		return emp;
	}
}
