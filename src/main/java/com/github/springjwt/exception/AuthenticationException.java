package com.github.springjwt.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable clause) {
        super(message, clause);
    }
}
