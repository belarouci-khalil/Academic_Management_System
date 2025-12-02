package com.example.academic.ui.forms;

import com.example.academic.model.Teacher;
import com.example.academic.service.TeacherService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulaire complet pour créer/modifier un enseignant
 */
public class TeacherForm extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField employeeNumberField;
    private JTextField emailField;
    private JTextField departmentField;
    private JTextField phoneNumberField;
    private ModernButton saveButton;
    private ModernButton cancelButton;
    private TeacherService teacherService;
    private Teacher existingTeacher;
    private Runnable onSaveCallback;

    public TeacherForm(JFrame parent, Teacher existingTeacher, Runnable onSaveCallback) {
        super(parent, true);
        this.teacherService = new TeacherService();
        this.existingTeacher = existingTeacher;
        this.onSaveCallback = onSaveCallback;
        
        setTitle(existingTeacher == null ? "Nouvel Enseignant" : "Modifier Enseignant");
        setSize(500, 450);
        setLocationRelativeTo(parent);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        firstNameField = new JTextField(25);
        firstNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lastNameField = new JTextField(25);
        lastNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        employeeNumberField = new JTextField(25);
        employeeNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        emailField = new JTextField(25);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        departmentField = new JTextField(25);
        departmentField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        phoneNumberField = new JTextField(25);
        phoneNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        saveButton = new ModernButton("Enregistrer", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        cancelButton = new ModernButton("Annuler", new Color(158, 158, 158), new Color(189, 189, 189), Color.WHITE);

        if (existingTeacher != null) {
            firstNameField.setText(existingTeacher.getFirstName());
            lastNameField.setText(existingTeacher.getLastName());
            employeeNumberField.setText(existingTeacher.getEmployeeNumber() != null ? existingTeacher.getEmployeeNumber() : "");
            emailField.setText(existingTeacher.getEmail() != null ? existingTeacher.getEmail() : "");
            departmentField.setText(existingTeacher.getDepartment() != null ? existingTeacher.getDepartment() : "");
            phoneNumberField.setText(existingTeacher.getPhoneNumber() != null ? existingTeacher.getPhoneNumber() : "");
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        StyledPanel mainPanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel(existingTeacher == null ? "Créer un nouvel enseignant" : "Modifier l'enseignant");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Prénom *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Nom *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Numéro employé:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(employeeNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Département/Spécialité:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(departmentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(phoneNumberField, gbc);

        JPanel buttonPanel = new StyledPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleSave() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String employeeNumber = employeeNumberField.getText().trim();
        String email = emailField.getText().trim();
        String department = departmentField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le prénom et le nom sont obligatoires", 
                "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validation email si fourni
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Format d'email invalide", 
                "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (existingTeacher == null) {
                // Créer
                Teacher teacher = teacherService.createTeacher(firstName, lastName);
                teacher.setEmployeeNumber(employeeNumber.isEmpty() ? null : employeeNumber);
                teacher.setEmail(email.isEmpty() ? null : email);
                teacher.setDepartment(department.isEmpty() ? null : department);
                teacher.setPhoneNumber(phoneNumber.isEmpty() ? null : phoneNumber);
                
                // Mettre à jour dans la base
                com.example.academic.repository.UserRepository userRepo = new com.example.academic.repository.UserRepository();
                userRepo.update(teacher);
                
                JOptionPane.showMessageDialog(this, "Enseignant créé avec succès!\nUsername: " + firstName + "\nPassword: " + lastName, 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modifier
                existingTeacher.setFirstName(firstName);
                existingTeacher.setLastName(lastName);
                existingTeacher.setEmployeeNumber(employeeNumber.isEmpty() ? null : employeeNumber);
                existingTeacher.setEmail(email.isEmpty() ? null : email);
                existingTeacher.setDepartment(department.isEmpty() ? null : department);
                existingTeacher.setPhoneNumber(phoneNumber.isEmpty() ? null : phoneNumber);
                
                com.example.academic.repository.UserRepository userRepo = new com.example.academic.repository.UserRepository();
                userRepo.update(existingTeacher);
                
                JOptionPane.showMessageDialog(this, "Enseignant modifié avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}

