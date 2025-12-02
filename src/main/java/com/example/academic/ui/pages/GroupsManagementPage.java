package com.example.academic.ui.pages;

import com.example.academic.model.Group;
import com.example.academic.service.GroupService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;
import com.example.academic.ui.forms.GroupForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Page de gestion des groupes avec tableau interactif
 */
public class GroupsManagementPage extends StyledPanel {
    private JTable groupsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private GroupService groupService;

    public GroupsManagementPage() {
        this.groupService = new GroupService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadGroups();
    }

    private void initializeComponents() {
        // Modèle de tableau
        String[] columns = {"Nom", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        groupsTable = new JTable(tableModel);
        groupsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        groupsTable.setRowHeight(35);
        groupsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // En-têtes avec meilleure visibilité
        javax.swing.table.JTableHeader header = groupsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        groupsTable.setShowGrid(true);
        groupsTable.setGridColor(new Color(200, 200, 200));
        groupsTable.setSelectionBackground(new Color(230, 240, 255));
        
        // Renderer pour les cellules
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cellRenderer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        for (int i = 0; i < groupsTable.getColumnCount(); i++) {
            groupsTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            groupsTable.getColumnModel().getColumn(i).setHeaderRenderer(new com.example.academic.ui.components.DefaultTableCellHeaderRenderer());
        }
        
        // Ajuster les largeurs
        groupsTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        groupsTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        
        // Recherche
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createTitledBorder("Rechercher"));
    }

    private void setupLayout() {
        // Panel supérieur avec titre et boutons
        JPanel topPanel = new StyledPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Gestion des Groupes");
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
        
        // Panel de recherche
        JPanel searchPanel = new StyledPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        searchField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchPanel.add(searchField);
        
        // Tableau avec scroll
        JScrollPane scrollPane = new JScrollPane(groupsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Liste des Groupes",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        // Panel central qui contient recherche et tableau
        JPanel centerPanel = new StyledPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Event handlers
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> loadGroups());
        
        // Recherche en temps réel
        searchField.addActionListener(e -> filterGroups());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterGroups(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterGroups(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterGroups(); }
        });
    }

    private void loadGroups() {
        tableModel.setRowCount(0);
        try {
            List<Group> groups = groupService.getAllGroups();
            for (Group group : groups) {
                tableModel.addRow(new Object[]{
                    group.getName(),
                    group.getDescription() != null ? group.getDescription() : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterGroups() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        try {
            List<Group> groups = groupService.getAllGroups();
            for (Group group : groups) {
                if (group.getName().toLowerCase().contains(searchText) ||
                    (group.getDescription() != null && group.getDescription().toLowerCase().contains(searchText))) {
                    tableModel.addRow(new Object[]{
                        group.getName(),
                        group.getDescription() != null ? group.getDescription() : ""
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        GroupForm form = new GroupForm((JFrame) SwingUtilities.getWindowAncestor(this), null, this::loadGroups);
        form.setVisible(true);
    }

    private void handleEdit() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un groupe à modifier", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String groupName = (String) tableModel.getValueAt(selectedRow, 0);
            List<Group> allGroups = groupService.getAllGroups();
            Group group = allGroups.stream()
                .filter(g -> g.getName().equals(groupName))
                .findFirst()
                .orElseThrow(() -> new Exception("Groupe non trouvé"));
            GroupForm form = new GroupForm((JFrame) SwingUtilities.getWindowAncestor(this), group, this::loadGroups);
            form.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un groupe à supprimer", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer ce groupe ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String groupName = (String) tableModel.getValueAt(selectedRow, 0);
                List<Group> allGroups = groupService.getAllGroups();
                Group group = allGroups.stream()
                    .filter(g -> g.getName().equals(groupName))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Groupe non trouvé"));
                groupService.deleteGroup(group.getId());
                JOptionPane.showMessageDialog(this, "Groupe supprimé avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadGroups();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

