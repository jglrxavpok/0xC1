package org.c1.maths;

import java.nio.*;

import org.c1.utils.*;

public class Vec3f implements IBufferWritable {

    public static final Vec3f NULL = new Vec3f(0, 0, 0);
    public static final Vec3f X = new Vec3f(1, 0, 0);
    public static final Vec3f Y = new Vec3f(0, 1, 0);
    public static final Vec3f Z = new Vec3f(0, 0, 1);
    private float x;
    private float y;
    private float z;

    public Vec3f(Vec2f v, float z) {
        this(v.x(), v.y(), z);
    }

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(Vec3f vector) {
        this.set(vector);
    }

    public Vec3f(Vec2f v) {
        this(v, 0);
    }

    public Vec3f() {
        ;
    }

    public float x() {
        return x;
    }

    public Vec3f x(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public Vec3f y(float y) {
        this.y = y;
        return this;
    }

    public float z() {
        return z;
    }

    public Vec3f z(float z) {
        this.z = z;
        return this;
    }

    public Vec3f add(Vec3f other) {
        return add(other.x(), other.y(), other.z());
    }

    public Vec3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3f sub(Vec3f other) {
        return sub(other.x(), other.y(), other.z());
    }

    public Vec3f sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3f mul(Vec3f other) {
        return mul(other.x(), other.y(), other.z());
    }

    public Vec3f mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vec3f div(Vec3f other) {
        return div(other.x(), other.y(), other.z());
    }

    public Vec3f div(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public Vec3f add(float factor) {
        return add(factor, factor, factor);
    }

    public Vec3f sub(float factor) {
        return sub(factor, factor, factor);
    }

    public Vec3f mul(float factor) {
        return mul(factor, factor, factor);
    }

    public Vec3f div(float factor) {
        return div(factor, factor, factor);
    }

    public Vec3f norm() {
        float l = length();
        if (l == 0.0) {
            x = 0;
            y = 0;
            z = 0;
        } else {
            x /= l;
            y /= l;
            z /= l;
        }
        return this;
    }

    public float sqlength() {
        float dx = x * x;
        float dy = y * y;
        float dz = z * z;
        return dx + dy + dz;
    }

    public float length() {
        return (float) Math.sqrt(sqlength());
    }

    public float dot(Vec3f other) {
        return dot(other.x(), other.y(), other.z());
    }

    public float dot(float x, float y, float z) {
        float nx = this.x * x;
        float ny = this.y * y;
        float nz = this.z * z;
        return nx + ny + nz;
    }

    public Vec3f cross(Vec3f other) {
        return cross(other.x(), other.y(), other.z());
    }

    public Vec3f cross(float x, float y, float z) {
        float nx = this.y * z - this.z * y;
        float ny = this.x * z - this.z * x;
        float nz = this.x * y - this.y * x;
        this.x = nx;
        this.y = ny;
        this.z = nz;
        return this;
    }

    @Override
    public int hashCode() {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;
        result = MULTIPLIER * result + Float.floatToRawIntBits(x);
        result = MULTIPLIER * result + Float.floatToRawIntBits(y);
        result = MULTIPLIER * result + Float.floatToRawIntBits(z);
        return result;
    }

    public Vec3f neg() {
        return new Vec3f(-x, -y, -z);
    }

    public Vec3f copy() {
        return new Vec3f(x, y, z);
    }

    public boolean isNull() {
        return x == 0.0 && y == 0.0 && z == 0.0;
    }

    public Vec2f xy() {
        return new Vec2f(x, y);
    }

    public Vec2f yx() {
        return new Vec2f(y, x);
    }

    public Vec2f xz() {
        return new Vec2f(x, z);
    }

    public Vec2f zx() {
        return new Vec2f(z, x);
    }

    public Vec2f yz() {
        return new Vec2f(y, z);
    }

    public Vec2f zy() {
        return new Vec2f(z, y);
    }

    public Vec3f rotate(float angle, Vec3f axis) {
        float sinAngle = (float) Math.sin(-angle);
        float cosAngle = (float) Math.cos(-angle);
        axis = axis.copy();
        Vec3f cross = copy().cross(axis.mul(sinAngle));
        Vec3f mul = copy().mul(cosAngle);
        float dot = dot(axis.mul(1 - cosAngle));
        return cross.add(mul.add(axis.mul(dot)));
    }

    public Vec3f rotate(Quaternion rotation) {
        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.mul(copy()).mul(conjugate);

        x = w.x();
        y = w.y();
        z = w.z();
        return this;
    }

    @Override
    public void write(FloatBuffer buffer) {
        buffer.put(x);
        buffer.put(y);
        buffer.put(z);
    }

    @Override
    public int getSize() {
        return 3;
    }

    public float max() {
        return Math.max(x(), Math.max(y(), z()));
    }

    /**
     * Sets the vector data to other vector
     */
    public void set(Vec3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public String toString() {
        return "vec3(" + x() + "," + y() + "," + z() + ")";
    }
}
