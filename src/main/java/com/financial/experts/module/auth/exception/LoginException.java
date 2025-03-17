package com.financial.experts.module.auth.exception;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}