package org.c1.level;


public class Particle implements ILocatable
{

    private String name;
    private float  x;
    private float  y;
    private float  z;
    private float  vx;
    private float  vy;
    private float  vz;
    private long   life;
    private Level  world;

    public Particle(String name, ILocatable loc, long life)
    {
        this(name, loc.getLevel(), loc.getX(), loc.getY(), loc.getZ(), life);
    }

    public Particle(String name, ILocatable loc, float vx, float vy, float vz, long life)
    {
        this(name, loc.getLevel(), loc.getX(), loc.getY(), loc.getZ(), vx, vy, vz, life);
    }

    public Particle(String name, Level w, float x, float y, float z, long life)
    {
        this(name, w, x, y, z, 0, 0, 0, life);
    }

    public Particle(String name, Level w, float x, float y, float z, float vx, float vy, float vz, long life)
    {
        this.name = name;
        this.world = w;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.life = life;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public Level getLevel()
    {
        return world;
    }

    @Override
    public float getX()
    {
        return x;
    }

    @Override
    public float getY()
    {
        return y;
    }

    @Override
    public float getZ()
    {
        return z;
    }

    public void update()
    {
        life-- ;
        x += vx;
        y += vy;
        z += vz;
    }

    public boolean shouldBeKilled()
    {
        return life <= 0;
    }

}
