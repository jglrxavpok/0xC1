package org.c1.level;

import org.c1.maths.*;
import org.c1.physics.*;

public abstract class GameObject {

    protected Transform transform;
    protected CollisionShape boundingBox;
    private boolean collidable;
    protected Level level;
    private String id;

    public GameObject(String id) {
        this.id = id;
        collidable = true;
        transform = new Transform();
        boundingBox = new AABB(new Vec3f());
    }

    public String getID() {
        return id;
    }

    public void setSize(Vec3f size) {
        if (!(boundingBox instanceof AABB)) {
            boundingBox = new AABB(size);
        }
        ((AABB) boundingBox).setSize(size);
    }

    public CollisionShape getBoundingBox() {
        return boundingBox;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public abstract void update(double delta);

    public abstract void render(double delta);

    public abstract boolean shouldDie();

    public void onDespawn(double delta) {
        ;
    }

    public Vec3f getPos() {
        return transform.pos();
    }

    public Quaternion getRotation() {
        return transform.rot();
    }

    public void setRotation(Quaternion q) {
        transform.rot(q);
    }

    public void setPos(Vec3f pos) {
        transform.pos(pos);
    }

    public Transform getTransform() {
        return transform;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }
}
