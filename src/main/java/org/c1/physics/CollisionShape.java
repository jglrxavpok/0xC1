package org.c1.physics;

import org.c1.maths.*;

public abstract class CollisionShape {

    private Vec3f position;

    public abstract boolean collidesSphere(Sphere o);

    public abstract boolean collidesAABB(AABB aabb);

    public abstract boolean isPointInside(Vec3f point);

    public boolean collides(CollisionShape shape) {
        if (shape instanceof Sphere) {
            return collidesSphere((Sphere) shape);
        } else if (shape instanceof AABB) {
            return collidesAABB((AABB) shape);
        }
        return false;
    }

    public Vec3f getPosition() {
        return position;
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }

    public abstract void setCentered(Vec3f pos);

}
