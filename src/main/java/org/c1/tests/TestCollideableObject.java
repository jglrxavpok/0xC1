package org.c1.tests;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.maths.*;

public class TestCollideableObject extends GameObject {

    private Model model;
    private Texture texture;

    public TestCollideableObject(Model model, Texture texture) {
        super("test_collisions");
        this.model = model;
        this.texture = texture;
        setSize(new Vec3f(1, 1, 1));
        model.setTexture(texture);
    }

    @Override
    public void update(double delta) {
        boundingBox.setPosition(this.getPos());
    }

    @Override
    public void render(double delta) {
        texture.bind(0);
        model.render();
    }

    @Override
    public boolean shouldDie() {
        return false;
    }

}
