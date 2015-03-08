package org.c1.client.render.lights;

import java.io.*;

import org.c1.client.render.*;
import org.c1.maths.*;
import org.c1.resources.*;

public class PointLight extends BaseLight
{
    private static final int COLOR_DEPTH = 256;
    private Vector3          attenuation;
    private float            range;

    public PointLight()
    {
        this(Vector3.NULL);
    }

    public PointLight(Vector3 attenuation)
    {
        this(Vector3.get(1, 1, 1), 1f, attenuation);
    }

    public PointLight(Vector3 color, float intensity, Vector3 attenuation)
    {
        super(color, intensity);

        double a = attenuation.getZ();
        double b = attenuation.getY();
        double c = attenuation.getX() - COLOR_DEPTH * getIntensity() * getColor().max();
        // ax²+bx+c = 0

        this.range = (float) (-b + Math.sqrt(b * b - 4 * a * c) / (2 * a));
        this.attenuation = attenuation;
        try
        {
            setShader(new Shader(new ResourceLocation("shaders/forward-point.vsh"), new ResourceLocation("shaders/forward-point.fsh")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public Vector3 getAttenuation()
    {
        return attenuation;
    }

    public void setAttenuation(Vector3 attenuation)
    {
        this.attenuation = attenuation;
    }

    public float getRange()
    {
        return range;
    }

    public void setRange(float range)
    {
        this.range = range;
    }

}
