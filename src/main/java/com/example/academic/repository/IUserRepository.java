package com.example.academic.repository;

import com.example.academic.model.User;
import com.example.academic.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface pour le repository User (Abstraction)
 */
public interface IUserRepository {
    User findByUsername(String username) throws UserNotFoundException;
    User findById(String id) throws UserNotFoundException;
    void save(User user);
    void update(User user) throws UserNotFoundException;
    void delete(String id) throws UserNotFoundException;
    List<User> findAll();
    boolean existsByUsername(String username);
}

