package edu.wm.werewolf.dao;

import org.springframework.beans.factory.annotation.Autowired;

import edu.wm.werewolf.util.ScoreConfig;
import edu.wm.werewolf.util.ScoreType;

public class PropertiesScoreConfigDAO implements IScoreConfigDAO {

	@Autowired ScoreConfig scoreConfig;
	
	public int getScoreForScoreType(ScoreType type) {
		return scoreConfig.getScoreForScoreType(type);
	}
	
}
