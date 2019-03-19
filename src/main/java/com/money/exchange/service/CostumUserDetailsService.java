package com.money.exchange.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.CustomUserDetails;
import com.money.exchange.entity.Users;
import com.money.exchange.repository.UsersRepository;

@Service
public class CostumUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usersRepository
                .findByUsername(username)
                .map(u -> new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                u.isActive(),
                u.isActive(),
                u.isActive(),
                u.isActive(),
                AuthorityUtils.createAuthorityList(
                        u.getRoles()
                                .stream()
                                .map(r -> "ROLE_" + r.getRole().toUpperCase())
                                .collect(Collectors.toList())
                                .toArray(new String[]{}))))
                .orElseThrow(() -> new UsernameNotFoundException("No user with "
                        + "the name " + username + "was found in the database"));
//		Optional<Users> user = usersRepository.findByUsername(username);
//		
//		user.orElseThrow(() -> new UsernameNotFoundException("Username not present in database"));
//		return user.map(users -> new CustomUserDetails(users))
//				.get();
	}

	
	
}
