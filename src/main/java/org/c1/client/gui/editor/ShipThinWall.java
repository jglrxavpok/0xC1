package org.c1.client.gui.editor;

import java.io.IOException;

import org.c1.client.OpenGLUtils;
import org.c1.client.render.RenderEngine;
import org.c1.client.render.Sprite;
import org.c1.client.render.Texture;
import org.c1.maths.Vec2f;
import org.c1.utils.CardinalDirection;

public class ShipThinWall extends EditorDirectionableComponent {

    private Sprite sprite;
    private float x;
    private float y;
    
    public ShipThinWall(float x, float y) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.size = new Vec2f(0.5f, 1f);
        try {
            sprite = new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(32,0,32,32,256,256));
            sprite.setSize(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(double delta, RenderEngine engine) {
        sprite.setAngle(getDirection().angle());
        sprite.render(x*32f, y*32f, engine);
    }

    @Override
    public void update(double delta) {
        // TODO Auto-generated method stub
        
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
