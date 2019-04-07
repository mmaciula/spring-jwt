package com.github.springjwt.security.jwt.service;

import com.github.springjwt.domain.user.User;
import com.github.springjwt.security.UserFactory;
import com.github.springjwt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DaoUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username.trim().isEmpty()){
            throw new IllegalArgumentException("No login email available");
        }

        User userFromDatabase = userRepository.findByEmail(username);

        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database.");
        } else {
            return UserFactory.create(userFromDatabase);
        }
    }

    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
}