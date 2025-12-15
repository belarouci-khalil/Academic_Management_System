package com.example.academic.ui.forms;

import com.example.academic.model.Grade;
import com.example.academic.model.Student;
import com.example.academic.model.Subject;
import com.example.academic.service.GradeService;
import com.example.academic.service.StudentService;
import com.example.academic.service.SubjectService;
import com.example.academic.service.TeacherService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Formulaire pour ajouter/modifier une note
 */
public class GradeForm extends JDialog {
    private JComboBox<String> studentCombo;
    private JComboBox<String> subjectCombo;
    private JTextField gradeField;
    private JTextArea commentArea;
    private ModernButton saveButton;
    private ModernButton cancelButton;
    private GradeService gradeService;
    private TeacherService teacherService;
    private StudentService studentService;
    private SubjectService subjectService;
    private String teacherId;
    private Grade existingGrade;
    private Runnable onSaveCallback;

    public GradeForm(JFrame parent, String teacherId, Grade existingGrade, Runnable onSaveCallback) {
        super(parent, true);
        this.teacherId = teacherId;
        this.existingGrade = existingGrade;
        this.onSaveCallback = onSaveCallback;
        this.gradeService = new GradeService();
        this.teacherService = new TeacherService();
        this.studentService = new StudentService();
        this.subjectService = new SubjectService();
        
        setTitle(existingGrade == null ? "Add Grade" : "Edit Grade");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }

    private void initializeComponents() {
        studentCombo = new JComboBox<>();
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        subjectCombo = new JComboBox<>();
        subjectCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        gradeField = new JTextField(10);
        gradeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        commentArea = new JTextArea(5, 30);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        
        saveButton = new ModernButton("Save", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        cancelButton = new ModernButton("Cancel", new Color(158, 158, 158), new Color(189, 189, 189), Color.WHITE);
    }

    private void loadData() {
        try {
            // Charger les subjects que le teacher enseigne
            List<String> subjectIds = teacherService.getSubjectIdsByTeacher(teacherId);
            for (String subjectId : subjectIds) {
                try {
                    Subject subject = subjectService.findById(subjectId);
                    subjectCombo.addItem(subject.getName() + " (" + subjectId + ")");
                } catch (Exception e) {
                    // Ignorer les erreurs
                }
            }
            
            // Charger les students (filtrés par les groups du teacher)
            List<Student> allStudents = studentService.getAllStudents();
            for (Student student : allStudents) {
                studentCombo.addItem(student.getFirstName() + " " + student.getLastName() + " (" + student.getId() + ")");
            }
            
            if (existingGrade != null) {
                // Pré-remplir les champs
                gradeField.setText(String.valueOf(existingGrade.getGrade()));
                commentArea.setText(existingGrade.getComment() != null ? existingGrade.getComment() : "");
                // Sélectionner les combos
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        StyledPanel mainPanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel(existingGrade == null ? "Add Grade" : "Edit Grade");
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
        mainPanel.add(new JLabel("Student *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(studentCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Subject *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(subjectCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Grade (0-20) *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(gradeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Comment:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPanel.add(new JScrollPane(commentArea), gbc);

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
        if (studentCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (subjectCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gradeText = gradeField.getText().trim();
        if (gradeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a grade", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeText);
            if (grade < 0 || grade > 20) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "The grade must be a number between 0 and 20", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String studentSelected = (String) studentCombo.getSelectedItem();
            String studentId = studentSelected.substring(studentSelected.indexOf("(") + 1, studentSelected.indexOf(")"));
            
            String subjectSelected = (String) subjectCombo.getSelectedItem();
            String subjectId = subjectSelected.substring(subjectSelected.indexOf("(") + 1, subjectSelected.indexOf(")"));

            String comment = commentArea.getText().trim();

            if (existingGrade == null) {
                // Créer
                gradeService.addGrade(studentId, subjectId, teacherId, grade, comment);
                JOptionPane.showMessageDialog(this, "Grade ajoutée avec succès!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modifier
                gradeService.updateGrade(existingGrade.getId(), grade, comment);
                JOptionPane.showMessageDialog(this, "Grade modifiée avec succès!", 
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

