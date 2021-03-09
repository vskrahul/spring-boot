package com.github.vskrahul.springsecurity.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.common.net.HttpHeaders;

import lombok.Data;

@Data
@Configuration
public class JwtConfig {

	@Value("application.jwt.secretKey")
	private String secretKey;
	
	@Value("application.jwt.tokenPrefix")
	private String tokenPrefix;
	
	@Value("application.jwt.tokenExpirationDay")
	private int tokenExpirationDays;
	
	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}
}
