package com.example.academic.ui.pages;

import com.example.academic.model.Student;
import com.example.academic.service.GroupService;
import com.example.academic.service.StudentService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Page de gestion des étudiants
 */
public class StudentsManagementPage extends StyledPanel {
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private StudentService studentService;
    private GroupService groupService;

    public StudentsManagementPage() {
        this.studentService = new StudentService();
        this.groupService = new GroupService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadStudents();
    }

    private void initializeComponents() {
        String[] columns = {"Prénom", "Nom", "Email", "Date Naissance", "Spécialité", "Section", "Groupe"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentsTable.setRowHeight(35);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // En-têtes avec meilleure visibilité
        javax.swing.table.JTableHeader header = studentsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        studentsTable.setShowGrid(true);
        studentsTable.setGridColor(new Color(200, 200, 200));
        studentsTable.setSelectionBackground(new Color(230, 240, 255));
        
        // Renderer pour les cellules
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cellRenderer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        for (int i = 0; i < studentsTable.getColumnCount(); i++) {
            studentsTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            studentsTable.getColumnModel().getColumn(i).setHeaderRenderer(new com.example.academic.ui.components.DefaultTableCellHeaderRenderer());
        }
        
        // Ajuster les largeurs
        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createTitledBorder("Rechercher"));
    }

    private void setupLayout() {
        JPanel topPanel = new StyledPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Gestion des Étudiants");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JPanel buttonPanel = new StyledPanel(new FlowLayout(FlowLayout.RIGHT));
        ModernButton addButton = new ModernButton("Ajouter", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        ModernButton editButton = new ModernButton("Modifier", new Color(255, 152, 0), new Color(255, 167, 38), Color.WHITE);
        ModernButton deleteButton = new ModernButton("Supprimer", new Color(198, 40, 40), new Color(211, 47, 47), Color.WHITE);
        ModernButton refreshButton = new ModernButton("Actualiser", new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel searchPanel = new StyledPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        searchField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchPanel.add(searchField);
        
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Liste des Étudiants",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        // Panel central qui contient recherche et tableau
        JPanel centerPanel = new StyledPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> loadStudents());
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterStudents(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterStudents(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterStudents(); }
        });
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        try {
            List<Student> students = studentService.getAllStudents();
            for (Student student : students) {
                String groupName = "Non assigné";
                if (student.getGroupId() != null && !student.getGroupId().isEmpty()) {
                    try {
                        groupName = groupService.findById(student.getGroupId()).getName();
                    } catch (Exception e) {
                        groupName = "Groupe inconnu";
                    }
                }
                tableModel.addRow(new Object[]{
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail() != null ? student.getEmail() : "N/A",
                    student.getDateOfBirth() != null ? student.getDateOfBirth() : "N/A",
                    student.getSpecialty() != null ? student.getSpecialty() : "N/A",
                    student.getSection() != null ? student.getSection() : "N/A",
                    groupName
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterStudents() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        try {
            List<Student> students = studentService.getAllStudents();
            for (Student student : students) {
                if (student.getFirstName().toLowerCase().contains(searchText) ||
                    student.getLastName().toLowerCase().contains(searchText) ||
                    student.getUsername().toLowerCase().contains(searchText) ||
                    (student.getEmail() != null && student.getEmail().toLowerCase().contains(searchText)) ||
                    (student.getSpecialty() != null && student.getSpecialty().toLowerCase().contains(searchText)) ||
                    (student.getSection() != null && student.getSection().toLowerCase().contains(searchText))) {
                    String groupName = "Non assigné";
                    if (student.getGroupId() != null && !student.getGroupId().isEmpty()) {
                        try {
                            groupName = groupService.findById(student.getGroupId()).getName();
                        } catch (Exception e) {
                            groupName = "Groupe inconnu";
                        }
                    }
                    tableModel.addRow(new Object[]{
                        student.getFirstName(),
                        student.getLastName(),
                        student.getUsername(),
                        groupName
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        com.example.academic.ui.forms.StudentForm form = new com.example.academic.ui.forms.StudentForm(
            (JFrame) SwingUtilities.getWindowAncestor(this), null, this::loadStudents);
        form.setVisible(true);
    }

    private void handleEdit() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant à modifier", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String firstName = (String) tableModel.getValueAt(selectedRow, 0);
            com.example.academic.service.UserService userService = new com.example.academic.service.UserService();
            com.example.academic.model.User user = userService.findByUsername(firstName);
            if (user instanceof Student) {
                Student student = (Student) user;
                com.example.academic.ui.forms.StudentForm form = new com.example.academic.ui.forms.StudentForm(
                    (JFrame) SwingUtilities.getWindowAncestor(this), student, this::loadStudents);
                form.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant à supprimer", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer cet étudiant ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                com.example.academic.service.UserService userService = new com.example.academic.service.UserService();
                com.example.academic.model.User user = userService.findByUsername(firstName); // username = firstName
                userService.deleteUser(user.getId());
                JOptionPane.showMessageDialog(this, "Étudiant supprimé avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

