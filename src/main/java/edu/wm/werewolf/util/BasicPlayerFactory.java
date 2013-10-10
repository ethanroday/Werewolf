package edu.wm.werewolf.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.domain.BasicPlayer;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.NoUserFoundException;

public class BasicPlayerFactory {

	@Autowired private IUserDAO userDAO;
	
	public BasicPlayer makeBasicPlayerFromPlayer(Player p) throws NoUserFoundException {
		User u = userDAO.getUserByUserID(p.getUserID());
		return new BasicPlayer(u.getFirstName(), u.getLastName(), u.getUserName(),
				p.getId(), p.getIsDead(), u.getImageURL(), u.getScore());
	}
	
	public List<BasicPlayer> makeBasicPlayersFromPlayers(List<Player> players) throws NoUserFoundException {
		List<BasicPlayer> basicPlayers = new ArrayList<>();
		for (Player p : players) {
			basicPlayers.add(makeBasicPlayerFromPlayer(p));
		}
		return basicPlayers;
	}
}
