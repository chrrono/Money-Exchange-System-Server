package com.money.exchange.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.Users;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.UsersRepository;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }
    
    public WorkPlace findWorkPlaceInfoAboutUser(String username) {
    	return usersRepository.findWorkPlaceInfoAboutUser(username);
    }
    
    public List<Users> findUserNameByWorkPlace(String workPlaceName){
    	return usersRepository.findUserNameByWorkPlace(workPlaceName);
    }

}
