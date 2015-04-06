package org.c1.client.render;

import org.c1.maths.*;

public class Sprite {

    private Texture text;
    private TextureRegion region;
    private Transform transform;
    private float angle;
    private float height;
    private float width;
    private VertexArray vertexArray;

    public Sprite(Texture text) {
        this(text, new StaticRegion(0, 0, 1, 1));
    }

    public Sprite(Texture text, TextureRegion region) {
        this.text = text;
        this.region = region;
        this.transform = new Transform();

        width = 1f;
        height = 1f;
        initVertices();
    }

    private void initVertices() {
        vertexArray = new VertexArray();
        float minU = region.minU();
        float minV = region.minV();
        float maxU = region.maxU();
        float maxV = region.maxV();
        vertexArray.addVertex(new Vec3f(0, 0, 0), new Vec2f(minU, minV), new Vec3f());
        vertexArray.addVertex(new Vec3f(1, 0, 0), new Vec2f(maxU, minV), new Vec3f());
        vertexArray.addVertex(new Vec3f(1, 1, 0), new Vec2f(maxU, maxV), new Vec3f());
        vertexArray.addVertex(new Vec3f(0, 1, 0), new Vec2f(minU, maxV), new Vec3f());

        vertexArray.addIndex(1);
        vertexArray.addIndex(0);
        vertexArray.addIndex(2);

        vertexArray.addIndex(2);
        vertexArray.addIndex(0);
        vertexArray.addIndex(3);
        vertexArray.upload();
    }

    public void render(float x, float y, RenderEngine engine) {
        setPos(x, y);
        render(engine);
    }

    public void setSize(float w, float h) {
        setWidth(w);
        setHeight(h);
        setCenter(w/2f,h/2f);
    }

    public void setHeight(float h) {
        this.height = h;
    }

    public void setWidth(float w) {
        this.width = w;
    }

    public void render(RenderEngine engine) {
        transform.scale(width, height, 1);
        Mat4f oldModelview = engine.getModelview();
        engine.setModelview(transform.getTransformationMatrix());
        text.bind(0);
        vertexArray.bind();
        vertexArray.render();
        engine.setModelview(oldModelview);
    }

    public void setAngle(float angle) {
        this.angle = angle;
        transform.rot(new Quaternion(Vec3f.Z, angle));
    }

    public float getAngle() {
        return angle;
    }

    public Vec2f getCenter() {
        return transform.rotCenter().xy();
    }

    public void setCenter(Vec2f center) {
        transform.rotCenter(center.x(), center.y(),0);
    }

    public void setCenter(float x, float y) {
        transform.rotCenter(x,y,0);
    }

    public void setPos(float x, float y) {
        transform.pos(x, y, 0);
    }

    public Vec2f getPos() {
        return transform.pos().xy();
    }
}
