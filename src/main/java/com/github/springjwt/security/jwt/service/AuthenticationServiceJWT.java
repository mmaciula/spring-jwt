package com.github.springjwt.security.jwt.service;

import com.github.springjwt.security.PrincipalUser;
import com.github.springjwt.security.jwt.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceJWT {
    @Autowired
    private TokenManager tokenManager;

    public boolean authenticateWithJwtToken(final String token){
        PrincipalUser principalUser = tokenManager.getUserDetails(token);

        if (principalUser != null){
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principalUser,
                    null, principalUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            return true;
        }

        return false;
    }
}