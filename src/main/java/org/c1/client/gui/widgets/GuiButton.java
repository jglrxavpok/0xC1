package org.c1.client.gui.widgets;

import org.c1.client.*;
import org.c1.client.gui.*;

public class GuiButton extends GuiComponent {

    private FontRenderer font;
    private String text;
    private int textColor;
    private int hoveredTextColor;
    private int mouseX;
    private int mouseY;

    public GuiButton(String text, FontRenderer font) {
        this(0, 0, text, font);
    }

    public GuiButton(float x, float y, String text, FontRenderer font) {
        this(x, y, 200, 20, text, font);
    }

    public GuiButton(float x, float y, float w, float h, String text, FontRenderer font) {
        super(x, y, w, h);
        this.font = font;
        this.text = text;
        textColor = 0xFFFFFFFF;
        hoveredTextColor = 0xFFFF0000;
    }

    public int getHoveredTextColor() {
        return hoveredTextColor;
    }

    public void setHoveredTextColor(int color) {
        hoveredTextColor = color;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    @Override
    public void render(double deltaTime) {
        int color = textColor;
        boolean mouseOn = isMouseOn(mouseX, mouseY, getWidth(), getHeight());
        if (mouseOn) {
            color = hoveredTextColor;
        }
        renderCentered(font, text, getPos().x() + getWidth() / 2f, getPos().y() + getHeight() / 2f - font.getCharHeight('A') / 2f, color);
    }

    @Override
    public void update(double deltaTime) {
        // TODO Implement GuiButton.update
    }

    public boolean onMousePressed(int x, int y, int button) {
        mouseX = x;
        mouseY = y;
        return isMouseOn(x, y, getWidth(), getHeight());
    }

    public boolean onMouseReleased(int x, int y, int button) {
        mouseX = x;
        mouseY = y;
        return isMouseOn(x, y, getWidth(), getHeight());
    }

    public boolean onScroll(int x, int y, int direction) {
        mouseX = x;
        mouseY = y;
        return isMouseOn(x, y, getWidth(), getHeight());
    }

    public boolean onMouseMoved(int x, int y, float dx, float dy) {
        mouseX = x;
        mouseY = y;
        return isMouseOn(x, y, getWidth(), getHeight());
    }

}
