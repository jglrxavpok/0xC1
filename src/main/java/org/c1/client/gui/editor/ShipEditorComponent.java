package org.c1.client.gui.editor;

import org.c1.client.render.*;
import org.c1.maths.Vec2f;
import org.c1.utils.CardinalDirection;

public abstract class ShipEditorComponent {

    private Vec2f pos;
    
    protected Vec2f size;

    public ShipEditorComponent(float x, float y) {
        pos = new Vec2f(x,y);
        size = new Vec2f(1,1);
    }

    public abstract void render(double delta, RenderEngine engine);

    public abstract void update(double delta);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract CardinalDirection getDirection();

    public abstract void setDirection(CardinalDirection dir);

    public void rotate() {
        setDirection(getDirection().next());
    }

    public Vec2f getPos() {
        return pos;
    }

    public Vec2f getSize() {
        return size;
    }
}
