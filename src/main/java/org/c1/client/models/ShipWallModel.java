package org.c1.client.models;

import java.io.*;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.maths.*;

public class ShipWallModel extends Model {

    public ShipWallModel(int index) {
        super();

        try {
            setTexture(new Texture("textures/ship/editor_ship.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addFace(northFace(index));
        addFace(eastFace(index));
        addFace(westFace(index));
        addFace(southFace(index));

        addFace(topFace(index));
    }

    private ModelFace topFace(int index) {
        ModelFace face = new ModelFace();
        float minU = 64f / 256f;
        float maxU = 80f / 256f;
        face.addVertex(new Vec3f(0, 1, 0), new Vec2f(minU, 1f), new Vec3f(0, 1, 0));
        face.addVertex(new Vec3f(0 + 0.25f, 1, 0), new Vec2f(maxU, 1f), new Vec3f(0, 1, 0));
        face.addVertex(new Vec3f(0 + 0.25f, 1, 0.25f), new Vec2f(maxU, 1f - 16f / 256f), new Vec3f(0, 1, 0));
        face.addVertex(new Vec3f(0, 1, 0.25f), new Vec2f(minU, 1f - 16f / 256f), new Vec3f(0, 1, 0));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    private ModelFace northFace(int index) {
        ModelFace face = new ModelFace();
        float w = 16f / 256f;
        float minU = index * w;
        float maxU = (index + 1) * w;
        face.addVertex(new Vec3f(0, 0 + 1f, 0.25f), new Vec2f(minU, 1f), new Vec3f(0, 0, 1));
        face.addVertex(new Vec3f(0 + 0.25f, 0 + 1f, 0.25f), new Vec2f(maxU, 1f), new Vec3f(0, 0, 1));
        face.addVertex(new Vec3f(0 + 0.25f, 0, 0.25f), new Vec2f(maxU, 1f - 64f / 256f), new Vec3f(0, 0, 1));
        face.addVertex(new Vec3f(0, 0, 0.25f), new Vec2f(minU, 1f - 64f / 256f), new Vec3f(0, 0, 1));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    private ModelFace eastFace(int index) {
        ModelFace face = new ModelFace();
        float w = 16f / 256f;
        float minU = index * w;
        float maxU = (index + 1) * w;
        face.addVertex(new Vec3f(0.25f, 0f, 0f), new Vec2f(minU, 1f), new Vec3f(1, 0, 0));
        face.addVertex(new Vec3f(0.25f, 1f, 0f), new Vec2f(maxU, 1f), new Vec3f(1, 0, 0));
        face.addVertex(new Vec3f(0.25f, 1f, 0.25f), new Vec2f(maxU, 1f - 64f / 256f), new Vec3f(1, 0, 0));
        face.addVertex(new Vec3f(0.25f, 0f, 0.25f), new Vec2f(minU, 1f - 64f / 256f), new Vec3f(1, 0, 0));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    private ModelFace westFace(int index) {
        ModelFace face = new ModelFace();
        float w = 16f / 256f;
        float minU = index * w;
        float maxU = (index + 1) * w;
        face.addVertex(new Vec3f(0.0f, 0f, 0f), new Vec2f(minU, 1f), new Vec3f(-1, 0, 0));
        face.addVertex(new Vec3f(0.0f, 1f, 0f), new Vec2f(maxU, 1f), new Vec3f(-1, 0, 0));
        face.addVertex(new Vec3f(0.0f, 1f, 0.25f), new Vec2f(maxU, 1f - 64f / 256f), new Vec3f(-1, 0, 0));
        face.addVertex(new Vec3f(0.0f, 0f, 0.25f), new Vec2f(minU, 1f - 64f / 256f), new Vec3f(-1, 0, 0));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    private ModelFace southFace(int index) {
        ModelFace face = new ModelFace();
        float w = 16f / 256f;
        float minU = index * w;
        float maxU = (index + 1) * w;
        face.addVertex(new Vec3f(0, 0 + 1f, 0), new Vec2f(minU, 1f), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(0 + 0.25f, 0 + 1f, 0), new Vec2f(maxU, 1f), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(0 + 0.25f, 0, 0f), new Vec2f(maxU, 1f - 64f / 256f), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(0, 0, 0f), new Vec2f(minU, 1f - 64f / 256f), new Vec3f(0, 0, -1));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

}
