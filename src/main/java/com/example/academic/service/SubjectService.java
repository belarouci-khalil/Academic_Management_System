package com.example.academic.service;

import com.example.academic.exception.DatabaseException;
import com.example.academic.model.Subject;
import com.example.academic.repository.ISubjectRepository;
import com.example.academic.repository.SubjectRepository;

import java.util.List;

/**
 * Service pour la gestion des matières
 */
public class SubjectService {
    private final ISubjectRepository subjectRepository;

    public SubjectService() {
        this.subjectRepository = new SubjectRepository();
    }

    public Subject createSubject(String name, String code, String description) {
        if (subjectRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Un subject avec le code '" + code + "' existe déjà");
        }

        Subject subject = new Subject(name, code, description);
        subjectRepository.save(subject);
        return subject;
    }

    public Subject findById(String id) throws DatabaseException {
        return subjectRepository.findById(id);
    }

    public Subject findByCode(String code) throws DatabaseException {
        return subjectRepository.findByCode(code);
    }

    public void updateSubject(Subject subject) throws DatabaseException {
        subjectRepository.update(subject);
    }

    public void deleteSubject(String id) throws DatabaseException {
        subjectRepository.delete(id);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
}

