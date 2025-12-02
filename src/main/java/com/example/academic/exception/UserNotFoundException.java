package com.example.academic.exception;

/**
 * Exception personnalisée pour utilisateur non trouvé
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

