
public class UserUpdateCommand {
	private String id;
	private String name;
	private String mailAddress;

	public UserUpdateCommand(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getId() {
		return id;
	}
	
	
}
