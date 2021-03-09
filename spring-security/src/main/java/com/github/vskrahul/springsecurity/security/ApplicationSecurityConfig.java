	package com.github.vskrahul.springsecurity.security;

import static com.github.vskrahul.springsecurity.security.ApplicationUserRole.STUDENT;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.vskrahul.springsecurity.jwt.JwtConfig;
import com.github.vskrahul.springsecurity.jwt.JwtTokenVerifier;
import com.github.vskrahul.springsecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.github.vskrahul.springsecurity.service.ApplicationUserDetailsService;

@Configuration
@EnableWebSecurity(debug = false)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	PasswordEncoder passwordEncoder;
	private ApplicationUserDetailsService applicationUserDetailsService;
	private final SecretKey secretKey;
	private final JwtConfig jwtConfig;

	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
			ApplicationUserDetailsService applicationUserDetailsService, SecretKey secretKey, JwtConfig jwtConfig) {
		this.passwordEncoder = passwordEncoder;
		this.applicationUserDetailsService = applicationUserDetailsService;
		this.secretKey = secretKey;
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(super.authenticationManager(), jwtConfig, secretKey))
				.addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
		        .authorizeRequests()
				.antMatchers("/", "index", "/css/*", "/js/*") 
				.permitAll()
				.antMatchers("/api/**").hasRole(STUDENT.name())
				.anyRequest()
				.authenticated();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(this.passwordEncoder);
		provider.setUserDetailsService(this.applicationUserDetailsService);
		return provider;
	}
	
}
