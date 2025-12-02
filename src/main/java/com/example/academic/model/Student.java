package com.example.academic.model;

import com.example.academic.enums.UserRole;
import org.bson.Document;

/**
 * Classe Student - Hérite de User
 * Implémente le polymorphisme
 */
public class Student extends User {
    private String studentNumber;
    private String groupId;
    private String email;
    private String dateOfBirth; // Format: YYYY-MM-DD
    private String specialty; // Spécialité
    private String section; // Section

    public Student() {
        super();
        this.role = UserRole.STUDENT;
    }

    public Student(String username, String password, String firstName, String lastName) {
        super(username, password, UserRole.STUDENT, firstName, lastName);
    }

    public Student(String username, String password, String firstName, String lastName, String studentNumber, String groupId) {
        super(username, password, UserRole.STUDENT, firstName, lastName);
        this.studentNumber = studentNumber;
        this.groupId = groupId;
    }

    public Student(String username, String password, String firstName, String lastName, String studentNumber, 
                   String groupId, String email, String dateOfBirth, String specialty, String section) {
        super(username, password, UserRole.STUDENT, firstName, lastName);
        this.studentNumber = studentNumber;
        this.groupId = groupId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.specialty = specialty;
        this.section = section;
    }

    @Override
    public void showInformation() {
        System.out.println("=== ÉTUDIANT ===");
        System.out.println("Nom: " + firstName + " " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Numéro étudiant: " + (studentNumber != null ? studentNumber : "N/A"));
        System.out.println("Groupe: " + (groupId != null ? groupId : "Non assigné"));
        System.out.println("Rôle: Étudiant");
    }

    @Override
    public Document toDocument() {
        Document doc = super.toDocument();
        if (studentNumber != null) {
            doc.append("studentNumber", studentNumber);
        }
        if (groupId != null) {
            doc.append("groupId", groupId);
        }
        if (email != null) {
            doc.append("email", email);
        }
        if (dateOfBirth != null) {
            doc.append("dateOfBirth", dateOfBirth);
        }
        if (specialty != null) {
            doc.append("specialty", specialty);
        }
        if (section != null) {
            doc.append("section", section);
        }
        return doc;
    }

    public static Student fromDocument(Document doc) {
        Student student = new Student(
            doc.getString("username"),
            doc.getString("password"),
            doc.getString("firstName"),
            doc.getString("lastName")
        );
        student.setId(doc.getObjectId("_id").toString());
        if (doc.containsKey("studentNumber")) {
            student.setStudentNumber(doc.getString("studentNumber"));
        }
        if (doc.containsKey("groupId")) {
            student.setGroupId(doc.getString("groupId"));
        }
        if (doc.containsKey("email")) {
            student.setEmail(doc.getString("email"));
        }
        if (doc.containsKey("dateOfBirth")) {
            student.setDateOfBirth(doc.getString("dateOfBirth"));
        }
        if (doc.containsKey("specialty")) {
            student.setSpecialty(doc.getString("specialty"));
        }
        if (doc.containsKey("section")) {
            student.setSection(doc.getString("section"));
        }
        return student;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}

