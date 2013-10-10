package edu.wm.werewolf.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;

import edu.wm.werewolf.dao.ICasualtyDAO;
import edu.wm.werewolf.dao.IGameDAO;
import edu.wm.werewolf.dao.IPlayerDAO;
import edu.wm.werewolf.dao.IScoreConfigDAO;
import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.domain.BasicPlayer;
import edu.wm.werewolf.domain.Casualty;
import edu.wm.werewolf.domain.Game;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.InappropriatePlayerException;
import edu.wm.werewolf.exceptions.InsufficientPlayersException;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;
import edu.wm.werewolf.exceptions.NoUserFoundException;
import edu.wm.werewolf.exceptions.OutOfKillRangeException;
import edu.wm.werewolf.exceptions.TimeOfDayException;
import edu.wm.werewolf.util.BasicPlayerFactory;
import edu.wm.werewolf.util.ScoreType;

public class GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameService.class);
	
	@Autowired private ICasualtyDAO casualtyDAO;
	@Autowired private IGameDAO gameDAO;
	@Autowired private IPlayerDAO playerDAO;
	@Autowired private IUserDAO userDAO;
	@Autowired private IScoreConfigDAO scoreConfigDAO;
	@Autowired private BasicPlayerFactory basicPlayerFactory;
	@Autowired private TaskScheduler myScheduler;
	
	private boolean isNight;
	
	public List<BasicPlayer> getAllAlive() throws NoUserFoundException {
		return basicPlayerFactory.makeBasicPlayersFromPlayers(
				playerDAO.getAllAlive());
	}
	
	public List<BasicPlayer> getNearbyPlayers(Player p) throws InappropriatePlayerException, NoUserFoundException {
		if (!p.getIsWerewolf()) {
			String msg = "Only werewolves can get nearby players.";
			throw new InappropriatePlayerException(msg);
		}
		double scentRange = gameDAO.getScentRange();
		return basicPlayerFactory.makeBasicPlayersFromPlayers(
				playerDAO.getNearbyPlayers(p, scentRange));
	}
	
	public List<BasicPlayer> getKillablePlayers(Player p) throws InappropriatePlayerException, NoUserFoundException {
		if (!p.getIsWerewolf()) {
			String msg = "Only werewolves can get killable players.";
			throw new InappropriatePlayerException(msg);
		}
		double killRange = gameDAO.getKillRange();
		List<Player> killable = playerDAO.getNearbyPlayers(p, killRange);
		int i = 0;
		while (i < killable.size()) {
			if (killable.get(i).getIsWerewolf() || killable.get(i).getIsDead()) {
				killable.remove(i);
			} else {
				i++;
			}
		}
		return basicPlayerFactory.makeBasicPlayersFromPlayers(killable);
				
	}
	
	public boolean isInKillRange(Player p1, Player p2) {
		double range = gameDAO.getKillRange();
		double dist = distance(p1, p2);
		//logger.info("In isInKillRange():");
		//logger.info("   "+p1.getId()+","+p2.getId());
		//logger.info("   "+p1.getLat()+","+p1.getLng());
		//logger.info("   "+p2.getLat()+","+p2.getLng());
		//logger.info("   Range: "+range+", Dist: "+dist);
		return dist <= range;
	}
	
	public List<BasicPlayer> getAllPlayers() throws NoUserFoundException {
		return basicPlayerFactory.makeBasicPlayersFromPlayers(
				playerDAO.getAllPlayers());
	}

	public BasicPlayer getBasicPlayerByID(String id) throws NoPlayerFoundException, NoUserFoundException {
		return basicPlayerFactory.makeBasicPlayerFromPlayer(playerDAO.getPlayerByID(id));
	}
	
	public BasicPlayer getBasicPlayerByUserID(String id) throws NoPlayerFoundException, NoUserFoundException {
		return basicPlayerFactory.makeBasicPlayerFromPlayer(playerDAO.getPlayerByUserID(id));
	}
	
	public Player getPlayerByID(String id) throws NoPlayerFoundException {
		Player p = playerDAO.getPlayerByID(id);
		logger.info(p.getId()+": "+p.getIsDead());
		return playerDAO.getPlayerByID(id);
	}
	
	public Player getPlayerByUserID(String id) throws NoPlayerFoundException {
		Player p = playerDAO.getPlayerByUserID(id);
		logger.info(p.getId()+": "+p.getIsDead());
		return playerDAO.getPlayerByUserID(id);
	}
	
	public void makeKill(Player killer, Player victim)
			throws InappropriatePlayerException, TimeOfDayException, OutOfKillRangeException, NoUserFoundException {
		if (!killer.getIsWerewolf() || killer.getIsDead() || victim.getIsWerewolf() || victim.getIsDead()) {
			String msg = "Only alive werewolves can make kills, and only alive townspeople are valid targets.";
			throw new InappropriatePlayerException(msg);
		}
		if (!isNight()) {
			String msg = "You can only kill at night!";
			throw new TimeOfDayException(msg);
		}
		if (!isInKillRange(killer,victim)) {
			throw new OutOfKillRangeException();
		}
		
		//Create and record the casualty
		Casualty c = new Casualty(new Date(), killer.getId(), victim.getId(), victim.getLat(), victim.getLng());
		casualtyDAO.insertCasualty(c);
		
		//Kill the victim
		doKill(victim);
		
		//Increment the user's score
		User killerUser = userDAO.getUserByUserID(killer.getUserID());
		int increment = scoreConfigDAO.getScoreForScoreType(ScoreType.WEREWOLF_KILL);
		killerUser.setScore(killerUser.getScore()+increment);
		userDAO.updateUser(killerUser);
	}
	
	public void updateLocation(Player p, double lat, double lng) {
		p.setLat(lat);
		p.setLng(lng);
		playerDAO.updatePlayer(p);
	}
	
	public void restartGame(Game g) throws InsufficientPlayersException {

		//End the game
		this.endGame();
		
		//Get the list of all users
		List<User> users = userDAO.getAllUsers();
		if (users.size() < 10) {
			String msg = "You need at least 10 players to play the game. There are only " + users.size() + ".";
			throw new InsufficientPlayersException(msg);
		}
		
		//Shuffle the users and calculate the number of werewolves
		Collections.shuffle(users);
		int werewolvesCutoff = (int) Math.floor((double) users.size()/10*3);
		
		//Add the new players to the database
		int i;
		User u; //A temporary pointer
		for (i = 0; i < werewolvesCutoff; i++) {
			u = users.get(i);
			Player p = new Player();
			p.setIsDead(false);
			p.setUserID(u.getId());
			p.setIsWerewolf(true);
			playerDAO.insertPlayer(p);
		}
		for (i = werewolvesCutoff; i < users.size(); i++) {
			u = users.get(i);
			Player p = new Player();
			p.setIsDead(false);
			p.setUserID(u.getId());
			p.setIsWerewolf(false);
			playerDAO.insertPlayer(p);
		}
		
		//Update the game in the database
		g.setRunning(true);
		Date start = new Date();
		start.setTime(start.getTime());
		g.setCreatedDate(start);
		gameDAO.updateGame(g);

		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		long aDay = now*g.getDayNightFreq()*60*1000;
		//Set to turn on night and count votes at a rate of
		//dayNightFreq*2 with a delay of dayNightFreq*2
		myScheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {endDay();}
		}, new Date(aDay*2), aDay*2);
		
		//Set to turn off night at a rate of dayNightFreq*2
		//with a delay of dayNightFreq
		myScheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {endNight();}
		}, new Date(aDay), aDay*2);
		
		//Set the current state to day
		this.isNight = false;
	}
	
	
	public void placeVote(Player voter, Player accused)
			throws TimeOfDayException, InappropriatePlayerException {
		if (isNight()) {
			String msg = "You can only vote during the day.";
			throw new TimeOfDayException(msg);
		}
		if (voter.getIsDead()) {
			String msg = "You can only vote if you're alive!";
			throw new InappropriatePlayerException(msg);
		}
		if (accused.getIsDead()) {
			String msg = "You can only vote on alive players!";
			throw new InappropriatePlayerException(msg);
		}
		voter.setVotedFor(accused.getId());
		playerDAO.updatePlayer(voter);
	}
	
	private boolean isNight() {
		return this.isNight;
	}
	
	public void endGame() {
		Game g = gameDAO.getGame();
		if (g == null) {
			g = new Game();
		}
		g.setRunning(false);
		gameDAO.updateGame(g);
		
		playerDAO.removeAllPlayers();
		casualtyDAO.removeAllCasualties();
	}
	
	private void endNight() {
		this.isNight = false;
	}
	
	private void endDay() {
		//Turn to night
		this.isNight = true;
		
		//TODO: There is a much more efficient way
		//to implement this. This is linear and dumb.
		//On the other hand, for the numbers we're dealing
		//with it doesn't really matter.
		
		//Map votes to Players
		List<Player> players = playerDAO.getAllAlive();
		Map<String,Integer> votes = new HashMap<>();
		for (Player player : players) {
			if (player.getVotedFor() != null) {
				Integer last = votes.get(player.getVotedFor());
				if (last == null) {
					votes.put(player.getVotedFor(), 1);
				}
				else {
					votes.put(player.getVotedFor(),
							votes.get(player.getVotedFor())+1);
				}					
			}
		}
		
		//Find the player with the most votes
		String mostVotes = "";
		Integer mostVotesNum = 0;
		for (String id : votes.keySet()) {
			if (votes.get(id) > mostVotesNum) {
				mostVotes = id;
				mostVotesNum = votes.get(id);
			}
		}
		
		//Kill that player and, if he/she was a werewolf,
		//reward the townspeople that voted for him/her
		try {
			//logger.info("mostVotes: "+mostVotes);
			Player accused = playerDAO.getPlayerByID(mostVotes);
			doKill(accused);
			logger.info("Back in endDay(). isDead is: "+playerDAO.getPlayerByID(accused.getId()).getIsDead());
			//logger.info(accused.getId()+": "+accused.getIsDead());
			//accused.setIsDead(true);
			//playerDAO.updatePlayer(accused);
			Player p = playerDAO.getPlayerByID(mostVotes);
			logger.info(p.getId()+": "+p.getIsDead());
			if (accused.getIsWerewolf()) {
				rewardTownspeople(mostVotes);
			}
		} catch (NoPlayerFoundException e) {
			logger.info("asdf");
			e.printStackTrace();
		}
		
		
		//Set all votedFor to null
		for (Player player : playerDAO.getAllPlayers()) {
			player.setVotedFor(null);
			playerDAO.updatePlayer(player);
		}
		
		//Check end conditions for the game
		if (playerDAO.getNumAliveWerewolves() >= playerDAO.getNumAliveTownspeople()) {
			rewardWinners(true);
			endGame();
		}
		if (playerDAO.getNumAliveWerewolves() == 0) {
			rewardWinners(false);
			endGame();
		}
	}
	
	private void rewardTownspeople(String votedFor) {
		int scoreIncrement = 
				scoreConfigDAO.getScoreForScoreType(ScoreType.TOWNSPERSON_CORRECT_VOTE);
		User u; //Temporary pointer
		for (Player player : playerDAO.getAllAlive()) {
			if (player.getVotedFor() == votedFor) {
				try {
					u = userDAO.getUserByUserID(player.getUserID());
					u.setScore(u.getScore()+scoreIncrement);
					userDAO.updateUser(u);
				} catch (NoUserFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void rewardWinners(boolean werewolvesWin) {
		ScoreType type = werewolvesWin ? ScoreType.WEREWOLF_WIN : ScoreType.TOWNSPERSON_WIN; 
		int scoreIncrement = scoreConfigDAO.getScoreForScoreType(type);
		User u; //Temporary pointer
		for (Player player : playerDAO.getAllAlive()) {
			if (player.getIsWerewolf() == werewolvesWin) {
				try {
					u = userDAO.getUserByUserID(player.getUserID());
					u.setScore(u.getScore()+scoreIncrement);
					userDAO.updateUser(u);
				} catch (NoUserFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Scheduled(fixedDelay=5000)
	public void checkGameOperation() {
		//logger.info("Doing stuff!!!");
	}
	
	public void clearAllData(User u) {
		userDAO.removeAllUsersExcept(u);
		playerDAO.removeAllPlayers();
		gameDAO.removeAllGames();
		casualtyDAO.removeAllCasualties();
	}

	public void forceNight() {
		this.endDay();
	}
	
	private double distance(Player p1, Player p2) {
		//Strange that MongoDB doesn't do this for me, but
		//using the Haversine formula...
		
		//Convert degrees to radians
		double radians = Math.PI/180;
		double p1Lng = p1.getLng()*radians;
		double p1Lat = p1.getLat()*radians;
		double p2Lng = p2.getLng()*radians;
		double p2Lat = p2.getLat()*radians;
	    
		//Do the calculations
	    double deltalng = p2Lng - p1Lng;
	    double deltalat = p2Lat - p1Lat;
	    double a = Math.pow(Math.sin(deltalat/2),2)+
	    		Math.cos(p1Lat)*Math.cos(p2Lat)*Math.pow(Math.sin(deltalng/2),2);
	    double c = Math.asin(Math.sqrt(a))*2;
	    
	    //Convert to meters and return
	    return c*6367*1000;
	}
	
	private void doKill(Player p) {
		logger.info("Killing player with id: "+p.getId());
		try {
			logger.info("   isDead is: "+playerDAO.getPlayerByID(p.getId()).getIsDead());
		} catch (NoPlayerFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.setIsDead(true);
		playerDAO.updatePlayer(p);
		logger.info("   Updating player in the database...");
		try {
			logger.info("   isDead is: "+playerDAO.getPlayerByID(p.getId()).getIsDead());
		} catch (NoPlayerFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void doKillID(String id) throws NoPlayerFoundException {
		Player p = playerDAO.getPlayerByID(id);
		p.setIsDead(true);
		playerDAO.updatePlayer(p);
	}
	
}