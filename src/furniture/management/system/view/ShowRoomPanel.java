package furniture.management.system.view;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;
import furniture.management.system.model.RotationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class ShowRoomPanel extends JPanel {
    private final RoomConfigPanel roomConfigPanel = new RoomConfigPanel();
    private final RotationManager rotationManager = new RotationManager();
    private final FurnitureConfigPanel furnitureConfigPanel;
    private final Renderer2D renderer2D;
    private final Renderer3D renderer3D;
    private final GLJPanel gl2DPanel;
    private final GLJPanel gl3DPanel;
    private final JPanel rightPanel;
    private final CardLayout cardLayout;
    private final JButton show2DButton = new JButton("Show 2D View");
    private final JButton show3DButton = new JButton("Show 3D View");
    private float rotationX = 30f;
    private float rotationY = 0f;
    private int lastX = -1;
    private int lastY = -1;
    private final float rotationSpeed = 0.5f;

    private static final String VIEW_2D = "2D";
    private static final String VIEW_3D = "3D";

    private final Color backgroundColor = new Color(245, 245, 250);
    private final Color buttonColor = new Color(0x009688);
    private final Color activeButtonColor = new Color(0x00796B);

    public ShowRoomPanel() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Initialize renderers and panels
        renderer2D = new Renderer2D(roomConfigPanel);
        renderer3D = new Renderer3D(roomConfigPanel, rotationManager);
        furnitureConfigPanel = new FurnitureConfigPanel(this::addFurniture, this::deleteFurniture);

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        gl2DPanel = new GLJPanel(caps);
        gl3DPanel = new GLJPanel(caps);

        gl2DPanel.addGLEventListener(renderer2D);
        gl3DPanel.addGLEventListener(renderer3D);

        renderer3D.setFurnitureConfigPanel(furnitureConfigPanel);
        renderer3D.setGL3DPanel(gl3DPanel);
        renderer2D.setRotationAngleY(rotationY); // Ensure 2D renderer is initialized

        // Setup UI components
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.add(gl2DPanel, VIEW_2D);
        rightPanel.add(gl3DPanel, VIEW_3D);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardLayout.show(rightPanel, VIEW_3D); // Default to 3D view

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(backgroundColor);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.add(roomConfigPanel, BorderLayout.NORTH);
        leftPanel.add(furnitureConfigPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup listeners
        setupMouseListeners();
        setupKeyListeners();
        setupRoomConfigListeners();
        setupFieldListeners();
        setupColorListeners();
    }

    private void deleteFurniture() {
        // Implementation to delete selected furniture
        RenderItem selected3D = renderer3D.getSelectedItem();
        if (selected3D != null) {
            // Remove from 3D renderer
            renderer3D.getItems().remove(selected3D);

            // Find and remove corresponding 2D item
            for (RenderItem item2D : renderer2D.getItems()) {
                if (item2D.type.equals(selected3D.type) &&
                        item2D.x == selected3D.x &&
                        item2D.z == -selected3D.z) {
                    renderer2D.getItems().remove(item2D);
                    break;
                }
            }

            // Clear selection and update views
            furnitureConfigPanel.setSelectedItem(null, null);
            gl2DPanel.repaint();
            gl3DPanel.repaint();
        }
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton saveBtn = createStyledButton("Save Design");
        JButton clearBtn = createStyledButton("Clear");
        JButton houseViewBtn = createStyledButton("Saved Files");

        saveBtn.addActionListener(e -> saveDesign());
        clearBtn.addActionListener(e -> clearDesign());
        houseViewBtn.addActionListener(e -> showHouseView());

        styleViewButton(show2DButton);
        styleViewButton(show3DButton);
        show2DButton.addActionListener(e -> show2DView());
        show3DButton.addActionListener(e -> show3DView());
        show3DButton.setBackground(activeButtonColor); // Default to 3D view active

        bottomPanel.add(saveBtn);
        bottomPanel.add(clearBtn);
        bottomPanel.add(houseViewBtn);
        bottomPanel.add(show2DButton);
        bottomPanel.add(show3DButton);

        return bottomPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(activeButtonColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != show2DButton && button != show3DButton) {
                    button.setBackground(buttonColor);
                } else {
                    updateViewButtonColor(button);
                }
            }
        });
        return button;
    }

    private void styleViewButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!(button == show2DButton && cardLayoutIs2D()) && !(button == show3DButton && !cardLayoutIs2D())) {
                    button.setBackground(activeButtonColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                updateViewButtonColor(button);
            }
        });
    }

    private void updateViewButtonColor(JButton button) {
        if (button == show2DButton && cardLayoutIs2D()) {
            button.setBackground(activeButtonColor);
        } else if (button == show3DButton && !cardLayoutIs2D()) {
            button.setBackground(activeButtonColor);
        } else {
            button.setBackground(buttonColor);
        }
    }

    private boolean cardLayoutIs2D() {
        return gl2DPanel.isVisible();
    }

    private void show2DView() {
        syncRenderItems(); // Sync data before rendering
        cardLayout.show(rightPanel, VIEW_2D);
        gl2DPanel.repaint();
    }

    private void show3DView() {
        syncRenderItems();
        cardLayout.show(rightPanel, VIEW_3D);
        show3DButton.setBackground(activeButtonColor);
        show2DButton.setBackground(buttonColor);
        gl3DPanel.requestFocusInWindow();
        gl3DPanel.repaint();
    }

    private void addFurniture() {
        String type = furnitureConfigPanel.getSelectedFurnitureType();
        float scale = furnitureConfigPanel.getScale();
        renderer2D.add(type, scale);
        renderer3D.add(type, scale);
        syncRenderItems();
        gl2DPanel.repaint();
        gl3DPanel.repaint();
    }

    private void syncRenderItems() {
        // Clear existing 2D items
        renderer2D.clearItems();

        // Copy all items from 3D to 2D
        for (RenderItem item3D : renderer3D.getItems()) {
            renderer2D.add(item3D.type, item3D.scale);
            RenderItem item2D = renderer2D.getItems().get(renderer2D.getItems().size() - 1);

            // Sync position, rotation, and dimensions
            item2D.x = item3D.x;
            item2D.z = -item3D.z; // Invert Z for 2D view
            item2D.rotationY = item3D.rotationY;
            item2D.width = item3D.width;
            item2D.height = item3D.height;
        }

        // Sync selected item
        RenderItem selected3D = renderer3D.getSelectedItem();
        if (selected3D != null) {
            // Find corresponding 2D item
            for (RenderItem item2D : renderer2D.getItems()) {
                if (item2D.type.equals(selected3D.type) &&
                        item2D.x == selected3D.x &&
                        item2D.z == -selected3D.z) {
                    renderer2D.setSelectedItem(item2D);
                    break;
                }
            }
        } else {
            renderer2D.setSelectedItem(null);
        }
    }

    private void clearDesign() {
        renderer2D.clearItems();
        renderer3D.clearItems();
        gl2DPanel.repaint();
        gl3DPanel.repaint();
    }

    private void saveDesign() {
        RoomDesign design = new RoomDesign(
                renderer3D.getItems(),
                roomConfigPanel.getRoomShape(),
                roomConfigPanel.getRoomWidth(),
                roomConfigPanel.getRoomDepth(),
                roomConfigPanel.getWallColor(),
                roomConfigPanel.getFloorColor()
        );
        String filename = "saved_design_" + UUID.randomUUID() + ".dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(design);
            JOptionPane.showMessageDialog(this, "Design saved as " + filename);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving design: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showHouseView() {
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        HouseViewPanel houseViewPanel = new HouseViewPanel();
        JButton saveBtn = createStyledButton("Save Design");
        JButton editButton = new JButton("Edit Selected Design");
        editButton.setEnabled(false);

        houseViewPanel.addPropertyChangeListener("selectedDesign", e -> {
            editButton.setEnabled(houseViewPanel.getSelectedDesign() != null);
        });

        editButton.addActionListener(e -> {
            RoomDesign design = houseViewPanel.getSelectedDesign();
            if (design != null) {
                loadDesign(design);
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);

        dialog.add(houseViewPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void loadDesign(RoomDesign design) {
        // Clear current design
        clearDesign();

        // Load room configuration
        roomConfigPanel.setRoomShape(design.getShape());
        roomConfigPanel.setRoomWidth(design.getWidth());
        roomConfigPanel.setRoomDepth(design.getDepth());
        roomConfigPanel.setWallColor(design.getWallColor());
        roomConfigPanel.setFloorColor(design.getFloorColor());

        // Load furniture items
        for (RenderItem item : design.getItems()) {
            renderer3D.add(item.getType(), item.getScale());
            RenderItem addedItem = renderer3D.getItems().get(renderer3D.getItems().size()-1);
            addedItem.setX(item.getX());
            addedItem.setZ(item.getZ());
            addedItem.setRotationY(item.getRotationY());
        }

        // Refresh views
        syncRenderItems();
        gl2DPanel.repaint();
        gl3DPanel.repaint();
    }

    private void setupMouseListeners() {
        MouseAdapter sharedMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    renderer3D.selectItemAt(lastX, lastY);
                    renderer2D.setSelectedItem(renderer3D.getSelectedItem());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    renderer3D.deselectItem();
                    renderer2D.setSelectedItem(null);
                }
                gl2DPanel.repaint();
                gl3DPanel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (lastX != -1 && lastY != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        RenderItem selected = renderer3D.getSelectedItem();
                        if (selected != null) {
                            float dx = (x - lastX) * 0.05f;
                            float dz = (y - lastY) * 0.05f;

                            renderer2D.moveSelectedItem(dx, dz);   // Keep this direction
                            selected.x += dx;
                            selected.z += dz; //  Fix: do NOT invert dz

                            // Clamp within room boundaries for 3D
                            float halfW = roomConfigPanel.getRoomWidth() / 2f;
                            float halfD = roomConfigPanel.getRoomDepth() / 2f;
                            float hw = selected.width * selected.scale / 2f;
                            float hh = selected.height * selected.scale / 2f;
                            selected.x = Math.max(-halfW + hw, Math.min(halfW - hw, selected.x));
                            selected.z = Math.max(-halfD + hh, Math.min(halfD - hh, selected.z));
                            syncRenderItems();
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (renderer3D.isResizingFloor) {
                            float dx = (x - lastX) * 0.05f;
                            float dy = (y - lastY) * 0.05f;
                            renderer3D.updateRoomDimensions(dx, dy);
                        } else {
                            rotationY += (x - lastX) * rotationSpeed;
                            rotationX += (y - lastY) * rotationSpeed;
                            rotationX = Math.max(-5f, Math.min(80f, rotationX));
                            renderer2D.setRotationAngleY(rotationY);
                            renderer3D.setRotationAngles(rotationX, rotationY);
                        }
                    }
                }
                lastX = x;
                lastY = y;
                gl2DPanel.repaint();
                gl3DPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastX = -1;
                lastY = -1;
                gl2DPanel.repaint();
                gl3DPanel.repaint();
            }
        };

        gl2DPanel.addMouseListener(sharedMouseAdapter);
        gl2DPanel.addMouseMotionListener(sharedMouseAdapter);
        gl2DPanel.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                renderer2D.zoomIn();
            } else {
                renderer2D.zoomOut();
            }
            gl2DPanel.repaint();
        });

        gl3DPanel.addMouseListener(sharedMouseAdapter);
        gl3DPanel.addMouseMotionListener(sharedMouseAdapter);
        gl3DPanel.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                renderer3D.zoomIn();
            } else {
                renderer3D.zoomOut();
            }
            gl3DPanel.repaint();
        });
    }

    private void setupKeyListeners() {
        gl3DPanel.setFocusable(true);
        gl3DPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    renderer3D.isResizingFloor = !renderer3D.isResizingFloor;
                    gl3DPanel.repaint();
                }
            }
        });

        gl2DPanel.setFocusable(true);
        gl2DPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    renderer3D.isResizingFloor = !renderer3D.isResizingFloor;
                    gl3DPanel.repaint();
                    gl2DPanel.repaint();
                }
            }
        });
    }

    private void setupRoomConfigListeners() {
        roomConfigPanel.addShapeChangeListener(() -> {
            renderer2D.clearItems();
            renderer3D.clearItems();
            syncRenderItems();
            gl2DPanel.repaint();
            gl3DPanel.repaint();
        });
    }

    private void setupFieldListeners() {
        roomConfigPanel.widthField.addActionListener(e -> {
            syncRenderItems();
            gl2DPanel.repaint();
            gl3DPanel.repaint();
        });
        roomConfigPanel.lengthField.addActionListener(e -> {
            syncRenderItems();
            gl2DPanel.repaint();
            gl3DPanel.repaint();
        });
    }

    private void setupColorListeners() {
        roomConfigPanel.wallColorBtn.addActionListener(e -> {
            renderer2D.setRotationAngleY(rotationY);
            gl2DPanel.repaint();
            gl3DPanel.repaint();
        });
        roomConfigPanel.floorColorBtn.addActionListener(e -> {
            gl2DPanel.repaint();
            gl3DPanel.repaint();
        });
    }
}