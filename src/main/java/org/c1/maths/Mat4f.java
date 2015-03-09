package org.c1.maths;

import java.nio.*;

import org.c1.utils.*;

public class Mat4f implements IBufferWritable {

    private float[] data = new float[4 * 4];

    public Mat4f() {
        ;
    }

    public Mat4f(float[] data) {
        for (int i = 0; i < data.length; i++)
            this.data[i] = data[i];
    }

    public Mat4f mul(Mat4f other) {
        Mat4f res = new Mat4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float firstRow = get(i, 0) * other.get(0, j);
                float secondRow = get(i, 1) * other.get(1, j);
                float thirdRow = get(i, 2) * other.get(2, j);
                float fourthRow = get(i, 3) * other.get(3, j);
                res.set(i, j, firstRow + secondRow + thirdRow + fourthRow);
            }
        }
        return res;
    }

    public Mat4f copy() {
        return new Mat4f(data);
    }

    public Mat4f identity() {
        all(0);
        set(0, 0, 1);
        set(1, 1, 1);
        set(2, 2, 1);
        set(3, 3, 1);
        return this;
    }

    public Mat4f translation(float x, float y, float z) {
        identity();
        set(0, 3, x);
        set(1, 3, y);
        set(2, 3, z);
        return this;
    }

    public Mat4f scale(float x, float y, float z) {
        identity();
        set(0, 0, x);
        set(1, 1, y);
        set(2, 2, z);
        return this;
    }

    public Mat4f rotation(float x, float y, float z) {
        Mat4f rotx = new Mat4f().identity();
        Mat4f roty = new Mat4f().identity();
        Mat4f rotz = new Mat4f().identity();

        rotx.set(1, 1, (float) Math.cos(x));
        rotx.set(1, 2, (float) -Math.sin(x));
        rotx.set(2, 1, (float) Math.sin(x));
        rotx.set(2, 2, (float) Math.cos(x));

        roty.set(0, 0, (float) Math.cos(y));
        roty.set(0, 2, (float) -Math.sin(y));
        roty.set(2, 0, (float) Math.sin(y));
        roty.set(2, 2, (float) Math.cos(y));

        rotz.set(0, 0, (float) Math.cos(z));
        rotz.set(0, 1, (float) -Math.sin(z));
        rotz.set(1, 0, (float) Math.sin(z));
        rotz.set(1, 1, (float) Math.cos(z));

        return set(rotz.mul(roty.mul(rotx)));
    }

    public Mat4f perspective(float fov, float aspectRatio, float near, float far) {
        all(0);

        float tanHlfFov = (float) Math.tan(fov / 2);
        float zRange = far - near;
        set(0, 0, 1.0f / (tanHlfFov * aspectRatio));
        set(1, 1, 1.0f / tanHlfFov);
        set(2, 2, (-near - far) / zRange);
        set(2, 3, 2 * far * near / zRange);
        return this;
    }

    public Mat4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        all(0);

        float width = right - left;
        float height = top - bottom;
        float depth = far - near;
        set(0, 0, 2f / width);
        set(0, 3, -(right + left) / width);
        set(1, 1, 2f / height);
        set(1, 3, -(top + bottom) / height);
        set(2, 2, -2f / depth);
        set(2, 3, -(far + near) / depth);
        set(3, 3, 1f);
        return this;
    }

    public Vec3f transform(Vec2f r) {
        return transform(r.toVec3());
    }

    public Vec3f transform(Vec3f r) {
        float x = get(0, 0) * r.x() + get(0, 1) * r.y() + get(0, 2) * r.z() + get(0, 3);
        float y = get(1, 0) * r.x() + get(1, 1) * r.y() + get(1, 2) * r.z() + get(1, 3);
        float z = get(2, 0) * r.x() + get(2, 1) * r.y() + get(2, 2) * r.z() + get(2, 3);
        r.x(x);
        r.y(y);
        r.z(z);
        return r;
    }

    public Mat4f set(Mat4f other) {
        for (int i = 0; i < data.length; i++)
            data[i] = other.data[i];
        return this;
    }

    public Mat4f set(int x, int y, float val) {
        data[y + x * 4] = val;
        return this;
    }

    public float get(int x, int y) {
        return data[y + x * 4];
    }

    public Mat4f all(float val) {
        for (int i = 0; i < data.length; i++)
            data[i] = val;
        return this;
    }

    public Mat4f rotation(Vec3f forward, Vec3f up, Vec3f right) {
        all(0);
        Vec3f f = forward;
        Vec3f r = right;
        Vec3f u = up;

        set(0, 0, r.x());
        set(0, 1, r.y());
        set(0, 2, r.z());
        set(1, 0, u.x());
        set(1, 1, u.y());
        set(1, 2, u.z());
        set(2, 0, f.x());
        set(2, 1, f.y());
        set(2, 2, f.z());
        set(3, 3, 1);

        return this;
    }

    @Override
    public void write(FloatBuffer buffer) {
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 4; i++)
                buffer.put(get(i, j));
    }

    @Override
    public int getSize() {
        return 16;
    }
}
