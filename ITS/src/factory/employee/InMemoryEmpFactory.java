package factory.employee;

import java.util.Date;

import domain.employee.EmpId;
import domain.employee.EmpName;
import domain.employee.EmpPassword;
import domain.employee.Employee;
import domain.employee.Gender;
import domain.employee.MailAddress;
import domain.employee.PhoneNumber;

public class InMemoryEmpFactory implements IEmpFactory {
	private int currentId = 0;
	
	@Override
	public Employee create(EmpName name, MailAddress mailAddress, Gender genderId, Date birthDay,
			PhoneNumber phoneNumber, EmpPassword password, String address) {
		currentId++;

		return new Employee(new EmpId(currentId), name, mailAddress, genderId, birthDay, phoneNumber, password, address);
	}

}
