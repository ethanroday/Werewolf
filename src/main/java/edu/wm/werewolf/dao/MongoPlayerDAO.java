package edu.wm.werewolf.dao;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Update.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public class MongoPlayerDAO implements IPlayerDAO {
	
	@Autowired private MongoOperations mongoTemplate;
	
	@Override
	public void insertPlayer(Player p) {
		mongoTemplate.save(p);		
	}

	@Override
	public List<Player> getAllAlive() {
		Query q = new Query(Criteria.where("isDead").is(false));
		return mongoTemplate.find(q, Player.class);
		
	}

	@Override
	public Player getPlayerByID(String id) throws NoPlayerFoundException {
		Player p = mongoTemplate.findById(id, Player.class);
		if (p == null) {
			throw new NoPlayerFoundException(id);
		}
		return p;
	}

	@Override
	public void removePlayer(Player p) {
		mongoTemplate.remove(p);
	}

	@Override
	public List<Player> getAllPlayers() {
		List<Player> players = mongoTemplate.findAll(Player.class);
		return players;
	}

	@Override
	public List<Player> getNearbyPlayers(Player p, double dist) {
		NearQuery geoNear = NearQuery.near(p.getLng(),p.getLat(),Metrics.KILOMETERS).maxDistance(dist/1000);
		GeoResults<Player> geoPlayers = mongoTemplate.geoNear(geoNear, Player.class);
		List<Player> players = new ArrayList<>();
		for (GeoResult<Player> geoPlayer : geoPlayers) {
			players.add(geoPlayer.getContent());
		}
		players.remove(0); //Remove p (always first because GeoResults are sorted)
		return players;
	}

	@Override
	public void updatePlayer(Player p) {
		mongoTemplate.save(p);
		
	}

	@Override
	public void removeAllPlayers() {
		mongoTemplate.remove(new Query(), Player.class);
		
	}

	@Override
	public int getNumAliveWerewolves() {
		Query q = new Query(Criteria.where("isWerewolf").is(true).
				andOperator(Criteria.where("isDead").is(false)));
		return (int) mongoTemplate.count(q, Player.class);
	}

	@Override
	public int getNumAliveTownspeople() {
		Query q = new Query(Criteria.where("isWerewolf").is(false).
				andOperator(Criteria.where("isDead").is(false)));
		return (int) mongoTemplate.count(q, Player.class);
	}

	@Override
	public Player getPlayerByUserID(String id) throws NoPlayerFoundException {
		Query q = new Query(Criteria.where("userID").is(id));
		return mongoTemplate.findOne(q, Player.class);
	}

}
