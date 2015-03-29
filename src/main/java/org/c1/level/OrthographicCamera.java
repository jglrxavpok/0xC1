package org.c1.level;

import org.c1.maths.*;

public class OrthographicCamera extends Camera {

    private float zoom;

    public OrthographicCamera(float left, float right, float bottom, float top) {
        super(new Mat4f().orthographic(left, right, bottom, top, -1f, 1f));
        zoom = 1f;
    }

    public OrthographicCamera(float width, float height) {
        this(0, width, 0, height);
    }

}
