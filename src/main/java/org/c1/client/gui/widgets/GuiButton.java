package org.c1.client.gui.widgets;

import java.io.*;

import org.c1.*;
import org.c1.client.*;
import org.c1.client.gui.*;
import org.c1.client.render.*;

public class GuiButton extends GuiComponent {

    private FontRenderer font;
    private String text;
    private int textColor;
    private int hoveredTextColor;
    private int mouseX;
    private int mouseY;
    private boolean drawBackground;
    private TextureAtlas atlas;

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
        drawBackground = true;

        try {
            atlas = new TextureAtlas("textures/gui/button.png", 32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        boolean mouseOn = isMouseOn(mouseX, mouseY, getWidth(), getHeight());
        boolean pressed = false;
        if (drawBackground) {
            renderBackground(deltaTime, mouseOn, pressed);
        }

        int color = textColor;
        if (mouseOn) {
            color = hoveredTextColor;
        }
        renderCentered(font, text, getPos().x() + getWidth() / 2f, getPos().y() + getHeight() / 2f - font.getCharHeight('A') / 2f, color);
    }

    private void renderBackground(double deltaTime, boolean mouseOn, boolean pressed) {
        int rowOffset = 0;
        if (pressed)
            rowOffset = 2;
        else if (mouseOn)
            rowOffset = 4;
        RenderEngine engine = C1Game.getInstance().getRenderEngine(); // TODO: find a way to use it without getInstance
        Sprite topLeftSprite = atlas.getSprite(0, 0 + rowOffset);
        topLeftSprite.setPos(getPos().x(), getPos().y() + getHeight() - 32f);
        topLeftSprite.render(engine);

        Sprite bottomLeftSprite = atlas.getSprite(0, 1 + rowOffset);
        bottomLeftSprite.setPos(getPos().x(), getPos().y());
        bottomLeftSprite.render(engine);

        Sprite topRightSprite = atlas.getSprite(1, 0 + rowOffset);
        topRightSprite.setPos(getPos().x() + getWidth() - 32f, getPos().y() + getHeight() - 32f);
        topRightSprite.render(engine);

        Sprite bottomRightSprite = atlas.getSprite(1, 1 + rowOffset);
        bottomRightSprite.setPos(getPos().x() + getWidth() - 32f, getPos().y());
        bottomRightSprite.render(engine);

        Sprite topSprite = atlas.getSprite(3, 0 + rowOffset);
        Sprite bottomSprite = atlas.getSprite(4, 1 + rowOffset);
        int xFrequency = (int) Math.floor(getWidth() / 32f);
        for (int x = 1; x < xFrequency; x++) {
            topSprite.setPos(getPos().x() + x * 32f, getPos().y() + getHeight() - 32f);
            topSprite.render(engine);

            bottomSprite.setPos(getPos().x() + x * 32f, getPos().y());
            bottomSprite.render(engine);
        }

        Sprite leftSprite = atlas.getSprite(3, 1 + rowOffset);
        Sprite rightSprite = atlas.getSprite(4, 0 + rowOffset);
        int yFrequency = (int) Math.floor(getHeight() / 32f);
        for (int y = 1; y < yFrequency; y++) {
            leftSprite.setPos(getPos().x(), getPos().y() + y * 32f);
            leftSprite.render(engine);

            rightSprite.setPos(getPos().x() + getWidth() - 32f, getPos().y() + y * 32f);
            rightSprite.render(engine);
        }

        Sprite centerSprite = atlas.getSprite(2, 0 + rowOffset);
        for (int x = 1; x < xFrequency; x++) {
            for (int y = 1; y < yFrequency; y++) {
                centerSprite.setPos(getPos().x() + x * 32f, getPos().y() + y * 32f);
                centerSprite.render(engine);
            }
        }
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
