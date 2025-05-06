package furniture.management.system.view;

import java.io.Serializable;

public class RenderItem implements Serializable {
    public final String type;
    public float scale;
    public float x = 0f;
    public float z = 0f;

    public RenderItem(String type, float scale) {
        this.type = type;
        this.scale = scale;
    }
}