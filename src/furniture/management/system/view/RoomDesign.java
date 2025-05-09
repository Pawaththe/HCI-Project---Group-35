package furniture.management.system.view;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomDesign implements Serializable {
    public final List<RenderItem> items;
    public final RoomConfigPanel.RoomShape shape;
    public final float width;
    public final float depth;
    public final Color wallColor;
    public final Color floorColor;

    public RoomDesign(List<RenderItem> items, RoomConfigPanel.RoomShape shape, float width, float depth, Color wallColor, Color floorColor) {
        this.items = new ArrayList<>(items);
        this.shape = shape;
        this.width = width;
        this.depth = depth;
        this.wallColor = wallColor;
        this.floorColor = floorColor;
    }

    public List<RenderItem> getItems() {
        return new ArrayList<>(items);
    }

    public RoomConfigPanel.RoomShape getShape() {
        return shape;
    }

    public float getWidth() {
        return width;
    }

    public float getDepth() {
        return depth;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public Color getFloorColor() {
        return floorColor;
    }
}