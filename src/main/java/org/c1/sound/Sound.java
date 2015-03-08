package org.c1.sound;

import java.net.*;

import org.c1.level.*;

public class Sound implements ILocatable
{

    private float     x;
    private float     y;
    private float     z;
    private float     volume;
    private float     pitch;
    private Level     world;
    private AudioInfo infos;

    public Sound(AudioInfo infos, float volume, Level w, float x, float y, float z)
    {
        this(infos, volume, 1, w, x, y, z);
    }

    public Sound(AudioInfo infos, float volume, ILocatable loc)
    {
        this(infos, volume, 1, loc.getLevel(), loc.getX(), loc.getY(), loc.getZ());
    }

    public Sound(AudioInfo infos, float volume, float pitch, ILocatable loc)
    {
        this(infos, volume, pitch, loc.getLevel(), loc.getX(), loc.getY(), loc.getZ());
    }

    public Sound(AudioInfo infos, float volume, float pitch, Level w, float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = w;
        this.volume = volume;
        this.pitch = pitch;
        this.infos = infos;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getVolume()
    {
        return volume;
    }

    public Level getLevel()
    {
        return world;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public URL getURL()
    {
        return infos.getURL();
    }

    public String getSourceName()
    {
        return infos.getID();
    }

    public String getFileIdentifier()
    {
        return infos.getFileIdentifier();
    }
}
