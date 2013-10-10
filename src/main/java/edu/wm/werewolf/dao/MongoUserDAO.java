package edu.wm.werewolf.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.DuplicateUserException;
import edu.wm.werewolf.exceptions.NoUserFoundException;

public class MongoUserDAO implements IUserDAO {

	@Autowired private MongoOperations mongoTemplate;
	
	@Override
	public void insertUser(User user) throws DuplicateUserException {
		Query q = new Query(Criteria.where("userName").is(user.getUserName()));
		if (mongoTemplate.find(q, User.class).size() != 0) {
			throw new DuplicateUserException(user.getUserName());
		}
		mongoTemplate.save(user);
	}

	@Override
	public User getUserByUsername(String username) throws NoUserFoundException {
		Query q = new Query(Criteria.where("userName").is(username));
		User u = mongoTemplate.findOne(q, User.class);
		if (u == null) {
			throw new NoUserFoundException(username);
		}
		return u;
	}

	@Override
	public List<User> getAllUsers() {
		return mongoTemplate.find(new Query(), User.class);
	}

	@Override
	public void updateUser(User user) {
		mongoTemplate.save(user);
	}

	@Override
	public User getUserByUserID(String id) throws NoUserFoundException {
		User u = mongoTemplate.findById(id, User.class);
		if (u == null) {
			throw new NoUserFoundException(id);
		}
		return u;
	}

	@Override
	public void removeAllUsersExcept(User u) {
		mongoTemplate.remove(new Query(), User.class);
		mongoTemplate.save(u);
	}

}
