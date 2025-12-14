package com.example.academic.ui.pages;

import com.example.academic.model.Subject;
import com.example.academic.model.Group;
import com.example.academic.service.SubjectService;
import com.example.academic.service.GroupService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Page d'assignation des matières aux groupes
 */
public class AssignSubjectGroupPage extends StyledPanel {
    private JComboBox<SubjectItem> subjectComboBox;
    private JComboBox<GroupItem> groupComboBox;
    private JTable assignmentsTable;
    private DefaultTableModel tableModel;
    private SubjectService subjectService;
    private GroupService groupService;

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

    public AssignSubjectGroupPage() {
        this.subjectService = new SubjectService();
        this.groupService = new GroupService();
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        loadAssignments();
    }

    private void initializeComponents() {
        // Combo boxes
        subjectComboBox = new JComboBox<>();
        groupComboBox = new JComboBox<>();
        
        // Tableau des assignations
        String[] columns = {"Matière", "Code", "Groupe", "Description"};
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
        
        loadSubjects();
        loadGroups();
    }

    private void setupLayout() {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Titre
        JLabel titleLabel = new JLabel("Assigner Matière → Groupe");
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
        
        // Matière
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Matière:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(subjectComboBox, gbc);
        
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
            com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                com.example.academic.database.MongoDBConnection.getInstance()
                    .getDatabase()
                    .getCollection("group_subjects");
            
            for (org.bson.Document doc : collection.find()) {
                String groupId = doc.getString("groupId");
                String subjectId = doc.getString("subjectId");
                
                try {
                    Group group = groupService.findById(groupId);
                    Subject subject = subjectService.findById(subjectId);
                    
                    tableModel.addRow(new Object[]{
                        subject.getName(),
                        subject.getCode(),
                        group.getName(),
                        group.getDescription()
                    });
                } catch (Exception e) {
                    // Ignorer les erreurs pour les relations invalides
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des assignations: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAssign() {
        SubjectItem selectedSubject = (SubjectItem) subjectComboBox.getSelectedItem();
        GroupItem selectedGroup = (GroupItem) groupComboBox.getSelectedItem();
        
        if (selectedSubject == null || selectedGroup == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une matière et un groupe",
                "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Vérifier si la relation existe déjà
            com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                com.example.academic.database.MongoDBConnection.getInstance()
                    .getDatabase()
                    .getCollection("group_subjects");
            
            org.bson.Document existing = collection.find(
                new org.bson.Document("groupId", selectedGroup.getGroup().getId())
                    .append("subjectId", selectedSubject.getSubject().getId())
            ).first();
            
            if (existing != null) {
                JOptionPane.showMessageDialog(this, 
                    "Cette assignation existe déjà",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Créer la relation
            org.bson.Document doc = new org.bson.Document("groupId", selectedGroup.getGroup().getId())
                .append("subjectId", selectedSubject.getSubject().getId());
            collection.insertOne(doc);
            
            JOptionPane.showMessageDialog(this, 
                "Assignation réussie: " + selectedSubject.getSubject().getName() + " → " + selectedGroup,
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
        
        String subjectName = (String) tableModel.getValueAt(selectedRow, 0);
        String groupName = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer l'assignation: " + subjectName + " → " + groupName + "?",
            "Confirmer suppression", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Trouver le subject et group correspondants
                List<Subject> subjects = subjectService.getAllSubjects();
                Subject subject = null;
                for (Subject s : subjects) {
                    if (s.getName().equals(subjectName)) {
                        subject = s;
                        break;
                    }
                }
                
                List<Group> groups = groupService.getAllGroups();
                Group group = null;
                for (Group g : groups) {
                    if (g.getName().equals(groupName)) {
                        group = g;
                        break;
                    }
                }
                
                if (subject != null && group != null) {
                    // Supprimer la relation dans MongoDB
                    com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                        com.example.academic.database.MongoDBConnection.getInstance()
                            .getDatabase()
                            .getCollection("group_subjects");
                    
                    collection.deleteOne(new org.bson.Document("groupId", group.getId())
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

