package com.example.academic.repository;

import com.example.academic.model.Subject;
import com.example.academic.exception.DatabaseException;

import java.util.List;

/**
 * Interface pour le repository Subject (Abstraction)
 */
public interface ISubjectRepository {
    Subject findById(String id) throws DatabaseException;
    Subject findByCode(String code) throws DatabaseException;
    void save(Subject subject);
    void update(Subject subject) throws DatabaseException;
    void delete(String id) throws DatabaseException;
    List<Subject> findAll();
    boolean existsByCode(String code);
}

