package org.c1.level.lights;

import java.io.*;

import org.c1.client.render.*;
import org.c1.maths.*;

public class SpotLight extends PointLight {

    private float cutoff;

    public SpotLight(Vec3f color, float intensity, Vec3f atten, float fov) {
        super(color, intensity, atten);
        this.cutoff = (float) Math.acos(fov / 2.0);

        try {
            setShader(new LightShader("shaders/lights/base_lighting.vsh", "shaders/lights/spotLight.fsh"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setShadowingData(new ShadowingData(new Mat4f().perspective(fov, 1f, 0.001f, this.getRange())));
        getShadowingData().setFlipCullFace(true);
        getShadowingData().setShadowMapSize(ShadowMapSize._256x256);
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }

    public Vec3f getDirection() {
        return getRotation().forward();
    }

}
