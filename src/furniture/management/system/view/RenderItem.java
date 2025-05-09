package furniture.management.system.view;

import furniture.management.system.model.GLUtil;

import java.awt.Color;
import java.io.Serializable;

public class RenderItem implements Serializable {
    public final String type;
    public float scale;
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float rotationY = 0f;
    public float width;
    public float height;
    public float depth;
    public Color color;
    public boolean useWoodMaterial;
    public GLUtil.Material material;

    public RenderItem(String type, float scale) {
        this.type = type;
        this.scale = scale;
        this.color = new Color(153, 88, 0); // Default to brown wood
        this.useWoodMaterial = true;
        this.material = GLUtil.Material.WOOD;
        switch (type) {
            case "CoffeeTable1":
                this.width = 3.5f;
                this.height = 1.0f;
                this.depth = 2.0f;
                break;
            case "CoffeeTable2":
                this.width = 3.0f;
                this.height = 1.0f;
                this.depth = 2.0f;
                break;
            case "CoffeeTable3":
                this.width = 3.0f;
                this.height = 1.2f;
                this.depth = 2.0f;
                break;
            case "ComputerDesk":
                this.width = 3.0f;
                this.height = 3.0f;
                this.depth = 2.0f;
                break;
            case "DiningChair":
                this.width = 3.0f;
                this.height = 5.0f;
                this.depth = 2.0f;
                break;
            case "DiningTable":
                this.width = 6.0f;
                this.height = 3.5f;
                this.depth = 3.0f;
                break;
            case "PantryCupboard":
                this.width = 9.5f;
                this.height = 4.0f;
                this.depth = 2.0f;
                break;
            case "TvStand":
                this.width = 8.5f;
                this.height = 5.5f;
                this.depth = 2.0f;
                break;
            case "Wardrobe":
                this.width = 6.0f;
                this.height = 6.0f;
                this.depth = 2.0f;
                break;
            case "Chair":
            case "Cupboard":
            case "Bed":
            default:
                this.width = 2.0f;
                this.height = 2.0f;
                this.depth = 2.0f;
                break;
        }
    }
    public String getType() { return type; }
    public float getScale() { return scale; }
    public float getX() { return x; }
    public float getZ() { return z; }
    public float getRotationY() { return rotationY; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    // Setters
    public void setScale(float scale) { this.scale = scale; }
    public void setX(float x) { this.x = x; }
    public void setZ(float z) { this.z = z; }
    public void setRotationY(float rotationY) { this.rotationY = rotationY; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }

}