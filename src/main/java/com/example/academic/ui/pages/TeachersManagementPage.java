package com.example.academic.ui.pages;

import com.example.academic.model.Teacher;
import com.example.academic.service.TeacherService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Page de gestion des enseignants
 */
public class TeachersManagementPage extends StyledPanel {
    private JTable teachersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TeacherService teacherService;

    public TeachersManagementPage() {
        this.teacherService = new TeacherService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadTeachers();
    }

    private void initializeComponents() {
        String[] columns = {"Prénom", "Nom", "Email", "Département", "Téléphone", "Numéro Employé"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        teachersTable = new JTable(tableModel);
        teachersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        teachersTable.setRowHeight(35);
        teachersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // En-têtes de colonnes avec meilleure visibilité
        JTableHeader header = teachersTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Style du tableau
        teachersTable.setShowGrid(true);
        teachersTable.setGridColor(new Color(200, 200, 200));
        teachersTable.setIntercellSpacing(new Dimension(1, 1));
        teachersTable.setSelectionBackground(new Color(230, 240, 255));
        teachersTable.setSelectionForeground(Color.BLACK);
        
        // Ajuster la largeur des colonnes
        teachersTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        teachersTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        teachersTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        teachersTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        teachersTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        teachersTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        // Renderer pour les cellules avec meilleure lisibilité
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cellRenderer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Appliquer le renderer à toutes les colonnes
        for (int i = 0; i < teachersTable.getColumnCount(); i++) {
            teachersTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            teachersTable.getColumnModel().getColumn(i).setHeaderRenderer(new com.example.academic.ui.components.DefaultTableCellHeaderRenderer());
        }
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel supérieur avec titre et boutons
        JPanel topPanel = new StyledPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Gestion des Enseignants");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JPanel buttonPanel = new StyledPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        ModernButton addButton = new ModernButton("Ajouter", new Color(46, 125, 50), new Color(56, 142, 60), Color.WHITE);
        ModernButton editButton = new ModernButton("Modifier", new Color(255, 152, 0), new Color(255, 167, 38), Color.WHITE);
        ModernButton deleteButton = new ModernButton("Supprimer", new Color(198, 40, 40), new Color(211, 47, 47), Color.WHITE);
        ModernButton refreshButton = new ModernButton("Actualiser", new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
        
        addButton.setPreferredSize(new Dimension(100, 35));
        editButton.setPreferredSize(new Dimension(100, 35));
        deleteButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Panel de recherche
        JPanel searchPanel = new StyledPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Tableau avec scroll
        JScrollPane scrollPane = new JScrollPane(teachersTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Liste des Enseignants",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        // Panel central qui contient recherche et tableau
        JPanel centerPanel = new StyledPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Assemblage
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Event handlers
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> loadTeachers());
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTeachers(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTeachers(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTeachers(); }
        });
    }

    private void loadTeachers() {
        tableModel.setRowCount(0);
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            for (Teacher teacher : teachers) {
                tableModel.addRow(new Object[]{
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.getEmail() != null ? teacher.getEmail() : "N/A",
                    teacher.getDepartment() != null ? teacher.getDepartment() : "N/A",
                    teacher.getPhoneNumber() != null ? teacher.getPhoneNumber() : "N/A",
                    teacher.getEmployeeNumber() != null ? teacher.getEmployeeNumber() : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTeachers() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            for (Teacher teacher : teachers) {
                if (teacher.getFirstName().toLowerCase().contains(searchText) ||
                    teacher.getLastName().toLowerCase().contains(searchText) ||
                    teacher.getUsername().toLowerCase().contains(searchText) ||
                    (teacher.getEmail() != null && teacher.getEmail().toLowerCase().contains(searchText)) ||
                    (teacher.getDepartment() != null && teacher.getDepartment().toLowerCase().contains(searchText))) {
                    tableModel.addRow(new Object[]{
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.getEmail() != null ? teacher.getEmail() : "N/A",
                        teacher.getDepartment() != null ? teacher.getDepartment() : "N/A",
                        teacher.getPhoneNumber() != null ? teacher.getPhoneNumber() : "N/A",
                        teacher.getEmployeeNumber() != null ? teacher.getEmployeeNumber() : "N/A"
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        com.example.academic.ui.forms.TeacherForm form = new com.example.academic.ui.forms.TeacherForm(
            (JFrame) SwingUtilities.getWindowAncestor(this), null, this::loadTeachers);
        form.setVisible(true);
    }

    private void handleEdit() {
        int selectedRow = teachersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant à modifier", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String firstName = (String) tableModel.getValueAt(selectedRow, 0);
            com.example.academic.service.UserService userService = new com.example.academic.service.UserService();
            com.example.academic.model.User user = userService.findByUsername(firstName);
            if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                com.example.academic.ui.forms.TeacherForm form = new com.example.academic.ui.forms.TeacherForm(
                    (JFrame) SwingUtilities.getWindowAncestor(this), teacher, this::loadTeachers);
                form.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = teachersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant à supprimer", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer cet enseignant ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Récupérer via le prénom (username = firstName)
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                com.example.academic.service.UserService userService = new com.example.academic.service.UserService();
                com.example.academic.model.User user = userService.findByUsername(firstName);
                userService.deleteUser(user.getId());
                JOptionPane.showMessageDialog(this, "Enseignant supprimé avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadTeachers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

