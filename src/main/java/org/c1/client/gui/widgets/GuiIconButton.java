package org.c1.client.gui.widgets;

import org.c1.C1Game;
import org.c1.client.FontRenderer;
import org.c1.client.gui.widgets.GuiButton;
import org.c1.client.render.RenderEngine;
import org.c1.client.render.Sprite;

public class GuiIconButton extends GuiButton {

    private final Sprite icon;

    public GuiIconButton(Sprite icon, int id) {
        this(0,0,icon,id);
    }

    public GuiIconButton(float x, float y, Sprite icon, int id) {
        super(x, y, null, null, id);
        this.setWidth(icon.getWidth() + 8);
        this.setHeight(icon.getHeight() + 8);

        if(getWidth() < 32)
            setWidth(32f);
        if(getHeight() < 32)
            setHeight(32f);
        this.icon = icon;
        setDrawText(false);
    }

    @Override
    public void render(double deltaTime) {
        super.render(deltaTime);
        RenderEngine engine = C1Game.getInstance().getRenderEngine();
        float offsetX = (getWidth()-icon.getWidth())/2f;
        float offsetY = (getHeight()-icon.getHeight())/2f;
        icon.render(getPos().x() + offsetX, getPos().y() + offsetY, engine);
    }
}
