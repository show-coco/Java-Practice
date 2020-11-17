package employee;

import java.util.Date;

public interface IEmpFactory {
	public Employee create(EmpName name, MailAddress mailAddress, Gender jenderId, Date birthDay,
			PhoneNumber phoneNumber, EmpPassword password, String address);
}
