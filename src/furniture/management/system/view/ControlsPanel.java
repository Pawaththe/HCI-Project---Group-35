package furniture.management.system.view;

import furniture.management.system.model.ColorManager;
import furniture.management.system.model.LightingManager;
import furniture.management.system.model.RotationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ControlsPanel extends JPanel {

    public ControlsPanel(RotationManager rotationManager, ColorManager colorManager, LightingManager lightingManager) {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240)); // Light gray background
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Modern colors
        Color buttonColor = new Color(0x009688); // Teal
        Color hoverColor = new Color(0x4DB6AC);  // Light teal
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Create modern buttons without FlatLaf
        JButton resetColorButton = createModernButton("Reset Colors", buttonColor, hoverColor, buttonFont);
        JButton floorColorButton = createModernButton("Floor Color", buttonColor, hoverColor, buttonFont);
        JButton backgroundColorButton = createModernButton("Background Color", buttonColor, hoverColor, buttonFont);
        JButton leftButton = createModernButton("↻ Rotate Left", buttonColor, hoverColor, buttonFont);
        JButton rightButton = createModernButton("↺ Rotate Right", buttonColor, hoverColor, buttonFont);
        JButton changeMaterialButton = createModernButton("Change Material", buttonColor, hoverColor, buttonFont);
        JButton lightButton = createModernButton("☀ Toggle Light", buttonColor, hoverColor, buttonFont);

        // (Keep all your existing ActionListeners here...)

        // Add buttons in a grid layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(resetColorButton, gbc);

        gbc.gridx++;
        add(floorColorButton, gbc);

        gbc.gridx++;
        add(backgroundColorButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(leftButton, gbc);

        gbc.gridx++;
        add(rightButton, gbc);

        gbc.gridx++;
        add(changeMaterialButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        add(lightButton, gbc);
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor, Font font) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }

                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border (flat design)
            }
        };

        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        return button;
    }
}