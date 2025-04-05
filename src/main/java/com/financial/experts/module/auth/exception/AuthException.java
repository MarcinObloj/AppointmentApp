package com.financial.experts.module.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public AuthException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
    }

    public AuthException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public static AuthException userNotFound() {
        return new AuthException("User not found", HttpStatus.NOT_FOUND, "AUTH-001");
    }

    public static AuthException invalidCredentials() {
        return new AuthException("Invalid email or password", HttpStatus.UNAUTHORIZED, "AUTH-002");
    }

    public static AuthException accountNotVerified() {
        return new AuthException("Account not verified", HttpStatus.FORBIDDEN, "AUTH-003");
    }

    public static AuthException emailAlreadyExists() {
        return new AuthException("Email already registered", HttpStatus.CONFLICT, "AUTH-004");
    }

    public static AuthException invalidToken() {
        return new AuthException("Invalid or expired token", HttpStatus.UNAUTHORIZED, "AUTH-005");
    }

    public static AuthException unauthorized() {
        return new AuthException("Unauthorized access", HttpStatus.UNAUTHORIZED, "AUTH-006");
    }

    public static AuthException accessDenied() {
        return new AuthException("Access denied", HttpStatus.FORBIDDEN, "AUTH-007");
    }
}