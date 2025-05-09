package furniture.management.system.view;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import furniture.management.system.model.Furniture;
import furniture.management.system.model.FurnitureFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HouseViewPanel extends JPanel {
    private final List<RoomDesign> designs = new ArrayList<>();
    private RoomDesign selectedDesign = null;
    private JPanel cardsPanel;
    private File selectedFile = null;

    public HouseViewPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        loadDesigns();
        initUI();
    }

    private void initUI() {
        // Modern panel for room previews
        cardsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        cardsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        cardsPanel.setBackground(new Color(240, 240, 240));

        // Modern scroll pane
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Modern scrollbar styling
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBackground(new Color(240, 240, 240));
        verticalScrollBar.setBorder(BorderFactory.createEmptyBorder());

        // Modern title
        JLabel titleLabel = new JLabel("Your Room Designs", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        titleLabel.setForeground(new Color(60, 60, 60));

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setEnabled(false);
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteSelectedDesign());

        buttonPanel.add(deleteButton);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        createDesignCards();

        // Add property change listener to enable/disable delete button
        addPropertyChangeListener("selectedDesign", evt -> {
            deleteButton.setEnabled(selectedDesign != null);
        });
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(220, 80, 80));
            }
        });
    }

    private void createDesignCards() {
        cardsPanel.removeAll();
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("saved_design_") && name.endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    RoomDesign design = (RoomDesign) in.readObject();
                    designs.add(design);
                    JPanel card = createDesignCard(design, file);
                    cardsPanel.add(card);
                } catch (Exception ex) {
                    System.err.println("Error loading design: " + ex.getMessage());
                }
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createDesignCard(RoomDesign design, File file) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(createDefaultBorder());

        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedDesign = design;
                selectedFile = file;
                resetCardBorders();
                cardPanel.setBorder(createSelectedBorder());
                firePropertyChange("selectedDesign", null, selectedDesign);
            }
        });

        // Safe label creation
        String designName = file.getName().replace("saved_design_", "").replace(".dat", "");
        JLabel designLabel = new JLabel(designName, SwingConstants.CENTER);
        designLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        designLabel.setForeground(new Color(80, 80, 80));
        designLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        GLJPanel glPanel = createPreviewPanel(design);

        cardPanel.add(designLabel, BorderLayout.NORTH);
        cardPanel.add(glPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    private void deleteSelectedDesign() {
        if (selectedFile != null && selectedDesign != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this design?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (selectedFile.delete()) {
                    designs.remove(selectedDesign);
                    selectedDesign = null;
                    selectedFile = null;
                    createDesignCards();
                    firePropertyChange("selectedDesign", null, null);
                    JOptionPane.showMessageDialog(this, "Design deleted successfully");
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to delete the design file",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private Border createDefaultBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private Border createSelectedBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private void resetCardBorders() {
        for (Component comp : cardsPanel.getComponents()) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setBorder(createDefaultBorder());
            }
        }
    }

    private GLJPanel createPreviewPanel(RoomDesign design) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        GLJPanel glPanel = new GLJPanel(caps);
        glPanel.setPreferredSize(new Dimension(400, 300));

        // Modern OpenGL panel styling
        glPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        glPanel.addGLEventListener(new GLEventListener() {
            @Override
            public void init(GLAutoDrawable d) {
                // Your existing init code
            }

            @Override
            public void dispose(GLAutoDrawable d) {
                // Your existing dispose code
            }

            @Override
            public void display(GLAutoDrawable d) {
                // Your existing display code
            }

            @Override
            public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
                // Your existing reshape code
            }
        });

        return glPanel;
    }

    private void loadDesigns() {
        designs.clear();
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("saved_design_") && name.endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    RoomDesign design = (RoomDesign) in.readObject();
                    designs.add(design);
                } catch (Exception ex) {
                    System.err.println("Error loading design: " + ex.getMessage());
                }
            }
        }
    }

    public RoomDesign getSelectedDesign() {
        return selectedDesign;
    }

    public void refreshDesigns() {
        loadDesigns();
        createDesignCards();
    }
}