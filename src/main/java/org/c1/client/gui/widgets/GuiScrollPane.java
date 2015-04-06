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
    private GuiComponent focused;
    private GuiListener listener;

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
            setScroll(scroll - dir);
            return true;
        }
        return false;
    }

    public void addComponent(GuiComponent component) {
        layout.onAdded(component, this);
        component.setOwner(this);
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

    public boolean onKeyPressed(int keycode, char eventchar) {
        for(GuiComponent comp : siblings) {
            if(comp.onKeyPressed(keycode, eventchar))
                return true;
        }
        return false;

    }

    public boolean onKeyReleased(int keycode, char eventchar) {
        for(GuiComponent comp : siblings) {
            if(comp.onKeyReleased(keycode, eventchar))
                return true;
        }
        return false;
    }

    public boolean onMousePressed(int x, int y, int button) {
        for(GuiComponent comp : siblings) {
            if(comp.onMousePressed(x, y, button)) {
                focused = comp;
                return true;
            }
        }
        return false;
    }

    public boolean onMouseReleased(int x, int y, int button) {
        for(GuiComponent comp : siblings) {
            if(comp.onMouseReleased(x, y, button)) {
                if(comp == focused) {
                    onComponentClicked(comp);
                }
                return true;
            }
        }
        return false;
    }

    public void setGuiListener(GuiListener listener) {
        this.listener = listener;
    }

    public GuiListener getListener() {
        return listener;
    }

    private void onComponentClicked(GuiComponent comp) {
        if(listener != null) {
            listener.onComponentClicked(comp);
        }
    }

    public boolean onMouseMoved(int x, int y, float dx, float dy) {
        for(GuiComponent comp : siblings) {
            if(comp.onMouseMoved(x, y, dx, dy))
                return true;
        }
        return false;
    }

}
