package org.c1.client.render.lights;

import java.io.*;

import org.c1.client.render.*;
import org.c1.maths.*;
import org.c1.resources.*;

public class SpotLight extends PointLight
{

    private float cutoff;

    public SpotLight(float cutoff)
    {
        this(Vector3.get(1, 1, 1), 1f, Vector3.get(0, 0, 1), cutoff);
    }

    public SpotLight(Vector3 atten, float cutoff)
    {
        this(Vector3.get(1, 1, 1), 1f, atten, cutoff);
    }

    public SpotLight(Vector3 color, float intensity, Vector3 atten, float fov)
    {
        super(color, intensity, atten);
        this.cutoff = (float) Math.acos(fov / 2.0);

        try
        {
            setShader(new Shader(new ResourceLocation("shaders", "forward-spot.vsh"), new ResourceLocation("shaders/forward-spot.fsh")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        setShadowingInfo(new ShadowingInfo(new Matrix4().initPerspective(fov, 1.0f, 0.1f, this.getRange())).flipFaces(true));
    }

    public float getCutoff()
    {
        return cutoff;
    }

    public void setCutoff(float cutoff)
    {
        this.cutoff = cutoff;
    }

    public Vector3 getDirection()
    {
        return getTransform().getTransformedRotation().getForward();
    }
}
