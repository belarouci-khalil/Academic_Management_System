package com.example.academic.service;

import com.example.academic.exception.DatabaseException;
import com.example.academic.exception.UserNotFoundException;
import com.example.academic.model.Grade;
import com.example.academic.model.Student;
import com.example.academic.repository.IGradeRepository;
import com.example.academic.repository.GradeRepository;

import java.util.List;

/**
 * Service pour la gestion des notes
 */
public class GradeService {
    private final IGradeRepository gradeRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public GradeService() {
        this.gradeRepository = new GradeRepository();
        this.teacherService = new TeacherService();
        this.studentService = new StudentService();
    }

    /**
     * Ajoute une note avec vérification des permissions
     */
    public Grade addGrade(String studentId, String subjectId, String teacherId, double grade, String comment) 
            throws UserNotFoundException, DatabaseException {
        
        // Vérifier que le teacher enseigne ce subject
        List<String> teacherSubjects = teacherService.getSubjectIdsByTeacher(teacherId);
        if (!teacherSubjects.contains(subjectId)) {
            throw new IllegalArgumentException("L'enseignant n'enseigne pas cette matière");
        }

        // Vérifier que le student est dans un group qui a ce subject
        Student student = studentService.findById(studentId);
        if (student.getGroupId() == null || student.getGroupId().isEmpty()) {
            throw new IllegalArgumentException("L'étudiant n'est pas dans un groupe");
        }

        // Vérifier que le group a ce subject (relation group-subject)
        com.mongodb.client.MongoCollection<org.bson.Document> collection = 
            com.example.academic.database.MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("group_subjects");

        org.bson.Document relation = collection.find(
            new org.bson.Document("groupId", student.getGroupId())
                .append("subjectId", subjectId)
        ).first();

        if (relation == null) {
            throw new IllegalArgumentException("Le groupe de l'étudiant n'a pas cette matière");
        }

        // Validation de la note
        if (grade < 0 || grade > 20) {
            throw new IllegalArgumentException("La note doit être entre 0 et 20");
        }

        Grade newGrade = new Grade(studentId, subjectId, teacherId, grade, comment);
        gradeRepository.save(newGrade);
        return newGrade;
    }

    public void updateGrade(String gradeId, double newGrade, String comment) throws DatabaseException {
        Grade grade = gradeRepository.findById(gradeId);
        grade.setGrade(newGrade);
        if (comment != null) {
            grade.setComment(comment);
        }
        gradeRepository.update(grade);
    }

    public List<Grade> getGradesByStudent(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesBySubject(String subjectId) {
        return gradeRepository.findBySubjectId(subjectId);
    }

    public List<Grade> getGradesByTeacher(String teacherId) {
        return gradeRepository.findByTeacherId(teacherId);
    }

    public void deleteGrade(String gradeId) throws DatabaseException {
        gradeRepository.delete(gradeId);
    }
}

