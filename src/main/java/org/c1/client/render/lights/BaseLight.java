package org.c1.client.render.lights;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.maths.*;

public abstract class BaseLight
{

    public static class LightCamTrans
    {
        public Vector3    pos = Vector3.NULL.copy();
        public Quaternion rot = Quaternion.NULL.copy();
    };

    private Vector3       color;
    private float         intensity;
    private Shader        shader;
    private ShadowingInfo shadowInfo;
    private boolean       enabled;
    protected Transform   transform;

    public BaseLight()
    {
        this(Vector3.get(1, 1, 1), 0.8f);
    }

    public BaseLight(Vector3 color, float intensity)
    {
        super();
        this.color = color;
        this.enabled = true;
        this.intensity = intensity;
        transform = new Transform();
    }

    public LightCamTrans getLightCamTrans(Camera mainCam)
    {
        LightCamTrans trans = new LightCamTrans();
        trans.rot = mainCam.getTransform().getTransformedRotation();
        trans.pos = mainCam.getTransform().getTransformedPos();
        return trans;
    }

    public ShadowingInfo getShadowingInfo()
    {
        return shadowInfo;
    }

    public BaseLight setEnabled(boolean b)
    {
        this.enabled = b;
        return this;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public BaseLight setShadowingInfo(ShadowingInfo shadowInfo)
    {
        this.shadowInfo = shadowInfo;
        return this;
    }

    public Shader getShader()
    {
        return shader;
    }

    public BaseLight setShader(Shader shader)
    {
        this.shader = shader;
        return this;
    }

    public Vector3 getColor()
    {
        return color;
    }

    public void setColor(Vector3 color)
    {
        this.color = color;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }

    public Transform getTransform()
    {
        return transform;
    }

}
