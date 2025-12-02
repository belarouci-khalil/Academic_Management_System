package com.example.academic.service;

import com.example.academic.enums.UserRole;
import com.example.academic.exception.InvalidCredentialsException;
import com.example.academic.exception.UserNotFoundException;
import com.example.academic.model.User;
import com.example.academic.repository.IUserRepository;
import com.example.academic.repository.UserRepository;

/**
 * Service pour la gestion des utilisateurs
 */
public class UserService {
    private final IUserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Authentifie un utilisateur
     */
    public User authenticate(String username, String password) throws InvalidCredentialsException, UserNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidCredentialsException("Username ne peut pas être vide");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidCredentialsException("Password ne peut pas être vide");
        }

        try {
            User user = userRepository.findByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new InvalidCredentialsException("Mot de passe incorrect");
            }
            return user;
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException("Username ou mot de passe incorrect");
        }
    }

    /**
     * Crée un utilisateur avec génération automatique username/password
     */
    public User createUser(String firstName, String lastName, UserRole role) {
        String username = firstName; // username = firstName
        String password = lastName;   // password = lastName

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' existe déjà");
        }

        User user;
        switch (role) {
            case ADMIN:
                user = new com.example.academic.model.Administrator(username, password, firstName, lastName);
                break;
            case TEACHER:
                user = new com.example.academic.model.Teacher(username, password, firstName, lastName);
                break;
            case STUDENT:
                user = new com.example.academic.model.Student(username, password, firstName, lastName);
                break;
            default:
                throw new IllegalArgumentException("Role invalide: " + role);
        }

        userRepository.save(user);
        return user;
    }

    public User findById(String id) throws UserNotFoundException {
        return userRepository.findById(id);
    }

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(String id) throws UserNotFoundException {
        userRepository.delete(id);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

