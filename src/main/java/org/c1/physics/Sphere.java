package org.c1.physics;

import org.c1.maths.*;

public class Sphere extends CollisionShape {

    private Vec3f position;
    private float radius;

    public Sphere() {
        this(new Vec3f(), 0.0f);
    }

    public Sphere(Vec3f pos) {
        this(pos, 0.0f);
    }

    public Sphere(Vec3f pos, float rad) {
        this.position = pos;
        this.radius = rad;
    }

    public boolean collidesSphere(Sphere o) {

        float xd = position.x() - o.position.x();
        float yd = position.y() - o.position.y();
        float zd = position.z() - o.position.z();

        float sumRadius = getRadius() + o.getRadius();
        float sqrRadius = sumRadius * sumRadius;

        float distSqr = (xd * xd) + (yd * yd) + (zd * zd);

        if (distSqr <= sqrRadius)
        {
            return true;
        }

        return false;
    }

    public boolean collidesAABB(AABB aabb) {
        return aabb.collidesSphere(this); // Yeah, I know that's not really good but the implementation would be the same, sorry ;(
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean isPointInside(Vec3f point) {
        float distance = point.copy().sub(position).length();
        return distance <= radius;
    }

    @Override
    public void setCentered(Vec3f pos) {
        setPosition(pos);
    }

    public String toString() {
        return "Sphere(Position: " + position + ", Radius: " + radius + ")";
    }

    /**
     * UNUSED -yet-
     */
	@Override
	public void setSize(Vec3f size) {
		
	}
}
