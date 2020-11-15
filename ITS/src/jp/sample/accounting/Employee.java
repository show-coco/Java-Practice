package jp.sample.accounting;

public class Employee {
	private EmpId id;
	private EmpName name;
	private MailAddress mailAddress;
	private Gender jenderId;

	public Employee(EmpId id, EmpName name, MailAddress mailAddress, Gender jenderId) {
		this.id = id;
		this.name = name;
		this.mailAddress = mailAddress;
		this.jenderId = jenderId;
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
	
	public void changeName(EmpName name) {
		if (name == null) throw new NullPointerException("nameがnullです");

		this.name = name;
	}
}
