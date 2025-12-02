package com.example.academic.ui;

import com.example.academic.model.Grade;
import com.example.academic.service.GradeService;
import com.example.academic.service.SubjectService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;
import com.example.academic.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dashboard Étudiant - Interface moderne
 */
public class StudentDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private GradeService gradeService;
    private String studentId;

    public StudentDashboard() {
        this.gradeService = new GradeService();
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("Dashboard Étudiant - Système Académique");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Session expirée. Veuillez vous reconnecter.");
            dispose();
            new LoginForm().setVisible(true);
            return;
        }

        studentId = SessionManager.getInstance().getCurrentUser().getId();
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        add(mainContentPanel, BorderLayout.CENTER);
        
        showHomePage();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new StyledPanel(new BorderLayout());
        header.setBackground(new Color(70, 130, 180));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        String welcomeText = "Bienvenue, " + SessionManager.getInstance().getCurrentUser().getFirstName() + 
                            " " + SessionManager.getInstance().getCurrentUser().getLastName();
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        
        ModernButton logoutButton = new ModernButton("Déconnexion", 
            new Color(198, 40, 40), new Color(211, 47, 47), Color.WHITE);
        logoutButton.addActionListener(e -> handleLogout());
        
        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);
        
        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new StyledPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));

        JLabel titleLabel = new JLabel("MENU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
        sidebar.add(titleLabel);

        addMenuButton(sidebar, "🏠 Accueil", "home");
        addMenuButton(sidebar, "📊 Mes Notes", "viewGrades");

        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }

    private void addMenuButton(JPanel panel, String text, String action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> showPage(action));
        panel.add(button);
    }

    private void showPage(String page) {
        cardLayout.show(mainContentPanel, page);
    }

    private void showHomePage() {
        JPanel homePanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel welcomeLabel = new JLabel("Bienvenue dans votre Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        homePanel.add(welcomeLabel, gbc);
        
        JLabel infoLabel = new JLabel("<html><center>Consultez vos notes dans la section 'Mes Notes'</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        homePanel.add(infoLabel, gbc);
        
        mainContentPanel.add(homePanel, "home");
        
        // Page des notes
        JPanel gradesPanel = createGradesPanel();
        mainContentPanel.add(gradesPanel, "viewGrades");
        
        cardLayout.show(mainContentPanel, "home");
    }

    private JPanel createGradesPanel() {
        StyledPanel panel = new StyledPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Mes Notes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JPanel topPanel = new StyledPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        // Filtre par matière
        JComboBox<String> subjectFilter = new JComboBox<>();
        subjectFilter.addItem("Toutes les matières");
        try {
            SubjectService subjectService = new SubjectService();
            List<com.example.academic.model.Subject> subjects = subjectService.getAllSubjects();
            for (com.example.academic.model.Subject subject : subjects) {
                subjectFilter.addItem(subject.getName());
            }
        } catch (Exception e) {
            // Ignorer
        }
        
        subjectFilter.addActionListener(e -> refreshGradesTable((DefaultTableModel) ((JTable) ((JScrollPane) panel.getComponent(1)).getViewport().getView()).getModel(), subjectFilter));
        
        JPanel filterPanel = new StyledPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrer par matière: "));
        filterPanel.add(subjectFilter);
        
        ModernButton refreshButton = new ModernButton("Actualiser", new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
        refreshButton.addActionListener(e -> refreshGradesTable((DefaultTableModel) ((JTable) ((JScrollPane) panel.getComponent(1)).getViewport().getView()).getModel(), subjectFilter));
        filterPanel.add(refreshButton);
        
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        String[] columns = {"Matière", "Note", "Enseignant", "Commentaire", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Notes"));
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Charger les notes
        refreshGradesTable(model, subjectFilter);
        
        return panel;
    }

    private void refreshGradesTable(DefaultTableModel model, JComboBox<String> subjectFilter) {
        model.setRowCount(0);
        try {
            List<Grade> grades = gradeService.getGradesByStudent(studentId);
            String selectedSubject = (String) subjectFilter.getSelectedItem();
            
            for (Grade grade : grades) {
                String subjectName = "N/A";
                String teacherName = "N/A";
                
                try {
                    SubjectService subjectService = new SubjectService();
                    com.example.academic.model.Subject subject = subjectService.findById(grade.getSubjectId());
                    subjectName = subject.getName();
                    
                    // Filtrer si une matière est sélectionnée
                    if (selectedSubject != null && !selectedSubject.equals("Toutes les matières") && !selectedSubject.equals(subjectName)) {
                        continue;
                    }
                } catch (Exception e) {}
                
                try {
                    com.example.academic.service.UserService userService = new com.example.academic.service.UserService();
                    com.example.academic.model.User teacher = userService.findById(grade.getTeacherId());
                    teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                } catch (Exception e) {}
                
                model.addRow(new Object[]{
                    subjectName,
                    grade.getGrade(),
                    teacherName,
                    grade.getComment() != null ? grade.getComment() : "",
                    grade.getCreatedAt() != null ? grade.getCreatedAt().toString() : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir vous déconnecter ?", 
            "Déconnexion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            dispose();
            new LoginForm().setVisible(true);
        }
    }
}

