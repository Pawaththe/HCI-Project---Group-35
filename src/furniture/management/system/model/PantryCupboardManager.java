package furniture.management.system.model;

import com.jogamp.opengl.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GLEventListener;
import furniture.management.system.view.ControlsPanel;
import furniture.management.system.view.RoundedButton;
import furniture.management.system.view.SavePanel;
import furniture.management.system.view.StartupUtils;

import javax.swing.*;
import java.awt.*;

public class PantryCupboardManager extends Furniture {
    private RotationManager rotationManager = new RotationManager();
    private ColorManager colorManager = new ColorManager();
    private LightingManager lightingManager = new LightingManager();

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Do not call super.display(drawable) to avoid clearing buffers
        // Rely on Renderer3D for lighting and transformations
        drawPantryCupboard(gl);
    }

    private void drawPantryCupboard(GL2 gl) {
        drawBaseCabinets(gl);
        drawCountertop(gl);
        drawWallCabinets(gl);
        drawOpenSpaceForOven(gl);
        // Remove floor and background rendering, as Renderer3D handles these
        // colorManager.applyFloorColor(gl);
        // GLUtil.drawFloor(gl);
        // colorManager.applyBackgroundColor(gl);
    }

    private void drawBaseCabinets(GL2 gl) {
        float cabinetWidth = 9.5f;
        float cabinetHeight = 1.9f;
        float cabinetDepth = 2.0f;

        gl.glColor3f(0.76f, 0.69f, 0.57f);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        GLUtil.drawCube(gl, cabinetWidth, cabinetHeight, cabinetDepth);
        gl.glPopMatrix();

        float doorWidth = cabinetWidth / 4.0f;
        float doorHeight = cabinetHeight;
        float doorDepth = 0.3f;
        float doorOffset = doorWidth / 2.0f;

        gl.glColor3f(0.63f, 0.32f, 0.18f);
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f * doorOffset, 0.0f, cabinetDepth / 2 + doorDepth / 2);
        GLUtil.drawCube(gl, doorWidth, doorHeight, 0.8f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f * doorOffset, 0.0f, cabinetDepth / 2 + doorDepth / 2);
        GLUtil.drawCube(gl, doorWidth, doorHeight, 0.8f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f * doorOffset, 0.0f, cabinetDepth / 2 + doorDepth / 2);
        GLUtil.drawCube(gl, doorWidth, doorHeight, 0.8f);
        gl.glPopMatrix();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }

    private void drawCountertop(GL2 gl) {
        float countertopWidth = 7.2f;
        float countertopDepth = 0.5f;
        float countertopHeight = 0.2f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 2.0f, 0.0f);
        GLUtil.drawCube(gl, countertopWidth, countertopHeight, countertopDepth);
        gl.glPopMatrix();
    }

    private void drawWallCabinets(GL2 gl) {
        float wallCabinetWidth = 6.5f;
        float wallCabinetHeight = 2.0f;
        float wallCabinetDepth = 2.5f;
        float verticalSpacing = 0.5f;

        gl.glColor3f(0.76f, 0.60f, 0.42f);
        gl.glPushMatrix();
        gl.glTranslatef(-1.75f, 2.0f + verticalSpacing, 0.0f);
        GLUtil.drawCube(gl, wallCabinetWidth, wallCabinetHeight, wallCabinetDepth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.75f, 2.0f + verticalSpacing, 0.0f);
        GLUtil.drawCube(gl, wallCabinetWidth, wallCabinetHeight, wallCabinetDepth);
        gl.glPopMatrix();

        gl.glColor3f(0.0f, 0.0f, 0.0f);
        float lineThickness = 0.09f;

        float firstLinePosition = 0.0f;
        float secondLinePosition = 1.7f;
        float thirdLinePosition = 3.4f;

        gl.glPushMatrix();
        gl.glTranslatef(firstLinePosition, 2.0f + verticalSpacing, wallCabinetDepth / 2);
        GLUtil.drawCube(gl, lineThickness, wallCabinetHeight, 3.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(secondLinePosition, 2.0f + verticalSpacing, wallCabinetDepth / 2);
        GLUtil.drawCube(gl, lineThickness, wallCabinetHeight, 3.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(thirdLinePosition, 2.0f + verticalSpacing, wallCabinetDepth / 2);
        GLUtil.drawCube(gl, lineThickness, wallCabinetHeight, 3.0f);
        gl.glPopMatrix();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }

    private void drawOpenSpaceForOven(GL2 gl) {
        float spaceWidth = 1.0f;
        float spaceHeight = 1.5f;
        float spaceDepth = 0.6f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        GLUtil.drawCube(gl, spaceWidth, spaceHeight, spaceDepth);
        gl.glPopMatrix();
    }

    public static class PantryCupboard2D extends Furniture2D {
        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            final GL2 gl = drawable.getGL().getGL2();
            drawPantryCupboard(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        }

        private void drawPantryCupboard(GL2 gl, int viewportWidth, int viewportHeight) {
            int centerX = viewportWidth / 2;
            int centerY = viewportHeight / 2;

            int cupboardWidth = 700;
            int cupboardHeight = 100;
            int cupboardX = centerX - (cupboardWidth / 2);
            int cupboardY = centerY - (cupboardHeight / 2) - 50;

            gl.glColor3f(0.65f, 0.50f, 0.39f);
            GLUtil2D.drawRectangle(gl, cupboardX, cupboardY, cupboardWidth, cupboardHeight);

            int shelfWidth = 100;
            int shelfHeight = 50;
            int shelfX = cupboardX + 370;
            int shelfY = cupboardY + cupboardHeight + 10;

            gl.glColor3f(0.59f, 0.41f, 0.31f);
            for (int i = 0; i < 3; i++) {
                GLUtil2D.drawRectangle(gl, shelfX, shelfY, shelfWidth, shelfHeight);
                shelfX += shelfWidth + 10;
            }

            int baseWidth = 700;
            int baseHeight = 80;
            int baseX = centerX - (baseWidth / 2);
            int baseY = cupboardY - 100;

            gl.glColor3f(0.72f, 0.54f, 0.04f);
            GLUtil2D.drawRectangle(gl, baseX, baseY, baseWidth, baseHeight);
            GLUtil2D.drawText(gl, "Pantry Cupboard Front View ( h:180cm * w:60cm * d:60cm ) ", 715, 220);
        }
    }

    public static class PantryCupboard2DPanel extends JPanel {
        public PantryCupboard2DPanel() {
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            PantryCupboard2D pantryCupboard2D = new PantryCupboard2D();
            gljPanel.addGLEventListener(pantryCupboard2D);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);
        }
    }

    public static class PantryCupboardDisplay extends JPanel {
        public PantryCupboardDisplay(boolean showControls) {
            PantryCupboardManager pantryCupboard = new PantryCupboardManager();
            setLayout(new BorderLayout());
            final GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            final GLJPanel gljPanel = new GLJPanel(capabilities);
            gljPanel.addGLEventListener(pantryCupboard);
            gljPanel.setSize(800, 600);
            add(gljPanel, BorderLayout.CENTER);

            if (showControls) {
                ControlsPanel controlsPanel = new ControlsPanel(pantryCupboard.rotationManager, pantryCupboard.colorManager, pantryCupboard.lightingManager);
                add(controlsPanel, BorderLayout.SOUTH);
                SavePanel savePanel = new SavePanel("PantryCupboardDisplay");
                add(savePanel, BorderLayout.NORTH);
            }

            final FPSAnimator animator = new FPSAnimator(gljPanel, 60, true);
            animator.start();
        }
    }

    public static class PantryCupboardStartUpPanel extends JPanel {
        public PantryCupboardStartUpPanel() {
            setBackground(new Color(171, 222, 247));
            setLayout(null);

            JLabel titleLabel = new JLabel("Pantry Cupboard");
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
            titleLabel.setForeground(Color.black);
            titleLabel.setBounds(50, 50, 400, 30);
            add(titleLabel);

            JLabel descriptionLabel = StartupUtils.createDescriptionLabel(
                    "You Can Select Either 2D or 3D model to Customize Your Design ....",
                    430, 190, 750, 30);
            add(descriptionLabel);

            JLabel backgroundImage = StartupUtils.createImageLabel("icons/pexels-max-vakhtbovycn-7587744.jpg", 0, 0, 1540, 760);
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
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PantryCupboardStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "PantryCupboardDisplay2d");
            });
            add(button2d);

            JButton button3d = new RoundedButton("3d Design", startColor, endColor);
            button3d.setBounds(900, 550, 100, 30);
            button3d.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PantryCupboardStartUpPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
                cardLayout.show(frame.getContentPane(), "PantryCupboardDisplay");
            });
            add(button3d);

            add(backgroundImage);
        }
    }
}