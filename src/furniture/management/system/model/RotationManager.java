package furniture.management.system.model;

public class RotationManager {
    private float rotationAngleY = 0.0f;

    public float getRotationAngleY() {
        return rotationAngleY;
    }

    public void rotateLeft() {
        rotationAngleY -= 10.0f;
    }

    public void rotateRight() {
        rotationAngleY += 10.0f;
    }
}
