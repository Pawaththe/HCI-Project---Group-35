package furniture.management.system.view;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import furniture.management.system.model.Furniture;
import furniture.management.system.model.FurnitureFactory;
import furniture.management.system.model.RotationManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Renderer3D implements GLEventListener {
    private final List<RenderItem> items = new ArrayList();
    private final RoomConfigPanel roomConfigPanel;
    private final RotationManager rotationManager;
    private final GLU glu = new GLU();
    private final GLUT glut = new GLUT();
    private RenderItem selectedItem = null;
    public boolean isResizingFloor = false;
    private Texture backgroundTexture = null;
    private Texture wallTexture = null;
    private float cameraY = 15.0F;
    private float cameraZ = 15.0F;
    private float rotationAngleX = 30.0F;
    private float rotationAngleY = 0.0F;

    public Renderer3D(RoomConfigPanel roomConfigPanel, RotationManager rotationManager) {
        this.roomConfigPanel = roomConfigPanel;
        this.rotationManager = rotationManager;
    }

    public void add(String type, float scale) {
        RenderItem item = new RenderItem(type, scale);
        this.items.add(item);
        item.x = (float)(this.items.size() % 3) * 5.0F - 5.0F;
        item.z = (float)(this.items.size() / 3) * 5.0F - 5.0F;
    }

    public void clearItems() {
        this.items.clear();
    }

    public List<RenderItem> getItems() {
        return new ArrayList(this.items);
    }

    public void zoomIn() {
        this.cameraY = Math.max(5.0F, this.cameraY - 1.0F);
        this.cameraZ = Math.max(5.0F, this.cameraZ - 1.0F);
    }

    public void zoomOut() {
        this.cameraY = Math.min(30.0F, this.cameraY + 1.0F);
        this.cameraZ = Math.min(30.0F, this.cameraZ + 1.0F);
    }

    public void selectItemAt(int mouseX, int mouseY) {
        if (this.items.isEmpty()) {
            this.selectedItem = null;
        } else {
            int currentIndex = this.selectedItem == null ? -1 : this.items.indexOf(this.selectedItem);
            int nextIndex = (currentIndex + 1) % this.items.size();
            this.selectedItem = (RenderItem)this.items.get(nextIndex);
        }
    }

    public RenderItem getSelectedItem() {
        return this.selectedItem;
    }

    public void setRotationAngles(float rotationX, float rotationY) {
        this.rotationAngleX = rotationX;
        this.rotationManager.rotateRight();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(2929);
        gl.glEnable(3042);
        gl.glBlendFunc(770, 771);
        gl.glEnable(3553);
        gl.glEnable(2896);
        gl.glEnable(16384);
        float[] lightPos = new float[]{10.0F, 10.0F, 10.0F, 1.0F};
        gl.glLightfv(16384, 4611, lightPos, 0);
        float[] ambient = new float[]{0.3F, 0.3F, 0.3F, 1.0F};
        float[] diffuse = new float[]{0.7F, 0.7F, 0.7F, 1.0F};
        gl.glLightfv(16384, 4608, ambient, 0);
        gl.glLightfv(16384, 4609, diffuse, 0);
        gl.glEnable(2903);
        gl.glColorMaterial(1032, 5634);

        IOException e;
        File wallImgFile;
        try {
            wallImgFile = new File("background.jpg");
            this.backgroundTexture = TextureIO.newTexture(wallImgFile, true);
        } catch (IOException var8) {
            e = var8;
            System.err.println("Error loading background texture: " + e.getMessage());
            gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        try {
            wallImgFile = new File("wall_texture.jpg");
            this.wallTexture = TextureIO.newTexture(wallImgFile, true);
        } catch (IOException var7) {
            e = var7;
            System.err.println("Error loading wall texture: " + e.getMessage());
        }

    }

    public void dispose(GLAutoDrawable drawable) {
        if (this.backgroundTexture != null) {
            this.backgroundTexture.destroy(drawable.getGL());
        }

        if (this.wallTexture != null) {
            this.wallTexture.destroy(drawable.getGL());
        }

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        this.glu.gluPerspective(45.0F, (float)width / (float)height, 1.0F, 100.0F);
        gl.glMatrixMode(5888);
    }

    public void display(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        gl.glClear(16640);
        if (this.backgroundTexture != null) {
            gl.glMatrixMode(5889);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
            gl.glMatrixMode(5888);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glDisable(2929);
            gl.glDisable(2896);
            this.backgroundTexture.enable(gl);
            this.backgroundTexture.bind(gl);
            gl.glBegin(7);
            gl.glTexCoord2f(0.0F, 0.0F);
            gl.glVertex2f(-1.0F, -1.0F);
            gl.glTexCoord2f(1.0F, 0.0F);
            gl.glVertex2f(1.0F, -1.0F);
            gl.glTexCoord2f(1.0F, 1.0F);
            gl.glVertex2f(1.0F, 1.0F);
            gl.glTexCoord2f(0.0F, 1.0F);
            gl.glVertex2f(-1.0F, 1.0F);
            gl.glEnd();
            this.backgroundTexture.disable(gl);
            gl.glEnable(2929);
            gl.glEnable(2896);
            gl.glMatrixMode(5889);
            gl.glPopMatrix();
            gl.glMatrixMode(5888);
            gl.glPopMatrix();
        }

        gl.glLoadIdentity();
        this.glu.gluLookAt(0.0F, this.cameraY, this.cameraZ, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
        gl.glRotatef(this.rotationAngleX, 1.0F, 0.0F, 0.0F);
        gl.glRotatef(this.rotationAngleY, 0.0F, 1.0F, 0.0F);
        RoomConfigPanel.RoomShape shape = this.roomConfigPanel.getRoomShape();
        float roomWidth = this.roomConfigPanel.getRoomWidth();
        float roomDepth = this.roomConfigPanel.getRoomDepth();
        this.drawRoomShape3D(gl, shape, roomWidth, roomDepth);
        gl.glPushMatrix();
        gl.glDisable(3553);
        gl.glColor3f((float)this.roomConfigPanel.getFloorColor().getRed() / 255.0F, (float)this.roomConfigPanel.getFloorColor().getGreen() / 255.0F, (float)this.roomConfigPanel.getFloorColor().getBlue() / 255.0F);
        this.drawFloor3D(gl, shape, roomWidth, roomDepth);
        gl.glEnable(3553);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glDisable(3553);
        gl.glDisable(2896);
        gl.glColor3f(0.0F, 0.0F, 0.0F);
        gl.glRasterPos3f(-roomWidth / 2.0F + 1.0F, 0.5F, -roomDepth / 2.0F + 1.0F);
        GLUT var10000 = this.glut;
        String var10002 = String.format("%.1f", roomWidth);
        var10000.glutBitmapString(7, "Width: " + var10002 + "m, Depth: " + String.format("%.1f", roomDepth) + "m");
        gl.glEnable(3553);
        gl.glEnable(2896);
        gl.glPopMatrix();
        Iterator var6 = this.items.iterator();

        while(var6.hasNext()) {
            RenderItem ri = (RenderItem)var6.next();
            gl.glPushMatrix();

            try {
                gl.glTranslatef(ri.x, 0.0F, ri.z);
                gl.glScalef(ri.scale, ri.scale, ri.scale);
                Furniture f3 = FurnitureFactory.create(ri.type);
                f3.display(d);
                gl.glLoadIdentity();
                gl.glColor3f(1.0F, 1.0F, 1.0F);
                gl.glLineWidth(1.0F);
            } catch (Exception var12) {
                Exception e = var12;
                String var10001 = ri.type;
                System.err.println("Error rendering 3D furniture item " + var10001 + ": " + e.getMessage());
            } finally {
                gl.glPopMatrix();
            }
        }

    }

    public void updateRoomDimensions(float dx, float dy) {
        float newWidth = this.roomConfigPanel.getRoomWidth() + dx;
        float newDepth = this.roomConfigPanel.getRoomDepth() + dy;
        newWidth = Math.max(2.0F, Math.min(50.0F, newWidth));
        newDepth = Math.max(2.0F, Math.min(50.0F, newDepth));
        this.roomConfigPanel.setRoomWidth(newWidth);
        this.roomConfigPanel.setRoomDepth(newDepth);
    }

    private void drawQuad(GL2 gl, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        gl.glBegin(7);
        gl.glVertex3f(x1, y1, z1);
        gl.glVertex3f(x2, y2, z2);
        gl.glVertex3f(x3, y3, z3);
        gl.glVertex3f(x4, y4, z4);
        gl.glEnd();
    }

    private void drawFloor3D(GL2 gl, RoomConfigPanel.RoomShape shape, float roomWidth, float roomDepth) {
        gl.glBegin(7);
        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
                gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
                gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
                gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
                break;
            case L_SHAPE:
                this.drawQuad(gl, -roomWidth / 2.0F, 0.0F, roomDepth / 2.0F, -roomWidth / 4.0F, 0.0F, roomDepth / 2.0F, -roomWidth / 4.0F, 0.0F, -roomDepth / 2.0F, -roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
                this.drawQuad(gl, -roomWidth / 4.0F, 0.0F, roomDepth / 2.0F, roomWidth / 2.0F, 0.0F, roomDepth / 2.0F, roomWidth / 2.0F, 0.0F, roomDepth / 4.0F, -roomWidth / 4.0F, 0.0F, roomDepth / 4.0F);
                this.drawQuad(gl, 0.0F, 0.0F, roomDepth / 4.0F, roomWidth / 2.0F, 0.0F, roomDepth / 4.0F, roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F, 0.0F, 0.0F, -roomDepth / 2.0F);
                this.drawQuad(gl, -roomWidth / 4.0F, 0.0F, roomDepth / 4.0F, 0.0F, 0.0F, roomDepth / 4.0F, 0.0F, 0.0F, -roomDepth / 2.0F, -roomWidth / 4.0F, 0.0F, -roomDepth / 2.0F);
                break;
            case U_SHAPE:
                float w = roomWidth;
                float d = roomDepth;
                float wallHeight = 5.0F;
                float x1 = -w / 2.0F;
                float x2 = -w / 4.0F;
                float x3 = w / 4.0F;
                float x4 = w / 2.0F;
                float z1 = -d / 2.0F;
                float z2 = -d / 4.0F;
                float z3 = d / 2.0F;
                this.drawQuad(gl, x1, 0.0F, z3, x2, 0.0F, z3, x2, 0.0F, z1, x1, 0.0F, z1);
                this.drawQuad(gl, x3, 0.0F, z3, x4, 0.0F, z3, x4, 0.0F, z1, x3, 0.0F, z1);
                this.drawQuad(gl, x2, 0.0F, z2, x3, 0.0F, z2, x3, 0.0F, z1, x2, 0.0F, z1);
                gl.glPushMatrix();
                gl.glDisable(3553);
                gl.glEnable(2896);
                gl.glColor4f((float)this.roomConfigPanel.getWallColor().getRed() / 255.0F, (float)this.roomConfigPanel.getWallColor().getGreen() / 255.0F, (float)this.roomConfigPanel.getWallColor().getBlue() / 255.0F, 0.8F);
                gl.glBegin(7);
                gl.glVertex3f(x2, 0.0F, z3);
                gl.glVertex3f(x2, 0.0F, z2);
                gl.glVertex3f(x2, wallHeight, z2);
                gl.glVertex3f(x2, wallHeight, z3);
                gl.glEnd();
                gl.glBegin(7);
                gl.glVertex3f(x3, 0.0F, z3);
                gl.glVertex3f(x3, 0.0F, z2);
                gl.glVertex3f(x3, wallHeight, z2);
                gl.glVertex3f(x3, wallHeight, z3);
                gl.glEnd();
                gl.glPopMatrix();
                break;
            case T_SHAPE:
                float tw = roomWidth;
                float td = roomDepth;
                float tx1 = -tw / 2.0F;
                float tx2 = -tw / 4.0F;
                float tx3 = tw / 4.0F;
                float tx4 = tw / 2.0F;
                float tz1 = -td / 2.0F;
                float tz2 = -td / 4.0F;
                float tz3 = td / 4.0F;
                float tz4 = td / 2.0F;
                this.drawQuad(gl, tx1, 0.0F, tz1, tx4, 0.0F, tz1, tx4, 0.0F, tz2, tx1, 0.0F, tz2);
                this.drawQuad(gl, tx2, 0.0F, tz2, tx3, 0.0F, tz2, tx3, 0.0F, tz4, tx2, 0.0F, tz4);
                break;
            case CIRCULAR:
                int segments = 128;
                float radius = Math.min(roomWidth, roomDepth) / 2.0F;
                gl.glEnd();
                gl.glBegin(6);
                gl.glVertex3f(0.0F, 0.0F, 0.0F);

                for(int i = 0; i <= segments; ++i) {
                    double angle = 6.283185307179586 * (double)i / (double)segments;
                    float x = radius * (float)Math.cos(angle);
                    float z = radius * (float)Math.sin(angle);
                    gl.glVertex3f(x, 0.0F, z);
                }

                gl.glEnd();
        }

        gl.glEnd();
    }

    private void drawRoomShape3D(GL2 gl, RoomConfigPanel.RoomShape shape, float roomWidth, float roomDepth) {
        float wallAlpha = 0.8F;
        gl.glColor4f(1.0F, 1.0F, 1.0F, wallAlpha);
        if (this.wallTexture != null) {
            this.wallTexture.enable(gl);
            this.wallTexture.bind(gl);
        } else {
            gl.glColor4f((float)this.roomConfigPanel.getWallColor().getRed() / 255.0F, (float)this.roomConfigPanel.getWallColor().getGreen() / 255.0F, (float)this.roomConfigPanel.getWallColor().getBlue() / 255.0F, wallAlpha);
        }

        float wallHeight = 5.0F;
        float texScaleX = roomWidth / 5.0F;
        float texScaleY = wallHeight / 5.0F;
        float texScaleZ = roomDepth / 5.0F;
        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                this.drawRectangularWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case L_SHAPE:
                this.drawLShapeWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case U_SHAPE:
                this.drawUShapeWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case T_SHAPE:
                this.drawTShapeWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case CIRCULAR:
                this.drawCircularWalls(gl, texScaleY, roomWidth, roomDepth);
        }

        if (this.wallTexture != null) {
            this.wallTexture.disable(gl);
        }

    }

    private void drawRectangularWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5.0F;
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
    }

    private void drawLShapeWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5.0F;
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX / 2.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 4.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX / 2.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 4.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ / 2.0F, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 4.0F);
        gl.glTexCoord2f(texScaleZ / 2.0F, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, roomDepth / 4.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 4.0F, 0.0F, roomDepth / 4.0F);
        gl.glTexCoord2f(texScaleX / 2.0F, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 4.0F);
        gl.glTexCoord2f(texScaleX / 2.0F, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, roomDepth / 4.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 4.0F, wallHeight, roomDepth / 4.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 4.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ / 4.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 4.0F, 0.0F, roomDepth / 4.0F);
        gl.glTexCoord2f(texScaleZ / 4.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 4.0F, wallHeight, roomDepth / 4.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 4.0F, wallHeight, roomDepth / 2.0F);
        gl.glEnd();
    }

    private void drawUShapeWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5.0F;
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, 0.0F);
        gl.glVertex3f(-roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, -roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, 0.0F);
        gl.glVertex3f(roomWidth / 2.0F, 0.0F, roomDepth / 2.0F);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, roomDepth / 2.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(roomWidth / 2.0F, wallHeight, -roomDepth / 2.0F);
        gl.glEnd();
        gl.glBegin(7);
        gl.glTexCoord2f(0.0F, 0.0F);
        gl.glVertex3f(-roomWidth / 4.0F, 0.0F, -roomDepth / 4.0F);
        gl.glTexCoord2f(texScaleX / 2.0F, 0.0F);
        gl.glVertex3f(roomWidth / 4.0F, 0.0F, -roomDepth / 4.0F);
        gl.glTexCoord2f(texScaleX / 2.0F, texScaleY);
        gl.glVertex3f(roomWidth / 4.0F, wallHeight, -roomDepth / 4.0F);
        gl.glTexCoord2f(0.0F, texScaleY);
        gl.glVertex3f(-roomWidth / 4.0F, wallHeight, -roomDepth / 4.0F);
        gl.glEnd();
    }

    private void drawTShapeWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float tw = roomWidth;
        float td = roomDepth;
        float tWallHeight = 5.0F;
        float tx1 = -tw / 2.0F;
        float tx2 = -tw / 4.0F;
        float tx3 = tw / 4.0F;
        float tx4 = tw / 2.0F;
        float tz1 = -td / 2.0F;
        float tz2 = -td / 4.0F;
        float tz3 = td / 4.0F;
        float tz4 = td / 2.0F;
        this.drawQuad(gl, tx2, 0.0F, tz4, tx3, 0.0F, tz4, tx3, tWallHeight, tz4, tx2, tWallHeight, tz4);
        this.drawQuad(gl, tx1, 0.0F, tz2, tx1, 0.0F, tz1, tx1, tWallHeight, tz1, tx1, tWallHeight, tz2);
        this.drawQuad(gl, tx4, 0.0F, tz2, tx4, 0.0F, tz1, tx4, tWallHeight, tz1, tx4, tWallHeight, tz2);
        this.drawQuad(gl, tx1, 0.0F, tz1, tx4, 0.0F, tz1, tx4, tWallHeight, tz1, tx1, tWallHeight, tz1);
        this.drawQuad(gl, tx2, 0.0F, tz4, tx2, 0.0F, tz2, tx2, tWallHeight, tz2, tx2, tWallHeight, tz4);
        this.drawQuad(gl, tx3, 0.0F, tz4, tx3, 0.0F, tz2, tx3, tWallHeight, tz2, tx3, tWallHeight, tz4);
    }

    private void drawCircularWalls(GL2 gl, float texScaleY, float roomWidth, float roomDepth) {
        float wallHeight = 5.0F;
        int segments = 128;
        float radius = Math.min(roomWidth, roomDepth) / 2.0F;
        float wallThickness = 0.05F;
        if (this.wallTexture != null) {
            this.wallTexture.enable(gl);
            this.wallTexture.bind(gl);
        }

        for(int i = 0; i < segments; ++i) {
            double angle1 = 6.283185307179586 * (double)i / (double)segments;
            double angle2 = 6.283185307179586 * (double)(i + 1) / (double)segments;
            float x1Inner = (radius - wallThickness) * (float)Math.cos(angle1);
            float z1Inner = (radius - wallThickness) * (float)Math.sin(angle1);
            float x2Inner = (radius - wallThickness) * (float)Math.cos(angle2);
            float z2Inner = (radius - wallThickness) * (float)Math.sin(angle2);
            float x1Outer = radius * (float)Math.cos(angle1);
            float z1Outer = radius * (float)Math.sin(angle1);
            float x2Outer = radius * (float)Math.cos(angle2);
            float z2Outer = radius * (float)Math.sin(angle2);
            gl.glBegin(7);
            gl.glTexCoord2f(0.0F, 0.0F);
            gl.glVertex3f(x1Inner, 0.0F, z1Inner);
            gl.glTexCoord2f(1.0F, 0.0F);
            gl.glVertex3f(x2Inner, 0.0F, z2Inner);
            gl.glTexCoord2f(1.0F, texScaleY);
            gl.glVertex3f(x2Outer, wallHeight, z2Outer);
            gl.glTexCoord2f(0.0F, texScaleY);
            gl.glVertex3f(x1Outer, wallHeight, z1Outer);
            gl.glEnd();
        }

        if (this.wallTexture != null) {
            this.wallTexture.disable(gl);
        }

    }
}
