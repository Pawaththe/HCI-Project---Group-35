package furniture.management.system.model;

import com.jogamp.opengl.GL2;
import java.awt.Color;
import furniture.management.system.view.RenderItem;

public class GLUtil {
    // Define color sets for different materials
    private static final float[][] WOOD_COLORS = {
            {0.6f, 0.345f, 0.0f}, // Brown
            {0.58f, 0.32f, 0.09f}, // Brown
            {0.77f, 0.6f, 0.31f},  // Tan
            {0.77f, 0.6f, 0.31f},  // Tan
            {0.98f, 0.58f, 0.23f}, // Orange
            {0.98f, 0.58f, 0.23f}  // Orange
    };

    private static final float[][] METAL_COLORS = {
            {0.42f, 0.42f, 0.42f}, // Dark grey
            {0.42f, 0.42f, 0.42f}, // Light grey
            {0.757f, 0.839f, 0.835f}, // Dark grey
            {0.757f, 0.839f, 0.835f}, // Dark grey
            {0.961f, 0.961f, 0.961f}, // Grey
            {0.961f, 0.961f, 0.961f}  // Grey
    };

    private static final float[][] PLASTIC_COLORS = {
            {0.769f, 0.059f, 0.675f}, // Blue
            {0.769f, 0.059f, 0.675f}, // Blue
            {0.949f, 0.804f, 0.929f}, // Magenta
            {0.949f, 0.804f, 0.929f}, // Magenta
            {0.941f, 0.893f, 0.933f}, // Cyan
            {0.941f, 0.893f, 0.933f}  // Cyan
    };

    // Define material properties for wood
    private static final float[] WOOD_AMBIENT = {0.15f, 0.07f, 0.02f, 1.0f};
    private static final float[] WOOD_DIFFUSE = {0.545f, 0.271f, 0.075f, 1.0f};
    private static final float[] WOOD_SPECULAR = {0.5f, 0.25f, 0.08f, 1.0f};
    private static final float WOOD_SHININESS = 20.0f;

    // Define material properties for metal
    private static final float[] METAL_AMBIENT = {0.2f, 0.2f, 0.2f, 1.0f};
    private static final float[] METAL_DIFFUSE = {0.757f, 0.839f, 0.835f, 1.0f};
    private static final float[] METAL_SPECULAR = {0.8f, 0.8f, 0.8f, 1.0f};
    private static final float METAL_SHININESS = 50.0f;

    // Define material properties for plastic
    private static final float[] PLASTIC_AMBIENT = {0.1f, 0.1f, 0.1f, 1.0f};
    private static final float[] PLASTIC_DIFFUSE = {0.769f, 0.059f, 0.675f, 1.0f};
    private static final float[] PLASTIC_SPECULAR = {0.5f, 0.5f, 0.5f, 1.0f};
    private static final float PLASTIC_SHININESS = 30.0f;

    // Default material and color set
    private static Material currentMaterial = Material.WOOD;
    private static float[][] currentColors = WOOD_COLORS;

    // Static variable for custom color
    public static Color customColor = null;

    // Method to set the current material and color set
    public static void setCurrentMaterial(Material material) {
        currentMaterial = material;
        switch (material) {
            case WOOD:
                currentColors = WOOD_COLORS;
                break;
            case METAL:
                currentColors = METAL_COLORS;
                break;
            case PLASTIC:
                currentColors = PLASTIC_COLORS;
                break;
        }
    }

    // Apply material properties
    private static void applyMaterial(GL2 gl) {
        float[] ambient, diffuse, specular;
        float shininess;
        switch (currentMaterial) {
            case METAL:
                ambient = METAL_AMBIENT;
                diffuse = METAL_DIFFUSE;
                specular = METAL_SPECULAR;
                shininess = METAL_SHININESS;
                break;
            case PLASTIC:
                ambient = PLASTIC_AMBIENT;
                diffuse = PLASTIC_DIFFUSE;
                specular = PLASTIC_SPECULAR;
                shininess = PLASTIC_SHININESS;
                break;
            case WOOD:
            default:
                ambient = WOOD_AMBIENT;
                diffuse = WOOD_DIFFUSE;
                specular = WOOD_SPECULAR;
                shininess = WOOD_SHININESS;
                break;
        }

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
    }

    public static void drawCube(GL2 gl, float width, float height, float depth) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        float halfDepth = depth / 2.0f;

