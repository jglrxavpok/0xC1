package org.c1.client.render.texture;

import java.awt.image.*;

import org.c1.client.render.*;
import org.c1.resources.*;

public class TextureMapSprite implements ITickable
{
    protected ResourceLocation location;
    protected TextureIcon      icon;
    protected BufferedImage    rawImage;

    protected boolean          useRawImage = false;

    @Override
    public void tick()
    {
        ;
    }
}
