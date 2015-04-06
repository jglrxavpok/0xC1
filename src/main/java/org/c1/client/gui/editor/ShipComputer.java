package org.c1.client.gui.editor;

import org.c1.client.OpenGLUtils;
import org.c1.client.render.RenderEngine;
import org.c1.client.render.Sprite;
import org.c1.client.render.Texture;

import java.io.IOException;

public class ShipComputer extends EditorDirectionableComponent {

    private Sprite sprite;

    public ShipComputer(int x, int y) {
        super(x, y);

        try {
            sprite = new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(64, 0, 32, 32, 256, 256));
            sprite.setSize(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(double delta, RenderEngine engine) {
        sprite.setAngle(getDirection().angle());
        sprite.render(getPos().x()*32f, getPos().y()*32f, engine);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }
}
