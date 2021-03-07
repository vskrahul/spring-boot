package com.github.vskrahul.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.vskrahul.springsecurity.dao.ApplicationUserDao;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

	private ApplicationUserDao applicationUserDao;
	
	@Autowired
	public ApplicationUserDetailsService(@Qualifier("fake") ApplicationUserDao applicationUserDao) {
		super();
		this.applicationUserDao = applicationUserDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return applicationUserDao
						.selectUserDetailsByName(username)
						.orElseThrow(() -> 
							new UsernameNotFoundException(String.format("%s doesn't exist", username)));
	}

}
