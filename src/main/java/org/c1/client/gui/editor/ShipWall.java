package org.c1.client.gui.editor;

import java.io.*;

import org.c1.client.OpenGLUtils;
import org.c1.client.render.*;
import org.c1.maths.Vec2f;
import org.c1.utils.CardinalDirection;

public class ShipWall extends ShipEditorComponent {

    private float x;
    private float y;
    private Sprite sprite;

    public ShipWall(float x, float y) {
        super(x,y);
        this.x = x;
        this.y = y;
        this.size = new Vec2f(1f, 1f);
        
        try {
            sprite = new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(0, 0, 32, 32, 256, 256));
            sprite.setSize(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(double delta, RenderEngine engine) {
        sprite.render(x*32f, y*32f, engine);
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

    @Override
    public CardinalDirection getDirection() {
        return CardinalDirection.NORTH;
    }

    @Override
    public void setDirection(CardinalDirection dir) {}

}
