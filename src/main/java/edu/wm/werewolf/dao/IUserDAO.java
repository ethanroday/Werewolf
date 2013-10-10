package edu.wm.werewolf.dao;

import java.util.List;

import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.DuplicateUserException;
import edu.wm.werewolf.exceptions.NoUserFoundException;

public interface IUserDAO {
	
	public void insertUser(User user) throws DuplicateUserException;
	public void updateUser(User user);
	public User getUserByUsername(String username) throws NoUserFoundException;
	public List<User> getAllUsers();
	public User getUserByUserID(String id) throws NoUserFoundException;
	public void removeAllUsersExcept(User u);
}
