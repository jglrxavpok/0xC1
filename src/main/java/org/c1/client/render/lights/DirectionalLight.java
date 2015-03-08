package org.c1.client.render.lights;

import java.io.*;

import org.c1.client.render.*;
import org.c1.maths.*;
import org.c1.resources.*;

public class DirectionalLight extends BaseLight
{

    private float halfArea;

    public DirectionalLight()
    {
        this(Vector3.get(1, 1, 1), 1f, 200);
    }

    public DirectionalLight(Vector3 color, float intensity)
    {
        this(color, intensity, 200);
    }

    public DirectionalLight(Vector3 color, float intensity, float shadowArea)
    {
        super(color, intensity);

        try
        {
            setShader(new Shader(new ResourceLocation("shaders", "forward-directional.vsh"), new ResourceLocation("shaders", "forward-directional.fsh")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        halfArea = shadowArea / 2.0f;
        setShadowingInfo(new ShadowingInfo(new Matrix4().initOrthographic(-halfArea, halfArea, -halfArea, halfArea, -halfArea, halfArea)).flipFaces(true));
    }

    public LightCamTrans getLightCamTrans(Camera mainCam)
    {
        LightCamTrans result = new LightCamTrans();
        result.pos = mainCam.getPos().add(mainCam.getForward().mul(halfArea));
        result.rot = getTransform().getTransformedRotation();

        double worldTexelSize = halfArea * 2.0 / (double) getShadowingInfo().getShadowMapSize().getSize();

        Vector3 lightCamPos = result.pos.rotate(result.rot.conjugate()).copy();

        lightCamPos.setX((float) (Math.floor(lightCamPos.getX() / worldTexelSize) * worldTexelSize));
        lightCamPos.setY((float) (Math.floor(lightCamPos.getY() / worldTexelSize) * worldTexelSize));

        result.pos = lightCamPos.rotate(result.rot);
        return result;
    }

    public Vector3 getDirection()
    {
        return getTransform().getTransformedRotation().getForward();
    }

}
