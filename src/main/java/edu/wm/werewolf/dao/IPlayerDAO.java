package edu.wm.werewolf.dao;

import java.util.List;

import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public interface IPlayerDAO {
	
	public void insertPlayer(Player p);
	
	public void updatePlayer(Player p);

	public List<Player> getAllAlive();
	
	public List<Player> getAllPlayers();
	
	public List<Player> getNearbyPlayers(Player p, double dist);
	
	public Player getPlayerByID(String id) throws NoPlayerFoundException;
	
	public Player getPlayerByUserID(String id) throws NoPlayerFoundException;
	
	public void removePlayer(Player p);
	
	public void removeAllPlayers();
	
	public int getNumAliveWerewolves();
	
	public int getNumAliveTownspeople();
}
