package com.github.springjwt.security.controller;

import com.github.springjwt.security.jwt.JwtUser;
import com.github.springjwt.security.jwt.TokenUtil;
import com.github.springjwt.security.jwt.service.DaoUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private TokenUtil jwtTokenUtil;
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private DaoUserDetailService userDetailService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailService.loadUserByUsername(username);

        return user;
    }
}
