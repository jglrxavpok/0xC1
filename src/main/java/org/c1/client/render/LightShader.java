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
        Light light = engine.getLight();

        getUniform("lightMatrix").setValueMat4(engine.getLightMatrix().mul(light.getTransform().getTransformationMatrix()));
        getUniform("R_shadowMap").setValuei(RenderEngine.SHADOW_MAP_SLOT);
        getUniform("R_shadowVarianceMin").setValuef(engine.getShadowVarianceMin());
        getUniform("R_shadowLightBleedingReduction").setValuef(engine.getShadowLightBleedingReduction());

        getUniform("Cam_eyePos").setValue3f(engine.getCurrentCamera().getTransform().pos());

        getUniform("specularPower").setValuef(8.0f);
        getUniform("specularIntensity").setValuef(1);

        Uniform pointLightUniform = getUniform("pointLight");
        if (pointLightUniform != null) {
            PointLight pointLight = (PointLight) engine.getLight();
            setupPointLight(pointLight, "pointLight");
        }

        Uniform spotLightUniform = getUniform("spotLight");
        if (spotLightUniform != null) {
            SpotLight spotLight = (SpotLight) engine.getLight();
            setupPointLight(spotLight, "spotLight.pointLight");
            getUniform("spotLight.direction").setValue3f(spotLight.getDirection());
            getUniform("spotLight.cutoff").setValuef(spotLight.getCutoff());
        }
    }

    private void setupPointLight(PointLight pointLight, String uniform) {
        setupBaseLight(pointLight, uniform + ".base");
        getUniform(uniform + ".atten.constant").setValuef(pointLight.getAttenuation().x());
        getUniform(uniform + ".atten.linear").setValuef(pointLight.getAttenuation().y());
        getUniform(uniform + ".atten.exponent").setValuef(pointLight.getAttenuation().z());

        getUniform(uniform + ".position").setValue3f(pointLight.getTransform().pos());

        getUniform(uniform + ".range").setValuef(pointLight.getRange());
    }

    private void setupBaseLight(Light light, String uniform) {
        getUniform(uniform + ".intensity").setValuef(light.getIntensity());
        getUniform(uniform + ".color").setValue3f(light.getColor());
    }

}
