package org.c1.level;

import org.c1.maths.*;

public class OrthographicCamera extends Camera {

    private float zoom;
    private Mat4f baseMatrix;

    public OrthographicCamera(float left, float right, float bottom, float top) {
        super(new Mat4f().orthographic(left, right, bottom, top, -1f, 1f));
        this.baseMatrix = getProjection().copy();
        zoom = 1f;
    }

    public OrthographicCamera(float width, float height) {
        this(0, width, 0, height);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        setProjection(baseMatrix.copy().mul(new Mat4f().scale(zoom, zoom, zoom)));
    }
}
