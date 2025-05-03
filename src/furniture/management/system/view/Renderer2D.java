package furniture.management.system.view;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import furniture.management.system.model.Furniture2D;
import furniture.management.system.model.FurnitureFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Renderer2D implements GLEventListener, Serializable {
    private final List<RenderItem> items = new ArrayList();
    private final RoomConfigPanel roomConfigPanel;
    private float rotationAngleY = 0.0F;
    private static final float ROOM_SCALE = 20.0F;

    public Renderer2D(RoomConfigPanel roomConfigPanel) {
        this.roomConfigPanel = roomConfigPanel;
    }

    public void add(String type, float scale) {
        this.items.add(new RenderItem(type, scale));
    }

    public void clearItems() {
        this.items.clear();
    }

    public List<RenderItem> getItems() {
        return new ArrayList(this.items);
    }

    public void setRotationAngleY(float angle) {
        this.rotationAngleY = angle;
    }

    public void init(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        gl.glClearColor((float)this.roomConfigPanel.getWallColor().getRed() / 255.0F, (float)this.roomConfigPanel.getWallColor().getGreen() / 255.0F, (float)this.roomConfigPanel.getWallColor().getBlue() / 255.0F, 1.0F);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, 800.0, 0.0, 600.0, -1.0, 1.0);
        gl.glMatrixMode(5888);
    }

    public void dispose(GLAutoDrawable d) {
    }

    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        GL2 gl = d.getGL().getGL2();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, (double)w, 0.0, (double)h, -1.0, 1.0);
        gl.glMatrixMode(5888);
    }

    public void display(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        gl.glClear(16384);
        gl.glLoadIdentity();
        int width = d.getSurfaceWidth();
        int height = d.getSurfaceHeight();
        gl.glTranslatef((float)width / 2.0F, (float)height / 2.0F, 0.0F);
        float scaledWidth = this.roomConfigPanel.getRoomWidth() * ((float)width / 20.0F);
        float scaledHeight = this.roomConfigPanel.getRoomDepth() * ((float)height / 20.0F);
        gl.glColor3f((float)this.roomConfigPanel.getFloorColor().getRed() / 255.0F, (float)this.roomConfigPanel.getFloorColor().getGreen() / 255.0F, (float)this.roomConfigPanel.getFloorColor().getBlue() / 255.0F);
        RoomConfigPanel.RoomShape shape = this.roomConfigPanel.getRoomShape();
        this.drawRoomShape2D(gl, shape, scaledWidth, scaledHeight);

        for(Iterator var8 = this.items.iterator(); var8.hasNext(); gl.glPopMatrix()) {
            RenderItem ri = (RenderItem)var8.next();
            gl.glPushMatrix();
            gl.glRotatef(this.rotationAngleY, 0.0F, 0.0F, 1.0F);
            float scaledX = ri.x * ((float)width / 20.0F);
            float scaledZ = ri.z * ((float)height / 20.0F);
            gl.glTranslatef(scaledX, scaledZ, 0.0F);
            gl.glScalef(ri.scale * ((float)width / 20.0F), ri.scale * ((float)height / 20.0F), 1.0F);

            try {
                Furniture2D f2 = FurnitureFactory.create2D(ri.type);
                f2.display(d);
            } catch (Exception var13) {
                Exception e = var13;
                String var10001 = ri.type;
                System.err.println("Error rendering 2D furniture item " + var10001 + ": " + e.getMessage());
            }
        }

    }

    private void drawRoomShape2D(GL2 gl, RoomConfigPanel.RoomShape shape, float w, float h) {
        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                gl.glBegin(7);
                gl.glVertex2f(-w / 2.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, h / 2.0F);
                gl.glVertex2f(-w / 2.0F, h / 2.0F);
                gl.glEnd();
                break;
            case L_SHAPE:
                gl.glBegin(7);
                gl.glVertex2f(-w / 2.0F, -h / 2.0F);
                gl.glVertex2f(-w / 4.0F, -h / 2.0F);
                gl.glVertex2f(-w / 4.0F, h / 2.0F);
                gl.glVertex2f(-w / 2.0F, h / 2.0F);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex2f(-w / 4.0F, h / 4.0F);
                gl.glVertex2f(w / 2.0F, h / 4.0F);
                gl.glVertex2f(w / 2.0F, h / 2.0F);
                gl.glVertex2f(-w / 4.0F, h / 2.0F);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex2f(0.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, h / 4.0F);
                gl.glVertex2f(0.0F, h / 4.0F);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex2f(-w / 4.0F, -h / 2.0F);
                gl.glVertex2f(0.0F, -h / 2.0F);
                gl.glVertex2f(0.0F, h / 4.0F);
                gl.glVertex2f(-w / 4.0F, h / 4.0F);
                gl.glEnd();
                break;
            case U_SHAPE:
                gl.glBegin(7);
                gl.glVertex2f(-w / 2.0F, -h / 2.0F);
                gl.glVertex2f(-w / 4.0F, -h / 2.0F);
                gl.glVertex2f(-w / 4.0F, h / 2.0F);
                gl.glVertex2f(-w / 2.0F, h / 2.0F);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex2f(w / 4.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, h / 2.0F);
                gl.glVertex2f(w / 4.0F, h / 2.0F);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex2f(-w / 4.0F, -h / 2.0F);
                gl.glVertex2f(w / 4.0F, -h / 2.0F);
                gl.glVertex2f(w / 4.0F, -h / 4.0F);
                gl.glVertex2f(-w / 4.0F, -h / 4.0F);
                gl.glEnd();
                break;
            case T_SHAPE:
                gl.glBegin(7);
                gl.glVertex2f(-w / 2.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, -h / 2.0F);
                gl.glVertex2f(w / 2.0F, -h / 4.0F);
                gl.glVertex2f(-w / 2.0F, -h / 4.0F);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex2f(-w / 4.0F, -h / 4.0F);
                gl.glVertex2f(w / 4.0F, -h / 4.0F);
                gl.glVertex2f(w / 4.0F, h / 2.0F);
                gl.glVertex2f(-w / 4.0F, h / 2.0F);
                gl.glEnd();
                break;
            case CIRCULAR:
                gl.glBegin(9);
                int segments = 64;

                for(int i = 0; i < segments; ++i) {
                    double angle = 6.283185307179586 * (double)i / (double)segments;
                    float x = w / 2.0F * (float)Math.cos(angle);
                    float y = h / 2.0F * (float)Math.sin(angle);
                    gl.glVertex2f(x, y);
                }

                gl.glEnd();
        }

    }
}