package employee;

public class EmpPassword {
	private String value;

	public EmpPassword(String value) {
		if (value != null && value.length() < 8) throw new IllegalArgumentException("パスワードは" + 8 + "文字以上必要です");

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpPassword other = (EmpPassword) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
