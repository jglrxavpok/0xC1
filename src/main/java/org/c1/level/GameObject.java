package org.c1.level;

import org.c1.maths.*;

public abstract class GameObject {

    protected Transform transform;

    public GameObject() {
        transform = new Transform();
    }

    public abstract void update(double delta);

    public abstract void render(double delta);

    public abstract boolean shouldDie();

    public void onDespawn(double delta) {
        ;
    }

    public Transform getTransform() {
        return transform;
    }
}
