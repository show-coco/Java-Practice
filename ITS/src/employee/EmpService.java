package employee;

public class EmpService {
	IEmpRepo employeeRepo; 
	
	public EmpService(IEmpRepo employeeRepo) {
		super();
		this.employeeRepo = employeeRepo;
	}



	public boolean exists(Employee employee) {
		EmpId targetId = employee.getId();
		Employee emp = employeeRepo.getById(targetId);
		if (emp != null) {
			System.out.println(emp.getId().getValue());
		}

		return emp != null;
	}
}
