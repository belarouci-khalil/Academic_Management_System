package com.example.academic.model;

import com.example.academic.enums.UserRole;

/**
 * Classe Administrator - Hérite de User
 * Implémente le polymorphisme
 */
public class Administrator extends User {

    public Administrator() {
        super();
        this.role = UserRole.ADMIN;
    }

    public Administrator(String username, String password, String firstName, String lastName) {
        super(username, password, UserRole.ADMIN, firstName, lastName);
    }

    @Override
    public void showInformation() {
        System.out.println("=== ADMINISTRATEUR ===");
        System.out.println("Nom: " + firstName + " " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Rôle: Administrateur");
    }
}

