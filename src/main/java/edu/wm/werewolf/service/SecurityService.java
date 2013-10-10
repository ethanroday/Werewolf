package edu.wm.werewolf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.wm.werewolf.domain.Role;
import edu.wm.werewolf.domain.User;
import edu.wm.werewolf.exceptions.NoUserFoundException;

public class SecurityService implements UserDetailsService {

	@Autowired UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		User u;
		try {
			u = userService.getUserByUsername(username);
		} catch (NoUserFoundException e) {
			throw new UsernameNotFoundException(username);
		}

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		return new org.springframework.security.core.userdetails.User(
				u.getUserName(),
				u.getPassword(),
				enabled,
				accountNonExpired,
				credentialsNonExpired,
				accountNonLocked,
				getAuthorities(u));
	}

	public Collection<SimpleGrantedAuthority> getAuthorities(User u) {
		return getGrantedAuthorities(u);
	}

	public List<SimpleGrantedAuthority> getGrantedAuthorities(User u) {

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : u.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.toString()));
		}

		return authorities;
	}

}
