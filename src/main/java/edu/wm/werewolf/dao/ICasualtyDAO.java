package edu.wm.werewolf.dao;

import java.util.Date;
import java.util.List;

import edu.wm.werewolf.domain.Casualty;

public interface ICasualtyDAO {

	public void insertCasualty(Casualty c);
	
	public List<Casualty> getKillsByPlayerID(String id);
	
	public List<Casualty> getCasualtiesInRange(float lat, float lng, float rad);
	
	public Casualty getDeathByPlayerID(String id);
	
	public List<Casualty> getCasualtiesOnDate(Date d);
	
	public List<Casualty> getAllCasualties();
	
	public void removeAllCasualties();	
}