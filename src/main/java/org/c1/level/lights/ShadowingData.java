package org.c1.level.lights;

import org.c1.maths.*;

public class ShadowingData {

    private Mat4f projection;
    private ShadowMapSize shadowMapSize;
    private boolean flips;
    private float lightBleedReduc;
    private float varianceMin;

    public ShadowingData(Mat4f projection) {
        this.projection = projection;
        this.varianceMin = 0.0000002f;
        this.lightBleedReduc = 0.8f;
        this.shadowMapSize = ShadowMapSize._1024x1024;
    }

    public Mat4f getProjectionMatrix() {
        return projection;
    }

    public void setProjectionMatrix(Mat4f projection) {
        this.projection = projection;
    }

    public ShadowMapSize getShadowMapSize() {
        return shadowMapSize;
    }

    public void setShadowMapSize(ShadowMapSize size) {
        this.shadowMapSize = size;
    }

    public boolean flipsCullFace() {
        return flips;
    }

    public void setFlipCullFace(boolean flip) {
        this.flips = flip;
    }

    public float getLightBleedingReduction() {
        return lightBleedReduc;
    }

    public float getVarianceMin() {
        return varianceMin;
    }

    public void setLightBleedingReduction(float red) {
        this.lightBleedReduc = red;
    }

    public void setVarianceMin(float var) {
        this.varianceMin = var;
    }
}
