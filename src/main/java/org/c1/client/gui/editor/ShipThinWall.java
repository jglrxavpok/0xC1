package org.c1.client.gui.editor;

import java.io.IOException;

import org.c1.client.render.RenderEngine;
import org.c1.client.render.Sprite;
import org.c1.client.render.StaticRegion;
import org.c1.client.render.Texture;
import org.c1.maths.Vec2f;

public class ShipThinWall extends ShipEditorComponent {

    private Sprite sprite;
    private float x;
    private float y;
    
    public ShipThinWall(float x, float y) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.size = new Vec2f(0.5f, 1f);
        
        try {
            sprite = new Sprite(new Texture("textures/ship/editor_ship.png"), new StaticRegion(32f/256f, 1f - 32f / 256f, 64f / 256f, 1f));
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

}
