package org.c1;

import org.c1.client.Model;
import org.c1.client.render.Texture;
import org.c1.level.GameObject;

public class TestCollideableObject extends GameObject {

    private Model model;
    private Texture texture;
    
    public TestCollideableObject(Model model, Texture texture){
        this.model = model;
        this.texture = texture;
        model.setTexture(texture);
    }
    
    @Override
    public void update(double delta) {
        
    }

    @Override
    public void render(double delta) {
        texture.bind();
        model.render();
    }

    @Override
    public boolean shouldDie() {
        // TODO Auto-generated method stub
        return false;
    }

}
