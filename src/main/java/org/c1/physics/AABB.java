package org.c1.physics;

import org.c1.maths.*;

public class AABB {

    private Vec3f position;
    private Vec3f size;

    //Used for down vector
    private Vec3f center;

    /**
     * Used for collision detection
     */
    private Vec3f halfSize;

    public AABB() {
        this(new Vec3f());
    }

    public AABB(Vec3f size) {
        this(new Vec3f(), size);
    }

    public AABB(Vec3f pos, Vec3f size) {
        this.position = pos;
        this.size = size;

        this.halfSize = size.copy().div(2);
        computerCenter();
    }

    public boolean collides(AABB other) {
        float x = position.x();
        float y = position.y();
        float z = position.z();
        float w = size.x();
        float h = size.y();
        float d = size.z();

        float otherX = other.position.x();
        float otherY = other.position.y();
        float otherZ = other.position.z();
        float otherW = other.size.x();
        float otherH = other.size.y();
        float otherD = other.size.z();

        if (x > otherX + otherW || y > otherY + otherH || z > otherZ + otherD || x + w < otherX || y + h < otherY || z + d < otherZ)
            return false;
        return true;
    }

    public boolean isPointInside(Vec3f point) {
        float x = position.x();
        float y = position.y();
        float z = position.z();
        float w = size.x();
        float h = size.y();
        float d = size.z();

        float pX = point.x();
        float pY = point.y();
        float pZ = point.z();

        if (pX < x || pY < y || pZ < z || pX > x + w || pY > y + h || pZ > z + d)
            return false;
        return true;
    }

    public void setPosition(Vec3f position) {
        this.position.set(position);
        computerCenter();
    }

    public Vec3f getPosition() {
        return position;
    }

    public Vec3f getCenter() {
        return center;
    }

    private void computerCenter() {
        this.center = position.copy().add(halfSize);
    }

    public String toString() {
        return "Position : " + position.toString() + " - Size : " + size.toString();
    }

    public Vec3f getSize() {
        return size;
    }

    public void setSize(Vec3f size) {
        this.size.set(size);
        halfSize = size.copy().div(2);
    }

    public void setCentered(Vec3f pos) {
        this.position.set(pos.copy().sub(halfSize));
    }
}
