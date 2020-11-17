package jp.sample.accounting;

import java.util.Date;

public class Employee {
	private EmpId id;
	private EmpName name;
	private MailAddress mailAddress;
	private Gender jenderId;
	private Date birthDay;
	private PhoneNumber phoneNumber;
	private EmpPassword password;
	private String address;

	public Employee(EmpId id, EmpName name, MailAddress mailAddress, Gender jenderId, Date birthDay,
			PhoneNumber phoneNumber, EmpPassword password, String address) {
		this.id = id;
		this.name = name;
		this.mailAddress = mailAddress;
		this.jenderId = jenderId;
		this.birthDay = birthDay;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.address = address;
	}

	public EmpName getName() {
		return name;
	}

	public MailAddress getMailAddress() {
		return mailAddress;
	}

	public Gender getJender() {
		return jenderId;
	}

	public EmpId getId() {
		return id;
	}
	
	public Gender getJenderId() {
		return jenderId;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public EmpPassword getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

	public void setName(EmpName name) {
		this.name = name;
	}
	
	public void setPassword(EmpPassword password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Employee [id=" + id.getValue() + ", name=" + name.getLastName() + " " + name.getFirstName() + ", mailAddress=" + mailAddress.getValue() + ", jenderId=" + jenderId.getId()
				+ ", birthDay=" + birthDay + ", phoneNumber=" + phoneNumber.getValue() + ", password=" + password + ", address="
				+ address + "]";
	}
}
