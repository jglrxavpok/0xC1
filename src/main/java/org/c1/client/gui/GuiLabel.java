package org.c1.client.gui;

import org.c1.client.*;

public class GuiLabel extends GuiComponent {

    private FontRenderer font;
    private String text;

    public GuiLabel(float x, float y, String text, FontRenderer font) {
        super(x, y);
        this.font = font;
        this.text = text;
    }

    @Override
    public void render(double deltaTime) {
        font.renderString(text, getPos().x(), getPos().y(), 0xFFFFFFFF);
    }

    @Override
    public void update(double deltaTime) {
        ;
    }

}