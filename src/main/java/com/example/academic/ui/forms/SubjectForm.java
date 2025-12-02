package com.example.academic.ui.forms;

import com.example.academic.model.Subject;
import com.example.academic.service.SubjectService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulaire pour créer/modifier une matière
 */
public class SubjectForm extends JDialog {
    private JTextField nameField;
    private JTextField codeField;
    private JTextArea descriptionArea;
    private ModernButton saveButton;
    private ModernButton cancelButton;
    private SubjectService subjectService;
    private Subject existingSubject;
    private Runnable onSaveCallback;

    public SubjectForm(JFrame parent, Subject existingSubject, Runnable onSaveCallback) {
        super(parent, true);
        this.subjectService = new SubjectService();
        this.existingSubject = existingSubject;
        this.onSaveCallback = onSaveCallback;
        
        setTitle(existingSubject == null ? "Nouvelle Matière" : "Modifier Matière");
        setSize(500, 350);
        setLocationRelativeTo(parent);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        nameField = new JTextField(30);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        codeField = new JTextField(30);
        codeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        saveButton = new ModernButton("Enregistrer", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        cancelButton = new ModernButton("Annuler", new Color(158, 158, 158), new Color(189, 189, 189), Color.WHITE);

        if (existingSubject != null) {
            nameField.setText(existingSubject.getName());
            codeField.setText(existingSubject.getCode());
            descriptionArea.setText(existingSubject.getDescription() != null ? existingSubject.getDescription() : "");
            codeField.setEditable(false); // Le code ne peut pas être modifié
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        StyledPanel mainPanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        JLabel titleLabel = new JLabel(existingSubject == null ? "Créer une nouvelle matière" : "Modifier la matière");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Nom
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Nom *:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);

        // Code
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel codeLabel = new JLabel("Code *:");
        codeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(codeLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(codeField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(descLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPanel.add(new JScrollPane(descriptionArea), gbc);

        // Boutons
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
        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire", 
                "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }

        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le code est obligatoire", 
                "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            codeField.requestFocus();
            return;
        }

        try {
            if (existingSubject == null) {
                // Créer
                subjectService.createSubject(name, code, description);
                JOptionPane.showMessageDialog(this, "Matière créée avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modifier
                existingSubject.setName(name);
                existingSubject.setDescription(description);
                subjectService.updateSubject(existingSubject);
                JOptionPane.showMessageDialog(this, "Matière modifiée avec succès!", 
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

