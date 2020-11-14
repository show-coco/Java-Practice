// 値オブジェクト
public class UserId {
	private String value;

	public UserId(String value) {
		if (value.isEmpty() || value == null) throw new IllegalArgumentException("valueがnullまたは空文字です");
		
		this.value = value;
	}
	
	public String getValue() { return value; }
	
}
