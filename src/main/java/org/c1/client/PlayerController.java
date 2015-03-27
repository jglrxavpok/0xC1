package org.c1.client;

import org.c1.level.*;
import org.c1.maths.*;

public class PlayerController extends GameObject {

    public Camera playerCam;
    private float yaw;
    private float pitch;

    public PlayerController(Mat4f projection) {
        super("player_controller");
        this.playerCam = new Camera(projection);
        this.boundingBox.setSize(new Vec3f(1, 1, 1));
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
        this.boundingBox.setCentered(this.getPos());
        this.getLevel().getGameObjects().stream()
                .filter(o -> o != this)
                .forEach(o -> {
                    if (o.isCollidable()) {
                        if (this.boundingBox.collides(o.getBoundingBox())) {
                            System.out.println("Collision with " + o + " < " + o.getBoundingBox());
                        }
                    }
                });
    }

    @Override
    public void render(double delta) {
        ;
    }

    //All movement methods returns whether or not they were successful ( collisions )

    public boolean walkForward(double deltaTime) {
        Vec3f translationForward = this.playerCam.getRotation().forward();
        float speed = (float) deltaTime * 5;
        translationForward.mul(speed);
        this.getTransform().translate(translationForward);
        this.playerCam.getTransform().translate(translationForward);
        return true;
    }

    public boolean walkBackwards(double deltaTime) {
        Vec3f translationForward = this.playerCam.getRotation().forward();
        float speed = (float) deltaTime * 5;
        translationForward.mul(speed);
        translationForward.mul(-1);
        this.getTransform().translate(translationForward);
        this.playerCam.getTransform().translate(translationForward);
        return true;
    }

    public boolean walkRight(double deltaTime) {
        Vec3f translationRight = this.playerCam.getRotation().right();
        float speed = (float) deltaTime * 5;
        translationRight.mul(speed);
        this.getTransform().translate(translationRight);
        this.playerCam.getTransform().translate(translationRight);
        return true;
    }

    public boolean walkLeft(double deltaTime) {
        Vec3f translationRight = this.playerCam.getRotation().right();
        float speed = (float) deltaTime * 5;
        translationRight.mul(speed);
        translationRight.mul(-1);
        this.getTransform().translate(translationRight);
        this.playerCam.getTransform().translate(translationRight);
        return true;
    }

    @Override
    public boolean shouldDie() {
        return false;
    }

    public Camera getCamera() {
        return this.playerCam;
    }

}
