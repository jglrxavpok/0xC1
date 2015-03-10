package org.c1.client.render;

import java.io.*;

import org.c1.level.lights.*;

public class LightShader extends Shader {

    public LightShader(String classpathStart) throws IOException {
        super(classpathStart);
    }

    public LightShader(String vertex, String frag) throws IOException {
        super(vertex, frag);
    }

    public void update(RenderEngine engine) {
        getUniform("lightMatrix").setValueMat4(engine.getLightMatrix());
        getUniform("R_shadowMap").setValuei(RenderEngine.SHADOW_MAP_SLOT);
        getUniform("R_shadowVarianceMin").setValuef(engine.getShadowVarianceMin());
        getUniform("R_shadowLightBleedingReduction").setValuef(engine.getShadowLightBleedingReduction());

        Uniform pointLightUniform = getUniform("pointLight");
        if (pointLightUniform != null) {
            PointLight pointLight = (PointLight) engine.getLight();
            getUniform("pointLight.atten.constant").setValuef(pointLight.getAttenuation().x());
            getUniform("pointLight.atten.linear").setValuef(pointLight.getAttenuation().y());
            getUniform("pointLight.atten.exponent").setValuef(pointLight.getAttenuation().z());

            getUniform("pointLight.position").setValue3f(pointLight.getTransform().transformedPos());

            getUniform("pointLight.range").setValuef(pointLight.getRange());

            setupBaseLight(pointLight, "pointLight.base");
        }
    }

    private void setupBaseLight(Light light, String uniform) {
        getUniform(uniform + ".intensity").setValuef(light.getIntensity());
        getUniform(uniform + ".color").setValue3f(light.getColor());
    }

}
