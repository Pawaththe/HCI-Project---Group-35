package furniture.management.system.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Login extends JFrame {
    private LoginSuccessListener listener;

    public Login(LoginSuccessListener listener) {
        super("Furniture Management System");
        this.listener = listener;

        Map<String, String> userCredentials = new HashMap<>();
        userCredentials.put("designer", "designer123");
        userCredentials.put("admin", "admin123");

        // Custom Background Panel
        class BackgroundPanel extends JPanel {
            private Image bgImage;

            public BackgroundPanel() {
                bgImage = new ImageIcon(ClassLoader.getSystemResource("icons/best-furniture-for-your-home-2022-hero.jpg")).getImage();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Login Panel with GridBagLayout and rounded corners
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        loginPanel.setOpaque(false);
        loginPanel.setBackground(new Color(255, 255, 255, 180)); // Semi-transparent white
        loginPanel.setPreferredSize(new Dimension(450, 320));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Heading
        JLabel heading = new JLabel("Furniture Matrix");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(heading, gbc);

        // Username
        JLabel label2 = new JLabel("Username:");
        label2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(label2, gbc);

        JTextField textField1 = new JTextField(15);
        textField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField1.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(textField1, gbc);

        // Password
        JLabel label3 = new JLabel("Password:");
        label3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(label3, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(passwordField, gbc);

        // Brown Button
        Color startColor = new Color(102, 51, 0);
        Color endColor = new Color(66, 33, 0);
        RoundedButton b1 = new RoundedButton("Login", startColor, endColor);
        b1.setForeground(new Color(255, 248, 240)); // Cream text
        b1.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(b1, gbc);

        // Center container
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerPanel.add(loginPanel, centerGbc);

        // Quote
        JPanel quotePanel = new JPanel();
        quotePanel.setBackground(new Color(0, 0, 0, 150));
        quotePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel quoteLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<b>\"Designing comfort, crafting style —<br>helping you create spaces that feel like home.\"</b>"
                + "</div></html>");
        quoteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        quoteLabel.setForeground(Color.WHITE);
        quotePanel.add(quoteLabel);

        centerGbc.gridy = 1;
        centerGbc.insets = new Insets(20, 10, 10, 10);
        centerPanel.add(quotePanel, centerGbc);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.BLACK);
        JLabel footerLabel = new JLabel("© 2025 Furniture Matrix. All rights reserved.");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        // Frame setup
        setUndecorated(false);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Fix: Make restore button functional
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                revalidate();
                repaint();
            }
        });

        // Login Action
        b1.addActionListener(e -> {
            String username = textField1.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(Login.this, "Username and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                listener.onLoginSuccess(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(Login.this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
