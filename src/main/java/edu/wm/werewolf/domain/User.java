package edu.wm.werewolf.domain;

import java.util.ArrayList;
import java.util.List;

public final class User {

	private String id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String imageURL;
	private List<Role> roles;
	private int score;
	
	public User() {
		roles = new ArrayList<Role>();
	}
	
	public User(String id, String firstName, String lastName, String userName,
			String password, String imageURL, List<Role> roles, int score) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.imageURL = imageURL;
		this.roles = roles;
		this.score = score;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
