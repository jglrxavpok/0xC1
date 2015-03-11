package org.c1.client;

import org.c1.level.*;
import org.c1.maths.*;

public class PlayerController extends GameObject {

    public Camera playerCam;

    public PlayerController(Mat4f projection) {
        this.playerCam = new Camera(projection);
    }

    public void mouseInput(float yaw, float pitch) {
        playerCam.getTransform().rotate(Vec3f.X, pitch);
        playerCam.getTransform().rotate(Vec3f.Y, yaw);
    }

    @Override
    public void update(double delta) {
        this.playerCam.getTransform().translate(getPos().x() - playerCam.getTransform().pos().x(), this.getTransform().pos().y() - playerCam.getTransform().pos().y(), this.getTransform().pos().z() - playerCam.getTransform().pos().z());
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
