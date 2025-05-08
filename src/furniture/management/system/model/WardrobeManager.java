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

import javax.swing.*;
import java.awt.*;

public class WardrobeManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Do not call super.display(drawable) to avoid clearing buffers
        // Rely on Renderer3D for lighting and transformations
        drawWardrobe(gl);
    }

    private void drawWardrobe(GL2 gl) {
        drawLeftRight(gl);
        drawLeftLeft(gl);
        drawRightLeft(gl);
        drawRightRight(gl);
        drawMiddledrawer(gl);
        drawTopSupport(gl);
        drawTopdrawer(gl);
        // Remove floor and background rendering, as Renderer3D handles these
        // colorManager.applyFloorColor(gl);
        // GLUtil.drawFloor(gl);
        // colorManager.applyBackgroundColor(gl);
    }

    private void drawLeftRight(GL2 gl) {
        float tableWidth = 1.5f;
        float tableHeight = 6.0f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-3.0f, -0.0f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    private void drawLeftLeft(GL2 gl) {
        float tableWidth = 1.5f;
        float tableHeight = 6.0f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-4.51f, -0.0f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    private void drawRightLeft(GL2 gl) {
        float tableWidth = 1.5f;
        float tableHeight = 6.0f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.0f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    private void drawRightRight(GL2 gl) {
        float tableWidth = 1.5f;
        float tableHeight = 6.0f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(3.01f, -0.0f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    private void drawMiddledrawer(GL2 gl) {
        float tableWidth = 2.96f;
        float tableHeight = 1.0f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-0.75f, -2.0f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    private void drawTopSupport(GL2 gl) {
        float tableWidth = 2.99f;
        float tableHeight = 0.3f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-0.75f, 2.85f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    private void drawTopdrawer(GL2 gl) {
        float tableWidth = 2.96f;
        float tableHeight = 1.0f;
        float tableDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-0.75f, -0.8f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    public static class Wardrobe2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            drawWardrobe(gl);
        }

        private void drawWardrobe(GL2 gl) {
            gl.glColor3f(0.545f, 0.271f, 0.075f);
            GLUtil2D.drawRectangle(gl, 600, 105, 300, 600);
            GLUtil2D.drawRectangle(gl, 1250, 105, 300, 600);

            gl.glColor3f(0.824f, 0.706f, 0.549f);
            GLUtil2D.drawRectangle(gl, 900, 105, 350, 150);
            GLUtil2D.drawRectangle(gl, 900, 350, 350, 150);

            gl.glColor3f(0.545f, 0.271f, 0.075f);
            GLUtil2D.drawRectangle(gl, 900, 685, 350, 20);
            GLUtil2D.drawText(gl, "-Top view of Wardrobe ( h:183cm * w:75cm * d:65cm )-", 845, 725);
        }
    }

    public static class Wardrobe2DPanel extends JPanel {
        public Wardrobe2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            Wardrobe2D wardrobe2D = new Wardrobe2D();
            gljPanel.addGLEventListener(wardrobe2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class WardrobeDisplay extends JPanel {
        public WardrobeDisplay(boolean showControls) {
            WardrobeManager wardrobe = new WardrobeManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(wardrobe);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(wardrobe.rotationManager, wardrobe.colorManager, wardrobe.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("WardrobeDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class WardrobeStartUpPanel extends JPanel {
        public WardrobeStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("Wardrobe");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/pexels-nugroho-wahyu-3119180.jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(WardrobeStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "WardrobeDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(WardrobeStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "WardrobeDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}