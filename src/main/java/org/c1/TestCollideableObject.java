package org.c1;

import org.c1.client.Model;
import org.c1.client.render.Texture;
import org.c1.level.GameObject;
import org.c1.physics.AABB;

public class TestCollideableObject extends GameObject {

    private Model model;
    private Texture texture;
    
    public TestCollideableObject(Model model, Texture texture){
        this.model = model;
        this.texture = texture;
        this.hitbox = new AABB(this.getPos(), this.getPos().add(2));
        model.setTexture(texture);
        this.physicsEnabled = true;
    }
    
    @Override
    public void update(double delta) {
        hitbox.position = this.getPos();
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
