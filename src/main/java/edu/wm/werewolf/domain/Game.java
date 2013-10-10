package edu.wm.werewolf.domain;

import java.util.Date;

public class Game {

	private String id;
	private Date createdDate;
	private int dayNightFreq;
	private boolean isRunning;
	private float scentRange;
	private float killRange;

	public Game() {}
	public Game(Date createdDate, int dayNightFreq,
			float scentRange, float killRange) {
		super();
		this.createdDate = createdDate;
		this.dayNightFreq = dayNightFreq;
		this.isRunning = false;
		this.scentRange = scentRange;
		this.killRange = killRange;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getDayNightFreq() {
		return dayNightFreq;
	}

	public void setDayNightFreq(int dayNightFreq) {
		this.dayNightFreq = dayNightFreq;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public float getScentRange() {
		return scentRange;
	}
	public void setScentRange(float scentRange) {
		this.scentRange = scentRange;
	}
	public float getKillRange() {
		return killRange;
	}
	public void setKillRange(float killRange) {
		this.killRange = killRange;
	}
	
}
