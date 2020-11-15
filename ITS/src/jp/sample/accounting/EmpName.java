package jp.sample.accounting;

public class EmpName {
	private String value;

	public EmpName(String value) {
		if (value == null) throw new NullPointerException(value);
		if (value.length() < 3) throw new  IllegalArgumentException("ユーザ名は3文字以上です。" + value);
		if (value.length() > 20) throw new  IllegalArgumentException("ユーザ名は20文字以下です。" + value);

		this.value = value;
	}

	public String getValue() { return value; }
}
