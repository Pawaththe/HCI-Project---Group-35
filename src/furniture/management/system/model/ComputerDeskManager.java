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

public class ComputerDeskManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Rely on Renderer3D for transformations
        drawComputerDesk(gl);
    }

    private void drawComputerDesk(GL2 gl) {
        drawTopSurface(gl);
        drawBase(gl);
        drawKeyboardHutch(gl);
        drawAprons(gl);
        drawBackSupport(gl);
        drawLeftCabinet(gl);
    }

    private void drawTopSurface(GL2 gl) {
        float width = 9.0f;
        float depth = 3.0f;
        float thickness = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 2.5f, 0.35f);
        GLUtil.drawCube(gl, width, thickness, depth);
        gl.glPopMatrix();
    }

    private void drawBase(GL2 gl) {
        float width = 10.0f;
        float depth = 3.0f;
        float height = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.5f, 0.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawKeyboardHutch(GL2 gl) {
        float width = 9.0f;
        float depth = 2.5f;
        float height = 0.1f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.6f, 0.4f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawAprons(GL2 gl) {
        float width = 0.1f;
        float depth = 0.3f;
        float height = 0.7f;

        // Left apron
        gl.glPushMatrix();
        gl.glTranslatef(-4.45f, 2.0f, 1-1.5f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();

        // Right apron
        gl.glPushMatrix();
        gl.glTranslatef(4.45f, 2.0f, 1.5f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawBackSupport(GL2 gl) {
        float width = 9.0f;
        float depth = 0.3f;
        float height = 9.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.5f, -1.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawLeftCabinet(GL2 gl) {
        float height = 3.7f;
        float depth = 2.5f;

        // Draw left side
        gl.glPushMatrix();
        gl.glTranslatef(-4.45f, -0.3f, 0.4f);
        GLUtil.drawCube(gl, 0.1f, height, depth);
        gl.glPopMatrix();

        // Draw right side
        gl.glPushMatrix();
        gl.glTranslatef(-2.45f, -0.3f, 0.4f);
        GLUtil.drawCube(gl, 0.1f, height, depth);
        gl.glPopMatrix();

        float shelfThickness = 0.1f;
        float spaceBetweenShelves = 1.0f;
        float startY = -1.5f;
        for (int i = 0; i < 3; i++) {
            float shelfY = startY + i * (shelfThickness + spaceBetweenShelves);
            gl.glPushMatrix();
            gl.glTranslatef(-3.45f, shelfY, depth / 45.5f);
            GLUtil.drawCube(gl, 2.1f, shelfThickness, -1.3f);
            gl.glPopMatrix();
        }
    }

    public static class ComputerDesk2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            drawComputerDesk2D(gl);
        }

        private void drawComputerDesk2D(GL2 gl) {
            GLUtil2D.drawText(gl, "Computer Desk Front View ( h:75cm * w:120cm * d:60cm ) ", 715, 220);

            // Draw desk top
            gl.glColor3f(0.77f, 0.6f, 0.31f);
            GLUtil2D.drawRectangle(gl, 600, 550, 700, 40);

            // Draw two legs
            gl.glColor3f(0.58f, 0.408f, 0.0f);
            GLUtil2D.drawRectangle(gl, 700, 250, 50, 300);
            GLUtil2D.drawRectangle(gl, 1150, 250, 50, 300);

            // Draw cabinets
            gl.glColor3f(0.77f, 0.6f, 0.31f);
            for (int i = 0; i < 5; i++) {
                GLUtil2D.drawRectangle(gl, 750, 420 - i * 40, 100, 20);
            }
        }
    }

    public static class ComputerDesk2DPanel extends JPanel {
        public ComputerDesk2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            ComputerDesk2D computerDesk2D = new ComputerDesk2D();
            gljPanel.addGLEventListener(computerDesk2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class ComputerDeskDisplay extends JPanel {
        public ComputerDeskDisplay(boolean showControls) {
            ComputerDeskManager computerDesk = new ComputerDeskManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(computerDesk);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(computerDesk.rotationManager, computerDesk.colorManager, computerDesk.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("ComputerDeskDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class ComputerDeskStartUpPanel extends JPanel {
        public ComputerDeskStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("Computer Desk");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/pexels-monstera-production-6373304.jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ComputerDeskStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "ComputerDeskDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ComputerDeskStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "ComputerDeskDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}