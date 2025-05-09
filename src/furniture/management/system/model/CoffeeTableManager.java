package furniture.management.system.model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import furniture.management.system.view.ControlsPanel;
import furniture.management.system.view.RoundedButton;
import furniture.management.system.view.SavePanel;
import furniture.management.system.view.StartupUtils;
import furniture.management.system.view.RenderItem;

import javax.swing.*;
import java.awt.*;

public class CoffeeTableManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();
    private String type; // To differentiate between CoffeeTable1, 2, 3

    public CoffeeTableManager(String type) {
        this.type = type;
    }

    public CoffeeTableManager() {
        this.type = "CoffeeTable1"; // Default to CoffeeTable1
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // The Renderer3D will handle transformations and lighting, so we only draw the coffee table
        drawCoffeeTable(gl, null); // Default rendering without specific RenderItem
    }

    // Overloaded method to support RenderItem for color and material
    public void display(GLAutoDrawable drawable, RenderItem item) {
        final GL2 gl = drawable.getGL().getGL2();
        drawCoffeeTable(gl, item);
    }

    private void drawCoffeeTable(GL2 gl, RenderItem item) {
        drawLeftLeg(gl, item);
        drawRightLeg(gl, item);
        drawTableTopOne(gl, item);
        drawFrontRight(gl, item);
        drawFrontLeft(gl, item);
        drawTableTopTwo(gl, item);
        drawTableTopThree(gl, item);
        drawTableTopFour(gl, item);
    }

    private void drawLeftLeg(GL2 gl, RenderItem item) {
        float tableWidth = 0.2f;
        float tableHeight = type.equals("CoffeeTable2") ? 5.0f : 4.5f; // Taller legs for CoffeeTable2
        float tableDepth = 0.15f;

        gl.glPushMatrix();
        gl.glTranslatef(-2.0f, 0.0f, 1.0f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawRightLeg(GL2 gl, RenderItem item) {
        float tableWidth = 0.2f;
        float tableHeight = type.equals("CoffeeTable2") ? 5.0f : 4.5f;
        float tableDepth = 0.15f;

        gl.glPushMatrix();
        gl.glTranslatef(1.2f, 0.0f, 1.0f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawFrontLeft(GL2 gl, RenderItem item) {
        float tableWidth = 0.2f;
        float tableHeight = type.equals("CoffeeTable2") ? 5.0f : 4.5f;
        float tableDepth = 0.15f;

        gl.glPushMatrix();
        gl.glTranslatef(-2.0f, 0.0f, 3.0f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawFrontRight(GL2 gl, RenderItem item) {
        float tableWidth = 0.2f;
        float tableHeight = type.equals("CoffeeTable2") ? 5.0f : 4.5f;
        float tableDepth = 0.15f;

        gl.glPushMatrix();
        gl.glTranslatef(1.2f, 0.0f, 3.0f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawTableTopOne(GL2 gl, RenderItem item) {
        float tableWidth = type.equals("CoffeeTable3") ? 1.2f : 1f; // Wider top for CoffeeTable3
        float tableHeight = 0.8f;
        float tableDepth = type.equals("CoffeeTable1") ? 3.5f : 3f; // Longer depth for CoffeeTable1

        gl.glPushMatrix();
        gl.glTranslatef(-2f, 2f, 2.0f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawTableTopTwo(GL2 gl, RenderItem item) {
        float tableWidth = type.equals("CoffeeTable3") ? 1.2f : 1f;
        float tableHeight = 0.8f;
        float tableDepth = type.equals("CoffeeTable1") ? 3.5f : 3f;

        gl.glPushMatrix();
        gl.glTranslatef(-0.95f, 2f, 2f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawTableTopThree(GL2 gl, RenderItem item) {
        float tableWidth = type.equals("CoffeeTable3") ? 1.2f : 1f;
        float tableHeight = 0.8f;
        float tableDepth = type.equals("CoffeeTable1") ? 3.5f : 3f;

        gl.glPushMatrix();
        gl.glTranslatef(0.1f, 2.0f, 2f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    private void drawTableTopFour(GL2 gl, RenderItem item) {
        float tableWidth = type.equals("CoffeeTable3") ? 1.2f : 1f;
        float tableHeight = 0.8f;
        float tableDepth = type.equals("CoffeeTable1") ? 3.5f : 3f;

        gl.glPushMatrix();
        gl.glTranslatef(1.15f, 2.0f, 2f);
        if (item != null) {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth, item);
        } else {
            GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        }
        gl.glPopMatrix();
    }

    public static class CoffeeTable2D extends Furniture2D {
        private String type;

        public CoffeeTable2D(String type) {
            this.type = type;
        }

        public CoffeeTable2D() {
            this.type = "CoffeeTable1";
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            drawCoffeeTable(gl);
        }

        private void drawCoffeeTable(GL2 gl) {
            gl.glColor3f(0.824f, 0.706f, 0.549f);
            float width = type.equals("CoffeeTable3") ? 400 : 325; // Wider for CoffeeTable3
            float height = type.equals("CoffeeTable1") ? 100 : 80; // Taller for CoffeeTable1
            GLUtil2D.drawRectangle(gl, 750, 400, width, height);
            GLUtil2D.drawRectangle(gl, 750, 400 + height, width, height);
            GLUtil2D.drawRectangle(gl, 750, 400 + 2 * height, width, height);
            GLUtil2D.drawRectangle(gl, 750, 400 + 3 * height, width, height);
            GLUtil2D.drawText(gl, " 16 in wide x 18 in long", 820, 750);
            GLUtil2D.drawText(gl, "-top view of " + type + "-", 810, 800);
        }
    }

    public static class CoffeeTable2DPanel extends JPanel {
        public CoffeeTable2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            CoffeeTable2D coffeeTable2D = new CoffeeTable2D();
            gljPanel.addGLEventListener(coffeeTable2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class CoffeeTableDisplay extends JPanel {
        public CoffeeTableDisplay(boolean showControls) {
            CoffeeTableManager coffeeTable = new CoffeeTableManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(coffeeTable);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(coffeeTable.rotationManager, coffeeTable.colorManager, coffeeTable.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("CoffeeTableDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class CoffeeTableStartUpPanel extends JPanel {
        public CoffeeTableStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/cup-saucer-wooden-table.jpg", 0, 0, 1540, 760);
            JLabel image2d = StartupUtils.createImageLabel("icons/2d.png", 510, 280, 256, 256);
            JLabel image3d = StartupUtils.createImageLabel("icons/3d_1666631.png", 820, 280, 256, 256);
            add(image2d);
            add(image3d);

            JPanel imageOverlay = StartupUtils.createTransparentOverlay(370, 150, 840, 500, 50);
            add(imageOverlay);

            Color startColor = new Color(143, 108, 74);
            Color endColor = new Color(107, 80, 54);

            JButton button2d = new RoundedButton("2d Design", startColor, endColor);
            button2d.setBounds(590, 550, 100, 30);
            button2d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(CoffeeTableStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "CoffeeTableDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(CoffeeTableStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "CoffeeTableDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}