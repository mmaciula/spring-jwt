package com.github.springjwt.security.controller;

import com.github.springjwt.security.jwt.AuthRequest;
import com.github.springjwt.security.jwt.TokenUtil;
import com.github.springjwt.security.jwt.service.AuthenticationServiceJWT;
import com.github.springjwt.security.jwt.service.DaoUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String header;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    //@Qualifier("jwtUserDetailsService")
    private DaoUserDetailService userDetailService;

    @PostMapping(value = "${jwt.authentication.path}")
    public ResponseEntity<AuthenticationServiceJWT> createAuthenticationToken(@RequestBody AuthRequest
                                                                                          authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailService
                .loadUserByUsername(authenticationRequest.getUsername());
        String token = tokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationServiceJWT(token));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
