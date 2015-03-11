package org.c1;

import org.c1.client.render.*;
import org.c1.level.*;

public class TestObject extends GameObject {

    private VertexArray vertexArray;
    private Texture texture;

    public TestObject(Texture texture, VertexArray vertexArray) {
        this.texture = texture;
        this.vertexArray = vertexArray;
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
        vertexArray.bind();
        vertexArray.render();
    }

}
