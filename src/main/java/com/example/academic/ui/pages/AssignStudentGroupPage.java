package com.example.academic.ui.pages;

import com.example.academic.model.Student;
import com.example.academic.model.Group;
import com.example.academic.service.StudentService;
import com.example.academic.service.GroupService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Page d'assignation des étudiants aux groupes
 */
public class AssignStudentGroupPage extends StyledPanel {
    private JComboBox<StudentItem> studentComboBox;
    private JComboBox<GroupItem> groupComboBox;
    private JTable assignmentsTable;
    private DefaultTableModel tableModel;
    private StudentService studentService;
    private GroupService groupService;

    private class StudentItem {
        private Student student;
        
        public StudentItem(Student student) {
            this.student = student;
        }
        
        public Student getStudent() {
            return student;
        }
        
        @Override
        public String toString() {
            return student.getFirstName() + " " + student.getLastName();
        }
    }

    private class GroupItem {
        private Group group;
        
        public GroupItem(Group group) {
            this.group = group;
        }
        
        public Group getGroup() {
            return group;
        }
        
        @Override
        public String toString() {
            return group.getName();
        }
    }

    public AssignStudentGroupPage() {
        this.studentService = new StudentService();
        this.groupService = new GroupService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadAssignments();
    }

    private void initializeComponents() {
        // Combo boxes
        studentComboBox = new JComboBox<>();
        groupComboBox = new JComboBox<>();
        
        // Tableau des assignations
        String[] columns = {"Étudiant", "Groupe", "Description"};
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
        
        loadStudents();
        loadGroups();
    }

    private void setupLayout() {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Titre
        JLabel titleLabel = new JLabel("Assigner Étudiant → Groupe");
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
        
        // Étudiant
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Étudiant:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(studentComboBox, gbc);
        
        // Groupe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Groupe:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(groupComboBox, gbc);
        
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
        
        // Supprimer assignation (change le groupe de l'étudiant)
        ModernButton removeButton = new ModernButton("Retirer du groupe", 
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

    private void loadStudents() {
        studentComboBox.removeAllItems();
        try {
            List<Student> students = studentService.getAllStudents();
            for (Student student : students) {
                studentComboBox.addItem(new StudentItem(student));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des étudiants: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGroups() {
        groupComboBox.removeAllItems();
        try {
            List<Group> groups = groupService.getAllGroups();
            for (Group group : groups) {
                groupComboBox.addItem(new GroupItem(group));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des groupes: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAssignments() {
        tableModel.setRowCount(0);
        try {
            List<Student> students = studentService.getAllStudents();
            int countWithGroup = 0;
            int totalStudents = students.size();
            
            System.out.println("DEBUG: Chargement de " + totalStudents + " étudiant(s)");
            
            for (Student student : students) {
                String groupId = student.getGroupId();
                System.out.println("DEBUG: Étudiant " + student.getFirstName() + " " + student.getLastName() + 
                    " - GroupId: " + (groupId != null ? groupId : "null"));
                
                if (groupId != null && !groupId.trim().isEmpty()) {
                    try {
                        Group group = groupService.findById(groupId);
                        tableModel.addRow(new Object[]{
                            student.getFirstName() + " " + student.getLastName(),
                            group.getName(),
                            group.getDescription()
                        });
                        countWithGroup++;
                        System.out.println("DEBUG: ✓ Assignation affichée pour " + student.getFirstName());
                    } catch (Exception e) {
                        // Groupe introuvable, afficher quand même l'étudiant
                        System.out.println("DEBUG: ✗ Groupe introuvable pour " + student.getFirstName() + 
                            " - ID: " + groupId + " - Erreur: " + e.getMessage());
                        tableModel.addRow(new Object[]{
                            student.getFirstName() + " " + student.getLastName(),
                            "Groupe introuvable (ID: " + groupId + ")",
                            "Erreur: " + e.getMessage()
                        });
                        countWithGroup++;
                    }
                } else {
                    System.out.println("DEBUG: ⚠ Étudiant " + student.getFirstName() + 
                        " n'a pas de groupe assigné (groupId: " + groupId + ")");
                }
            }
            
            System.out.println("DEBUG: Total avec groupe: " + countWithGroup + " / " + totalStudents);
            
            // Message informatif si aucune assignation trouvée
            if (countWithGroup == 0) {
                if (students.isEmpty()) {
                    tableModel.addRow(new Object[]{
                        "Aucun étudiant trouvé dans le système",
                        "Créez d'abord des étudiants",
                        "Puis assignez-les à un groupe"
                    });
                } else {
                    tableModel.addRow(new Object[]{
                        "Aucun étudiant n'est assigné à un groupe",
                        totalStudents + " étudiant(s) disponible(s) sans groupe",
                        "Sélectionnez un étudiant et un groupe ci-dessus pour assigner"
                    });
                }
            }
        } catch (Exception e) {
            String errorMsg = "Erreur lors du chargement des assignations: " + e.getMessage() + "\n\n" + 
                "Détails: " + e.getClass().getSimpleName();
            System.err.println("DEBUG: ERREUR dans loadAssignments: " + errorMsg);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, errorMsg,
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAssign() {
        StudentItem selectedStudent = (StudentItem) studentComboBox.getSelectedItem();
        GroupItem selectedGroup = (GroupItem) groupComboBox.getSelectedItem();
        
        if (selectedStudent == null || selectedGroup == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant et un groupe",
                "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            studentService.assignToGroup(
                selectedStudent.getStudent().getId(),
                selectedGroup.getGroup().getId()
            );
            JOptionPane.showMessageDialog(this, 
                "Assignation réussie: " + selectedStudent + " → " + selectedGroup,
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
        
        String studentName = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir retirer " + studentName + " de son groupe?",
            "Confirmer suppression", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                List<Student> students = studentService.getAllStudents();
                Student student = null;
                for (Student s : students) {
                    if ((s.getFirstName() + " " + s.getLastName()).equals(studentName)) {
                        student = s;
                        break;
                    }
                }
                
                if (student != null) {
                    // Retirer l'étudiant du groupe (assigner à null ou chaîne vide)
                    studentService.assignToGroup(student.getId(), "");
                    JOptionPane.showMessageDialog(this, "Étudiant retiré du groupe avec succès",
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

