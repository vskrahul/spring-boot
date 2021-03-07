package com.github.vskrahul.springsecurity.security;

import static com.github.vskrahul.springsecurity.security.ApplicationUserPermission.COURSE_READ;
import static com.github.vskrahul.springsecurity.security.ApplicationUserPermission.COURSE_WRITE;
import static com.github.vskrahul.springsecurity.security.ApplicationUserPermission.STUDENT_READ;
import static com.github.vskrahul.springsecurity.security.ApplicationUserPermission.STUDENT_WRITE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;


public enum ApplicationUserRole {

	ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
	ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ)),
	STUDENT(Sets.newHashSet());
	
	private final Set<ApplicationUserPermission> permissions;
	
	private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<SimpleGrantedAuthority> grantedAuthorities() {
		Set<SimpleGrantedAuthority> permissions = this.permissions.stream()
						.map(p -> new SimpleGrantedAuthority(p.getPermission()))
						.collect(Collectors.toSet());
		permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return permissions;
	}
}
