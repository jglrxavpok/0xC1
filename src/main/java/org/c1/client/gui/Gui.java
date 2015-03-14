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
        for (GuiComponent component : components) {
            component.render(delta);
        }
    }

    public void update(double delta) {
        for (GuiComponent component : components) {
            component.update(delta);
        }
    }

    public void addComponent(GuiComponent comp) {
        components.add(comp);
    }
}
