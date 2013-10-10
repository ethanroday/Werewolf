package edu.wm.werewolf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.DuplicateUserException;
import edu.wm.werewolf.exceptions.NoUserFoundException;

public class UserService {
	
	@Autowired IUserDAO userDAO;
	
	public void insertUser(User u) throws DuplicateUserException {
		userDAO.insertUser(u);
	}
	
	public User getUserByUsername(String username) throws NoUserFoundException {
		return userDAO.getUserByUsername(username);
	}
	
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}
}