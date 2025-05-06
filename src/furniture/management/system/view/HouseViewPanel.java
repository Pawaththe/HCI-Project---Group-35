package furniture.management.system.view;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class HouseViewPanel extends JPanel {
    private final List<RoomDesign> designs = new ArrayList<>();

    public HouseViewPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        loadDesigns();

        // Modern panel for room previews
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(new Color(240, 240, 240));

        // Modern scroll pane
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Modern scrollbar styling
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBackground(new Color(240, 240, 240));
        verticalScrollBar.setBorder(BorderFactory.createEmptyBorder());

        // Add modern title
        JLabel titleLabel = new JLabel("Your Room Designs", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        titleLabel.setForeground(new Color(60, 60, 60));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Create modern preview panels
        for (RoomDesign design : designs) {
            GLJPanel glPanel = createRoomPreview(design);

            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

// Safe label creation:
            String designName = "Design " + (designs.indexOf(design) + 1);
            JLabel designLabel = new JLabel(designName, SwingConstants.CENTER);
            designLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            designLabel.setForeground(new Color(80, 80, 80));
            designLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

            cardPanel.add(designLabel, BorderLayout.NORTH);
            cardPanel.add(glPanel, BorderLayout.CENTER);

            // ➔ Now actually add to gridPanel
            gridPanel.add(cardPanel);
        }}

    private void loadDesigns() {
        File dir = new File(".");
        for (File file : dir.listFiles((d, name) -> name.startsWith("saved_design_") && name.endsWith(".dat"))) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                RoomDesign design = (RoomDesign) in.readObject();
                designs.add(design);
            } catch (Exception ex) {
                System.err.println("Error loading design: " + ex.getMessage());
            }
        }
    }

    private GLJPanel createRoomPreview(RoomDesign design) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        GLJPanel glPanel = new GLJPanel(caps);
        glPanel.setPreferredSize(new Dimension(400, 300));

        // Modern OpenGL panel styling
        glPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        glPanel.addGLEventListener(new GLEventListener() {
            // [Keep all your existing OpenGL code exactly as is]
            // This includes all the init(), dispose(), display(), reshape() methods
            // and all the wall/floor drawing methods
            // ...

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

            // All your existing drawing methods...
        });

        return glPanel;
    }
}