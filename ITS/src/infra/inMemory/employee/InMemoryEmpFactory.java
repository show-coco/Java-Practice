package infra.inMemory.employee;

import java.util.Date;

import domain.models.employee.EmpId;
import domain.models.employee.EmpName;
import domain.models.employee.EmpPassword;
import domain.models.employee.Employee;
import domain.models.employee.Gender;
import domain.models.employee.IEmpFactory;
import domain.models.employee.MailAddress;
import domain.models.employee.PhoneNumber;

public class InMemoryEmpFactory implements IEmpFactory {
	private int currentId = 0;
	
	@Override
	public Employee create(EmpName name, MailAddress mailAddress, Gender genderId, Date birthDay,
			PhoneNumber phoneNumber, EmpPassword password, String address) {
		currentId++;

		return new Employee(new EmpId(currentId), name, mailAddress, genderId, birthDay, phoneNumber, password, address);
	}

}
