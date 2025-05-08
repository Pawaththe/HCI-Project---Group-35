package furniture.management.system.model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import furniture.management.system.model.*;
import furniture.management.system.view.ControlsPanel;
import furniture.management.system.view.RoundedButton;
import furniture.management.system.view.SavePanel;
import furniture.management.system.view.StartupUtils;

import javax.swing.*;
import java.awt.*;

public class BedManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Do not call super.display(drawable) to avoid clearing buffers
        // Rely on Renderer3D for lighting and transformations
        drawBed(gl);
    }

    private void drawBed(GL2 gl) {
        drawHeadboard(gl);
        drawFootboard(gl);
        drawCleat(gl);
        drawAdditionalCleats(gl);
        drawTopRail(gl);
        drawBottomFrontRail(gl);
        drawFrontLegs(gl);
        drawSideDrawersLegs(gl);
        drawSideRails(gl);
        // Remove floor and background rendering, as Renderer3D handles these
        // colorManager.applyFloorColor(gl);
        // GLUtil.drawFloor(gl);
        // colorManager.applyBackgroundColor(gl);
    }

    private void drawHeadboard(GL2 gl) {
        float width = 8.0f;
        float height = 4.0f;
        float depth = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 2.0f, -5.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawFootboard(GL2 gl) {
        float width = 8.0f;
        float height = 2.0f;
        float depth = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -1.0f, 5.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawCleat(GL2 gl) {
        float width = 7.5f;
        float height = 0.2f;
        float depth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, -4.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawAdditionalCleats(GL2 gl) {
        float cleatSpacing = 2.0f;

        for (int i = 1; i <= 4; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(0.0f, 0.0f, i * cleatSpacing);
            drawCleat(gl);
            gl.glPopMatrix();
        }
    }

    private void drawTopRail(GL2 gl) {
        float width = 8.0f;
        float height = 0.5f;
        float depth = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 3.75f, -5.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawBottomFrontRail(GL2 gl) {
        float width = 8.0f;
        float height = 0.5f;
        float depth = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.25f, -5.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    private void drawFrontLegs(GL2 gl) {
        float width = 0.5f;
        float height = 2.0f;
        float depth = 0.5f;
        float spaceBetweenLegs = 7.0f;

        float startX = 3.5f;
        for (int i = 0; i < 2; i++) {
            float legX = startX - i * spaceBetweenLegs;
            gl.glPushMatrix();
            gl.glTranslatef(legX, -1.0f, -5.0f);
            GLUtil.drawCube(gl, width, height, depth);
            gl.glPopMatrix();
        }
    }

    private void drawSideDrawersLegs(GL2 gl) {
        float width = 0.5f;
        float height = 2.0f;
        float depth = 0.5f;
        float spaceBetweenLegs = 7.0f;

        float startX = 3.5f;
        for (int i = 0; i < 2; i++) {
            float legX = startX - i * spaceBetweenLegs;
            gl.glPushMatrix();
            gl.glTranslatef(legX, -1.0f, 5.0f);
            GLUtil.drawCube(gl, width, height, depth);
            gl.glPopMatrix();
        }
    }

    private void drawSideRails(GL2 gl) {
        float width = 0.5f;
        float height = 1.0f;
        float depth = 10.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-3.5f, -1.0f, 0.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(3.5f, -1.0f, 0.0f);
        GLUtil.drawCube(gl, width, height, depth);
        gl.glPopMatrix();
    }

    public static class Bed2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            drawBed(gl);
        }

        private void drawBed(GL2 gl) {
            gl.glColor3f(0.788f, 0.914f, 0.941f);
            GLUtil2D.drawRectangle(gl, 750, 105, 500, 770);
            GLUtil2D.drawText(gl, "Bed", 975, 400);
            GLUtil2D.drawText(gl, "Full/Double Bed", 935, 350);
            GLUtil2D.drawText(gl, "54 inches wide x 75 inches long (137 cm x 190.5 cm)", 775, 300);

            gl.glColor3f(1.0f, 0.988f, 0.0f);
            GLUtil2D.drawRectangle(gl, 785, 700, 200, 140);
            GLUtil2D.drawText(gl, "left pillow", 850, 765);

            gl.glColor3f(1.0f, 0.988f, 0.0f);
            GLUtil2D.drawRectangle(gl, 1015, 700, 200, 140);
            GLUtil2D.drawText(gl, "right pillow", 1065, 765);

            gl.glColor3f(0.91f, 0.729f, 0.867f);
            GLUtil2D.drawRectangle(gl, 440, 600, 250, 250);
            GLUtil2D.drawText(gl, "left side table", 515, 725);

            gl.glColor3f(0.91f, 0.729f, 0.867f);
            GLUtil2D.drawRectangle(gl, 1310, 590, 250, 250);
            GLUtil2D.drawText(gl, "right side table", 1380, 720);
        }
    }

    public static class Bed2DPanel extends JPanel {
        public Bed2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            Bed2D bed2D = new Bed2D();
            gljPanel.addGLEventListener(bed2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class BedDisplay extends JPanel {
        public BedDisplay(boolean showControls) {
            BedManager bed = new BedManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(bed);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(bed.rotationManager, bed.colorManager, bed.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("BedDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class BedStartUpPanel extends JPanel {
        public BedStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("Bed");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/pexels-burst-545034.jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(BedStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "BedDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(BedStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "BedDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}