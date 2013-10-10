package edu.wm.werewolf.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import edu.wm.werewolf.domain.Casualty;

public class MongoCasualtyDAO implements ICasualtyDAO {

	@Autowired private MongoOperations mongoTemplate;
	
	@Override
	public void insertCasualty(Casualty c) {
		mongoTemplate.save(c);
	}

	@Override
	public List<Casualty> getKillsByPlayerID(String id) {
		Query q = new Query(Criteria.where("killerID").is(id));
		return mongoTemplate.find(q, Casualty.class);
	}

	@Override
	public List<Casualty> getCasualtiesInRange(float lat, float lng, float rad) {
		NearQuery near = NearQuery.near(lng,lat).maxDistance(rad);
		GeoResults<Casualty> results = mongoTemplate.geoNear(near, Casualty.class);
		List<Casualty> casualties = new ArrayList<>();
		for (GeoResult<Casualty> res : results) {
			casualties.add(res.getContent());
		}
		return casualties;
	}

	@Override
	public Casualty getDeathByPlayerID(String id) {
		Query q = new Query(Criteria.where("victimID").is(id));
		return mongoTemplate.findOne(q, Casualty.class);
	}

	@Override
	public List<Casualty> getCasualtiesOnDate(Date d) {
		Query q = new Query(Criteria.where("casualtyDate").is(d));
		return mongoTemplate.find(q, Casualty.class);
	}

	@Override
	public List<Casualty> getAllCasualties() {
		return mongoTemplate.findAll(Casualty.class);
	}

	@Override
	public void removeAllCasualties() {
		mongoTemplate.remove(new Query(), Casualty.class);
	}

}
