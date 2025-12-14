package com.example.academic.ui;

import com.example.academic.enums.UserRole;
import com.example.academic.exception.InvalidCredentialsException;
import com.example.academic.model.User;
import com.example.academic.service.UserService;
import com.example.academic.ui.components.ModernButton;
import com.example.academic.util.SessionManager;

import javax.swing.*;
import java.awt.*;


public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ModernButton loginButton;
    private ModernButton cancelButton;
    private JLabel errorLabel;
    private JPanel mainPanel;
    private UserService userService;

    public LoginForm() {
        this.userService = new UserService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Système d'Information Académique");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 247, 250));

        // Champs avec style moderne
        usernameField = new JTextField(25);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        usernameField.setBackground(Color.WHITE);

        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setBackground(Color.WHITE);

        loginButton = new ModernButton("Se connecter", 
            new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 40));

        cancelButton = new ModernButton("Annuler", 
            new Color(158, 158, 158), new Color(189, 189, 189), Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(211, 47, 47));
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal avec fond dégradé
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(70, 130, 180),
                    0, getHeight(), new Color(100, 149, 237)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Logo/Titre
        JLabel titleLabel = new JLabel("Système Académique");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel subtitleLabel = new JLabel("Connexion");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(240, 240, 240));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        // Panel de formulaire (blanc)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));
        formPanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));

        // Labels avec style
        JLabel usernameLabel = new JLabel("Nom d'utilisateur");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(70, 70, 70));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(70, 70, 70));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 8, 0));

        // Ajout des composants au formulaire
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(errorLabel);
        formPanel.add(Box.createVerticalStrut(20));

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel);

        // Info en bas
        JLabel infoLabel = new JLabel("<html><center style='color: #888; font-size: 10px;'>Username = Prénom<br>Password = Nom (sensible à la casse)</center></html>");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Assemblage
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(formPanel);
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        // Bouton Connexion
        loginButton.addActionListener(e -> handleLogin());

        // Bouton Annuler
        cancelButton.addActionListener(e -> System.exit(0));

        // Entrée sur les champs
        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> handleLogin());

        // Effet focus sur les champs
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validation
        if (username.isEmpty()) {
            showError("Veuillez entrer un nom d'utilisateur");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Veuillez entrer un mot de passe");
            passwordField.requestFocus();
            return;
        }

        // Authentification
        try {
            User user = userService.authenticate(username, password);
            
            // Sauvegarder la session
            SessionManager.getInstance().setCurrentUser(user);
            
            // Masquer le formulaire de connexion
            this.setVisible(false);
            
            // Rediriger selon le rôle
            redirectToDashboard(user.getRole());
            
        } catch (InvalidCredentialsException e) {
            showError("Nom d'utilisateur ou mot de passe incorrect");
            passwordField.setText("");
            passwordField.requestFocus();
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void redirectToDashboard(UserRole role) {
        switch (role) {
            case ADMIN:
                AdminDashboard adminDashboard = new AdminDashboard();
                adminDashboard.setVisible(true);
                break;
            case TEACHER:
                TeacherDashboard teacherDashboard = new TeacherDashboard();
                teacherDashboard.setVisible(true);
                break;
            case STUDENT:
                StudentDashboard studentDashboard = new StudentDashboard();
                studentDashboard.setVisible(true);
                break;
            default:
                showError("Rôle non reconnu");
                this.setVisible(true);
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public static void main(String[] args) {
        // Utiliser le look and feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
    }
}