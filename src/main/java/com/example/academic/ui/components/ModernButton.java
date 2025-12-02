package com.example.academic.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Bouton moderne avec style personnalisé
 */
public class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;

    public ModernButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = hoverColor;
        
        setBackground(bgColor);
        setForeground(textColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Effet hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(ModernButton.this.hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(ModernButton.this.backgroundColor);
            }
        });
    }

    public ModernButton(String text) {
        this(text, new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE);
    }
}

