package edu.wm.werewolf.exceptions;

public class InsufficientPlayersException extends Exception {

	private String msg;
	
	private static final long serialVersionUID = 1L;

	public InsufficientPlayersException(String msg) {
		super();
		this.msg = msg;
	}
	
	public String getMessage() {
		return msg;
	}
	
}
