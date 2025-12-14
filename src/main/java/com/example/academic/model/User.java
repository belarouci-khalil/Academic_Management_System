package com.example.academic.model;

import com.example.academic.enums.UserRole;
import org.bson.Document;

/**
 * Classe abstraite User - Base pour tous les utilisateurs
 * Utilise l'héritage et le polymorphisme
 */
public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected UserRole role;
    protected String firstName;
    protected String lastName;

    // Constructeurs
    public User() {
    }

    public User(String username, String password, UserRole role, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Méthode abstraite pour polymorphisme
    public abstract void showInformation();

    // Méthode pour convertir en Document MongoDB
    public Document toDocument() {
        Document doc = new Document();
        if (id != null) doc.append("_id", id);
        doc.append("username", username)
           .append("password", password)
           .append("role", role.toString())
           .append("firstName", firstName)
           .append("lastName", lastName);
        return doc;
    }

    // Méthode statique pour créer User depuis Document
    public static User fromDocument(Document doc) {
        UserRole role = UserRole.valueOf(doc.getString("role"));
        String username = doc.getString("username");
        String password = doc.getString("password");
        String firstName = doc.getString("firstName");
        String lastName = doc.getString("lastName");
        String id = doc.getObjectId("_id").toString();

        User user;
        switch (role) {
            case ADMIN:
                // Administrator n'a pas de champs spécifiques, création directe
                user = new Administrator(username, password, firstName, lastName);
                user.setId(id);
                break;
            case TEACHER:
                // Utiliser Teacher.fromDocument pour charger tous les champs spécifiques
                user = Teacher.fromDocument(doc);
                break;
            case STUDENT:
                // Utiliser Student.fromDocument pour charger tous les champs spécifiques (groupId, etc.)
                user = Student.fromDocument(doc);
                break;
            default:
                throw new IllegalArgumentException("Role inconnu: " + role);
        }
        return user;
    }

    // Getters et Setters (Encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

