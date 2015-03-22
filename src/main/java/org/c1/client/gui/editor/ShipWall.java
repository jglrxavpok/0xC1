package org.c1.client.gui.editor;

import java.io.*;

import org.c1.client.render.*;

public class ShipWall extends ShipEditorComponent {

    private float x;
    private float y;
    private Sprite sprite;

    public ShipWall(float x, float y) {
        this.x = x;
        this.y = y;
        try {
            sprite = new Sprite(new Texture("textures/ship/editor_ship.png"), new StaticRegion(0, 1f - 32f / 256f, 32f / 256f, 1f));
            sprite.setSize(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(double delta, RenderEngine engine) {
        sprite.render(x, y, engine);
    }

    @Override
    public void update(double delta) {
        ;
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
