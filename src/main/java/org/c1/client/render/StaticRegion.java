package org.c1.client.render;

public class StaticRegion implements TextureRegion {

    private float minU;
    private float maxU;
    private float minV;
    private float maxV;

    public StaticRegion(float minU, float minV, float maxU, float maxV) {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    @Override
    public float minU() {
        return minU;
    }

    @Override
    public float maxU() {
        return maxU;
    }

    @Override
    public float minV() {
        return minV;
    }

    @Override
    public float maxV() {
        return maxV;
    }

}
