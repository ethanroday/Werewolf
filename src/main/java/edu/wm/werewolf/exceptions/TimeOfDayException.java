package edu.wm.werewolf.exceptions;

public class TimeOfDayException extends Exception {
	
	private String msg;
	
	private static final long serialVersionUID = 1L;

	public TimeOfDayException(String msg) {
		super();
		this.msg = msg;
	}
	
	public String getMessage() {
		return msg;
	}
}
