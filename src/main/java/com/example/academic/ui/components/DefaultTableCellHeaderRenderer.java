package com.example.academic.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renderer personnalisé pour les en-têtes de tableau avec meilleure lisibilité
 */
public class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer {
    public DefaultTableCellHeaderRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
        setFont(new Font("Segoe UI", Font.BOLD, 15));
        setBackground(new Color(70, 130, 180));
        setForeground(Color.WHITE);
        setOpaque(true);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 2, 1, new Color(50, 100, 150)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(value != null ? value.toString() : "");
        return this;
    }
}

