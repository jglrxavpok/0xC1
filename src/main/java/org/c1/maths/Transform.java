package org.c1.maths;

public class Transform {

    private Quaternion rotation;
    private Vec3f position;
    private Vec3f scale;

    public Transform() {
        rotation = new Quaternion();
        position = new Vec3f();
        scale = new Vec3f(1, 1, 1);
    }

    public Vec3f scale() {
        return scale;
    }

    public Transform scale(Vec3f v) {
        return scale(v.x(), v.y(), v.z());
    }

    public Transform scale(float x, float y, float z) {
        scale.x(x);
        scale.y(y);
        scale.z(z);
        return this;
    }

    public Vec3f pos() {
        return position;
    }

    public Transform pos(Vec3f v) {
        return pos(v.x(), v.y(), v.z());
    }

    public Transform pos(float x, float y, float z) {
        position.x(x);
        position.y(y);
        position.z(z);
        return this;
    }

    public Quaternion rot() {
        return rotation;
    }

    public Transform rot(Quaternion q) {
        return rot(q.x(), q.y(), q.z(), q.w());
    }

    public Transform rot(float x, float y, float z, float w) {
        rotation.x(x);
        rotation.y(y);
        rotation.z(z);
        rotation.w(w);
        return this;
    }

    public Transform rotate(Vec3f axis, float angle) {
        rotation = new Quaternion(axis, angle).mul(rotation).normalize();
        return this;
    }

    public Mat4f getTransformationMatrix() {
        Mat4f translationMatrix = new Mat4f().translation(position.x(), position.y(), position.z());
        Mat4f rotationMatrix = rotation.toRotationMatrix();
        Mat4f scaling = new Mat4f().scale(scale.x(), scale.y(), scale.z());
        return translationMatrix.mul(rotationMatrix.mul(scaling));
    }

    public Vec3f translate(float x, float y, float z) {
        position.x(position.x() + x);
        position.y(position.y() + y);
        position.z(position.z() + z);

        return this.position;
    }

    public Vec3f transform(Vec3f v) {
        return getTransformationMatrix().transform(v);
    }

    public void set(Transform transform) {
        position.set(transform.position);
        rotation.set(transform.rotation);
    }

    public Vec3f translate(Vec3f v) {
        return translate(v.x(), v.y(), v.z());
    }
}
