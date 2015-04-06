package org.c1.client.gui.editor;

import java.io.IOException;

import org.c1.client.OpenGLUtils;
import org.c1.client.render.RenderEngine;
import org.c1.client.render.Sprite;
import org.c1.client.render.Texture;
import org.c1.maths.Vec2f;

public class ShipThinWall extends ShipEditorComponent {

    private Sprite sprite;
    private float x;
    private float y;
    
    private int rotation;
    
    public ShipThinWall(float x, float y) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.size = new Vec2f(0.5f, 1f);
        this.rotation = 0;
        
        try {
            sprite = new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(32,0,32,32,256,256));
            sprite.setSize(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(double delta, RenderEngine engine) {
        sprite.setAngle(rotation * 1.5707f);
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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void rotate() {
       if(this.rotation < 3){
           this.rotation++;
       } else {
           this.rotation = 0;
       }
    }

}
