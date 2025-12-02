package com.example.academic.service;

import com.example.academic.enums.UserRole;
import com.example.academic.exception.UserNotFoundException;
import com.example.academic.model.Teacher;
import com.example.academic.model.User;
import com.example.academic.repository.IUserRepository;
import com.example.academic.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des enseignants
 */
public class TeacherService {
    private final IUserRepository userRepository;
    private final com.example.academic.repository.ISubjectRepository subjectRepository;

    public TeacherService() {
        this.userRepository = new UserRepository();
        this.subjectRepository = new com.example.academic.repository.SubjectRepository();
    }

    public Teacher createTeacher(String firstName, String lastName) {
        UserService userService = new UserService();
        User user = userService.createUser(firstName, lastName, UserRole.TEACHER);
        return (Teacher) user;
    }

    public Teacher findById(String id) throws UserNotFoundException {
        User user = userRepository.findById(id);
        if (user instanceof Teacher) {
            return (Teacher) user;
        }
        throw new UserNotFoundException("User avec id '" + id + "' n'est pas un enseignant");
    }

    public List<Teacher> getAllTeachers() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof Teacher)
                .map(user -> (Teacher) user)
                .collect(Collectors.toList());
    }

    // Pour MongoDB, on stocke les relations teacher-subject dans une collection séparée
    public void assignSubjectToTeacher(String teacherId, String subjectId) throws UserNotFoundException {
        findById(teacherId); // Vérifier que le teacher existe
        // Vérifier que le subject existe
        try {
            subjectRepository.findById(subjectId);
        } catch (com.example.academic.exception.DatabaseException e) {
            throw new IllegalArgumentException("Subject avec id '" + subjectId + "' non trouvé");
        }

        // Stocker la relation dans MongoDB
        com.mongodb.client.MongoCollection<org.bson.Document> collection = 
            com.example.academic.database.MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("teacher_subjects");

        // Vérifier si la relation existe déjà
        org.bson.Document existing = collection.find(
            new org.bson.Document("teacherId", teacherId)
                .append("subjectId", subjectId)
        ).first();

        if (existing == null) {
            org.bson.Document doc = new org.bson.Document("teacherId", teacherId)
                .append("subjectId", subjectId);
            collection.insertOne(doc);
        }
    }

    public List<String> getSubjectIdsByTeacher(String teacherId) {
        com.mongodb.client.MongoCollection<org.bson.Document> collection = 
            com.example.academic.database.MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("teacher_subjects");

        List<String> subjectIds = new ArrayList<>();
        for (org.bson.Document doc : collection.find(new org.bson.Document("teacherId", teacherId))) {
            subjectIds.add(doc.getString("subjectId"));
        }
        return subjectIds;
    }
}

