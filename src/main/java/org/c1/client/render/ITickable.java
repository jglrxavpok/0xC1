package org.c1.client.render;

public interface ITickable
{

    /**
     * Method called each tick before rendering. Used by animated textures to update themselves
     */
    void tick();
}
