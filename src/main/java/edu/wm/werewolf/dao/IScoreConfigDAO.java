package edu.wm.werewolf.dao;

import edu.wm.werewolf.util.ScoreType;

public interface IScoreConfigDAO {

	public int getScoreForScoreType(ScoreType type);
}
