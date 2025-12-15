package com.example.academic.ui;

import com.example.academic.ui.components.ModernButton;
import com.example.academic.ui.components.StyledPanel;
import com.example.academic.ui.pages.GroupsManagementPage;
import com.example.academic.ui.pages.SubjectsManagementPage;
import com.example.academic.ui.pages.TeachersManagementPage;
import com.example.academic.ui.pages.StudentsManagementPage;
import com.example.academic.ui.pages.AssignTeacherSubjectPage;
import com.example.academic.ui.pages.AssignStudentGroupPage;
import com.example.academic.ui.pages.AssignSubjectGroupPage;
import com.example.academic.util.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard Administrateur - Interface moderne
 */
public class AdminDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    
    public AdminDashboard() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("Administrator Dashboard - Academic System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Vérifier la session
        if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Session expired. Please log in again.");
            dispose();
            new LoginForm().setVisible(true);
            return;
        }
        
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header avec bienvenue
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Menu latéral moderne
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Contenu principal avec CardLayout
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Initialiser toutes les pages
        initializePages();
        
        // Page d'accueil par défaut
        showHomePage();
    }

    private void initializePages() {
        // Page Groupes
        GroupsManagementPage groupsPage = new GroupsManagementPage();
        mainContentPanel.add(groupsPage, "groups");
        
        TeachersManagementPage teachersPage = new TeachersManagementPage();
        mainContentPanel.add(teachersPage, "teachers");
        
        StudentsManagementPage studentsPage = new StudentsManagementPage();
        mainContentPanel.add(studentsPage, "students");
        
        SubjectsManagementPage subjectsPage = new SubjectsManagementPage();
        mainContentPanel.add(subjectsPage, "subjects");
        
        // Pages d'assignation
        AssignTeacherSubjectPage assignTeacherSubjectPage = new AssignTeacherSubjectPage();
        mainContentPanel.add(assignTeacherSubjectPage, "assignTeacherSubject");
        
        AssignStudentGroupPage assignStudentGroupPage = new AssignStudentGroupPage();
        mainContentPanel.add(assignStudentGroupPage, "assignStudentGroup");
        
        AssignSubjectGroupPage assignSubjectGroupPage = new AssignSubjectGroupPage();
        mainContentPanel.add(assignSubjectGroupPage, "assignSubjectGroup");
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
        
        ModernButton logoutButton = new ModernButton("Logout", 
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

        // Titre
        JLabel titleLabel = new JLabel("MENU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
        sidebar.add(titleLabel);

        // Boutons de menu
        addMenuButton(sidebar, " Home", "home");
        addMenuButton(sidebar, " Groups", "groups");
        addMenuButton(sidebar, " Teachers", "teachers");
        addMenuButton(sidebar, " Students", "students");
        addMenuButton(sidebar, " Subjects", "subjects");
        
        sidebar.add(Box.createVerticalStrut(20));
        
        JLabel assignTitle = new JLabel("ASSIGNATIONS");
        assignTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        assignTitle.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        assignTitle.setForeground(new Color(100, 100, 100));
        sidebar.add(assignTitle);
        
        addMenuButton(sidebar, "🔗 Teacher → Subject", "assignTeacherSubject");
        addMenuButton(sidebar, "🔗 Student → Group", "assignStudentGroup");
        addMenuButton(sidebar, "🔗 Subject → Group", "assignSubjectGroup");

        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }

    private JButton currentSelectedButton = null;
    
    private void addMenuButton(JPanel panel, String text, String action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != currentSelectedButton) {
                    button.setBackground(new Color(240, 240, 240));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != currentSelectedButton) {
                    button.setBackground(Color.WHITE);
                }
            }
        });
        
        button.addActionListener(e -> {
            // Désélectionner le bouton précédent
            if (currentSelectedButton != null) {
                currentSelectedButton.setBackground(Color.WHITE);
            }
            // Sélectionner le nouveau bouton
            currentSelectedButton = button;
            button.setBackground(new Color(230, 240, 255));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(10, 12, 10, 15)
            ));
            showPage(action);
        });
        
        panel.add(button);
    }

    private void showPage(String page) {
        cardLayout.show(mainContentPanel, page);
    }

    private void showHomePage() {
        JPanel homePanel = new StyledPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel welcomeLabel = new JLabel("Welcome to the Academic Information System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        homePanel.add(welcomeLabel, gbc);
        
        JLabel infoLabel = new JLabel("<html><center>Select an option from the left menu to get started</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        homePanel.add(infoLabel, gbc);
        
        mainContentPanel.add(homePanel, "home");
        cardLayout.show(mainContentPanel, "home");
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to log out?",
            "Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            dispose();
            new LoginForm().setVisible(true);
        }
    }
}

