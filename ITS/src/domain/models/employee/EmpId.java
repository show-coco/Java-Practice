package domain.models.employee;

public class EmpId {
	private int value;

	public EmpId(int value) {
		this.value = value;
	}

	public int getValue() {
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
		EmpId other = (EmpId) obj;
		if (value != other.value)
			return false;
		return true;
	}
	
}
