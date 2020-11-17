package domain.employee;

import java.util.regex.PatternSyntaxException;

public class PhoneNumber {
	private String value;

	public PhoneNumber(String value) {
		if (value != null && !value.matches("^0\\d{2,3}-\\d{1,4}-\\d{4}$")) {
			throw new PatternSyntaxException("電話番号の形式が正しくありません", "^0\\d{2,3}-\\d{1,4}-\\d{4}$", -1);
		}
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
