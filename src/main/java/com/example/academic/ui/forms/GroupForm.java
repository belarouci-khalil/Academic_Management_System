package com.example.academic.ui.forms;

import com.example.academic.model.Group;
import com.example.academic.service.GroupService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulaire pour créer/modifier un groupe
 */
public class GroupForm extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private ModernButton saveButton;
    private ModernButton cancelButton;
    private GroupService groupService;
    private Group existingGroup;
    private Runnable onSaveCallback;

    public GroupForm(JFrame parent, Group existingGroup, Runnable onSaveCallback) {
        super(parent, true);
        this.groupService = new GroupService();
        this.existingGroup = existingGroup;
        this.onSaveCallback = onSaveCallback;
        
        setTitle(existingGroup == null ? "New Group" : "Edit Group");
        setSize(500, 300);
        setLocationRelativeTo(parent);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        nameField = new JTextField(30);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        saveButton = new ModernButton("Save", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        cancelButton = new ModernButton("Cancel", new Color(158, 158, 158), new Color(189, 189, 189), Color.WHITE);

        if (existingGroup != null) {
            nameField.setText(existingGroup.getName());
            descriptionArea.setText(existingGroup.getDescription() != null ? existingGroup.getDescription() : "");
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        StyledPanel mainPanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        JLabel titleLabel = new JLabel(existingGroup == null ? "Create New Group" : "Edit Group");
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
        JLabel nameLabel = new JLabel("Group Name *:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
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
        String description = descriptionArea.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Group name is required", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }

        try {
            if (existingGroup == null) {
                // Créer
                groupService.createGroup(name, description);
                JOptionPane.showMessageDialog(this, "Group created successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modifier
                existingGroup.setName(name);
                existingGroup.setDescription(description);
                groupService.updateGroup(existingGroup);
                JOptionPane.showMessageDialog(this, "Group updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

