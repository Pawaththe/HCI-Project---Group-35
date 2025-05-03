package furniture.management.system.model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public abstract class Furniture implements GLEventListener {
    public Furniture() {
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(16640);
        gl.glLoadIdentity();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }

        float h = (float)width / (float)height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        float fovy = 45.0F;
        float aspect = h;
        float zNear = 1.0F;
        float zFar = 20.0F;
        gl.glFrustum((double)(-aspect) * Math.tan(Math.toRadians((double)fovy) / 2.0), (double)aspect * Math.tan(Math.toRadians((double)fovy) / 2.0), -Math.tan(Math.toRadians((double)fovy) / 2.0), Math.tan(Math.toRadians((double)fovy) / 2.0), (double)zNear, (double)zFar);
        gl.glMatrixMode(5888);
        gl.glLoadIdentity();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(7424);
        gl.glEnable(2929);
        gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        gl.glClearDepth(1.0);
        gl.glEnable(2896);
        gl.glEnable(16384);
        gl.glEnable(2903);
    }

    public void dispose(GLAutoDrawable drawable) {
    }
}