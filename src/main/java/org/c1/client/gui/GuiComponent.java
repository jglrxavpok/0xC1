package org.c1.client.gui;

import org.c1.client.*;
import org.c1.maths.*;
import org.c1.physics.*;

public abstract class GuiComponent {

    private Vec2f pos;
    private AABB boundingBox;

    public GuiComponent(float x, float y) {
        boundingBox = new AABB();
        setPos(new Vec2f(x, y));
    }

    public Vec2f getPos() {
        return pos;
    }

    public void setPos(Vec2f pos) {
        this.pos = pos;
        boundingBox.setPosition(new Vec3f(pos));
    }

    public void renderCentered(FontRenderer font, String text, float x, float y, int color) {
        float w = font.getStringWidth(text);
        float textX = x - w / 2f;
        font.renderString(text, textX, y, color);
    }

    public boolean isMouseOn(int mx, int my, float w, float h) {
        return isMouseOn(mx, my, getPos().x(), getPos().y(), w, h);
    }

    public boolean isMouseOn(int mx, int my, float x, float y, float w, float h) {
        boundingBox.setPosition(new Vec3f(x, y, 0));
        boundingBox.setSize(new Vec3f(w, h, 0));
        return boundingBox.isPointInside(new Vec3f(mx, my, 0));
    }

    public abstract void render(double deltaTime);

    public abstract void update(double deltaTime);

    public void onKeyPressed(int keycode, char eventchar) {}

    public void onKeyReleased(int keycode, char eventchar) {}

    public void onMousePressed(int x, int y, int button) {}

    public void onMouseReleased(int x, int y, int button) {}

    public void onScroll(int x, int y, int direction) {}

    public void onMouseMoved(int x, int y, float dx, float dy) {}
}
