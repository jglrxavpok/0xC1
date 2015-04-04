package org.c1.maths;

public class Plane {

    private Vec3f normal;
    private Vec3f point;

    public Plane() {
        normal = new Vec3f();
        point = new Vec3f();
    }

    public Plane(Vec3f point, Vec3f normal) {
        this.normal = normal;
        this.point = point;
    }

    public void setNormal(Vec3f normal) {
        this.normal.set(normal);
    }

    public void setPoint(Vec3f point) {
        this.point.set(point);
    }

    public float getDistance(Vec3f point) {
        if (point == null || normal == null || this.point == null)
            return 0f;
        float a = normal.x();
        float b = normal.y();
        float c = normal.z();
        float d = -a * this.point.x() - b * this.point.y() - c * this.point.z();
        float numerator = new Vec3f(a * point.x(), b * point.y(), c * point.z()).add(d).length();
        float denominator = (float) Math.sqrt(a * a + b * b + c * c);
        return numerator / denominator;
    }
}
