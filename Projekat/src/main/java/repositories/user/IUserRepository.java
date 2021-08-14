package repositories.user;

import java.util.List;

import model.User;

public interface IUserRepository {
	 public User findUser(String email);
	 public List<User> allUser();
	 public User addUser(User user);
	 public Integer userActivity(String email);
	 public User updateUser(User user, String email);
}
