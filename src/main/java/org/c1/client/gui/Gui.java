package org.c1.client.gui;

import java.util.*;

import com.google.common.collect.*;

import org.c1.*;

public abstract class Gui {

    private List<GuiComponent> components;
    protected C1Game game;

    public Gui(C1Game gameInstance) {
        this.game = gameInstance;
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
    }

    public void onKeyPressed(int keycode, char eventchar) {
        components.forEach(comp -> comp.onKeyPressed(keycode, eventchar));
    }

    public void onKeyReleased(int keycode, char eventchar) {
        components.forEach(comp -> comp.onKeyReleased(keycode, eventchar));
    }

    public void clearComponents() {
        components.clear();
    }
}
