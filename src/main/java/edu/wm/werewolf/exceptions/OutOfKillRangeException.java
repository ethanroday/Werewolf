package edu.wm.werewolf.exceptions;

public class OutOfKillRangeException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public OutOfKillRangeException() {
		super();
	}
	
	public String getMessage() {
		return "Out of kill range!";
	}
}
