package org.c1.tests;

import org.c1.client.Model;
import org.c1.client.ModelFace;
import org.c1.maths.Vec2f;
import org.c1.maths.Vec3f;

public class TestCubicModel extends Model {
    
    public TestCubicModel(){
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
        
        ModelFace face_back = new ModelFace();
        face_back.addVertex(new Vec3f(left, top, -2f), new Vec2f(0, 0), new Vec3f(0, 0, -1));
        face_back.addVertex(new Vec3f(right, top, -2f), new Vec2f(1, 0), new Vec3f(0, 0, -1));
        face_back.addVertex(new Vec3f(right, bottom, -2f), new Vec2f(1, 1), new Vec3f(0, 0, -1));
        face_back.addVertex(new Vec3f(left, bottom, -2f), new Vec2f(0, 1), new Vec3f(0, 0, -1));

        face_back.addIndex(1);
        face_back.addIndex(0);
        face_back.addIndex(2);

        face_back.addIndex(2);
        face_back.addIndex(0);
        face_back.addIndex(3);
        addFace(face_back, true);
        
        ModelFace face_right = new ModelFace();
        face_right.addVertex(new Vec3f(right, top, 0), new Vec2f(0, 0), new Vec3f(0, 0, -1));
        face_right.addVertex(new Vec3f(right, top, -2f), new Vec2f(1, 0), new Vec3f(0, 0, -1));
        face_right.addVertex(new Vec3f(right, bottom, -2f), new Vec2f(1, 1), new Vec3f(0, 0, -1));
        face_right.addVertex(new Vec3f(right, bottom, 0), new Vec2f(0, 1), new Vec3f(0, 0, -1));

        face_right.addIndex(1);
        face_right.addIndex(0);
        face_right.addIndex(2);

        face_right.addIndex(2);
        face_right.addIndex(0);
        face_right.addIndex(3);
        addFace(face_right, true);
        
        ModelFace face_left = new ModelFace();
        face_left.addVertex(new Vec3f(left, top, 0), new Vec2f(0, 0), new Vec3f(0, 0, -1));
        face_left.addVertex(new Vec3f(left, top, -2f), new Vec2f(1, 0), new Vec3f(0, 0, -1));
        face_left.addVertex(new Vec3f(left, bottom, -2f), new Vec2f(1, 1), new Vec3f(0, 0, -1));
        face_left.addVertex(new Vec3f(left, bottom, 0), new Vec2f(0, 1), new Vec3f(0, 0, -1));

        face_left.addIndex(1);
        face_left.addIndex(0);
        face_left.addIndex(2);

        face_left.addIndex(2);
        face_left.addIndex(0);
        face_left.addIndex(3);
        addFace(face_left, true);
        
        
    }

}
