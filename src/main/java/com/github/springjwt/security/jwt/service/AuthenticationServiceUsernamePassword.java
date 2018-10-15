package com.github.springjwt.security.jwt.service;

import com.github.springjwt.security.PrincipalUser;
import com.github.springjwt.security.jwt.TokenManager;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceUsernamePassword {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceUsernamePassword.class);
    @Autowired
    @Qualifier("customAuthenticationManager")
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;

    public SignedJWT authenticate(final String email, final String password){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication.getPrincipal() != null) {
                return tokenManager.createNewToken((PrincipalUser) authentication.getPrincipal());
            }
        } catch (AuthenticationException authException) {
            LOGGER.debug("Authentication failed for user:\"" + email + ".\" Reason " + authException.getClass());
        }

        return null;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
}