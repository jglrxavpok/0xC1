package org.c1.maths;

import java.nio.*;

import org.c1.utils.*;

public class Quaternion implements IBufferWritable {
    public static final Quaternion NULL = new Quaternion(0, 0, 0, 1);

    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion() {
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vec3f axis, float angle) {
        float sinHalfAngle = (float) Math.sin(angle / 2);
        float cosHalfAngle = (float) Math.cos(angle / 2);

        this.x = axis.x() * sinHalfAngle;
        this.y = axis.y() * sinHalfAngle;
        this.z = axis.z() * sinHalfAngle;
        this.w = cosHalfAngle;
    }

    // From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
    public Quaternion(Mat4f rot) {
        float trace = (float) (rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2));

        if (trace > 0) {
            float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
            w = 0.25f / s;
            x = (rot.get(1, 2) - rot.get(2, 1)) * s;
            y = (rot.get(2, 0) - rot.get(0, 2)) * s;
            z = (rot.get(0, 1) - rot.get(1, 0)) * s;
        } else {
            if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
                w = (rot.get(1, 2) - rot.get(2, 1)) / s;
                x = 0.25f * s;
                y = (rot.get(1, 0) + rot.get(0, 1)) / s;
                z = (rot.get(2, 0) + rot.get(0, 2)) / s;
            } else if (rot.get(1, 1) > rot.get(2, 2)) {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
                w = (rot.get(2, 0) - rot.get(0, 2)) / s;
                x = (rot.get(1, 0) + rot.get(0, 1)) / s;
                y = 0.25f * s;
                z = (rot.get(2, 1) + rot.get(1, 2)) / s;
            } else {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
                w = (rot.get(0, 1) - rot.get(1, 0)) / s;
                x = (rot.get(2, 0) + rot.get(0, 2)) / s;
                y = (rot.get(1, 2) + rot.get(2, 1)) / s;
                z = 0.25f * s;
            }
        }

        float length = length();
        x /= length;
        y /= length;
        z /= length;
        w /= length;
    }

    public Mat4f toRotationMatrix() {
        Vec3f forward = new Vec3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
        Vec3f up = new Vec3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
        Vec3f right = new Vec3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

        return new Mat4f().rotation(forward, up, right);
    }

    public Vec3f forward() {
        return new Vec3f(0, 0, 1).rotate(this);
    }

    public Vec3f back() {
        return new Vec3f(0, 0, -1).rotate(this);
    }

    public Vec3f up() {
        return new Vec3f(0, 1, 0).rotate(this);
    }

    public Vec3f down() {
        return new Vec3f(0, -1, 0).rotate(this);
    }

    public Vec3f right() {
        return new Vec3f(1, 0, 0).rotate(this);
    }

    public Vec3f left() {
        return new Vec3f(-1, 0, 0).rotate(this);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest) {
        Quaternion correctedDest = dest;

        if (shortest && this.dot(dest) < 0)
            correctedDest = new Quaternion(-dest.x(), -dest.y(), -dest.z(), -dest.w());

        return correctedDest.sub(this).mul(lerpFactor).add(this).normalize();
    }

    public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest) {
        final float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if (shortest && cos < 0) {
            cos = -cos;
            correctedDest = new Quaternion(-dest.x(), -dest.y(), -dest.z(), -dest.w());
        }

        if (Math.abs(cos) >= 1 - EPSILON)
            return nlerp(correctedDest, lerpFactor, false);

        float sin = (float) Math.sqrt(1.0f - cos * cos);
        float angle = (float) Math.atan2(sin, cos);
        float invSin = 1.0f / sin;

        float srcFactor = (float) (Math.sin((1.0f - lerpFactor) * angle) * invSin);
        float destFactor = (float) (Math.sin((lerpFactor) * angle) * invSin);

        return this.mul(srcFactor).add(correctedDest.mul(destFactor)).normalize();
    }

    public Quaternion sub(Quaternion r) {
        return new Quaternion(x - r.x(), y - r.y(), z - r.z(), w - r.w());
    }

    public Quaternion add(Quaternion r) {
        return new Quaternion(x + r.x(), y + r.y(), z + r.z(), w + r.w());
    }

    public float dot(Quaternion r) {
        return x * r.x() + y * r.y() + z * r.z() + w * r.w();
    }

    public Quaternion normalize() {
        float l = length();
        x /= l;
        y /= l;
        z /= l;
        w /= l;
        return this;
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion mul(float d) {
        return new Quaternion(x * d, y * d, z * d, w * d);
    }

    public Quaternion mul(Quaternion r) {
        float w_ = w * r.w() - x * r.x() - y * r.y() - z * r.z();
        float x_ = x * r.w() + w * r.x() + y * r.z() - z * r.y();
        float y_ = y * r.w() + w * r.y() + z * r.x() - x * r.z();
        float z_ = z * r.w() + w * r.z() + x * r.y() - y * r.x();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion mul(Vec3f r) {
        float w_ = -x * r.x() - y * r.y() - z * r.z();
        float x_ = w * r.x() + y * r.z() - z * r.y();
        float y_ = w * r.y() + z * r.x() - x * r.z();
        float z_ = w * r.z() + x * r.y() - y * r.x();
        return new Quaternion(x_, y_, z_, w_);
    }

    public float x() {
        return x;
    }

    public void x(float x) {
        this.x = x;
    }

    public float y() {
        return y;
    }

    public void y(float y) {
        this.y = y;
    }

    public float z() {
        return z;
    }

    public void z(float z) {
        this.z = z;
    }

    public float w() {
        return w;
    }

    public void w(float w) {
        this.w = w;
    }

    public boolean equals(Object o) {
        if (o instanceof Quaternion) {
            Quaternion other = (Quaternion) o;
            return other.x() == x() && other.y() == y() && other.z() == z() && other.w() == w();
        }
        return false;
    }

    public Quaternion set(Quaternion v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }

    public Quaternion copy() {
        return new Quaternion(x, y, z, w);
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec3f xyz() {
        return new Vec3f(x, y, z);
    }

    public Quaternion div(Quaternion other) {
        return new Quaternion(x / other.x, y / other.y, z / other.z, w / other.w);
    }

    public Quaternion div(float factor) {
        return new Quaternion(x / factor, y / factor, z / factor, w / factor);
    }

    @Override
    public void write(FloatBuffer buffer) {
        buffer.put(x);
        buffer.put(y);
        buffer.put(z);
        buffer.put(w);
    }

    @Override
    public int getSize() {
        return 4;
    }
}
