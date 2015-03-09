package org.c1.level.lights;

import org.c1.maths.*;

public class ShadowingData {

    private Mat4f projection;
    private ShadowMapSize shadowMapSize;
    private boolean flips;

    public ShadowingData(Mat4f projection) {
        this.projection = projection;
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
        // TODO Implement ShadowingData.getLightBleedingReduction
        throw new RuntimeException("ShadowingData.getLightBleedingReduction is not implemented yet");
    }

    public float getVarianceMin() {
        // TODO Implement ShadowingData.getVarianceMin
        throw new RuntimeException("ShadowingData.getVarianceMin is not implemented yet");
    }
}
