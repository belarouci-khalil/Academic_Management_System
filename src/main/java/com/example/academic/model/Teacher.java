package com.example.academic.model;

import com.example.academic.enums.UserRole;
import org.bson.Document;

/**
 * Classe Teacher - Hérite de User
 * Implémente le polymorphisme
 */
public class Teacher extends User {
    private String employeeNumber;
    private String email;
    private String department; // Département/Spécialité
    private String phoneNumber;

    public Teacher() {
        super();
        this.role = UserRole.TEACHER;
    }

    public Teacher(String username, String password, String firstName, String lastName) {
        super(username, password, UserRole.TEACHER, firstName, lastName);
    }

    public Teacher(String username, String password, String firstName, String lastName, String employeeNumber) {
        super(username, password, UserRole.TEACHER, firstName, lastName);
        this.employeeNumber = employeeNumber;
    }

    public Teacher(String username, String password, String firstName, String lastName, String employeeNumber, 
                   String email, String department, String phoneNumber) {
        super(username, password, UserRole.TEACHER, firstName, lastName);
        this.employeeNumber = employeeNumber;
        this.email = email;
        this.department = department;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void showInformation() {
        System.out.println("=== ENSEIGNANT ===");
        System.out.println("Nom: " + firstName + " " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Numéro employé: " + (employeeNumber != null ? employeeNumber : "N/A"));
        System.out.println("Rôle: Enseignant");
    }

    @Override
    public Document toDocument() {
        Document doc = super.toDocument();
        if (employeeNumber != null) {
            doc.append("employeeNumber", employeeNumber);
        }
        if (email != null) {
            doc.append("email", email);
        }
        if (department != null) {
            doc.append("department", department);
        }
        if (phoneNumber != null) {
            doc.append("phoneNumber", phoneNumber);
        }
        return doc;
    }

    public static Teacher fromDocument(Document doc) {
        Teacher teacher = new Teacher(
            doc.getString("username"),
            doc.getString("password"),
            doc.getString("firstName"),
            doc.getString("lastName")
        );
        teacher.setId(doc.getObjectId("_id").toString());
        if (doc.containsKey("employeeNumber")) {
            teacher.setEmployeeNumber(doc.getString("employeeNumber"));
        }
        if (doc.containsKey("email")) {
            teacher.setEmail(doc.getString("email"));
        }
        if (doc.containsKey("department")) {
            teacher.setDepartment(doc.getString("department"));
        }
        if (doc.containsKey("phoneNumber")) {
            teacher.setPhoneNumber(doc.getString("phoneNumber"));
        }
        return teacher;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

