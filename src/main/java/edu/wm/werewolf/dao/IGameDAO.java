package edu.wm.werewolf.dao;

import edu.wm.werewolf.domain.Game;

public interface IGameDAO {

	public Game getGame();
	public void updateGame(Game g);
	public void pauseGame();
	public void resumeGame();
	public double getScentRange();
	public double getKillRange();
	public void removeAllGames();
	
}
