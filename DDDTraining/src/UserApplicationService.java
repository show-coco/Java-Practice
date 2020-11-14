
public class UserApplicationService {
	private IUserRepository userRepository;
	private UserService userService;
	
	public UserApplicationService(IUserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	public void register(string name) {
		User user = new User(new UserName(name));
		
		if (userService.exists(user)) {
			throw new Exception(user.getId() + "既に存在しています");
		}
		
		userRepository.save(user);
	}
	
	public UserData get(String userId) {
		UserId targetId = new UserId(userId);
		User user = userRepository.find(targetId);
		
		if (user == null) return null;

		return new UserData(user);
	}
	
	public void update(UserUpdateCommand command) {
		UserId targetId = new UserId(command.getId());
		User user = userRepository.find(targetId);
		
		if (user == null) throw new Exception("idが" + targetId + "のユーザは存在しません");
		
		String name = command.getName();
		if (name != null) {
			UserName newUserName = new UserName(name);
			user.ChangeName(newUserName);
			if (userService.exists()) {
				throw new Exception("ユーザは既に存在しています");
			}			
		}

		userRepository.save(user);
	}
	
	public void delete(UserDeleteCommand command) {
		UserId targetId = new UserId(command.getId());
		User user = userRepository.find(targetId);
		
		if (user == null) throw new Exception("idが" + targetId + "のユーザは存在しません");
		
		userRepository.delete(user);
	}
}
