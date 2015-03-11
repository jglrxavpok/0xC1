package org.c1.level.lights;

import java.io.*;

import org.c1.client.render.*;
import org.c1.maths.*;

public class PointLight extends Light {

    private static final int COLOR_DEPTH = 256;
    private Vec3f atten;
    private float range;

    public PointLight(Vec3f color, float intensity, Vec3f atten) {
        super(color, intensity);
        this.atten = atten;

        float a = atten.z();
        float b = atten.y();
        float c = atten.x() - COLOR_DEPTH * getIntensity() * getColor().max();
        // ax²+bx+c = 0

        this.range = (float) (-b + Math.sqrt(b * b - 4 * a * c) / (2 * a));
        try {
            setShader(new LightShader("shaders/lights/base_lighting.vsh", "shaders/lights/pointLight.fsh"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRange(float newRange) {
        this.range = newRange;
    }

    public float getRange() {
        return range;
    }

    public Vec3f getAttenuation() {
        return atten;
    }

}
