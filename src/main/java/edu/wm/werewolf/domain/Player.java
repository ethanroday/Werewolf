package edu.wm.werewolf.domain;

public class Player {
	
	private String id;
	private Boolean isDead;
	private double[] loc;
	private String userID; //We make this a string to avoid coupling Player to UserDAO
	private Boolean isWerewolf;
	private String votedFor; //id of the player this player last voted for
	
	public Player() {
		this.loc = new double[2];
	}
	
	public Player(String id, Boolean isDead, double lat, double lng, String userID,
			Boolean isWerewolf) {
		super();
		this.id = id;
		this.isDead = isDead;
		this.loc = new double[2];
		this.loc[0] = lng;
		this.loc[1] = lat;
		this.userID = userID;
		this.isWerewolf = isWerewolf;
	}

	public Player(String id, Boolean isDead, double[] loc, String userID,
			Boolean isWerewolf, String votedFor) {
		super();
		this.id = id;
		this.isDead = isDead;
		this.loc = loc;
		this.userID = userID;
		this.isWerewolf = isWerewolf;
		this.votedFor = votedFor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsDead() {
		return isDead;
	}

	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}

	public double[] getLoc() {
		return loc;
	}

	public void setLoc(double[] loc) {
		this.loc = loc;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Boolean getIsWerewolf() {
		return isWerewolf;
	}

	public void setIsWerewolf(Boolean isWerewolf) {
		this.isWerewolf = isWerewolf;
	}

	public String getVotedFor() {
		return votedFor;
	}

	public void setVotedFor(String votedFor) {
		this.votedFor = votedFor;
	}
	
	public double getLat() {
		return loc[1];
	}
	public void setLat(double lat) {
		this.loc[1] = lat;
	}
	public double getLng() {
		return loc[0];
	}
	public void setLng(double lng) {
		this.loc[0] = lng;
	}
	
}
