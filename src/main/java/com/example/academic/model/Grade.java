package com.example.academic.model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

/**
 * Classe Grade - Représente une note
 * Utilise l'encapsulation avec validation
 */
public class Grade {
    private String id;
    private String studentId;
    private String subjectId;
    private String teacherId;
    private double grade; // Note entre 0 et 20
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Grade() {
    }

    public Grade(String studentId, String subjectId, String teacherId, double grade, String comment) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.setGrade(grade); // Utilise le setter pour validation
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Document toDocument() {
        Document doc = new Document();
        if (id != null && !id.isEmpty()) {
            doc.append("_id", new ObjectId(id));
        }
        doc.append("studentId", studentId)
           .append("subjectId", subjectId)
           .append("teacherId", teacherId)
           .append("grade", grade)
           .append("createdAt", createdAt.toString())
           .append("updatedAt", updatedAt.toString());
        if (comment != null) {
            doc.append("comment", comment);
        }
        return doc;
    }

    public static Grade fromDocument(Document doc) {
        Grade grade = new Grade(
            doc.getString("studentId"),
            doc.getString("subjectId"),
            doc.getString("teacherId"),
            doc.getDouble("grade"),
            doc.getString("comment")
        );
        grade.setId(doc.getObjectId("_id").toString());
        if (doc.containsKey("createdAt")) {
            grade.setCreatedAt(LocalDateTime.parse(doc.getString("createdAt")));
        }
        if (doc.containsKey("updatedAt")) {
            grade.setUpdatedAt(LocalDateTime.parse(doc.getString("updatedAt")));
        }
        return grade;
    }

    // Getters et Setters (Encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        if (grade < 0 || grade > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20");
        }
        this.grade = grade;
        this.updatedAt = LocalDateTime.now();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", grade=" + grade +
                ", comment='" + comment + '\'' +
                '}';
    }
}

