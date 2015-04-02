package org.c1.client;

import java.util.concurrent.atomic.*;

import org.c1.level.*;
import org.c1.maths.*;
import org.c1.physics.*;

public class PlayerController extends GameObject {

    private Camera playerCam;
    private float yaw;
    private float pitch;

    public PlayerController(Mat4f projection) {
        super("player_controller");
        this.playerCam = new Camera(projection);
        //setSize(new Vec3f(1, 1, 1));
        this.boundingBox = new Sphere(new Vec3f(), 0.5f);
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
                            System.out.println("Collision with " + o + " < " + boundingBox + " & " + o.getBoundingBox());
                        }
                    }
                });
    }

    private boolean hasFreeSpace(Vec3f pos) {
        AtomicBoolean result = new AtomicBoolean(true);
        Vec3f oldPos = getPos();
        boundingBox.setPosition(pos);
        this.getLevel().getGameObjects().stream()
                .filter(o -> o != this)
                .forEach(o -> {
                    if (o.isCollidable()) {
                        if (this.boundingBox.collides(o.getBoundingBox())) {
                            result.set(false);
                        }
                    }
                });
        boundingBox.setPosition(oldPos);
        return result.get();
    }

    @Override
    public void render(double delta) {
        ;
    }

    //All movement methods returns whether or not they were successful ( collisions )

    public boolean walkForward(float speed, double deltaTime) {
        Vec3f newPos = this.playerCam.getRotation().forward();
        newPos.mul(speed);
        newPos.add(getPos());

        if (hasFreeSpace(newPos)) {
            setPos(newPos);
            playerCam.setPos(newPos);
        }
        return true;
    }

    public boolean walkBackwards(float speed, double deltaTime) {
        return walkForward(-speed, deltaTime);
    }

    public boolean walkRight(float speed, double deltaTime) {
        Vec3f newPos = this.playerCam.getRotation().right();
        newPos.mul(speed);
        newPos.add(getPos());
        if (hasFreeSpace(newPos)) {
            setPos(newPos);
            playerCam.setPos(newPos);
        }
        return true;
    }

    public boolean walkLeft(float speed, double deltaTime) {
        return walkRight(-speed, deltaTime);
    }

    @Override
    public boolean shouldDie() {
        return false;
    }

    public Camera getCamera() {
        return this.playerCam;
    }

}
