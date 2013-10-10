package edu.wm.werewolf.exceptions;

public class NoPlayerFoundException extends Exception {

	private String ID;
	
	private static final long serialVersionUID = 1L;

	public NoPlayerFoundException(String ID) {
		super();
		this.ID = ID;
	}
	
	public String getUserID() {
		return ID;
	}
	
	public String getMessage() {
		return "No player was found with ID "+ID;
	}

}
