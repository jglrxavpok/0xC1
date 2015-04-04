package org.c1.client.model;

import org.c1.client.*;
import org.c1.maths.*;

public class ModelCube extends Model {

    public ModelCube(Vec3f pos, Vec3f size) {
        super();
        addBox(pos, size);
    }

}
