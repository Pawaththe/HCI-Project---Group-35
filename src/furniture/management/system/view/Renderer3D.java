package furniture.management.system.view;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import furniture.management.system.model.Furniture;
import furniture.management.system.model.FurnitureFactory;
import furniture.management.system.model.RotationManager;
import furniture.management.system.model.DragHelper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Renderer3D implements GLEventListener {
    private final List<RenderItem> items = new ArrayList<>();
    private final RoomConfigPanel roomConfigPanel;
    private final RotationManager rotationManager;
    private final GLU glu = new GLU();
    private final GLUT glut = new GLUT();
    private RenderItem selectedItem = null;
    public boolean isResizingFloor = false;
    private Texture backgroundTexture = null;
    private Texture wallTexture = null;
    private float cameraY = 15f;
    private float cameraZ = 15f;
    private float rotationAngleX = 30f;
    private float rotationAngleY = 0f;
    private FurnitureConfigPanel furnitureConfigPanel;
    private GLJPanel gl3DPanel;
    private boolean isDragging = false;
    private int lastMouseX, lastMouseY;
    private float zoom = 1.0f; // Optional if you plan to use consistent zoom scaling


    public Renderer3D(RoomConfigPanel roomConfigPanel, RotationManager rotationManager) {
        this.roomConfigPanel = roomConfigPanel;
        this.rotationManager = rotationManager;
    }

    public void setFurnitureConfigPanel(FurnitureConfigPanel furnitureConfigPanel) {
        this.furnitureConfigPanel = furnitureConfigPanel;
    }
    public void mousePressed(int x, int y) {
        lastMouseX = x;
        lastMouseY = y;
        isDragging = true;
    }

    public void mouseReleased() {
        isDragging = false;
    }


    public void setGL3DPanel(GLJPanel gl3DPanel) {
        this.gl3DPanel = gl3DPanel;
    }

    public void add(String type, float scale) {
        RenderItem item = new RenderItem(type, scale);
        items.add(item);
        item.x = (items.size() % 3) * 5f - 5f;
        item.z = (items.size() / 3) * 5f - 5f;
    }

    public void clearItems() {
        items.clear();
        selectedItem = null;
        if (furnitureConfigPanel != null) {
            furnitureConfigPanel.setSelectedItem(null, null);
        }
    }

    public List<RenderItem> getItems() {
        return this.items;
    }

    public void zoomIn() {
        cameraY = Math.max(5f, cameraY - 1f);
        cameraZ = Math.max(5f, cameraZ - 1f);
    }

    public void zoomOut() {
        cameraY = Math.min(30f, cameraY + 1f);
        cameraZ = Math.min(30f, cameraZ + 1f);
    }

    public void selectItemAt(int mouseX, int mouseY) {
        if (items.isEmpty()) {
            selectedItem = null;
            if (furnitureConfigPanel != null) {
                furnitureConfigPanel.setSelectedItem(null, null);
            }
            return;
        }
        int currentIndex = selectedItem == null ? -1 : items.indexOf(selectedItem);
        int nextIndex = (currentIndex + 1) % items.size();
        selectedItem = items.get(nextIndex);
        if (furnitureConfigPanel != null) {
            furnitureConfigPanel.setSelectedItem(selectedItem, () -> {
                if (gl3DPanel != null) {
                    gl3DPanel.repaint();
                }
            });
        }
        if (gl3DPanel != null) {
            gl3DPanel.repaint();
        }
    }

    public void deselectItem() {
        selectedItem = null;
        if (furnitureConfigPanel != null) {
            furnitureConfigPanel.setSelectedItem(null, null);
        }
        if (gl3DPanel != null) {
            gl3DPanel.repaint();
        }
    }

    public RenderItem getSelectedItem() {
        return selectedItem;
    }

    public void setRotationAngles(float rotationX, float rotationY) {
        this.rotationAngleX = rotationX;
        this.rotationAngleY = rotationY;
        rotationManager.rotateRight();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        float[] lightPos = {10f, 10f, 10f, 1f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        float[] ambient = {0.3f, 0.3f, 0.3f, 1f};
        float[] diffuse = {0.7f, 0.7f, 0.7f, 1f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        try {
            File imgFile = new File("background.jpg");
            backgroundTexture = TextureIO.newTexture(imgFile, true);
        } catch (IOException e) {
            System.err.println("Error loading background texture: " + e.getMessage());
            gl.glClearColor(1f, 1f, 1f, 1f);
        }

        try {
            File wallImgFile = new File("wall_texture.jpg");
            wallTexture = TextureIO.newTexture(wallImgFile, true);
        } catch (IOException e) {
            System.err.println("Error loading wall texture: " + e.getMessage());
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (backgroundTexture != null) backgroundTexture.destroy(drawable.getGL());
        if (wallTexture != null) wallTexture.destroy(drawable.getGL());
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45f, (float) width / height, 1, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Draw background texture
        if (backgroundTexture != null) {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glOrtho(-1, 1, -1, 1, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glLoadIdentity();

            gl.glDisable(GL.GL_DEPTH_TEST);
            gl.glDisable(GL2.GL_LIGHTING);
            backgroundTexture.enable(gl);
            backgroundTexture.bind(gl);

            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0f, 0f);
            gl.glVertex2f(-1f, -1f);
            gl.glTexCoord2f(1f, 0f);
            gl.glVertex2f(1f, -1f);
            gl.glTexCoord2f(1f, 1f);
            gl.glVertex2f(1f, 1f);
            gl.glTexCoord2f(0f, 1f);
            gl.glVertex2f(-1f, 1f);
            gl.glEnd();

            backgroundTexture.disable(gl);
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glEnable(GL2.GL_LIGHTING);

            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glPopMatrix();
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPopMatrix();
        }

        gl.glLoadIdentity();

        glu.gluLookAt(
                0f, cameraY, cameraZ,
                0f, 0f, 0f,
                0f, 1f, 0f
        );

        gl.glRotatef(rotationAngleX, 1f, 0f, 0f);
        gl.glRotatef(rotationAngleY, 0f, 1f, 0f);

        RoomConfigPanel.RoomShape shape = roomConfigPanel.getRoomShape();
        float roomWidth = roomConfigPanel.getRoomWidth();
        float roomDepth = roomConfigPanel.getRoomDepth();
        drawRoomShape3D(gl, shape, roomWidth, roomDepth);

        gl.glPushMatrix();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(
                roomConfigPanel.getFloorColor().getRed() / 255f,
                roomConfigPanel.getFloorColor().getGreen() / 255f,
                roomConfigPanel.getFloorColor().getBlue() / 255f
        );
        drawFloor3D(gl, shape, roomWidth, roomDepth);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glDisable(GL2.GL_LIGHTING);
        drawRuler(gl, shape, roomWidth, roomDepth);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glPopMatrix();

        // Draw furniture items
        for (RenderItem ri : items) {
            gl.glPushMatrix();
            try {
                gl.glTranslatef(ri.x, 0f, ri.z);
                gl.glRotatef(ri.rotationY, 0f, 1f, 0f);
                gl.glScalef(ri.scale, ri.scale, ri.scale);

                Furniture f3 = FurnitureFactory.create(ri.type);
                f3.display(drawable);

                if (ri == selectedItem) {
                    gl.glPushMatrix();
                    gl.glDisable(GL2.GL_LIGHTING);
                    gl.glColor3f(1f, 1f, 1f);
                    gl.glLineWidth(2f);
                    float w = ri.width * ri.scale;
                    float h = ri.height * ri.scale;
                    float depth = ri.width * ri.scale; // Renamed 'd' to 'depth' to avoid conflict
                    gl.glBegin(GL.GL_LINE_LOOP);
                    gl.glVertex3f(-w / 2, 0f, -depth / 2);
                    gl.glVertex3f(w / 2, 0f, -depth / 2);
                    gl.glVertex3f(w / 2, h, -depth / 2);
                    gl.glVertex3f(-w / 2, h, -depth / 2);
                    gl.glEnd();
                    gl.glBegin(GL.GL_LINE_LOOP);
                    gl.glVertex3f(-w / 2, 0f, depth / 2);
                    gl.glVertex3f(w / 2, 0f, depth / 2);
                    gl.glVertex3f(w / 2, h, depth / 2);
                    gl.glVertex3f(-w / 2, h, depth / 2);
                    gl.glEnd();
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(-w / 2, 0f, -depth / 2);
                    gl.glVertex3f(-w / 2, 0f, depth / 2);
                    gl.glVertex3f(w / 2, 0f, -depth / 2);
                    gl.glVertex3f(w / 2, 0f, depth / 2);
                    gl.glVertex3f(-w / 2, h, -depth / 2);
                    gl.glVertex3f(-w / 2, h, depth / 2);
                    gl.glVertex3f(w / 2, h, -depth / 2);
                    gl.glVertex3f(w / 2, h, depth / 2);
                    gl.glEnd();

                    float[] modelview = new float[16];
                    float[] projection = new float[16];
                    int[] viewport = new int[4];
                    gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, modelview, 0);
                    gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projection, 0);
                    gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

                    float[] winPos = new float[3];
                    boolean status = glu.gluProject(0, h + 0.5f, 0, modelview, 0, projection, 0, viewport, 0, winPos, 0);
                    if (status) {
                        gl.glMatrixMode(GL2.GL_PROJECTION);
                        gl.glPushMatrix();
                        gl.glLoadIdentity();
                        gl.glOrtho(0, viewport[2], 0, viewport[3], -1, 1);
                        gl.glMatrixMode(GL2.GL_MODELVIEW);
                        gl.glPushMatrix();
                        gl.glLoadIdentity();
                        gl.glRasterPos2f(winPos[0] - 40, winPos[1]);
                        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12,
                                String.format("W: %.1f ft", ri.width * ri.scale));
                        gl.glMatrixMode(GL2.GL_PROJECTION);
                        gl.glPopMatrix();
                        gl.glMatrixMode(GL2.GL_MODELVIEW);
                        gl.glPopMatrix();
                    }

                    status = glu.gluProject(0, -0.5f, 0, modelview, 0, projection, 0, viewport, 0, winPos, 0);
                    if (status) {
                        gl.glMatrixMode(GL2.GL_PROJECTION);
                        gl.glPushMatrix();
                        gl.glLoadIdentity();
                        gl.glOrtho(0, viewport[2], 0, viewport[3], -1, 1);
                        gl.glMatrixMode(GL2.GL_MODELVIEW);
                        gl.glPushMatrix();
                        gl.glLoadIdentity();
                        gl.glRasterPos2f(winPos[0] - 40, winPos[1]);
                        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12,
                                String.format("H: %.1f ft", ri.height * ri.scale));
                        gl.glMatrixMode(GL2.GL_PROJECTION);
                        gl.glPopMatrix();
                        gl.glMatrixMode(GL2.GL_MODELVIEW);
                        gl.glPopMatrix();
                    }

                    gl.glLineWidth(1f);
                    gl.glEnable(GL2.GL_LIGHTING);
                    gl.glPopMatrix();
                }

            } catch (Exception e) {
                System.err.println("Error rendering 3D furniture item " + ri.type + ": " + e.getMessage());
            } finally {
                gl.glPopMatrix();
            }
        }
    }

    public void updateRoomDimensions(float dx, float dy) {
        float newWidth = roomConfigPanel.getRoomWidth() + dx;
        float newDepth = roomConfigPanel.getRoomDepth() + dy;
        newWidth = Math.max(2f, Math.min(50f, newWidth));
        newDepth = Math.max(2f, Math.min(50f, newDepth));
        roomConfigPanel.setRoomWidth(newWidth);
        roomConfigPanel.setRoomDepth(newDepth);
    }

    private void drawQuad(GL2 gl, float x1, float y1, float z1,
                          float x2, float y2, float z2,
                          float x3, float y3, float z3,
                          float x4, float y4, float z4) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(x1, y1, z1);
        gl.glVertex3f(x2, y2, z2);
        gl.glVertex3f(x3, y3, z3);
        gl.glVertex3f(x4, y4, z4);
        gl.glEnd();
    }

    private void drawFloor3D(GL2 gl, RoomConfigPanel.RoomShape shape, float roomWidth, float roomDepth) {
        gl.glBegin(GL2.GL_QUADS);
        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                gl.glVertex3f(-roomWidth / 2, 0f, roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
                break;

            case L_SHAPE:
                drawQuad(gl,
                        -roomWidth / 2, 0f, roomDepth / 2,
                        -roomWidth / 4, 0f, roomDepth / 2,
                        -roomWidth / 4, 0f, -roomDepth / 2,
                        -roomWidth / 2, 0f, -roomDepth / 2
                );
                drawQuad(gl,
                        -roomWidth / 4, 0f, roomDepth / 2,
                        roomWidth / 2, 0f, roomDepth / 2,
                        roomWidth / 2, 0f, roomDepth / 4,
                        -roomWidth / 4, 0f, roomDepth / 4
                );
                drawQuad(gl,
                        0f, 0f, roomDepth / 4,
                        roomWidth / 2, 0f, roomDepth / 4,
                        roomWidth / 2, 0f, -roomDepth / 2,
                        0f, 0f, -roomDepth / 2
                );
                drawQuad(gl,
                        -roomWidth / 4, 0f, roomDepth / 4,
                        0f, 0f, roomDepth / 4,
                        0f, 0f, -roomDepth / 2,
                        -roomWidth / 4, 0f, -roomDepth / 2
                );
                break;

            case U_SHAPE:
                float w = roomConfigPanel.getRoomWidth();
                float d = roomConfigPanel.getRoomDepth();
                float x1 = -w / 2f;
                float x2 = -w / 4f;
                float x3 = w / 4f;
                float x4 = w / 2f;
                float z1 = -d / 2f;
                float z2 = -d / 4f;
                float z3 = d / 2f;
                drawQuad(gl, x1, 0f, z3, x2, 0f, z3, x2, 0f, z1, x1, 0f, z1);
                drawQuad(gl, x3, 0f, z3, x4, 0f, z3, x4, 0f, z1, x3, 0f, z1);
                drawQuad(gl, x2, 0f, z2, x3, 0f, z2, x3, 0f, z1, x2, 0f, z1);
                break;

            case T_SHAPE:
                float tw = roomWidth;
                float td = roomDepth;
                float tx1 = -tw / 2f;
                float tx2 = -tw / 4f;
                float tx3 = tw / 4f;
                float tx4 = tw / 2f;
                float tz1 = -td / 2f;
                float tz2 = -td / 4f;
                float tz3 = td / 4f;
                float tz4 = td / 2f;
                drawQuad(gl, tx1, 0f, tz1, tx4, 0f, tz1, tx4, 0f, tz2, tx1, 0f, tz2);
                drawQuad(gl, tx2, 0f, tz2, tx3, 0f, tz2, tx3, 0f, tz4, tx2, 0f, tz4);
                break;

            case CIRCULAR:
                int segments = 128;
                float radius = Math.min(roomWidth, roomDepth) / 2f;
                gl.glEnd();
                gl.glBegin(GL2.GL_TRIANGLE_FAN);
                gl.glVertex3f(0f, 0f, 0f);
                for (int i = 0; i <= segments; i++) {
                    double angle = 2.0 * Math.PI * i / segments;
                    float x = radius * (float) Math.cos(angle);
                    float z = radius * (float) Math.sin(angle);
                    gl.glVertex3f(x, 0f, z);
                }
                gl.glEnd();
                break;
        }
        gl.glEnd();
    }

    public void mouseDragged(int x, int y) {
        if (isDragging && selectedItem != null) {
            float dx = DragHelper.computeDX(lastMouseX, x, zoom);
            float dz = -DragHelper.computeDZ(lastMouseY, y, zoom); // ✅ INVERT for 3D


            selectedItem.x += dx;
            selectedItem.z += dz;

            // Clamp within room boundaries
            float halfW = roomConfigPanel.getRoomWidth() / 2f;
            float halfD = roomConfigPanel.getRoomDepth() / 2f;
            float hw = selectedItem.width * selectedItem.scale / 2f;
            float hh = selectedItem.height * selectedItem.scale / 2f;

            selectedItem.x = Math.max(-halfW + hw, Math.min(halfW - hw, selectedItem.x));
            selectedItem.z = Math.max(-halfD + hh, Math.min(halfD - hh, selectedItem.z));

            lastMouseX = x;
            lastMouseY = y;

            if (gl3DPanel != null) gl3DPanel.repaint();
        }
    }

    private void drawRoomShape3D(GL2 gl, RoomConfigPanel.RoomShape shape, float roomWidth, float roomDepth) {
        float wallAlpha = 0.8f;
        gl.glColor4f(1f, 1f, 1f, wallAlpha);

        if (wallTexture != null) {
            wallTexture.enable(gl);
            wallTexture.bind(gl);
        } else {
            gl.glColor4f(
                    roomConfigPanel.getWallColor().getRed() / 255f,
                    roomConfigPanel.getWallColor().getGreen() / 255f,
                    roomConfigPanel.getWallColor().getBlue() / 255f,
                    wallAlpha
            );
        }

        float wallHeight = 5f;
        float texScaleX = roomWidth / 5f;
        float texScaleY = wallHeight / 5f;
        float texScaleZ = roomDepth / 5f;

        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                drawRectangularWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case L_SHAPE:
                drawLShapeWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case U_SHAPE:
                drawUShapeWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case T_SHAPE:
                drawTShapeWalls(gl, texScaleX, texScaleY, texScaleZ, roomWidth, roomDepth);
                break;
            case CIRCULAR:
                drawCircularWalls(gl, texScaleY, roomWidth, roomDepth);
                break;
        }

        if (wallTexture != null) {
            wallTexture.disable(gl);
        }
    }

    private void drawRectangularWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5f;
        // Back wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glEnd();

        // Front wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleX, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Left wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glEnd();

        // Right wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glEnd();
    }

    private void drawLShapeWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5f;
        // Outer back wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glEnd();

        // Outer left wall (left segment)
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 2, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 2, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Inner left wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 4, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 4);
        gl.glTexCoord2f(texScaleZ / 4, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Front wall (top segment)
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Inner right wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 4);
        gl.glTexCoord2f(texScaleZ / 4, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 4, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, roomDepth / 4);
        gl.glEnd();

        // Inner back wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(0f, 0f, roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(0f, wallHeight, roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 4);
        gl.glEnd();

        // Inner front wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(0f, 0f, roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(0f, wallHeight, roomDepth / 4);
        gl.glEnd();
    }

    private void drawUShapeWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5f;
        // Left arm outer wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Left arm inner wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 2, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleZ / 2, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, -roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Right arm outer wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleZ, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Right arm inner wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 2, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleZ / 2, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, -roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Base wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 2, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 2, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, -roomDepth / 2);
        gl.glEnd();

        // Left base inner wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glEnd();

        // Right base inner wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, -roomDepth / 2);
        gl.glEnd();
    }

    private void drawTShapeWalls(GL2 gl, float texScaleX, float texScaleY, float texScaleZ, float roomWidth, float roomDepth) {
        float wallHeight = 5f;
        // Bottom wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 2);
        gl.glTexCoord2f(texScaleX, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 2);
        gl.glEnd();

        // Left bottom inner wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 2, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, -roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 2, wallHeight, -roomDepth / 4);
        gl.glEnd();

        // Right bottom inner wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, 0f);
        gl.glVertex3f(roomWidth / 2, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleX / 4, texScaleY);
        gl.glVertex3f(roomWidth / 2, wallHeight, -roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, -roomDepth / 4);
        gl.glEnd();

        // Left stem wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 2, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleZ / 2, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, -roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Right stem wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleZ / 2, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, -roomDepth / 4);
        gl.glTexCoord2f(texScaleZ / 2, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, -roomDepth / 4);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();

        // Top stem wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 2, 0f);
        gl.glVertex3f(roomWidth / 4, 0f, roomDepth / 2);
        gl.glTexCoord2f(texScaleX / 2, texScaleY);
        gl.glVertex3f(roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glTexCoord2f(0f, texScaleY);
        gl.glVertex3f(-roomWidth / 4, wallHeight, roomDepth / 2);
        gl.glEnd();
    }

    private void drawCircularWalls(GL2 gl, float texScaleY, float roomWidth, float roomDepth) {
        float wallHeight = 5f;
        float radius = Math.min(roomWidth, roomDepth) / 2f;
        int segments = 128;
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            float x = radius * (float) Math.cos(angle);
            float z = radius * (float) Math.sin(angle);
            gl.glTexCoord2f((float) i / segments * 2f, 0f);
            gl.glVertex3f(x, 0f, z);
            gl.glTexCoord2f((float) i / segments * 2f, texScaleY);
            gl.glVertex3f(x, wallHeight, z);
        }
        gl.glEnd();
    }

    private void drawRuler(GL2 gl, RoomConfigPanel.RoomShape shape, float roomWidth, float roomDepth) {
        gl.glColor3f(0f, 0f, 0f);
        gl.glLineWidth(2f);
        float tickSize = 0.2f;
        float metersPerTick = 1f;
        int numTicksWidth = (int) (roomWidth / metersPerTick);
        int numTicksDepth = (int) (roomDepth / metersPerTick);

        switch (shape) {
            case RECTANGLE:
            case SQUARE:
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, roomDepth / 2);
                gl.glEnd();

                for (int i = 0; i <= numTicksWidth; i++) {
                    float x = -roomWidth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(x, 0.1f, -roomDepth / 2);
                    gl.glVertex3f(x, 0.1f + tickSize, -roomDepth / 2);
                    gl.glEnd();
                }

                for (int i = 0; i <= numTicksDepth; i++) {
                    float z = -roomDepth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(-roomWidth / 2, 0.1f, z);
                    gl.glVertex3f(-roomWidth / 2, 0.1f + tickSize, z);
                    gl.glEnd();
                }
                break;

            case L_SHAPE:
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, roomDepth / 2);
                gl.glVertex3f(-roomWidth / 4, 0.1f, roomDepth / 2);
                gl.glVertex3f(-roomWidth / 4, 0.1f, roomDepth / 4);
                gl.glVertex3f(roomWidth / 2, 0.1f, roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, roomDepth / 4);
                gl.glVertex3f(0f, 0.1f, roomDepth / 4);
                gl.glVertex3f(0f, 0.1f, -roomDepth / 2);
                gl.glEnd();

                for (int i = 0; i <= numTicksWidth; i++) {
                    float x = -roomWidth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(x, 0.1f, -roomDepth / 2);
                    gl.glVertex3f(x, 0.1f + tickSize, -roomDepth / 2);
                    gl.glEnd();
                }

                for (int i = 0; i <= numTicksDepth; i++) {
                    float z = -roomDepth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(-roomWidth / 2, 0.1f, z);
                    gl.glVertex3f(-roomWidth / 2, 0.1f + tickSize, z);
                    gl.glEnd();
                }
                break;

            case U_SHAPE:
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, roomDepth / 2);
                gl.glVertex3f(-roomWidth / 4, 0.1f, -roomDepth / 4);
                gl.glVertex3f(-roomWidth / 4, 0.1f, roomDepth / 2);
                gl.glVertex3f(roomWidth / 4, 0.1f, -roomDepth / 4);
                gl.glVertex3f(roomWidth / 4, 0.1f, roomDepth / 2);
                gl.glEnd();

                for (int i = 0; i <= numTicksWidth; i++) {
                    float x = -roomWidth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(x, 0.1f, -roomDepth / 2);
                    gl.glVertex3f(x, 0.1f + tickSize, -roomDepth / 2);
                    gl.glEnd();
                }

                for (int i = 0; i <= numTicksDepth; i++) {
                    float z = -roomDepth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(-roomWidth / 2, 0.1f, z);
                    gl.glVertex3f(-roomWidth / 2, 0.1f + tickSize, z);
                    gl.glEnd();
                }
                break;

            case T_SHAPE:
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(roomWidth / 2, 0.1f, -roomDepth / 2);
                gl.glVertex3f(-roomWidth / 2, 0.1f, -roomDepth / 4);
                gl.glVertex3f(-roomWidth / 4, 0.1f, -roomDepth / 4);
                gl.glVertex3f(roomWidth / 4, 0.1f, -roomDepth / 4);
                gl.glVertex3f(roomWidth / 2, 0.1f, -roomDepth / 4);
                gl.glVertex3f(-roomWidth / 4, 0.1f, -roomDepth / 4);
                gl.glVertex3f(-roomWidth / 4, 0.1f, roomDepth / 2);
                gl.glVertex3f(roomWidth / 4, 0.1f, -roomDepth / 4);
                gl.glVertex3f(roomWidth / 4, 0.1f, roomDepth / 2);
                gl.glEnd();

                for (int i = 0; i <= numTicksWidth; i++) {
                    float x = -roomWidth / 2 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(x, 0.1f, -roomDepth / 2);
                    gl.glVertex3f(x, 0.1f + tickSize, -roomDepth / 2);
                    gl.glEnd();
                }

                for (int i = 0; i <= numTicksDepth; i++) {
                    float z = -roomDepth / 4 + i * metersPerTick;
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(-roomWidth / 4, 0.1f, z);
                    gl.glVertex3f(-roomWidth / 4, 0.1f + tickSize, z);
                    gl.glEnd();
                }
                break;

            case CIRCULAR:
                float radius = Math.min(roomWidth, roomDepth) / 2f;
                gl.glBegin(GL.GL_LINE_LOOP);
                for (int i = 0; i < 128; i++) {
                    double angle = 2.0 * Math.PI * i / 128;
                    float x = radius * (float) Math.cos(angle);
                    float z = radius * (float) Math.sin(angle);
                    gl.glVertex3f(x, 0.1f, z);
                }
                gl.glEnd();

                for (int i = 0; i <= numTicksWidth; i++) {
                    double angle = 2.0 * Math.PI * i / numTicksWidth;
                    float x1 = radius * (float) Math.cos(angle);
                    float z1 = radius * (float) Math.sin(angle);
                    float x2 = (radius + tickSize) * (float) Math.cos(angle);
                    float z2 = (radius + tickSize) * (float) Math.sin(angle);
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3f(x1, 0.1f, z1);
                    gl.glVertex3f(x2, 0.1f, z2);
                    gl.glEnd();
                }
                break;
        }
        gl.glLineWidth(1f);
    }

}