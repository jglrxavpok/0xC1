package org.c1.level;

import org.c1.maths.*;

public class Camera extends GameObject {

    private Mat4f projection;
    private	Vec3f cameraPos;

    public Camera(float fov, float aspectRatio, float near, float far) {
        this(new Mat4f().perspective(fov, aspectRatio, near, far));
    }

    public Camera(Mat4f projection) {
        this.projection = projection;
    }

    public Mat4f getProjection() {
        return projection;
    }

    public void setProjection(Mat4f projection) {
        this.projection = projection;
    }

    public Vec3f getCameraPos() {
		return cameraPos;
	}

	public void setCameraPos(Vec3f cameraPos) {
		this.cameraPos = cameraPos;
	}

	@Override
    public void update(double delta) {
        ;
    }

    @Override
    public void render(double delta) {
        ;
    }

    @Override
    public boolean shouldDie() {
        return false;
    }

    public Mat4f getViewProjection() {
        Mat4f cameraRotation = getTransform().rot().conjugate().toRotationMatrix();
        cameraPos = getTransform().transformedPos().mul(-1);
        Mat4f cameraTranslation = new Mat4f().translation(cameraPos.x(), cameraPos.y(), cameraPos.z());
        return projection.mul(cameraRotation.mul(cameraTranslation));
    }
}
