package com.example.academic.ui.pages;

import com.example.academic.model.Teacher;
import com.example.academic.model.Subject;
import com.example.academic.service.TeacherService;
import com.example.academic.service.SubjectService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Page d'assignation des enseignants aux matières
 */
public class AssignTeacherSubjectPage extends StyledPanel {
    private JComboBox<TeacherItem> teacherComboBox;
    private JComboBox<SubjectItem> subjectComboBox;
    private JTable assignmentsTable;
    private DefaultTableModel tableModel;
    private TeacherService teacherService;
    private SubjectService subjectService;

    private class TeacherItem {
        private Teacher teacher;
        
        public TeacherItem(Teacher teacher) {
            this.teacher = teacher;
        }
        
        public Teacher getTeacher() {
            return teacher;
        }
        
        @Override
        public String toString() {
            return teacher.getFirstName() + " " + teacher.getLastName();
        }
    }

    private class SubjectItem {
        private Subject subject;
        
        public SubjectItem(Subject subject) {
            this.subject = subject;
        }
        
        public Subject getSubject() {
            return subject;
        }
        
        @Override
        public String toString() {
            return subject.getName() + " (" + subject.getCode() + ")";
        }
    }

    public AssignTeacherSubjectPage() {
        this.teacherService = new TeacherService();
        this.subjectService = new SubjectService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadAssignments();
    }

    private void initializeComponents() {
        // Combo boxes
        teacherComboBox = new JComboBox<>();
        subjectComboBox = new JComboBox<>();
        
        // Tableau des assignations
        String[] columns = {"Enseignant", "Matière", "Code"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        assignmentsTable = new JTable(tableModel);
        assignmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        assignmentsTable.setRowHeight(35);
        assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        javax.swing.table.JTableHeader header = assignmentsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        loadTeachers();
        loadSubjects();
    }

    private void setupLayout() {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Titre
        JLabel titleLabel = new JLabel("Assigner Enseignant → Matière");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        // Panel de formulaire
        JPanel formPanel = new StyledPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Nouvelle Assignation"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Enseignant
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Enseignant:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(teacherComboBox, gbc);
        
        // Matière
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Matière:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(subjectComboBox, gbc);
        
        // Bouton Assigner
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        ModernButton assignButton = new ModernButton("Assigner", 
            new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
        assignButton.addActionListener(e -> handleAssign());
        formPanel.add(assignButton, gbc);
        
        // Tableau des assignations existantes
        JPanel tablePanel = new StyledPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Assignations Existantes"));
        
        JPanel tableTopPanel = new StyledPanel(new BorderLayout());
        ModernButton refreshButton = new ModernButton("Actualiser", 
            new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
        refreshButton.addActionListener(e -> loadAssignments());
        tableTopPanel.add(refreshButton, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(assignmentsTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        tablePanel.add(tableTopPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Supprimer assignation
        ModernButton removeButton = new ModernButton("Supprimer l'assignation sélectionnée", 
            new Color(198, 40, 40), new Color(211, 47, 47), Color.WHITE);
        removeButton.addActionListener(e -> handleRemoveAssignment());
        tablePanel.add(removeButton, BorderLayout.SOUTH);
        
        // Assemblage
        JPanel topPanel = new StyledPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);
    }

    private void loadTeachers() {
        teacherComboBox.removeAllItems();
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            for (Teacher teacher : teachers) {
                teacherComboBox.addItem(new TeacherItem(teacher));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des enseignants: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSubjects() {
        subjectComboBox.removeAllItems();
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject subject : subjects) {
                subjectComboBox.addItem(new SubjectItem(subject));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des matières: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAssignments() {
        tableModel.setRowCount(0);
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            for (Teacher teacher : teachers) {
                List<String> subjectIds = teacherService.getSubjectIdsByTeacher(teacher.getId());
                for (String subjectId : subjectIds) {
                    try {
                        Subject subject = subjectService.findById(subjectId);
                        tableModel.addRow(new Object[]{
                            teacher.getFirstName() + " " + teacher.getLastName(),
                            subject.getName(),
                            subject.getCode()
                        });
                    } catch (Exception e) {
                        // Ignorer les erreurs
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des assignations: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAssign() {
        TeacherItem selectedTeacher = (TeacherItem) teacherComboBox.getSelectedItem();
        SubjectItem selectedSubject = (SubjectItem) subjectComboBox.getSelectedItem();
        
        if (selectedTeacher == null || selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant et une matière",
                "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            teacherService.assignSubjectToTeacher(
                selectedTeacher.getTeacher().getId(),
                selectedSubject.getSubject().getId()
            );
            JOptionPane.showMessageDialog(this, 
                "Assignation réussie: " + selectedTeacher + " → " + selectedSubject.getSubject().getName(),
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            loadAssignments();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'assignation: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveAssignment() {
        int selectedRow = assignmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une assignation à supprimer",
                "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String teacherName = (String) tableModel.getValueAt(selectedRow, 0);
        String subjectName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer l'assignation: " + teacherName + " → " + subjectName + "?",
            "Confirmer suppression", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Trouver le teacher et subject correspondants
                List<Teacher> teachers = teacherService.getAllTeachers();
                Teacher teacher = null;
                for (Teacher t : teachers) {
                    if ((t.getFirstName() + " " + t.getLastName()).equals(teacherName)) {
                        teacher = t;
                        break;
                    }
                }
                
                List<Subject> subjects = subjectService.getAllSubjects();
                Subject subject = null;
                for (Subject s : subjects) {
                    if (s.getName().equals(subjectName)) {
                        subject = s;
                        break;
                    }
                }
                
                if (teacher != null && subject != null) {
                    // Supprimer la relation dans MongoDB
                    com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                        com.example.academic.database.MongoDBConnection.getInstance()
                            .getDatabase()
                            .getCollection("teacher_subjects");
                    
                    collection.deleteOne(new org.bson.Document("teacherId", teacher.getId())
                        .append("subjectId", subject.getId()));
                    
                    JOptionPane.showMessageDialog(this, "Assignation supprimée avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadAssignments();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

