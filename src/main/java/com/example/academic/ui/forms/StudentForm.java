package com.example.academic.ui.forms;

import com.example.academic.model.Student;
import com.example.academic.service.GroupService;
import com.example.academic.service.StudentService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulaire complet pour créer/modifier un étudiant
 */
public class StudentForm extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField studentNumberField;
    private JTextField emailField;
    private JTextField dateOfBirthField;
    private JTextField specialtyField;
    private JTextField sectionField;
    private JComboBox<String> groupCombo;
    private ModernButton saveButton;
    private ModernButton cancelButton;
    private StudentService studentService;
    private GroupService groupService;
    private Student existingStudent;
    private Runnable onSaveCallback;

    public StudentForm(JFrame parent, Student existingStudent, Runnable onSaveCallback) {
        super(parent, true);
        this.studentService = new StudentService();
        this.groupService = new GroupService();
        this.existingStudent = existingStudent;
        this.onSaveCallback = onSaveCallback;
        
        setTitle(existingStudent == null ? "Nouvel Étudiant" : "Modifier Étudiant");
        setSize(550, 600);
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
        
        studentNumberField = new JTextField(25);
        studentNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        emailField = new JTextField(25);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        dateOfBirthField = new JTextField(25);
        dateOfBirthField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateOfBirthField.setToolTipText("Format: YYYY-MM-DD (ex: 2000-05-15)");
        
        specialtyField = new JTextField(25);
        specialtyField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        sectionField = new JTextField(25);
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        groupCombo = new JComboBox<>();
        groupCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        groupCombo.addItem("Aucun groupe");
        try {
            java.util.List<com.example.academic.model.Group> groups = groupService.getAllGroups();
            for (com.example.academic.model.Group group : groups) {
                groupCombo.addItem(group.getName());
            }
        } catch (Exception e) {
            // Ignorer
        }
        
        saveButton = new ModernButton("Enregistrer", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        cancelButton = new ModernButton("Annuler", new Color(158, 158, 158), new Color(189, 189, 189), Color.WHITE);

        if (existingStudent != null) {
            firstNameField.setText(existingStudent.getFirstName());
            lastNameField.setText(existingStudent.getLastName());
            studentNumberField.setText(existingStudent.getStudentNumber() != null ? existingStudent.getStudentNumber() : "");
            emailField.setText(existingStudent.getEmail() != null ? existingStudent.getEmail() : "");
            dateOfBirthField.setText(existingStudent.getDateOfBirth() != null ? existingStudent.getDateOfBirth() : "");
            specialtyField.setText(existingStudent.getSpecialty() != null ? existingStudent.getSpecialty() : "");
            sectionField.setText(existingStudent.getSection() != null ? existingStudent.getSection() : "");
            
            // Sélectionner le groupe
            if (existingStudent.getGroupId() != null) {
                try {
                    com.example.academic.model.Group group = groupService.findById(existingStudent.getGroupId());
                    for (int i = 0; i < groupCombo.getItemCount(); i++) {
                        if (groupCombo.getItemAt(i).equals(group.getName())) {
                            groupCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Ignorer
                }
            }
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        StyledPanel mainPanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel(existingStudent == null ? "Créer un nouvel étudiant" : "Modifier l'étudiant");
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
        mainPanel.add(new JLabel("Numéro étudiant:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(studentNumberField, gbc);

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
        mainPanel.add(new JLabel("Date de naissance:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(dateOfBirthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Spécialité:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(specialtyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(sectionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Groupe:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(groupCombo, gbc);

        JPanel buttonPanel = new StyledPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(scrollPane, BorderLayout.CENTER);
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
        String studentNumber = studentNumberField.getText().trim();
        String email = emailField.getText().trim();
        String dateOfBirth = dateOfBirthField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String section = sectionField.getText().trim();

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

        // Validation date si fournie
        if (!dateOfBirth.isEmpty() && !dateOfBirth.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez YYYY-MM-DD (ex: 2000-05-15)", 
                "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Récupérer le groupe sélectionné
            String groupId = null;
            if (groupCombo.getSelectedIndex() > 0) {
                String selectedGroupName = (String) groupCombo.getSelectedItem();
                java.util.List<com.example.academic.model.Group> groups = groupService.getAllGroups();
                for (com.example.academic.model.Group group : groups) {
                    if (group.getName().equals(selectedGroupName)) {
                        groupId = group.getId();
                        break;
                    }
                }
            }

            if (existingStudent == null) {
                // Créer
                Student student = studentService.createStudent(firstName, lastName, groupId);
                student.setStudentNumber(studentNumber.isEmpty() ? null : studentNumber);
                student.setEmail(email.isEmpty() ? null : email);
                student.setDateOfBirth(dateOfBirth.isEmpty() ? null : dateOfBirth);
                student.setSpecialty(specialty.isEmpty() ? null : specialty);
                student.setSection(section.isEmpty() ? null : section);
                
                // Mettre à jour dans la base
                com.example.academic.repository.UserRepository userRepo = new com.example.academic.repository.UserRepository();
                userRepo.update(student);
                
                JOptionPane.showMessageDialog(this, "Étudiant créé avec succès!\nUsername: " + firstName + "\nPassword: " + lastName, 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modifier
                existingStudent.setFirstName(firstName);
                existingStudent.setLastName(lastName);
                existingStudent.setStudentNumber(studentNumber.isEmpty() ? null : studentNumber);
                existingStudent.setEmail(email.isEmpty() ? null : email);
                existingStudent.setDateOfBirth(dateOfBirth.isEmpty() ? null : dateOfBirth);
                existingStudent.setSpecialty(specialty.isEmpty() ? null : specialty);
                existingStudent.setSection(section.isEmpty() ? null : section);
                existingStudent.setGroupId(groupId);
                
                com.example.academic.repository.UserRepository userRepo = new com.example.academic.repository.UserRepository();
                userRepo.update(existingStudent);
                
                JOptionPane.showMessageDialog(this, "Étudiant modifié avec succès!", 
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

