package org.c1.level;

import java.util.*;

import com.google.common.collect.*;

import org.c1.level.lights.*;

public class Level {

    private List<GameObject> gameObjects;
    private List<GameObject> spawningList;
    private List<GameObject> despawningList;
    private List<Light> lights;

    public Level() {
        gameObjects = Lists.newArrayList();
        spawningList = Lists.newArrayList();
        despawningList = Lists.newArrayList();
        lights = Lists.newArrayList();
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addGameObject(GameObject o) {
        if (o instanceof Light) {
            addLight((Light) o);
        } else {
            spawningList.add(o);
        }
    }

    public void removeGameObject(GameObject o) {
        if (o instanceof Light) {
            removeLight((Light) o);
        } else {
            despawningList.add(o);
        }
    }

    public void update(double delta) {
        gameObjects.addAll(spawningList);
        for (GameObject o : despawningList) {
            o.onDespawn(delta);
            gameObjects.remove(o);
        }
        spawningList.clear();
        despawningList.clear();
        for (GameObject o : gameObjects) {
            o.update(delta);
            if (o.shouldDie()) {
                removeGameObject(o);
            }
        }
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void removeLight(Light light) {
        lights.remove(light);
    }

    public List<Light> getLights() {
        return lights;
    }
}
