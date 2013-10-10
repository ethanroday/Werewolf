package edu.wm.werewolf.exceptions;

public class DuplicateUserException extends Exception {
	
	private String username;
	
	private static final long serialVersionUID = 1L;

	public DuplicateUserException(String userID) {
		super();
		this.username = userID;
	}
	
	public String getUserID() {
		return username;
	}
	
	public String getMessage() {
		return "A user with username "+username+" already exists";
	}
}
