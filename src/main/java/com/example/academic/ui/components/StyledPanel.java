package com.example.academic.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel avec style moderne
 */
public class StyledPanel extends JPanel {
    public StyledPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public StyledPanel(LayoutManager layout) {
        super(layout);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}

