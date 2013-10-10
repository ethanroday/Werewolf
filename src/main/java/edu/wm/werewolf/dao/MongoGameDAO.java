package edu.wm.werewolf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import edu.wm.werewolf.domain.Game;

public class MongoGameDAO implements IGameDAO {

	@Autowired private MongoOperations mongoTemplate;
	
	@Override
	public Game getGame() {
		return mongoTemplate.findOne(new Query(), Game.class);
	}

	@Override
	public void updateGame(Game g) {
		mongoTemplate.remove(new Query(), Game.class);
		mongoTemplate.save(g);
	}

	@Override
	public void pauseGame() {
		mongoTemplate.updateFirst(new Query(), 
				Update.update("isRunning", false), Game.class);
	}

	@Override
	public void resumeGame() {
		mongoTemplate.updateFirst(new Query(), 
				Update.update("isRunning", true), Game.class);
		
	}

	@Override
	public double getScentRange() {
		//TODO: Surely there's a way 
		//to just get the range directly
		return mongoTemplate.findOne(new Query(), Game.class).getScentRange();
	}

	@Override
	public double getKillRange() {
		//TODO: Surely there's a way 
		//to just get the range directly
		return mongoTemplate.findOne(new Query(), Game.class).getKillRange();
	}

	@Override
	public void removeAllGames() {
		mongoTemplate.remove(new Query(), Game.class);
	}
	
}
