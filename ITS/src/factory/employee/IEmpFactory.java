package factory.employee;

import java.util.Date;

import domain.employee.EmpName;
import domain.employee.EmpPassword;
import domain.employee.Employee;
import domain.employee.Gender;
import domain.employee.MailAddress;
import domain.employee.PhoneNumber;

public interface IEmpFactory {
	public Employee create(EmpName name, MailAddress mailAddress, Gender jenderId, Date birthDay,
			PhoneNumber phoneNumber, EmpPassword password, String address);
}
