package org.c1.client;

import org.c1.level.*;
import org.c1.maths.*;

public class PlayerController extends GameObject {

    public Camera playerCam;
    private float yaw;
    private float pitch;

    public PlayerController(Mat4f projection) {
        this.playerCam = new Camera(projection);
    }

    public void mouseInput(float yaw, float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
        Quaternion yawRot = new Quaternion(Vec3f.Y, this.yaw);
        Quaternion pitchRot = new Quaternion(Vec3f.X, this.pitch);
        playerCam.getTransform().rot(yawRot.mul(pitchRot));
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(double delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean shouldDie() {
        return false;
    }

    public Camera getCamera() {
        return this.playerCam;
    }

}
