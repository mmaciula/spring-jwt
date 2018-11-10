package com.github.springjwt.security.jwt.filter;

import com.github.springjwt.security.jwt.service.DaoUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public AuthorizationTokenFilter(DaoUserDetailService userDetailService, String tokenHeader) {
        this.userDetailService = userDetailService;
        this.tokenHeader = tokenHeader;
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
            // TODO get username from token (create TokenUtils class)
        }
    }
}
