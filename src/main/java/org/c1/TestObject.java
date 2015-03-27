package org.c1;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.level.*;

public class TestObject extends GameObject {

    private Model model;
    private Texture texture;

    public TestObject(Texture texture, Model model) {
        super("test_object");
        this.texture = texture;
        this.model = model;
        setCollidable(false);
        model.setTexture(texture);
    }

    @Override
    public void update(double delta) {
        ;
    }

    @Override
    public boolean shouldDie() {
        return false;
    }

    @Override
    public void render(double delta) {
        texture.bind();
        model.render();
    }

}
