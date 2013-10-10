package edu.wm.werewolf.domain;

public class BasicPlayer {

	private String firstName;
	private String lastName;
	private String userName;
	private String id;
	private boolean isDead;
	private String imageURL;
	private int score;
	
	public BasicPlayer(){}

	public BasicPlayer(String firstName, String lastName, String userName,
			String id, boolean isDead, String imageURL, int score) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.id = id;
		this.isDead = isDead;
		this.imageURL = imageURL;
		this.score = score;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


}
