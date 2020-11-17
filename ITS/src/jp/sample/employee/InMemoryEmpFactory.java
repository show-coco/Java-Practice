package jp.sample.employee;

import java.util.Date;

public class InMemoryEmpFactory implements IEmpFactory {
	private int currentId = 0;
	
	@Override
	public Employee create(EmpName name, MailAddress mailAddress, Gender genderId, Date birthDay,
			PhoneNumber phoneNumber, EmpPassword password, String address) {
		currentId++;

		return new Employee(new EmpId(currentId), name, mailAddress, genderId, birthDay, phoneNumber, password, address);
	}

}
