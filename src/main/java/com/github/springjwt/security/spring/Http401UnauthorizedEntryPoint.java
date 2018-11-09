package com.github.springjwt.security.spring;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Returns 401 error code (Unauthorized) when authentication fails, overriding default Spring’s redirecting
 */

@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long SERIAL_VERSION_UID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied 401");
    }
}
