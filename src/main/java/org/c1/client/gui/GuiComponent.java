package org.c1.client.gui;

import org.c1.maths.*;

public abstract class GuiComponent {

    private Vec2f pos;

    public GuiComponent(float x, float y) {
        setPos(new Vec2f(x, y));
    }

    public Vec2f getPos() {
        return pos;
    }

    public void setPos(Vec2f pos) {
        this.pos = pos;
    }

    public abstract void render(double deltaTime);

    public abstract void update(double deltaTime);
}
