package org.c1.client.gui.widgets;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import com.google.common.collect.*;

import org.c1.client.gui.*;
import org.c1.client.gui.layout.*;

public class GuiScrollPane extends GuiComponent {

    private float scroll;
    private List<GuiComponent> siblings;
    private GuiLayout layout;

    public GuiScrollPane(float w, float h) {
        this(0, 0, w, h);
    }

    public GuiScrollPane(float x, float y, float w, float h) {
        super(x, y, w, h,-1);
        this.layout = new AbsoluteLayout();
        siblings = Lists.newArrayList();
    }

    public float getScroll() {
        return scroll;
    }

    public void setScroll(float scroll) {
        float oldScroll = this.scroll;
        this.scroll = scroll;
        siblings.forEach(comp -> comp.setPos(comp.getPos().add(0, this.scroll - oldScroll)));
    }

    public boolean onScroll(int x, int y, int dir) {
        if (isMouseOn(x, y, getWidth(), getHeight())) {
            float scrollAmount = dir;
            setScroll(scroll - scrollAmount);
            return true;
        }
        return false;
    }

    public void addComponent(GuiComponent component) {
        layout.onAdded(component, this);
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
        glScissor((int) getPos().x(), (int) getPos().y(), (int) getWidth(), (int) getHeight());

        siblings.forEach(comp -> comp.render(deltaTime));
        glDisable(GL_SCISSOR_TEST);
    }

    @Override
    public void update(double deltaTime) {
        siblings.forEach(comp -> comp.update(deltaTime));
    }

    public void setLayout(GuiLayout layout) {
        this.layout = layout;
    }

    public void resetScrollToTop() {
        GuiComponent topComponent = siblings
                .stream()
                .sorted((a, b) -> -Float.compare(a.getPos().y(), b.getPos().y()))
                .findFirst().get();
        float minY = topComponent.getPos().y();
        float dy = (getHeight() - topComponent.getHeight()) - minY;
        setScroll(dy);
    }

}
