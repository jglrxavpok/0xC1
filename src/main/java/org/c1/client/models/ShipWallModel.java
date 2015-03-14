package org.c1.client.models;

import java.io.*;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.maths.*;

public class ShipWallModel extends Model {

    public ShipWallModel(float x, float y, float z) {
        super();

        try {
            setTexture(new Texture("textures/ship/editor_ship.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addFace(northFace(x, y, z));
        addFace(eastFace(x, y, z));
        addFace(westFace(x, y, z));
        addFace(southFace(x, y, z));
    }

    private ModelFace northFace(float x, float y, float z) {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(x, y, z + 1f), new Vec2f(0, 1f), new Vec3f(0, 0, 1));
        face.addVertex(new Vec3f(x + 1f, y, z + 1f), new Vec2f(64f / 256f, 1f), new Vec3f(0, 0, 1));
        face.addVertex(new Vec3f(x + 1f, y + 1f, z + 1f), new Vec2f(64f / 256f, 1f - 64f / 256f), new Vec3f(0, 0, 1));
        face.addVertex(new Vec3f(x, y + 1f, z + 1f), new Vec2f(0, 1f - 64f / 256f), new Vec3f(0, 0, 1));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    private ModelFace eastFace(float x, float y, float z) {
        ModelFace face = new ModelFace();
        return face;
    }

    private ModelFace westFace(float x, float y, float z) {
        ModelFace face = new ModelFace();
        return face;
    }

    private ModelFace southFace(float x, float y, float z) {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(x + 1f, y, z), new Vec2f(0, 1f), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(x, y, z), new Vec2f(64f / 256f, 1f), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(x, y + 1f, z), new Vec2f(64f / 256f, 1f - 64f / 256f), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(x + 1f, y + 1f, z), new Vec2f(0, 1f - 64f / 256f), new Vec3f(0, 0, -1));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

}
