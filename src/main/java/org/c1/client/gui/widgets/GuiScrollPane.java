package org.c1.client.gui.widgets;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import com.google.common.collect.*;

import org.c1.client.gui.*;

public class GuiScrollPane extends GuiComponent {

    private float width;
    private float height;
    private float scroll;
    private List<GuiComponent> siblings;

    public GuiScrollPane(float x, float y, float w, float h) {
        super(x, y);
        siblings = Lists.newArrayList();
        this.width = w;
        this.height = h;
    }

    public float getScroll() {
        return scroll;
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    public void onScroll(int x, int y, int dir) {
        scroll += dir;
    }

    public void addComponent(GuiComponent component) {
        if (!siblings.contains(component)) {
            component.getPos().add(getPos());
        }
        siblings.add(component);
    }

    public void removeComponent(GuiComponent component) {
        siblings.remove(component);
    }

    @Override
    public void render(double deltaTime) {
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) getPos().x(), (int) getPos().y(), (int) width, (int) height);

        siblings.forEach(comp -> comp.render(deltaTime));
        glDisable(GL_SCISSOR_TEST);
    }

    @Override
    public void update(double deltaTime) {
        siblings.forEach(comp -> comp.update(deltaTime));
    }

}
