import java.util.UUID;

// エンティティ(ライフサイクルを持つ)
public class User {
	private UserId id;
	private UserName name;

	public User(UserName name) {
		if (name == null) throw new NullPointerException("nameがnullです"); 
		
		this.id = new UserId(UUID.randomUUID().toString());
		this.name = name;
	}
	
	public User(UserId id, UserName name) {
		if (id == null) throw new NullPointerException("idがnullです");
		if (name == null) throw new NullPointerException("nameがnulldです");
		
		this.id = id;
		this.name = name;
	}
	
	public UserId getId() { return id; }
	public UserName getName() { return name; }	
	public void setName(UserName name) { this.name = name; }

	public void ChangeName(UserName name) {
		if (name == null) throw new NullPointerException("nameがnullです");
		
		this.name = name;
	}
}
