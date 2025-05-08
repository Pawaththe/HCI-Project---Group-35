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

public class TvStandManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Do not call super.display(drawable) to avoid clearing buffers
        // Rely on Renderer3D for lighting and transformations
        drawTvStand(gl);
    }

    private void drawTvStand(GL2 gl) {
        drawBase(gl);
        drawColumns(gl);
        drawSpeakerSupports(gl);
        drawShelves(gl);
        drawTopShelf(gl);
        // Remove floor and background rendering, as Renderer3D handles these
        // colorManager.applyFloorColor(gl);
        // GLUtil.drawFloor(gl);
        // colorManager.applyBackgroundColor(gl);
    }

    private void drawBase(GL2 gl) {
        float baseWidth = 6.0f;
        float baseDepth = 1.5f;
        float baseHeight = 0.3f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.5f, 0.0f);
        GLUtil.drawCube(gl, 8.5f, baseHeight, baseDepth);
        gl.glPopMatrix();
    }

    private void drawColumns(GL2 gl) {
        float columnWidth = 0.3f;
        float columnHeight = 5.5f;
        float columnDepth = 0.6f;
        float gapIncrease = 1.5f;

        gl.glPushMatrix();
        gl.glTranslatef(-2.0f - gapIncrease, 0.0f, 0.0f);
        GLUtil.drawCube(gl, columnWidth, columnHeight, columnDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.0f + gapIncrease, 0.0f, 0.0f);
        GLUtil.drawCube(gl, columnWidth, columnHeight, columnDepth);
        gl.glPopMatrix();
    }

    private void drawSpeakerSupports(GL2 gl) {
        float speakerSupportWidth = 0.6f;
        float speakerSupportHeight = 3.0f;
        float speakerSupportDepth = 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(-3.5f, 0.5f, 0.0f);
        GLUtil.drawCube(gl, speakerSupportWidth, speakerSupportHeight, speakerSupportDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(3.5f, 0.5f, 0.0f);
        GLUtil.drawCube(gl, speakerSupportWidth, speakerSupportHeight, speakerSupportDepth);
        gl.glPopMatrix();
    }

    private void drawShelves(GL2 gl) {
        float shelfWidth = 7.0f;
        float shelfThickness = 0.2f;
        float shelfDepth = 1.7f;
        float bottomShelfYPosition = -1.8f;
        float shelfDistance = 0.6f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, bottomShelfYPosition, 0.0f);
        GLUtil.drawCube(gl, shelfWidth, shelfThickness, shelfDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, bottomShelfYPosition + shelfDistance, 0.0f);
        GLUtil.drawCube(gl, shelfWidth, shelfThickness, shelfDepth);
        gl.glPopMatrix();
    }

    private void drawTopShelf(GL2 gl) {
        float topShelfWidth = 6.9f;
        float topShelfDepth = 0.5f;
        float topShelfHeight = 0.2f;
        float topShelfElevation = 2.8f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, topShelfElevation, 0.0f);
        GLUtil.drawCube(gl, topShelfWidth, topShelfHeight, topShelfDepth);
        gl.glPopMatrix();
    }

    public static class TvStand2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            int[] viewport = new int[4];
            gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
            int width = viewport[2];
            int height = viewport[3];
            drawTvStand(gl, width, height);
        }

        private void drawTvStand(GL2 gl, int viewportWidth, int viewportHeight) {
            int centerX = viewportWidth / 2;
            int centerY = viewportHeight / 4;

            int tvStandWidth = 800;
            int tvStandHeight = 100;
            int tvStandX = centerX - (tvStandWidth / 2);
            int tvStandY = centerY - (tvStandHeight / 2);

            gl.glColor3f(0.55f, 0.27f, 0.07f);
            GLUtil2D.drawRectangle(gl, tvStandX, tvStandY, tvStandWidth, tvStandHeight);

            int tvWidth = 700;
            int tvHeight = 400;
            int tvX = centerX - (tvWidth / 2);
            int tvY = tvStandY + tvStandHeight + 10;
            gl.glColor3f(0.1f, 0.1f, 0.1f);
            GLUtil2D.drawRectangle(gl, tvX, tvY, tvWidth, tvHeight);

            int compartmentWidth = 160;
            int compartmentHeight = 90;

            gl.glColor3f(0.72f, 0.45f, 0.20f);
            GLUtil2D.drawRectangle(gl, tvStandX + 20, tvStandY + 10, compartmentWidth, compartmentHeight);
            GLUtil2D.drawRectangle(gl, tvStandX + tvStandWidth - compartmentWidth - 20, tvStandY + 10, compartmentWidth, compartmentHeight);

            GLUtil2D.drawText(gl, " TV Stand Front View ( Large TVs: 55-75 inches diagonal ) ", 715, 130);
        }
    }

    public static class TvStand2DPanel extends JPanel {
        public TvStand2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            TvStand2D tvStand2D = new TvStand2D();
            gljPanel.addGLEventListener(tvStand2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class TvStandDisplay extends JPanel {
        public TvStandDisplay(boolean showControls) {
            TvStandManager tvStand = new TvStandManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(tvStand);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(tvStand.rotationManager, tvStand.colorManager, tvStand.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("TvStandDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class TvStandStartUpPanel extends JPanel {
        public TvStandStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("TV Stand");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/pexels-max-vakhtbovycn-6523268.jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(TvStandStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "TvStandDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(TvStandStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "TvStandDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}