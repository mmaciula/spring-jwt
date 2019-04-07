package com.github.springjwt.security.controller;

import com.github.springjwt.security.jwt.JwtUser;
import com.github.springjwt.security.jwt.TokenUtil;
import com.github.springjwt.security.jwt.service.DaoUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private TokenUtil jwtTokenUtil;
    @Autowired
    //@Qualifier("jwtUserDetailsService")
    private DaoUserDetailService userDetailService;

    @GetMapping(value = "user")
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        return (JwtUser) userDetailService.loadUserByUsername(username);
    }
}
