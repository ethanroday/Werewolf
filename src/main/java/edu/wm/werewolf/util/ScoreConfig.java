package edu.wm.werewolf.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ScoreConfig {

	private Properties prop;
	
	public ScoreConfig(String scorePropertiesFile) throws FileNotFoundException, IOException {
		prop = new Properties();
		prop.load(ScoreConfig.class.getClassLoader().getResourceAsStream(scorePropertiesFile));
	}
	
	public int getScoreForScoreType(ScoreType type) {
		String scoreStr = prop.getProperty(type.toString());
		return Integer.parseInt(scoreStr);
	}
}