        if (customColor != null) {
            // Use custom color when set
            float[] colorArray = {
                    customColor.getRed() / 255f,
                    customColor.getGreen() / 255f,
                    customColor.getBlue() / 255f,
                    1.0f
            };
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, colorArray, 0);
            float[] specular = {0.3f, 0.3f, 0.3f, 1.0f};
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
            gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 10.0f);

            for (int i = 0; i < 6; i++) {
                gl.glBegin(GL2.GL_QUADS);
                gl.glColor3fv(colorArray, 0); // Apply color per face, works with GL_COLOR_MATERIAL
                switch (i) {
                    case 0: // Front face
                        gl.glNormal3f(0, 0, 1);
                        gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                        break;
                    case 1: // Back face
                        gl.glNormal3f(0, 0, -1);
                        gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                        break;
                    case 2: // Left face
                        gl.glNormal3f(-1, 0, 0);
                        gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                        break;
                    case 3: // Right face
                        gl.glNormal3f(1, 0, 0);
                        gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                        break;
                    case 4: // Top face
                        gl.glNormal3f(0, 1, 0);
                        gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                        break;
                    case 5: // Bottom face
                        gl.glNormal3f(0, -1, 0);
                        gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                        break;
                }
                gl.glEnd();
            }
        } else {
            // Default behavior with material
            applyMaterial(gl);
            for (int i = 0; i < 6; i++) {
                gl.glBegin(GL2.GL_QUADS);
                gl.glColor3fv(currentColors[i], 0);
                switch (i) {
                    case 0: // Front face
                        gl.glNormal3f(0, 0, 1);
                        gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                        break;
                    case 1: // Back face
                        gl.glNormal3f(0, 0, -1);
                        gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                        break;
                    case 2: // Left face
                        gl.glNormal3f(-1, 0, 0);
                        gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                        break;
                    case 3: // Right face
                        gl.glNormal3f(1, 0, 0);
                        gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                        break;
                    case 4: // Top face
                        gl.glNormal3f(0, 1, 0);
                        gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                        break;
                    case 5: // Bottom face
                        gl.glNormal3f(0, -1, 0);
                        gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                        gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                        gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                        break;
                }
                gl.glEnd();
            }
        }
    }

    public static void drawCube(GL2 gl, float width, float height, float depth, Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        float halfDepth = depth / 2.0f;

        // Set custom color as material
        float[] colorArray = {color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f};
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, colorArray, 0);
        float[] specular = {0.3f, 0.3f, 0.3f, 1.0f};
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 10.0f);

        for (int i = 0; i < 6; i++) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3fv(colorArray, 0);
            switch (i) {
                case 0: // Front face
                    gl.glNormal3f(0, 0, 1);
                    gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                    break;
                case 1: // Back face
                    gl.glNormal3f(0, 0, -1);
                    gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                    break;
                case 2: // Left face
                    gl.glNormal3f(-1, 0, 0);
                    gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                    break;
                case 3: // Right face
                    gl.glNormal3f(1, 0, 0);
                    gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                    break;
                case 4: // Top face
                    gl.glNormal3f(0, 1, 0);
                    gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                    break;
                case 5: // Bottom face
                    gl.glNormal3f(0, -1, 0);
                    gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                    break;
            }
            gl.glEnd();
        }
    }

    public static void drawCube(GL2 gl, float width, float height, float depth, RenderItem item) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        float halfDepth = depth / 2.0f;

        // Set material properties
        if (item.material != null) {
            setCurrentMaterial(item.material);
            applyMaterial(gl);
        } else {
            // Default material for custom color
            float[] defaultAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
            float[] defaultSpecular = {0.5f, 0.5f, 0.5f, 1.0f};
            float defaultShininess = 20.0f;
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, defaultAmbient, 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, defaultSpecular, 0);
            gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, defaultShininess);
        }

        // Set color
        float[] colorArray = item.color != null ?
                new float[]{item.color.getRed() / 255f, item.color.getGreen() / 255f, item.color.getBlue() / 255f, 1.0f} :
                new float[]{1.0f, 1.0f, 1.0f, 1.0f}; // Default white
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, colorArray, 0);

        // Draw cube faces
        for (int i = 0; i < 6; i++) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3fv(colorArray, 0);
            switch (i) {
                case 0: // Front face
                    gl.glNormal3f(0, 0, 1);
                    gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                    break;
                case 1: // Back face
                    gl.glNormal3f(0, 0, -1);
                    gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                    break;
                case 2: // Left face
                    gl.glNormal3f(-1, 0, 0);
                    gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                    break;
                case 3: // Right face
                    gl.glNormal3f(1, 0, 0);
                    gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                    break;
                case 4: // Top face
                    gl.glNormal3f(0, 1, 0);
                    gl.glVertex3f(-halfWidth, halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, halfHeight, halfDepth);
                    break;
                case 5: // Bottom face
                    gl.glNormal3f(0, -1, 0);
                    gl.glVertex3f(-halfWidth, -halfHeight, halfDepth);
                    gl.glVertex3f(-halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, -halfDepth);
                    gl.glVertex3f(halfWidth, -halfHeight, halfDepth);
                    break;
            }
            gl.glEnd();
        }
    }

    public static void drawCustomFloor(GL2 gl, float[][] points) {
        gl.glBegin(GL2.GL_POLYGON);
        for (float[] point : points) {
            gl.glVertex3f(point[0], -2.5f, point[1]);
        }
        gl.glEnd();
    }

    public static void drawFloor(GL2 gl) {
        // Placeholder for floor rendering
    }

    public enum Material {
        WOOD,
        METAL,
        PLASTIC
    }
}