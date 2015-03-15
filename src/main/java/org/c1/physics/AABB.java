package org.c1.physics;

import org.c1.maths.Vec3f;
import org.lwjgl.opengl.GL11;

public class AABB {

    public Vec3f position;
    public Vec3f size;

    //Used for down vector
    public Vec3f center;

    //Used for collision detection
    public Vec3f halfSize;

    public AABB(Vec3f pos, Vec3f size) {
        this.position = pos;
        this.size = size;

        this.halfSize = size.div(2);
        this.center = position.sub(halfSize);
    }

    public static boolean collides(AABB a, AABB b) {
        if (Math.abs(a.center.x() - b.center.x()) < (a.halfSize.x() + b.halfSize.x())) {
            if (Math.abs(a.center.y() - b.center.y()) < (a.halfSize.y() + b.halfSize.y())) {
                if (Math.abs(a.center.z() - b.center.z()) < (a.halfSize.z() + b.halfSize.z())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isPointInside(AABB aabb, Vec3f point) {
        if (Math.abs(aabb.center.x() - point.x()) < aabb.halfSize.x()) {
            if (Math.abs(aabb.center.y() - point.y()) < aabb.halfSize.y()) {
                if (Math.abs(aabb.center.z() - point.z()) < aabb.halfSize.z()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setPosition(Vec3f position){
        this.position.set(position);
    }

    public String toString() {
        return new String("Position : " + position.toString() + " - Size : " + size.toString());
    }
}
