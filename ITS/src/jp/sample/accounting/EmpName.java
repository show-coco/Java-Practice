package jp.sample.accounting;

public class EmpName {
	private String firstName;
	private String lastName;

	public EmpName(String firstName, String lastName) {
		if (firstName == null || lastName == null || firstName == "" || lastName == "") throw new NullPointerException("名前がnullです");

		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
