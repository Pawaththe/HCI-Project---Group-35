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

public class DiningChairManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Rely on Renderer3D for lighting and transformations
        drawChair(gl);
    }

    private void drawChair(GL2 gl) {
        drawLegs(gl);
        drawStretchers(gl);
        drawAprons(gl);
        drawBack(gl);
        // Remove floor and background rendering, as Renderer3D handles these
        // colorManager.applyFloorColor(gl);
        // GLUtil.drawFloor(gl);
        // colorManager.applyBackgroundColor(gl);
    }

    private void drawLegs(GL2 gl) {
        float legWidth = 0.3f;
        float legHeight = 3.0f;
        float legDepth = 0.3f;

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, -1.5f, -1.9f);
        GLUtil.drawCube(gl, legWidth, legHeight, legDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -1.5f, -1.9f);
        GLUtil.drawCube(gl, legWidth, legHeight, legDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, -1.5f, 1.0f);
        GLUtil.drawCube(gl, legWidth, legHeight, legDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -1.5f, 1.0f);
        GLUtil.drawCube(gl, legWidth, legHeight, legDepth);
        gl.glPopMatrix();
    }

    private void drawStretchers(GL2 gl) {
        float stretcherWidth = 3.0f;
        float stretcherHeight = 0.3f;
        float stretcherDepth = 0.3f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.0f, -1.9f);
        GLUtil.drawCube(gl, stretcherWidth, stretcherHeight, stretcherDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.0f, 1.0f);
        GLUtil.drawCube(gl, stretcherWidth, stretcherHeight, stretcherDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, -2.0f, -0.5f);
        GLUtil.drawCube(gl, stretcherDepth, stretcherHeight, 3.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.8f - stretcherDepth, -2.0f, -0.5f);
        GLUtil.drawCube(gl, stretcherDepth, stretcherHeight, 3.0f);
        gl.glPopMatrix();
    }

    private void drawAprons(GL2 gl) {
        float apronWidth = 3.0f;
        float apronHeight = 0.3f;
        float apronDepth = 0.3f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.15f, -1.9f);
        GLUtil.drawCube(gl, apronWidth, apronHeight, apronDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.15f, 1.0f);
        GLUtil.drawCube(gl, apronWidth, apronHeight, apronDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, -0.15f, -0.5f);
        GLUtil.drawCube(gl, apronDepth, apronHeight, 3.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.8f - apronDepth, -0.15f, -0.5f);
        GLUtil.drawCube(gl, apronDepth, apronHeight, 3.0f);
        gl.glPopMatrix();
    }

    private void drawBack(GL2 gl) {
        float backWidth = 3.0f;
        float backHeight = 5.0f;
        float backDepth = 0.3f;

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 2.0f, -1.9f);
        GLUtil.drawCube(gl, 0.3f, backHeight, backDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 2.0f, -1.9f);
        GLUtil.drawCube(gl, 0.3f, backHeight, backDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.5f, -1.9f);
        GLUtil.drawCube(gl, backWidth, 0.3f, backDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 4.0f, -1.9f);
        GLUtil.drawCube(gl, backWidth, 0.3f, backDepth);
        gl.glPopMatrix();

        float splatWidth = 0.3f;
        float splatHeight = 2.6f;
        float splatDepth = 0.1f;

        gl.glPushMatrix();
        gl.glTranslatef(-0.9f, 2.8f, -1.9f);
        GLUtil.drawCube(gl, splatWidth, splatHeight, splatDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 2.8f, -1.9f);
        GLUtil.drawCube(gl, splatWidth, splatHeight, splatDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.9f, 2.8f, -1.9f);
        GLUtil.drawCube(gl, splatWidth, splatHeight, splatDepth);
        gl.glPopMatrix();
    }

    public static class DiningChair2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            gl.glColor3f(0.77f, 0.6f, 0.31f);
            drawDiningChair2D(gl);
        }

        private void drawDiningChair2D(GL2 gl) {
            GLUtil2D.drawText(gl, "Standard Dining Chair ( h:90cm * w:50cm * d:50cm ) Side View ", 705, 140);
            gl.glColor3f(0.77f, 0.6f, 0.31f);
            GLUtil2D.drawRectangle(gl, 750, 400, 350, 20);
            GLUtil2D.drawRectangle(gl, 750, 190, 20, 650);
            GLUtil2D.drawRectangle(gl, 1100, 190, 20, 230);
            GLUtil2D.drawRectangle(gl, 750, 220, 350, 20);
        }
    }

    public static class DiningChair2DPanel extends JPanel {
        public DiningChair2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            DiningChair2D diningChair2D = new DiningChair2D();
            gljPanel.addGLEventListener(diningChair2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class DiningChairDisplay extends JPanel {
        public DiningChairDisplay(boolean showControls) {
            DiningChairManager diningChair = new DiningChairManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(diningChair);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(diningChair.rotationManager, diningChair.colorManager, diningChair.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("DiningChairDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class DiningChairStartUpPanel extends JPanel {
        public DiningChairStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("Dining Chair");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/contemporary-living-room-interior-design-with-luxury-armchair.jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DiningChairStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "DiningChairDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DiningChairStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "DiningChairDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}