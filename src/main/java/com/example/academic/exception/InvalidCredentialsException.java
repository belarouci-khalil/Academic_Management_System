package com.example.academic.exception;

/**
 * Exception pour credentials invalides
 */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

