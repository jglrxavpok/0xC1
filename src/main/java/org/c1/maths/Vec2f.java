package org.c1.maths;

public class Vec2f {
    public static final Vec2f NULL = new Vec2f(0, 0);
    public static final Vec2f X = new Vec2f(1, 0);
    public static final Vec2f Y = new Vec2f(0, 1);
    private float x;
    private float y;

    public Vec2f() {
        this(0, 0);
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return x;
    }

    public Vec2f x(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public Vec2f y(float y) {
        this.y = y;
        return this;
    }

    public Vec2f add(Vec2f other) {
        return add(other.x(), other.y());
    }

    public Vec2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2f sub(Vec2f other) {
        return sub(other.x(), other.y());
    }

    public Vec2f sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2f mul(Vec2f other) {
        return mul(other.x(), other.y());
    }

    public Vec2f mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec2f div(Vec2f other) {
        return div(other.x(), other.y());
    }

    public Vec2f div(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vec2f add(float factor) {
        return add(factor, factor);
    }

    public Vec2f sub(float factor) {
        return sub(factor, factor);
    }

    public Vec2f mul(float factor) {
        return mul(factor, factor);
    }

    public Vec2f div(float factor) {
        return div(factor, factor);
    }

    public Vec2f norm() {
        float l = length();
        if (l == 0.0) {
            x = 0;
            y = 0;
        } else {
            x /= l;
            y /= l;
        }
        return this;
    }

    public float sqlength() {
        float dx = x * x;
        float dy = y * y;
        return dx + dy;
    }

    public float length() {
        return (float) Math.sqrt(sqlength());
    }

    public float dot(Vec2f other) {
        return dot(other.x(), other.y());
    }

    public float dot(float x, float y) {
        float nx = this.x * x;
        float ny = this.y * y;
        return nx + ny;
    }

    public float cross(Vec2f other) {
        return cross(other.x(), other.y());
    }

    public float cross(float x, float y) {
        return this.x * y - this.y * x;
    }

    @Override
    public int hashCode() {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;
        result = MULTIPLIER * result + Float.floatToRawIntBits(x);
        result = MULTIPLIER * result + Float.floatToRawIntBits(y);
        return result;
    }

    public Vec2f neg() {
        return new Vec2f(-x, -y);
    }

    public Vec2f copy() {
        return new Vec2f(x, y);
    }

    public boolean isNull() {
        return x == 0.0 && y == 0.0;
    }

    public Vec3f toVec3() {
        return new Vec3f(x, y, 0);
    }

    public Vec2f rotate(float angle) {
        float l = length();
        float nx = (float) (Math.cos(angle) * l);
        float ny = (float) (-Math.sin(angle) * l);
        return new Vec2f(nx, ny);
    }

    public float dist(Vec2f o) {
        return new Vec2f(x, y).sub(o).length();
    }

    public Vec2f lerp(Vec2f o, float factor) {
        return new Vec2f(o.x() * factor + x * (1f - factor), o.y() * factor + y * (1f - factor));
    }
}
