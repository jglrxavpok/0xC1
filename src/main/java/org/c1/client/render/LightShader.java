package org.c1.client.render;

import java.io.*;

public class LightShader extends Shader {

    public LightShader(String classpathStart) throws IOException {
        super(classpathStart);
    }

    public void update(RenderEngine engine) {
        getUniform("shadowVarianceMin").setValuef(engine.getShadowVarianceMin());
        getUniform("shadowLightBleedingReduction").setValuef(engine.getShadowLightBleedingReduction());
    }

}
