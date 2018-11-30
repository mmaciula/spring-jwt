package com.github.springjwt.security.jwt.filter;

import com.github.springjwt.security.jwt.TokenUtil;
import com.github.springjwt.security.jwt.service.DaoUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DaoUserDetailService userDetailService;
    private final String tokenHeader;
    private final TokenUtil tokenUtil;

    public AuthorizationTokenFilter(DaoUserDetailService userDetailService, TokenUtil tokenUtil,
                                    @Value("${jwt.header}") String tokenHeader) {
        this.userDetailService = userDetailService;
        this.tokenHeader = tokenHeader;
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Authentication for ", request.getRequestURL());

        final String requestHeader = request.getHeader(tokenHeader);

        String username = null;
        String authToken = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
            username = tokenUtil.getUsernameFromToken(authToken);
        } else {
            logger.warn("Bearer string not found!");
        }

        filterChain.doFilter(request, response);
    }
}
