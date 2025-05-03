package furniture.management.system.model;

public class RotationManager {
    private float rotationAngleY = 0.0F;

    public RotationManager() {
    }

    public float getRotationAngleY() {
        return this.rotationAngleY;
    }

    public void rotateLeft() {
        this.rotationAngleY -= 10.0F;
    }

    public void rotateRight() {
        this.rotationAngleY += 10.0F;
    }
}