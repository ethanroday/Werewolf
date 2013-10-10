package edu.wm.werewolf.domain;

import java.util.Date;

public class Casualty {

	private String id;
	private Date casualtyDate;
	private String killerID;
	private String victimID;
	private double[] loc;
	
	public Casualty(Date casualtyDate, String killerID, String victimID,
			double lat, double lng) {
		super();
		this.casualtyDate = casualtyDate;
		this.killerID = killerID;
		this.victimID = victimID;
		this.loc = new double[2];
		this.loc[0] = lng;
		this.loc[1] = lat;
	}
	
	public Date getCasualtyDate() {
		return casualtyDate;
	}
	public void setCasualtyDate(Date casualtyDate) {
		this.casualtyDate = casualtyDate;
	}
	public String getKillerID() {
		return killerID;
	}
	public void setKillerID(String killerID) {
		this.killerID = killerID;
	}
	public String getVictimID() {
		return victimID;
	}
	public void setVictimID(String victimID) {
		this.victimID = victimID;
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
	public double[] getLoc() {
		return loc;
	}
	public void setLoc(double[] loc) {
		this.loc = loc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
}
