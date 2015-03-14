package org.c1.tests;

import org.c1.client.*;
import org.c1.maths.*;

public class TestModel extends Model {

    public TestModel() {
        super();
        float left = -1f;
        float right = 1f;
        float top = -1f;
        float bottom = 1f;
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(left, top, 0), new Vec2f(0, 0), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(right, top, 0), new Vec2f(1, 0), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(right, bottom, 0), new Vec2f(1, 1), new Vec3f(0, 0, -1));
        face.addVertex(new Vec3f(left, bottom, 0), new Vec2f(0, 1), new Vec3f(0, 0, -1));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        addFace(face, true);
    }
}
