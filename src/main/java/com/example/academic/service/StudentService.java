package com.example.academic.service;

import com.example.academic.enums.UserRole;
import com.example.academic.exception.UserNotFoundException;
import com.example.academic.model.Student;
import com.example.academic.model.User;
import com.example.academic.repository.IUserRepository;
import com.example.academic.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des étudiants
 */
public class StudentService {
    private final IUserRepository userRepository;

    public StudentService() {
        this.userRepository = new UserRepository();
    }

    public Student createStudent(String firstName, String lastName, String groupId) {
        UserService userService = new UserService();
        User user = userService.createUser(firstName, lastName, UserRole.STUDENT);
        
        Student student = (Student) user;
        if (groupId != null && !groupId.isEmpty()) {
            student.setGroupId(groupId);
            try {
                userRepository.update(student);
            } catch (UserNotFoundException e) {
                throw new RuntimeException("Erreur lors de la mise à jour de l'étudiant", e);
            }
        }
        
        return student;
    }

    public Student findById(String id) throws UserNotFoundException {
        User user = userRepository.findById(id);
        if (user instanceof Student) {
            return (Student) user;
        }
        throw new UserNotFoundException("User avec id '" + id + "' n'est pas un étudiant");
    }

    public void assignToGroup(String studentId, String groupId) {
        try {
            Student student = findById(studentId);
            student.setGroupId(groupId);
            userRepository.update(student);
        } catch (UserNotFoundException e) {
            throw new IllegalArgumentException("Étudiant non trouvé: " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByGroup(String groupId) {
        return getAllStudents().stream()
                .filter(student -> groupId.equals(student.getGroupId()))
                .collect(Collectors.toList());
    }
}

