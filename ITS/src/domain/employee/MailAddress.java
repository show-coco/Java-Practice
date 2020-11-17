package domain.employee;

import java.util.regex.PatternSyntaxException;

public class MailAddress {
	String value;

	public MailAddress(String value) {
		if (value == null) throw new NullPointerException("メールアドレスがnullです");
		if (!value.matches("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
			throw new PatternSyntaxException("メールアドレスが正しい形式ではありません", "^[a-zA-Z0-9.!#$%&'*+\\\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$", -1);
		}

		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
