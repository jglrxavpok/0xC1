package org.c1.client.gui;

import java.util.*;

import com.google.common.collect.*;

import org.c1.*;
import org.c1.client.gui.layout.*;
import org.c1.utils.*;

public abstract class Gui extends GuiComponent implements MouseConstants {

    private List<GuiComponent> components;
    protected C1Game game;
    private AbsoluteLayout layout;

    public Gui(C1Game gameInstance) {
        super(0, 0, gameInstance.getDisplayWidth(), gameInstance.getDisplayHeight());
        this.game = gameInstance;
        layout = new AbsoluteLayout();
        components = Lists.newArrayList();
    }

    public abstract void init();

    public void render(double delta) {
        components.forEach(comp -> comp.render(delta));
    }

    public void update(double delta) {
        components.forEach(comp -> comp.update(delta));
    }

    public void addComponent(GuiComponent comp) {
        components.add(comp);
        layout.onAdded(comp, this);
    }

    public boolean onKeyPressed(int keycode, char eventchar) {
        for (GuiComponent comp : components) {
            if (comp.onKeyPressed(keycode, eventchar))
                return true;
        }
        return false;

    }

    public boolean onKeyReleased(int keycode, char eventchar) {
        for (GuiComponent comp : components) {
            if (comp.onKeyReleased(keycode, eventchar))
                return true;
        }
        return false;
    }

    public void clearComponents() {
        components.clear();
    }

    public boolean onMousePressed(int x, int y, int button) {
        for (GuiComponent comp : components) {
            if (comp.onMousePressed(x, y, button))
                return true;
        }
        return false;
    }

    public boolean onMouseReleased(int x, int y, int button) {
        for (GuiComponent comp : components) {
            if (comp.onMouseReleased(x, y, button))
                return true;
        }
        return false;
    }

    public boolean onScroll(int x, int y, int direction) {
        for (GuiComponent comp : components) {
            if (comp.onScroll(x, y, direction))
                return true;
        }
        return false;

    }

    public boolean onMouseMoved(int x, int y, int dx, int dy) {
        for (GuiComponent comp : components) {
            if (comp.onMouseMoved(x, y, dx, dy))
                return true;
        }
        return false;
    }

    public boolean locksMouse() {
        return true;
    }
}
