package com.github.springjwt.domain.user;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserRepository {
    public User findByEmail(String email){
        User user = new User();
        user.setEmail(email);
        user.setId(UUID.randomUUID());
        user.setPassword("5baa61e4c");
        user.setSalt(null);
        user.setUsername("User1");

        return user;
    }
}