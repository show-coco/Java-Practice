
public class UserData {
	String id;
	String name;

	public UserData(User source) {
		this.id = source.getId().getValue();
		this.name = source.getName().getValue();
	}

	public String getId() { return id; }
	public String getName() { return name; }
}
