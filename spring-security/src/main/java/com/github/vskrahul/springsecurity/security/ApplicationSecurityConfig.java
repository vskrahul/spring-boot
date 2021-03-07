package com.github.vskrahul.springsecurity.security;

import static com.github.vskrahul.springsecurity.security.ApplicationUserRole.STUDENT;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.github.vskrahul.springsecurity.service.ApplicationUserDetailsService;

@Configuration
@EnableWebSecurity(debug = false)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	PasswordEncoder passwordEncoder;
	
	private ApplicationUserDetailsService applicationUserDetailsService;
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
			ApplicationUserDetailsService applicationUserDetailsService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.applicationUserDetailsService = applicationUserDetailsService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and() //--> enable this when the client deals with cookies based
				.csrf().disable()
		        .authorizeRequests()
				.antMatchers("/", "index", "/css/*", "/js/*") 
				.permitAll()
				.antMatchers("/api/**").hasRole(STUDENT.name())
				/*.antMatchers(HttpMethod.POST, "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission(), STUDENT_WRITE.getPermission())
				.antMatchers(HttpMethod.PUT, "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission(), STUDENT_WRITE.getPermission())
				.antMatchers(HttpMethod.DELETE, "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission(), STUDENT_WRITE.getPermission())
				.antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())*/
				.anyRequest()
				.authenticated()
				.and()
				//.httpBasic();
				.formLogin()
					.loginPage("/login")
					.permitAll()
					.defaultSuccessUrl("/courses", true)
					.passwordParameter("password")
					.usernameParameter("username")
				.and()
				.rememberMe() // defaults to 2 weeks
					.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
					.key("secure")
					.rememberMeParameter("remember-me")
				.and()
				.logout()
					.logoutUrl("/logout")
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
					.clearAuthentication(true)
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID", "remember-me", "XSRF-TOKEN")
					.logoutSuccessUrl("/login");
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
