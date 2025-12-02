package com.example.academic.repository;

import com.example.academic.model.Group;
import com.example.academic.exception.DatabaseException;

import java.util.List;

/**
 * Interface pour le repository Group (Abstraction)
 */
public interface IGroupRepository {
    Group findById(String id) throws DatabaseException;
    Group findByName(String name) throws DatabaseException;
    void save(Group group);
    void update(Group group) throws DatabaseException;
    void delete(String id) throws DatabaseException;
    List<Group> findAll();
    boolean existsByName(String name);
}

