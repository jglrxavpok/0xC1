package org.c1.level.lights;

import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.maths.*;

public abstract class Light extends GameObject {

    private boolean active;
    private Vec3f color;
    private boolean shadowing;
    private float intensity;
    private Shader shader;
    private ShadowingData shadowingData;

    public Light(Vec3f color, float intensity) {
        super("light");
        this.color = color;
        active = true;
        shadowing = true;
        setCollidable(false);
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setColor(Vec3f color) {
        this.color = color;
    }

    public Vec3f getColor() {
        return color;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isShadowing() {
        return shadowing;
    }

    public void setShadowing(boolean shadowing) {
        this.shadowing = shadowing;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void setShadowingData(ShadowingData data) {
        this.shadowingData = data;
    }

    public ShadowingData getShadowingData() {
        return shadowingData;
    }

    @Override
    public void update(double delta) {
        ;
    }

    @Override
    public void render(double delta) {
        ;
    }

    @Override
    public boolean shouldDie() {
        return false;
    }

}
