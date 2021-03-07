package com.github.vskrahul.springsecurity.dao;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface ApplicationUserDao {

	Optional<UserDetails> selectUserDetailsByName(String username);
}
