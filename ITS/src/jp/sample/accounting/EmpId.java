package jp.sample.accounting;

public class EmpId {
	private String value;

	public EmpId(String value) {
		if (value.isEmpty() || value == null) throw new IllegalArgumentException("EmpIdがnullまたは空文字です");

		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
