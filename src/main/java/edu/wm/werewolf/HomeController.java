package edu.wm.werewolf;

import java.security.Principal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wm.werewolf.domain.BasicPlayer;
import edu.wm.werewolf.domain.Game;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.domain.Role;
import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.InappropriatePlayerException;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;
import edu.wm.werewolf.exceptions.NoUserFoundException;
import edu.wm.werewolf.service.GameService;
import edu.wm.werewolf.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired GameService gameService;
	@Autowired UserService userService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/nearby", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getNearbyPlayers(Principal principal) {
		JSONResponse r = new JSONResponse();
		
		try {
			Player p = gameService.getPlayerByUserID(userService.getUserByUsername(principal.getName()).getId());
			List<BasicPlayer> nearby = gameService.getNearbyPlayers(p);
			r.setObject(nearby);
			r.setStatus("OK");
		} catch(Exception e) {
			e.printStackTrace();
			r.setStatus(e.getMessage());
			r.setObject(e);
		}
		
		return r;
	}
	
	@RequestMapping(value = "/killable", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getKillablePlayers(Principal principal) {
		JSONResponse r = new JSONResponse();
		
		try {
			Player p = gameService.getPlayerByUserID(userService.getUserByUsername(principal.getName()).getId());
			List<BasicPlayer> killable = gameService.getKillablePlayers(p);
			r.setObject(killable);
			r.setStatus("OK");
		} catch(Exception e) {
			e.printStackTrace();
			r.setStatus(e.getMessage());
			r.setObject(e);
		}
		
		return r;
	}
	
	@RequestMapping(value = "/restart", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public JSONResponse restartGame(@RequestParam("dayNightFreq") String dayNightFreqStr,
			@RequestParam("scentRange") String scentRangeStr, @RequestParam("killRange") String killRangeStr,
			Principal principal) {
		JSONResponse r = new JSONResponse();
		try {
			int dayNightFreq = Integer.parseInt(dayNightFreqStr);
			float scentRange = Float.parseFloat(scentRangeStr);
			float killRange = Float.parseFloat(killRangeStr);
			Game g = new Game(null, dayNightFreq, scentRange, killRange);
			gameService.restartGame(g);
			r.setStatus("OK");
		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(e.getMessage());
			r.setObject(e);
		}
		return r;
	}
	
	@RequestMapping(value = "/players/alive", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getAllAlive() {
		JSONResponse r = new JSONResponse();
		
		try {
			List<BasicPlayer> alive = gameService.getAllAlive();
			r.setObject(alive);
			r.setStatus("OK");
		} catch(Exception e) {
			r.setObject(e);
			r.setStatus(e.getMessage());
		}
		
		return r;
	}
	
	@RequestMapping(value = "/vote", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public JSONResponse voteFor(@RequestParam("id") String id,
			Principal principal) {
		JSONResponse r = new JSONResponse();
		try {
			Player voter = gameService.getPlayerByUserID(userService.getUserByUsername(principal.getName()).getId());
			Player accused = gameService.getPlayerByID(id);
			gameService.placeVote(voter, accused);
			r.setStatus("OK");
		} catch (Exception e) {
			r.setStatus(e.getMessage());
			r.setObject(e);
		}
		return r;
	}	
	
	@RequestMapping(value = "/players", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getAllPlayers() {
		JSONResponse r = new JSONResponse();
		
		try {
			List<BasicPlayer> alive = gameService.getAllPlayers();
			r.setObject(alive);
			r.setStatus("OK");
		} catch(Exception e) {
			r.setObject(e);
			r.setStatus(e.getMessage());
		}
		
		return r;
	}
	
	@RequestMapping(value = "/players/me", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getThisPlayer(Principal principal) {
		try {
			User u = userService.getUserByUsername(principal.getName());
			Player p = gameService.getPlayerByUserID(u.getId());
			return new JSONResponse("OK",p);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONResponse(e.getMessage(),e);
		}
	}
	
	@RequestMapping(value = "/players/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getBasicPlayerByID(@PathVariable String id, Principal principal) {
		try {
			BasicPlayer p = gameService.getBasicPlayerByID(id);
			return new JSONResponse("OK",p);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONResponse(e.getMessage(),e);
		}
	}
	
	@RequestMapping(value = "/players/full/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getPlayerByID(@PathVariable String id, Principal principal) {
		try {
			Player p = gameService.getPlayerByID(id);
			return new JSONResponse("OK",p);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONResponse(e.getMessage(),e);
		}
	}
	
	@RequestMapping(value = "/location", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public JSONResponse updateLocation(@RequestParam("lat") String latStr,
			@RequestParam("lng") String lngStr, Principal principal) {
		JSONResponse r = new JSONResponse();
		
		try {
			double lat = Double.parseDouble(latStr);
			double lng = Double.parseDouble(lngStr);
			Player p = gameService.getPlayerByUserID(userService.getUserByUsername(principal.getName()).getId());
			gameService.updateLocation(p, lat, lng);
			r.setStatus("OK");
		} catch (Exception e) {
			r.setStatus(e.getMessage());
			r.setObject(e);
			e.printStackTrace();
		}
		
		return r;
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getUsers() {
		JSONResponse r = new JSONResponse();
		
		r.setObject(userService.getAllUsers());
		r.setStatus("OK");
		
		return r;
	}
	
	@RequestMapping(value = "/users/{name}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getUserByUsername(@PathVariable String name) {
		JSONResponse r = new JSONResponse();
		
		try {
			User u = userService.getUserByUsername(name);
			r.setObject(u);
			r.setStatus("OK");
		} catch (Exception e) {
			r.setStatus(e.getMessage());
		}
		
		return r;
	}

	@RequestMapping(value = "/users/me", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse getThisUser(Principal principal) {
		JSONResponse r = new JSONResponse();
		
		try {
			User u = userService.getUserByUsername(principal.getName());
			r.setObject(u);
			r.setStatus("OK");
		} catch (Exception e) {
			r.setStatus(e.getMessage());
		}
		
		return r;
	}
	
	@RequestMapping(value = "/users/add", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public JSONResponse addUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("userName") String userName, @RequestParam("password") String password,
			@RequestParam("imageURL") String imageURL) {
		JSONResponse r = new JSONResponse();
		
		try {
			List<Role> roles = new ArrayList<Role>();
			roles.add(Role.ROLE_USER);
			User u = new User(null, firstName, lastName, userName, password, imageURL, roles, 0);
			userService.insertUser(u);
			r.setStatus("OK");
		} catch (Exception e) {
			r.setStatus(e.getMessage());
			r.setObject(e);
		}
		
		return r;
	}
	
	@RequestMapping(value = "/kill", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public JSONResponse updateLocation(@RequestParam("id") String id,
			Principal principal) {
		JSONResponse r = new JSONResponse();
		try {
			Player killer = gameService.getPlayerByUserID(userService.getUserByUsername(principal.getName()).getId());
			Player victim = gameService.getPlayerByID(id);
			gameService.makeKill(killer, victim);
			r.setStatus("OK");
		} catch (Exception e) {
			r.setStatus(e.getMessage());
			r.setObject(e);
		}
		return r;
	}
	
	@RequestMapping(value = "/clearAllData", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse clearAll(Principal principal) throws NoUserFoundException {
		gameService.clearAllData(userService.getUserByUsername(principal.getName()));
		return new JSONResponse("OK",null);
	}	
	
	@RequestMapping(value = "/forceNight", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public JSONResponse forceNight() {
		gameService.forceNight();
		
		return new JSONResponse("OK",null);
	}
	
}