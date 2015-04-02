package org.c1.physics;

import org.c1.maths.*;

public class AABB extends CollisionShape {

    public static final int TOP_PLANE_INDEX = 0;
    public static final int BOTTOM_PLANE_INDEX = 1;
    public static final int EAST_PLANE_INDEX = 2;
    public static final int WEST_PLANE_INDEX = 3;
    public static final int NORTH_PLANE_INDEX = 4;
    public static final int SOUTH_PLANE_INDEX = 5;
    private Vec3f size;

    //Used for down vector
    private Vec3f center;

    /**
     * Used for collision detection
     */
    private Vec3f halfSize;
    private Plane[] planes;

    public AABB() {
        this(new Vec3f());
    }

    public AABB(Vec3f size) {
        this(new Vec3f(), size);
    }

    public AABB(Vec3f pos, Vec3f size) {
        planes = new Plane[6];
        planes[BOTTOM_PLANE_INDEX] = new Plane();
        planes[BOTTOM_PLANE_INDEX].setNormal(new Vec3f(0, -1, 0));

        planes[TOP_PLANE_INDEX] = new Plane();
        planes[TOP_PLANE_INDEX].setNormal(new Vec3f(0, 1, 0));

        planes[EAST_PLANE_INDEX] = new Plane();
        planes[EAST_PLANE_INDEX].setNormal(new Vec3f(1, 0, 0));

        planes[WEST_PLANE_INDEX] = new Plane();
        planes[WEST_PLANE_INDEX].setNormal(new Vec3f(-1, 0, 0));

        planes[NORTH_PLANE_INDEX] = new Plane();
        planes[NORTH_PLANE_INDEX].setNormal(new Vec3f(0, 0, -1));

        planes[SOUTH_PLANE_INDEX] = new Plane();
        planes[SOUTH_PLANE_INDEX].setNormal(new Vec3f(0, 0, 1));
        this.size = size;

        this.halfSize = size.copy().div(2);
        setPosition(pos);
        computeCenterAndPlanes();
    }

    public boolean collidesAABB(AABB other) {
        float x = getPosition().x();
        float y = getPosition().y();
        float z = getPosition().z();
        float w = size.x();
        float h = size.y();
        float d = size.z();

        float otherX = other.getPosition().x();
        float otherY = other.getPosition().y();
        float otherZ = other.getPosition().z();
        float otherW = other.size.x();
        float otherH = other.size.y();
        float otherD = other.size.z();

        if (x > otherX + otherW || y > otherY + otherH || z > otherZ + otherD || x + w < otherX || y + h < otherY || z + d < otherZ)
            return false;
        return true;
    }

    public boolean isPointInside(Vec3f point) {
        float x = getPosition().x();
        float y = getPosition().y();
        float z = getPosition().z();
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
        super.setPosition(position);
        computeCenterAndPlanes();
    }

    public Vec3f getCenter() {
        return center;
    }

    private void computeCenterAndPlanes() {
        this.center = getPosition().copy().add(halfSize);
        planes[TOP_PLANE_INDEX].setPoint(getCenter().copy().add(0, halfSize.y(), 0));
        planes[BOTTOM_PLANE_INDEX].setPoint(getCenter().copy().sub(0, halfSize.y(), 0));
        planes[WEST_PLANE_INDEX].setPoint(getCenter().copy().add(halfSize.x(), 0, 0));
        planes[EAST_PLANE_INDEX].setPoint(getCenter().copy().sub(halfSize.x(), 0, 0));
        planes[SOUTH_PLANE_INDEX].setPoint(getCenter().copy().add(0, 0, halfSize.z()));
        planes[NORTH_PLANE_INDEX].setPoint(getCenter().copy().sub(0, 0, halfSize.z()));
    }

    public Plane getPlane(int index) {
        return planes[index];
    }

    public float getDistance(Vec3f point) {
        return 0f; // TODO: Implement getDistance
    }

    public String toString() {
        return "AABB(Position: " + getPosition().toString() + ", Size: " + size.toString() + ")";
    }

    public Vec3f getSize() {
        return size;
    }

    public void setSize(Vec3f size) {
        this.size.set(size);
        halfSize = size.copy().div(2);
        computeCenterAndPlanes();
    }

    public void setCentered(Vec3f pos) {
        this.getPosition().set(pos.copy().sub(halfSize));
        computeCenterAndPlanes();
    }

    @Override
    public boolean collidesSphere(Sphere o) {
        if (isPointInside(o.getPosition())) {
            return true;
        } else {
            float d = getDistance(o.getPosition());
            return d < o.getRadius();
        }
    }
}
