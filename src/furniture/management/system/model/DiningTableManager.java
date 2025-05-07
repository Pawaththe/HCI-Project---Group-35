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

public class DiningTableManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Do not call super.display(drawable) to avoid clearing buffers
        // Rely on Renderer3D for lighting and other global states
        drawTable(gl);
    }

    private void drawTable(GL2 gl) {
        // Draw table components
        drawLegs(gl);
        drawTableTop(gl);
        drawStretchers(gl);
        // Remove floor and background rendering, as Renderer3D handles these
        // colorManager.applyFloorColor(gl);
        // GLUtil.drawFloor(gl);
        // colorManager.applyBackgroundColor(gl);
    }

    private void drawStretchers(GL2 gl) {
        float stretcherWidth = 0.1f;
        float stretcherHeight = 0.2f;
        float stretcherDepth = 1.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-2.0f, -2.0f, 0.4f);
        GLUtil.drawCube(gl, stretcherWidth, stretcherHeight, stretcherDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.0f, -2.0f, 0.4f);
        GLUtil.drawCube(gl, stretcherWidth, stretcherHeight, stretcherDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.0f, -2.0f, -0.4f);
        GLUtil.drawCube(gl, stretcherWidth, stretcherHeight, stretcherDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.0f, -2.0f, -0.4f);
        GLUtil.drawCube(gl, stretcherWidth, stretcherHeight, stretcherDepth);
        gl.glPopMatrix();
    }

    private void drawLegs(GL2 gl) {
        float legWidth = 0.2f;
        float legHeight = 3.5f;

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                gl.glPushMatrix();
                gl.glTranslatef(i * 2.00f, -2.0f, j * 0.8f);
                GLUtil.drawCube(gl, legWidth, legHeight, legWidth);
                gl.glPopMatrix();
            }
        }
    }

    private void drawTableTop(GL2 gl) {
        float tableWidth = 6.0f;
        float tableHeight = 0.5f;
        float tableDepth = 3.0f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, 0.0f);
        GLUtil.drawCube(gl, tableWidth, tableHeight, tableDepth);
        gl.glPopMatrix();
    }

    public static class DiningTable2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            drawDiningTable(gl);
        }

        private void drawDiningTable(GL2 gl) {
            gl.glColor3f(0.824f, 0.706f, 0.549f);
            GLUtil2D.drawRectangle(gl, 675, 350, 550, 300);
            GLUtil2D.drawText(gl, "-Dining Table top view-", 850, 500);
            GLUtil2D.drawText(gl, " 36 in wide x 29 in long", 860, 475);

            gl.glColor3f(0.545f, 0.271f, 0.075f);
            GLUtil2D.drawRectangle(gl, 675, 675, 100, 100);
            GLUtil2D.drawRectangle(gl, 825, 675, 100, 100);
            GLUtil2D.drawRectangle(gl, 975, 675, 100, 100);
            GLUtil2D.drawRectangle(gl, 1125, 675, 100, 100);
            GLUtil2D.drawRectangle(gl, 675, 225, 100, 100);
            GLUtil2D.drawRectangle(gl, 825, 225, 100, 100);
            GLUtil2D.drawRectangle(gl, 975, 225, 100, 100);
            GLUtil2D.drawRectangle(gl, 1125, 225, 100, 100);
        }
    }

    public static class DiningTable2DPanel extends JPanel {
        public DiningTable2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            DiningTable2D diningTable2D = new DiningTable2D();
            gljPanel.addGLEventListener(diningTable2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class DiningTableDisplay extends JPanel {
        public DiningTableDisplay(boolean showControls) {
            DiningTableManager diningTable = new DiningTableManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(diningTable);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(diningTable.rotationManager, diningTable.colorManager, diningTable.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("DiningTableDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class DiningTableStartUpPanel extends JPanel {
        public DiningTableStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("Dining Table");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/2151020072 (1).jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DiningTableStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "DiningTableDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DiningTableStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "DiningTableDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}