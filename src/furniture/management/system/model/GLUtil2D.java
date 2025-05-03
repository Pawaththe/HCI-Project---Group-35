package furniture.management.system.model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class GLUtil2D {
    public GLUtil2D() {
    }

    public static void drawRectangle(GL2 gl, float x, float y, float width, float height) {
        gl.glBegin(7);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + width, y);
        gl.glVertex2f(x + width, y + height);
        gl.glVertex2f(x, y + height);
        gl.glEnd();
    }

    public static void drawText(GL2 gl, String text, float x, float y) {
        float[] color = new float[]{0.0F, 0.0F, 0.0F};
        gl.glColor3fv(color, 0);
        gl.glRasterPos2f(x, y);
        GLUT glut = new GLUT();
        glut.glutBitmapString(8, text);
    }
}