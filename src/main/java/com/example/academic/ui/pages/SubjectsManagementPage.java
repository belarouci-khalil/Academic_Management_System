package com.example.academic.ui.pages;

import com.example.academic.model.Subject;
import com.example.academic.service.SubjectService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;
import com.example.academic.ui.forms.SubjectForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Page de gestion des matières avec tableau interactif
 */
public class SubjectsManagementPage extends StyledPanel {
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private SubjectService subjectService;

    public SubjectsManagementPage() {
        this.subjectService = new SubjectService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadSubjects();
    }

    private void initializeComponents() {
        String[] columns = {"Nom", "Code", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        subjectsTable = new JTable(tableModel);
        subjectsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjectsTable.setRowHeight(35);
        subjectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // En-têtes avec meilleure visibilité
        javax.swing.table.JTableHeader header = subjectsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        subjectsTable.setShowGrid(true);
        subjectsTable.setGridColor(new Color(200, 200, 200));
        subjectsTable.setSelectionBackground(new Color(230, 240, 255));
        
        // Renderer pour les cellules
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cellRenderer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        for (int i = 0; i < subjectsTable.getColumnCount(); i++) {
            subjectsTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            subjectsTable.getColumnModel().getColumn(i).setHeaderRenderer(new com.example.academic.ui.components.DefaultTableCellHeaderRenderer());
        }
        
        // Ajuster les largeurs
        subjectsTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        subjectsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        subjectsTable.getColumnModel().getColumn(2).setPreferredWidth(400);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createTitledBorder("Rechercher"));
    }

    private void setupLayout() {
        JPanel topPanel = new StyledPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Gestion des Matières");
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
        
        JScrollPane scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Liste des Matières",
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
        refreshButton.addActionListener(e -> loadSubjects());
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterSubjects(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterSubjects(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterSubjects(); }
        });
    }

    private void loadSubjects() {
        tableModel.setRowCount(0);
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject subject : subjects) {
                tableModel.addRow(new Object[]{
                    subject.getName(),
                    subject.getCode(),
                    subject.getDescription() != null ? subject.getDescription() : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterSubjects() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject subject : subjects) {
                if (subject.getName().toLowerCase().contains(searchText) ||
                    subject.getCode().toLowerCase().contains(searchText) ||
                    (subject.getDescription() != null && subject.getDescription().toLowerCase().contains(searchText))) {
                    tableModel.addRow(new Object[]{
                        subject.getName(),
                        subject.getCode(),
                        subject.getDescription() != null ? subject.getDescription() : ""
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        SubjectForm form = new SubjectForm((JFrame) SwingUtilities.getWindowAncestor(this), null, this::loadSubjects);
        form.setVisible(true);
    }

    private void handleEdit() {
        int selectedRow = subjectsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une matière à modifier", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String code = (String) tableModel.getValueAt(selectedRow, 1);
            Subject subject = subjectService.findByCode(code);
            SubjectForm form = new SubjectForm((JFrame) SwingUtilities.getWindowAncestor(this), subject, this::loadSubjects);
            form.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = subjectsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une matière à supprimer", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer cette matière ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String code = (String) tableModel.getValueAt(selectedRow, 1);
                Subject subject = subjectService.findByCode(code);
                subjectService.deleteSubject(subject.getId());
                JOptionPane.showMessageDialog(this, "Matière supprimée avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadSubjects();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

