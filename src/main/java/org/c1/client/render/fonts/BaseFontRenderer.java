package org.c1.client.render.fonts;

import java.io.*;

import org.c1.client.*;
import org.c1.client.render.texture.*;
import org.c1.resources.*;

public class BaseFontRenderer extends FontRenderer
{

    public BaseFontRenderer() throws IOException
    {
        super(new TextureAtlas(OpenGLHelper.loadTexture(C1Client.getClient().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/font.png"))), 16, 16), null);
    }

    @Override
    public double getCharSpacing(char c, char next)
    {
        return -8;
    }

    @Override
    public float getCharWidth(char c)
    {
        return 16;
    }

    @Override
    public float getCharHeight(char c)
    {
        return 16;
    }
}
