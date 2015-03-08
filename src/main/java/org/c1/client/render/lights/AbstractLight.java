package org.c1.client.render.lights;

import org.c1.maths.*;

public abstract class AbstractLight
{

    private Vector3 color;
    private float   intensity;

    public AbstractLight(Vector3 color, float intensity)
    {
        this.color = color;
        this.intensity = intensity;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public Vector3 getColor()
    {
        return color;
    }

    public void setColor(Vector3 color)
    {
        this.color = color;
    }
}
