package org.c1.client.gui.widgets;

import java.io.*;

import org.c1.client.*;
import org.c1.client.gui.*;
import org.c1.client.render.*;
import org.c1.client.render.texture.*;
import org.c1.resources.*;

public class GuiImage extends GuiWidget
{

    private TextureRegion region;

    public GuiImage(int id, int x, int y, ResourceLocation location) throws IOException
    {
        super(id, x, y, 0, 0);
        Texture texture = OpenGLHelper.loadTexture(C1Client.getClient().getAssetsLoader().getResource(location));
        this.region = new TextureRegion(texture);
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    public GuiImage(int id, int x, int y, int w, int h, ResourceLocation location) throws IOException
    {
        this(id, x, y, w, h, OpenGLHelper.loadTexture(C1Client.getClient().getAssetsLoader().getResource(location)));
    }

    public GuiImage(int id, int x, int y, int w, int h, Texture texture)
    {
        this(id, x, y, w, h, new TextureRegion(texture));
    }

    public GuiImage(int id, int x, int y, int w, int h, TextureRegion textureRegion)
    {
        super(id, x, y, w, h);
        this.region = textureRegion;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        engine.bindTexture(region.getTexture());
        Gui.drawTexturedRect(engine, getX(), getY(), getWidth(), getHeight(), region.getMinU(), region.getMinV(), region.getMaxU(), region.getMaxV());
    }

    public TextureRegion getRegion()
    {
        return region;
    }

}
