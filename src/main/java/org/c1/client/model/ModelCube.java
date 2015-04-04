package org.c1.client.model;

import org.c1.client.*;
import org.c1.maths.*;

public class ModelCube extends Model {

    public ModelCube(Vec3f pos, Vec3f size) {
        super();
        float left = -1f;
        float right = 1f;
        float top = -1f;
        float bottom = 1f;
        float back = -1f;
        float front = 1f;
        
        
        
        ModelFace frontFace = new ModelFace();
        frontFace.addVertex(new Vec3f(left, top, back), new Vec2f(0, 0), new Vec3f(0, 0, -1));
        frontFace.addVertex(new Vec3f(right, top, back), new Vec2f(1, 0), new Vec3f(0, 0, -1));
        frontFace.addVertex(new Vec3f(right, bottom, back), new Vec2f(1, 1), new Vec3f(0, 0, -1));
        frontFace.addVertex(new Vec3f(left, bottom, back), new Vec2f(0, 1), new Vec3f(0, 0, -1));

        frontFace.addIndex(1);
        frontFace.addIndex(0);
        frontFace.addIndex(2);

        frontFace.addIndex(2);
        frontFace.addIndex(0);
        frontFace.addIndex(3);
        addFace(frontFace, true);

        ModelFace backFace = new ModelFace();
        backFace.addVertex(new Vec3f(right, top, front), new Vec2f(0, 0), new Vec3f(0, 0, 1));
        backFace.addVertex(new Vec3f(left, top, front), new Vec2f(1, 0), new Vec3f(0, 0, 1));
        backFace.addVertex(new Vec3f(left, bottom, front), new Vec2f(1, 1), new Vec3f(0, 0, 1));
        backFace.addVertex(new Vec3f(right, bottom, front), new Vec2f(0, 1), new Vec3f(0, 0, 1));

        backFace.addIndex(1);
        backFace.addIndex(0);
        backFace.addIndex(2);

        backFace.addIndex(2);
        backFace.addIndex(0);
        backFace.addIndex(3);
        addFace(backFace, true);

        ModelFace rightFace = new ModelFace();
        rightFace.addVertex(new Vec3f(right, top, back), new Vec2f(0, 0), new Vec3f(1, 0, 0));
        rightFace.addVertex(new Vec3f(right, top, front), new Vec2f(1, 0), new Vec3f(1, 0, 0));
        rightFace.addVertex(new Vec3f(right, bottom, front), new Vec2f(1, 1), new Vec3f(1, 0, 0));
        rightFace.addVertex(new Vec3f(right, bottom, back), new Vec2f(0, 1), new Vec3f(1, 0, 0));

        rightFace.addIndex(1);
        rightFace.addIndex(0);
        rightFace.addIndex(2);

        rightFace.addIndex(2);
        rightFace.addIndex(0);
        rightFace.addIndex(3);
        addFace(rightFace, true);

        ModelFace leftFace = new ModelFace();
        leftFace.addVertex(new Vec3f(left, top, front), new Vec2f(0, 0), new Vec3f(-1, 0, 0));
        leftFace.addVertex(new Vec3f(left, top, back), new Vec2f(1, 0), new Vec3f(-1, 0, 0));
        leftFace.addVertex(new Vec3f(left, bottom, back), new Vec2f(1, 1), new Vec3f(-1, 0, 0));
        leftFace.addVertex(new Vec3f(left, bottom, front), new Vec2f(0, 1), new Vec3f(-1, 0, 0));

        leftFace.addIndex(1);
        leftFace.addIndex(0);
        leftFace.addIndex(2);

        leftFace.addIndex(2);
        leftFace.addIndex(0);
        leftFace.addIndex(3);
        addFace(leftFace, true);
        
        ModelFace topFace = new ModelFace();
        topFace.addVertex(new Vec3f(left, top, back), new Vec2f(0, 0), new Vec3f(-1, 0, 0));
        topFace.addVertex(new Vec3f(right, top, back), new Vec2f(1, 0), new Vec3f(-1, 0, 0));
        topFace.addVertex(new Vec3f(right, top, front), new Vec2f(1, 1), new Vec3f(-1, 0, 0));
        topFace.addVertex(new Vec3f(left, top, front), new Vec2f(0, 1), new Vec3f(-1, 0, 0));

        topFace.addIndex(1);
        topFace.addIndex(0);
        topFace.addIndex(2);

        topFace.addIndex(2);
        topFace.addIndex(0);
        topFace.addIndex(3);
        addFace(topFace, true);
        
        ModelFace bottomFace = new ModelFace();
        bottomFace.addVertex(new Vec3f(left, bottom, back), new Vec2f(0, 0), new Vec3f(-1, 0, 0));
        bottomFace.addVertex(new Vec3f(right, bottom, back), new Vec2f(1, 0), new Vec3f(-1, 0, 0));
        bottomFace.addVertex(new Vec3f(right, bottom, front), new Vec2f(1, 1), new Vec3f(-1, 0, 0));
        bottomFace.addVertex(new Vec3f(left, bottom, front), new Vec2f(0, 1), new Vec3f(-1, 0, 0));

        bottomFace.addIndex(1);
        bottomFace.addIndex(0);
        bottomFace.addIndex(2);

        bottomFace.addIndex(2);
        bottomFace.addIndex(0);
        bottomFace.addIndex(3);
        addFace(bottomFace, true);
    }

}
