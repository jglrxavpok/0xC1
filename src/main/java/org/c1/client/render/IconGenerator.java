package org.c1.client.render;

import org.c1.client.render.texture.*;
import org.c1.resources.*;

public interface IconGenerator
{
    /**
     * Generates an icon from the given ResourceLocation
     */
    public TextureIcon generateIcon(ResourceLocation loc);

    /**
     * Generates an icon from the given String
     */
    public TextureIcon generateIcon(String loc);
}
