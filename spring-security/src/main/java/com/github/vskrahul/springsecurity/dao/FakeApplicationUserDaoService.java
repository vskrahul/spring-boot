package com.github.vskrahul.springsecurity.dao;

import static com.github.vskrahul.springsecurity.security.ApplicationUserRole.ADMIN;
import static com.github.vskrahul.springsecurity.security.ApplicationUserRole.ADMINTRAINEE;
import static com.github.vskrahul.springsecurity.security.ApplicationUserRole.STUDENT;

import java.util.Optional;
import java.util.Set;

import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.github.vskrahul.springsecurity.auth.ApplicationUser;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
		super();
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<UserDetails> selectUserDetailsByName(String username) {
		return this.getUserDetails()
					.stream()
					.filter(user -> user.getUsername().equals(username))
					.findFirst();
	}
	
	private Set<UserDetails> getUserDetails() {
		Set<UserDetails> userDetails = Sets.newSet(
					new ApplicationUser(
							ADMIN.grantedAuthorities(), 
							"admin", 
							this.passwordEncoder.encode("admin"), 
							true, 
							true, 
							true, 
							true),
					
					new ApplicationUser(ADMINTRAINEE.grantedAuthorities(), 
							"admintrainee", 
							this.passwordEncoder.encode("admin"), 
							true, 
							true, 
							true, 
							true),
					
					new ApplicationUser(STUDENT.grantedAuthorities(), 
							"rahul", 
							this.passwordEncoder.encode("rahul"), 
							true, 
							true, 
							true, 
							true)
				);
		
		return userDetails;
	}

}
