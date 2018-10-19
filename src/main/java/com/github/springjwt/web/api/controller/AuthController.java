package com.github.springjwt.web.api.controller;

import com.github.springjwt.security.jwt.service.AuthenticationServiceUsernamePassword;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
    @Value("${jwt.result}")
    private String defaultTokenResponse;
    @Autowired
    private AuthenticationServiceUsernamePassword authUserPassword;

    @RequestMapping(value = "/authentication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(String email, String password, HttpServletRequest request,
                                               HttpServletResponse response) {
        if (email != null && password != null){
            try {
                SignedJWT token = authUserPassword.authenticate(email, password);

                if (token != null){
                    return new ResponseEntity<String>(String.format(defaultTokenResponse, token.serialize()),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
                }
            } catch (BadCredentialsException badCredentials) {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
    }
}