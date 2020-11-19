package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import domain.models.employee.EmpId;
import domain.models.employee.EmpName;
import domain.models.employee.EmpPassword;
import domain.models.employee.Employee;
import domain.models.employee.Gender;
import domain.models.employee.IEmpFactory;
import domain.models.employee.IEmpRepo;
import domain.models.employee.MailAddress;
import domain.models.employee.PhoneNumber;
import domain.services.EmpService;

public class EmployeeApplicationService {
	private IEmpFactory empFactory;
	private IEmpRepo empRepo;
	private EmpService empService;

	public EmployeeApplicationService(IEmpFactory empFactory, IEmpRepo employeeRepo, EmpService employeeService) {
		this.empFactory = empFactory;
		this.empRepo = employeeRepo;
		this.empService = employeeService;
	}
	
	public void register(String firstName, String lastName, String mailAddress, int genderId, String birthDayStr, String phoneNumber, String pw, String address) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date birthDay = new Date();

		try {
			birthDay = df.parse(birthDayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Employee employee = empFactory.create(new EmpName(firstName, lastName), new MailAddress(mailAddress), new Gender(genderId), birthDay, new PhoneNumber(phoneNumber), new EmpPassword(pw), address);
		
		if (empService.exists(employee)) {
			throw new IllegalArgumentException(employee.getId().getValue() + "は既に存在しています");
		}

		empRepo.save(employee);
	}
	
	public Employee get(int id) {
		return empRepo.getById(new EmpId(id));
	}
	
	public ArrayList<Employee> getAll() {
		return empRepo.getAll(); 
	}

	public void changeName(Employee employee, String firstName, String lastName) {
		employee.setName(new EmpName(firstName, lastName));
		empRepo.save(employee);
	}
	
	public void changePw(Employee employee, String pw) {
		employee.setPassword(new EmpPassword(pw));
		empRepo.save(employee);
	}
	
	public boolean login(int id, String pw) {
		Employee emp = empRepo.getByIdPw(new EmpId(id), new EmpPassword(pw));
		return emp != null;
	}
}
