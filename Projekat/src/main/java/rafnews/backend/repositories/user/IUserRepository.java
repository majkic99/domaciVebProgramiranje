package rafnews.backend.repositories.user;

import java.util.List;

import rafnews.backend.model.User;

public interface IUserRepository {
	 public User findUser(String email);
	 public List<User> allUser(int page, int perPage);
	 public User addUser(User user);
	 public Integer userActivity(String email);
	 public User updateUser(User user, String email);
}
