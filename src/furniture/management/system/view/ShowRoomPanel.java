package furniture.management.system.view;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import furniture.management.system.model.RotationManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

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
    private float rotationX = 30.0F;
    private float rotationY = 0.0F;
    private int lastX = -1;
    private int lastY = -1;
    private final float rotationSpeed = 0.5F;
    private static final String VIEW_2D = "2D";
    private static final String VIEW_3D = "3D";
    private final Color backgroundColor = new Color(245, 245, 250);
    private final Color buttonColor = new Color(38536);
    private final Color activeButtonColor = new Color(31083);

    public ShowRoomPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(this.backgroundColor);
        this.renderer2D = new Renderer2D(this.roomConfigPanel);
        this.renderer3D = new Renderer3D(this.roomConfigPanel, this.rotationManager);
        this.furnitureConfigPanel = new FurnitureConfigPanel(this::addFurniture);
        GLProfile profile = GLProfile.get("GL2");
        GLCapabilities caps = new GLCapabilities(profile);
        this.gl2DPanel = new GLJPanel(caps);
        this.gl3DPanel = new GLJPanel(caps);
        this.gl2DPanel.addGLEventListener(this.renderer2D);
        this.gl3DPanel.addGLEventListener(this.renderer3D);
        this.setupMouseListeners();
        this.setupKeyListeners();
        this.cardLayout = new CardLayout();
        this.rightPanel = new JPanel(this.cardLayout);
        this.rightPanel.add(this.gl2DPanel, "2D");
        this.rightPanel.add(this.gl3DPanel, "3D");
        this.cardLayout.show(this.rightPanel, "3D");
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(this.backgroundColor);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.add(this.roomConfigPanel, "North");
        leftPanel.add(this.furnitureConfigPanel, "Center");
        JPanel bottomPanel = this.createBottomPanel();
        this.add(leftPanel, "West");
        this.add(this.rightPanel, "Center");
        this.add(bottomPanel, "South");
        this.roomConfigPanel.addShapeChangeListener(() -> {
            this.renderer2D.clearItems();
            this.renderer3D.clearItems();
            this.gl2DPanel.repaint();
            this.gl3DPanel.repaint();
        });
        this.widthFieldListener();
        this.heightFieldListener();
        this.colorButtonListener();
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(1, 10, 10));
        panel.setBackground(this.backgroundColor);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(this.backgroundColor);
        JButton saveBtn = this.createStyledButton("Save Design");
        JButton clearBtn = this.createStyledButton("Clear");
        JButton houseViewBtn = this.createStyledButton("House View");
        saveBtn.addActionListener((e) -> {
            this.saveDesign();
        });
        clearBtn.addActionListener((e) -> {
            this.clearDesign();
        });
        houseViewBtn.addActionListener((e) -> {
            this.showHouseView();
        });
        this.show2DButton.setText("Show 2D View");
        this.show3DButton.setText("Show 3D View");
        this.styleViewButton(this.show2DButton);
        this.styleViewButton(this.show3DButton);
        this.show2DButton.addActionListener((e) -> {
            this.show2DView();
        });
        this.show3DButton.addActionListener((e) -> {
            this.show3DView();
        });
        this.show3DButton.setBackground(this.activeButtonColor);
        bottomPanel.add(saveBtn);
        bottomPanel.add(clearBtn);
        bottomPanel.add(houseViewBtn);
        bottomPanel.add(this.show2DButton);
        bottomPanel.add(this.show3DButton);
        return bottomPanel;
    }

    private void styleViewButton(final JButton button) {
        button.setFocusPainted(false);
        button.setBackground(this.buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", 0, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if ((button != ShowRoomPanel.this.show2DButton || !ShowRoomPanel.this.cardLayoutIs2D()) && (button != ShowRoomPanel.this.show3DButton || ShowRoomPanel.this.cardLayoutIs2D())) {
                    button.setBackground(ShowRoomPanel.this.activeButtonColor);
                }

            }

            public void mouseExited(MouseEvent e) {
                if (button == ShowRoomPanel.this.show2DButton && ShowRoomPanel.this.cardLayoutIs2D()) {
                    button.setBackground(ShowRoomPanel.this.activeButtonColor);
                } else if (button == ShowRoomPanel.this.show3DButton && !ShowRoomPanel.this.cardLayoutIs2D()) {
                    button.setBackground(ShowRoomPanel.this.activeButtonColor);
                } else {
                    button.setBackground(ShowRoomPanel.this.buttonColor);
                }

            }
        });
    }

    private JButton createStyledButton(String text) {
        final JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(this.buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", 0, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ShowRoomPanel.this.activeButtonColor);
            }

            public void mouseExited(MouseEvent e) {
                if (button != ShowRoomPanel.this.show3DButton && button != ShowRoomPanel.this.show2DButton) {
                    button.setBackground(ShowRoomPanel.this.buttonColor);
                } else if (button == ShowRoomPanel.this.show2DButton && ShowRoomPanel.this.cardLayoutIs2D()) {
                    button.setBackground(ShowRoomPanel.this.activeButtonColor);
                } else if (button == ShowRoomPanel.this.show3DButton && !ShowRoomPanel.this.cardLayoutIs2D()) {
                    button.setBackground(ShowRoomPanel.this.activeButtonColor);
                } else {
                    button.setBackground(ShowRoomPanel.this.buttonColor);
                }

            }
        });
        return button;
    }

    private void show2DView() {
        this.cardLayout.show(this.rightPanel, "2D");
        this.show2DButton.setBackground(this.activeButtonColor);
        this.show3DButton.setBackground(this.buttonColor);
        this.gl2DPanel.repaint();
    }

    private void show3DView() {
        this.cardLayout.show(this.rightPanel, "3D");
        this.show3DButton.setBackground(this.activeButtonColor);
        this.show2DButton.setBackground(this.buttonColor);
        this.gl3DPanel.repaint();
    }

    private boolean cardLayoutIs2D() {
        Component[] var1 = this.rightPanel.getComponents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Component comp = var1[var3];
            if (comp.isVisible() && comp == this.gl2DPanel) {
                return true;
            }
        }

        return false;
    }

    private void widthFieldListener() {
        this.roomConfigPanel.widthField.addActionListener((e) -> {
            this.gl2DPanel.repaint();
            this.gl3DPanel.repaint();
        });
    }

    private void heightFieldListener() {
        this.roomConfigPanel.heightField.addActionListener((e) -> {
            this.gl2DPanel.repaint();
            this.gl3DPanel.repaint();
        });
    }

    private void colorButtonListener() {
        this.roomConfigPanel.wallColorBtn.addActionListener((e) -> {
            this.gl2DPanel.repaint();
            this.gl3DPanel.repaint();
        });
        this.roomConfigPanel.floorColorBtn.addActionListener((e) -> {
            this.gl2DPanel.repaint();
            this.gl3DPanel.repaint();
        });
    }

    private void addFurniture() {
        String type = this.furnitureConfigPanel.getSelectedFurnitureType();
        float scale = this.furnitureConfigPanel.getScale();
        this.renderer2D.add(type, scale);
        this.renderer3D.add(type, scale);
        this.gl2DPanel.repaint();
        this.gl3DPanel.repaint();
    }

    private void clearDesign() {
        this.renderer2D.clearItems();
        this.renderer3D.clearItems();
        this.gl2DPanel.repaint();
        this.gl3DPanel.repaint();
    }

    private void saveDesign() {
        RoomDesign design = new RoomDesign(this.renderer3D.getItems(), this.roomConfigPanel.getRoomShape(), this.roomConfigPanel.getRoomWidth(), this.roomConfigPanel.getRoomDepth(), this.roomConfigPanel.getWallColor(), this.roomConfigPanel.getFloorColor());
        String filename = "saved_design_" + String.valueOf(UUID.randomUUID()) + ".dat";

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));

            try {
                out.writeObject(design);
                JOptionPane.showMessageDialog(this, "Design saved as " + filename);
            } catch (Throwable var7) {
                try {
                    out.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            out.close();
        } catch (Exception var8) {
            Exception ex = var8;
            JOptionPane.showMessageDialog(this, "Error saving design: " + ex.getMessage(), "Error", 0);
        }

    }

    private void showHouseView() {
        JFrame frame = new JFrame("House View");
        frame.setDefaultCloseOperation(2);
        frame.add(new HouseViewPanel());
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo((Component)null);
        frame.setVisible(true);
    }

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ShowRoomPanel.this.lastX = e.getX();
                ShowRoomPanel.this.lastY = e.getY();
                if (e.getButton() == 1) {
                    ShowRoomPanel.this.renderer3D.selectItemAt(ShowRoomPanel.this.lastX, ShowRoomPanel.this.lastY);
                }

            }

            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (ShowRoomPanel.this.lastX != -1 && ShowRoomPanel.this.lastY != -1) {
                    float dy;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        RenderItem selected = ShowRoomPanel.this.renderer3D.getSelectedItem();
                        if (selected != null) {
                            dy = (float)(x - ShowRoomPanel.this.lastX) * 0.05F;
                            float dz = (float)(y - ShowRoomPanel.this.lastY) * 0.05F;
                            selected.x += dy;
                            selected.z += dz;
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (ShowRoomPanel.this.renderer3D.isResizingFloor) {
                            float dx = (float)(x - ShowRoomPanel.this.lastX) * 0.05F;
                            dy = (float)(y - ShowRoomPanel.this.lastY) * 0.05F;
                            ShowRoomPanel.this.renderer3D.updateRoomDimensions(dx, dy);
                        } else {
                            ShowRoomPanel var10000 = ShowRoomPanel.this;
                            var10000.rotationY += (float)(x - ShowRoomPanel.this.lastX) * 0.5F;
                            var10000 = ShowRoomPanel.this;
                            var10000.rotationX += (float)(y - ShowRoomPanel.this.lastY) * 0.5F;
                            ShowRoomPanel.this.rotationX = Math.max(-5.0F, Math.min(80.0F, ShowRoomPanel.this.rotationX));
                            ShowRoomPanel.this.renderer2D.setRotationAngleY(ShowRoomPanel.this.rotationY);
                            ShowRoomPanel.this.renderer3D.setRotationAngles(ShowRoomPanel.this.rotationX, ShowRoomPanel.this.rotationY);
                        }
                    }
                }

                ShowRoomPanel.this.lastX = x;
                ShowRoomPanel.this.lastY = y;
                ShowRoomPanel.this.gl2DPanel.repaint();
                ShowRoomPanel.this.gl3DPanel.repaint();
            }
        };
        MouseWheelListener wheelListener = (e) -> {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                this.renderer3D.zoomIn();
            } else {
                this.renderer3D.zoomOut();
            }

            this.gl3DPanel.repaint();
        };
        this.gl3DPanel.addMouseListener(mouseAdapter);
        this.gl3DPanel.addMouseMotionListener(mouseAdapter);
        this.gl3DPanel.addMouseWheelListener(wheelListener);
    }

    private void setupKeyListeners() {
        this.gl3DPanel.setFocusable(true);
        this.gl3DPanel.requestFocusInWindow();
        this.gl3DPanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32) {
                    ShowRoomPanel.this.renderer3D.isResizingFloor = !ShowRoomPanel.this.renderer3D.isResizingFloor;
                }

            }
        });
    }
}