package org.c1;

import org.c1.level.*;

public interface IParticleHandler
{

    void spawnParticle(String id, Level w, float x, float y, float z);

    void spawnParticle(String id, ILocatable loc);

    void spawnParticle(Particle particle);

    void updateAllParticles();
}
