package com.money.exchange.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails extends Users implements UserDetails {

	public CustomUserDetails(final Users users) {
			super(users);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return super.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_"+role.getRole()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		
		return null;
	}

	@Override
	public String getUsername() {
		
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

	
}
