package org.c1.client.gui.editor;

import org.c1.client.render.*;
import org.c1.maths.Vec2f;

public abstract class ShipEditorComponent {

    private Vec2f pos;

    public ShipEditorComponent(float x, float y) {
        pos = new Vec2f(x,y);
    }

    public abstract void render(double delta, RenderEngine engine);

    public abstract void update(double delta);

    public abstract int getWidth();

    public abstract int getHeight();

    public Vec2f getPos() {
        return pos;
    }
}
