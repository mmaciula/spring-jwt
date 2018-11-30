package com.github.springjwt.security.jwt.service;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AuthenticationServiceJWT implements Serializable {
    private static final long serialVersionUID = 1250166508152483573L;
    private String token;

    public AuthenticationServiceJWT() {
        super();
    }

    public AuthenticationServiceJWT(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}