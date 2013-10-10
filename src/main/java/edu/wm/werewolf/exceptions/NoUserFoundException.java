package edu.wm.werewolf.exceptions;

public class NoUserFoundException extends Exception {
	
	private String principalName;
	
	private static final long serialVersionUID = 1L;

	public NoUserFoundException(String pName) {
		super();
		this.principalName = pName;
	}
	
	public String getPrincipalName() {
		return principalName;
	}
	
	public String getMessage() {
		return "No user was found for Principal "+principalName;
	}

}
