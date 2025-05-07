package furniture.management.system.view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class Renderer2D implements GLEventListener {
    private final List<RenderItem> items = new ArrayList<>();
    private final RoomConfigPanel roomConfigPanel;
    private final GLUT glut = new GLUT();
    private RenderItem selectedItem;

    private float zoom = 1.0f;
    private float panX = 0.0f;
    private float panY = 0.0f;
    private int lastMouseX, lastMouseY;
    private boolean isDragging = false;
    private static final float MIN_ZOOM = 0.1f;
    private static final float MAX_ZOOM = 3.0f;
    private float globalRotationY = 0.0f;
    private GLJPanel glPanel;

    public Renderer2D(RoomConfigPanel roomConfigPanel) {
        this.roomConfigPanel = roomConfigPanel;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(1f, 1f, 1f, 1f); // background color
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void zoomIn() {
        zoom = Math.min(MAX_ZOOM, zoom + 0.1f);
    }

    public void setGLPanel(GLJPanel glPanel) {
        this.glPanel = glPanel;
    }

    public void zoomOut() {
        zoom = Math.max(MIN_ZOOM, zoom - 0.1f);
    }

    public void resetView() {
        zoom = 1.0f;
        panX = 0f;
        panY = 0f;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        if (gl == null) return;

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        setupViewport(gl, drawable);
        drawRoom(gl);
        drawFurniture(gl);
        drawGrid(gl);
    }

    private void setupViewport(GL2 gl, GLAutoDrawable drawable) {
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();
        float aspect = (float) width / height;
        float viewWidth = 20.0f / zoom;
        float viewHeight = viewWidth / aspect;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-viewWidth / 2 + panX, viewWidth / 2 + panX,
                -viewHeight / 2 + panY, viewHeight / 2 + panY,
                -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotatef(globalRotationY, 0, 0, 1);
    }

    public void setRotationAngleY(float angle) {
        this.globalRotationY = angle;
    }

    private void drawRoom(GL2 gl) {
        float w = roomConfigPanel.getRoomWidth();
        float d = roomConfigPanel.getRoomDepth();
        RoomConfigPanel.RoomShape shape = roomConfigPanel.getRoomShape();

        gl.glColor3f(
                roomConfigPanel.getFloorColor().getRed() / 255f,
                roomConfigPanel.getFloorColor().getGreen() / 255f,
                roomConfigPanel.getFloorColor().getBlue() / 255f
        );

        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                gl.glBegin(GL2.GL_QUADS);
                // NEW (Y/Z-axis flipped)
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(w / 2, -d / 2);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(-w / 2, d / 2);
                gl.glEnd();
                break;

            case L_SHAPE:
                gl.glBegin(GL2.GL_QUADS);
                // Vertical part of L
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(-w / 4, -d / 2);
                gl.glVertex2f(-w / 4, d / 2);
                gl.glVertex2f(-w / 2, d / 2);

                // Horizontal part of L
                gl.glVertex2f(-w / 4, d / 4);
                gl.glVertex2f(w / 2, d / 4);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(-w / 4, d / 2);
                gl.glEnd();
                break;


            case U_SHAPE:
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(-w / 4, -d / 2);
                gl.glVertex2f(-w / 4, d / 2);
                gl.glVertex2f(-w / 2, d / 2);

                gl.glVertex2f(w / 4, -d / 2);
                gl.glVertex2f(w / 2, -d / 2);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(w / 4, d / 2);

                gl.glVertex2f(-w / 4, -d / 2);
                gl.glVertex2f(w / 4, -d / 2);
                gl.glVertex2f(w / 4, -d / 4);
                gl.glVertex2f(-w / 4, -d / 4);
                gl.glEnd();
                break;

            case T_SHAPE:
                gl.glBegin(GL2.GL_QUADS);

                // Top horizontal bar of the T
                gl.glVertex2f(-w / 2, d / 2);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(w / 2, d / 4);
                gl.glVertex2f(-w / 2, d / 4);

                // Bottom vertical stem of the T
                gl.glVertex2f(-w / 4, d / 4);
                gl.glVertex2f(w / 4, d / 4);
                gl.glVertex2f(w / 4, -d / 2);
                gl.glVertex2f(-w / 4, -d / 2);

                gl.glEnd();
                break;


            case CIRCULAR:
                float r = Math.min(w, d) / 2f;
                int segs = 64;
                gl.glBegin(GL2.GL_POLYGON);
                for (int i = 0; i < segs; i++) {
                    double theta = 2.0 * Math.PI * i / segs;
                    float x = (float) Math.cos(theta) * r;
                    float y = (float) Math.sin(theta) * r;
                    gl.glVertex2f(x, y);
                }
                gl.glEnd();
                break;
        }
        // Draw walls (as border lines) using wall color
        gl.glColor3f(
                roomConfigPanel.getWallColor().getRed() / 255f,
                roomConfigPanel.getWallColor().getGreen() / 255f,
                roomConfigPanel.getWallColor().getBlue() / 255f
        );
        gl.glLineWidth(3f);
        gl.glBegin(GL2.GL_LINE_LOOP);

        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                // NEW (Y/Z-axis flipped)
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(w / 2, -d / 2);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(-w / 2, d / 2);
                break;

            case L_SHAPE:
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(-w / 4, -d / 2);
                gl.glVertex2f(-w / 4, d / 4);
                gl.glVertex2f(w / 2, d / 4);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(-w / 2, d / 2);
                break;

            case U_SHAPE:
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(-w / 4, -d / 2);
                gl.glVertex2f(-w / 4, d / 2);
                gl.glVertex2f(w / 4, d / 2);
                gl.glVertex2f(w / 4, -d / 2);
                gl.glVertex2f(w / 2, -d / 2);
                gl.glVertex2f(w / 2, d / 2);
                gl.glVertex2f(-w / 2, d / 2);
                break;

            case T_SHAPE:
                gl.glVertex2f(-w / 2, -d / 2);
                gl.glVertex2f(w / 2, -d / 2);
                gl.glVertex2f(w / 2, -d / 4);
                gl.glVertex2f(w / 4, -d / 4);
                gl.glVertex2f(w / 4, d / 2);
                gl.glVertex2f(-w / 4, d / 2);
                gl.glVertex2f(-w / 4, -d / 4);
                gl.glVertex2f(-w / 2, -d / 4);
                break;

            case CIRCULAR:
                int segments = 64;
                float r = Math.min(w, d) / 2f;
                for (int i = 0; i <= segments; i++) {
                    double angle = 2 * Math.PI * i / segments;
                    float x = (float) Math.cos(angle) * r;
                    float y = (float) Math.sin(angle) * r;
                    gl.glVertex2f(x, y);
                }
                break;
        }
        gl.glEnd();
        gl.glLineWidth(1f);

    }

    private void drawFurniture(GL2 gl) {
        for (RenderItem item : items) {
            gl.glPushMatrix();
            gl.glTranslatef(item.x, item.z, 0);
            gl.glRotatef(item.rotationY, 0, 0, 1);

            if (item.type.startsWith("CoffeeTable")) {
                drawCoffeeTable2D(gl, item); // Your custom drawing function
            } else {
                gl.glColor3f(0.6f, 0.4f, 0.2f); // default color
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2f(-item.width / 2, -item.height / 2);
                gl.glVertex2f(item.width / 2, -item.height / 2);
                gl.glVertex2f(item.width / 2, item.height / 2);
                gl.glVertex2f(-item.width / 2, item.height / 2);
                gl.glEnd();
            }

            if (item == selectedItem) {
                // Highlight selected item
                gl.glColor3f(1, 1, 1);
                gl.glLineWidth(2);
                gl.glBegin(GL2.GL_LINE_LOOP);
                gl.glVertex2f(-item.width / 2, -item.height / 2);
                gl.glVertex2f(item.width / 2, -item.height / 2);
                gl.glVertex2f(item.width / 2, item.height / 2);
                gl.glVertex2f(-item.width / 2, item.height / 2);
                gl.glEnd();
                gl.glLineWidth(1);

                // Draw dimensions
                gl.glColor3f(0, 0, 0);
                gl.glRasterPos2f(0, item.height / 2 + 0.2f);
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, item.type);

                gl.glRasterPos2f(0, -item.height / 2 - 0.5f);
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, String.format("W: %.1fft", item.width * item.scale));

                gl.glRasterPos2f(0, -item.height / 2 - 1.0f);
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, String.format("H: %.1fft", item.height * item.scale));
            }

            gl.glPopMatrix();
        }
    }


    private void drawCoffeeTable2D(GL2 gl, RenderItem item) {
        gl.glColor3f(0.824f, 0.706f, 0.549f); // Light wood tone

        float segmentHeight = item.height / 4f;
        float y = -item.height / 2;

        for (int i = 0; i < 4; i++) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(-item.width / 2, y + i * segmentHeight);
            gl.glVertex2f(item.width / 2, y + i * segmentHeight);
            gl.glVertex2f(item.width / 2, y + (i + 1) * segmentHeight);
            gl.glVertex2f(-item.width / 2, y + (i + 1) * segmentHeight);
            gl.glEnd();
        }
    }


    private void drawGrid(GL2 gl) {
        float w = roomConfigPanel.getRoomWidth();
        float d = roomConfigPanel.getRoomDepth();
        gl.glColor3f(0.9f, 0.9f, 0.9f);
        gl.glLineWidth(1);
        gl.glBegin(GL2.GL_LINES);

        for (float x = -w / 2; x <= w / 2; x += 1.0f) {
            gl.glVertex2f(x, -d / 2);
            gl.glVertex2f(x, d / 2);
        }

        for (float z = d / 2; z >= -d / 2; z -= 1.0f)
        {
            gl.glVertex2f(-w / 2, z);
            gl.glVertex2f(w / 2, z);
        }

        gl.glEnd();

        gl.glColor3f(0.5f, 0.5f, 0.5f);
        gl.glRasterPos2f(w / 2 - 0.5f, 0.3f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, "X");
        gl.glRasterPos2f(0.3f, -d / 2 + 0.5f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, "Z");
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    public void mousePressed(int x, int y) {
        lastMouseX = x;
        lastMouseY = y;
        isDragging = true;
    }

    public void mouseDragged(int x, int y) {
        if (isDragging) {
            float dx = DragHelper.computeDX(lastMouseX, x, zoom);
            float dz = -DragHelper.computeDZ(lastMouseY, y, zoom); // Invert for 3D consistency
// Z represents up in 2D logic

            panX += dx;
            panY += dz; // Now consistent with 3D

            lastMouseX = x;
            lastMouseY = y;
        }
    }


    public void moveSelectedItem(float dx, float dz) {
        if (selectedItem != null) {
            selectedItem.x += dx;
            selectedItem.z -= dz;

            float halfW = roomConfigPanel.getRoomWidth() / 2f;
            float halfD = roomConfigPanel.getRoomDepth() / 2f;
            float hw = selectedItem.width * selectedItem.scale / 2f;
            float hh = selectedItem.height * selectedItem.scale / 2f;

            selectedItem.x = Math.max(-halfW + hw, Math.min(halfW - hw, selectedItem.x));
            selectedItem.z = Math.max(-halfD + hh, Math.min(halfD - hh, selectedItem.z));
        }
    }


    public void mouseReleased() {
        isDragging = false;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        float zoomFactor = e.getWheelRotation() < 0 ? 1.1f : 0.9f;
        zoom *= zoomFactor;
        zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom));
    }

    public void add(String type, float scale) {
        RenderItem item = new RenderItem(type, scale);

        // Default initial position in the center
        item.x = 0f;
        item.z = 0f;

        // Compute room bounds
        float halfW = roomConfigPanel.getRoomWidth() / 2f;
        float halfD = roomConfigPanel.getRoomDepth() / 2f;
        float hw = item.width * item.scale / 2f;
        float hh = item.height * item.scale / 2f;

        // Clamp to room boundaries
        item.x = Math.max(-halfW + hw, Math.min(halfW - hw, item.x));
        item.z = Math.max(-halfD + hh, Math.min(halfD - hh, item.z));

        items.add(item);

        if (glPanel != null) {
            glPanel.repaint(); // Force redraw
        }
    }



    public void clearItems() {
        items.clear();
    }

    public List<RenderItem> getItems() {
        return items;
    }

    public void setSelectedItem(RenderItem item) {
        this.selectedItem = item;
    }

    public class DragHelper {
        public static float computeDX(int lastX, int currentX, float zoom) {
            return (currentX - lastX) / (200f * zoom);
        }

        public static float computeDZ(int lastY, int currentY, float zoom) {
            return (currentY - lastY) / (200f * zoom);  // Consistent positive up
        }
    }

}

