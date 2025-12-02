package com.example.academic.repository;

import com.example.academic.model.Grade;
import com.example.academic.exception.DatabaseException;

import java.util.List;

/**
 * Interface pour le repository Grade (Abstraction)
 */
public interface IGradeRepository {
    Grade findById(String id) throws DatabaseException;
    void save(Grade grade);
    void update(Grade grade) throws DatabaseException;
    void delete(String id) throws DatabaseException;
    List<Grade> findByStudentId(String studentId);
    List<Grade> findBySubjectId(String subjectId);
    List<Grade> findByTeacherId(String teacherId);
    List<Grade> findByStudentAndSubject(String studentId, String subjectId);
    List<Grade> findAll();
}

