package furniture.management.system.model;

public class DragHelper {
    public static float computeDX(int lastX, int currentX, float zoom) {
        return (currentX - lastX) / (200f * zoom);
    }

    public static float computeDZ(int lastY, int currentY, float zoom) {
        return (currentY - lastY) / (200f * zoom);
    }
}