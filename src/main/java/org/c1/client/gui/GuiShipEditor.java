package org.c1.client.gui;

import java.util.*;

import com.google.common.collect.*;

import org.c1.*;
import org.c1.client.gui.editor.*;
import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.maths.*;
import org.lwjgl.input.*;

public class GuiShipEditor extends Gui {

    private List<ShipEditorComponent> components;
    private ShipWall[][][] grid;
    private Vec2f firstSelector;
    private Vec2f secondSelector;
    private Camera camera;
    private double t;

    public GuiShipEditor(C1Game gameInstance) {
        super(gameInstance);
    }

    @Override
    public void init() {
        components = Lists.newArrayList();
        grid = new ShipWall[10][10][5];

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    grid[x][y][z] = new ShipWall(x, y, z);
                }
            }
        }

        firstSelector = new Vec2f(-1, -1);
        secondSelector = new Vec2f(-1, -1);

        camera = new Camera(new Mat4f().perspective((float) Math.toRadians(90), 16f / 9f, 0.001f, 10000f));
        addComponent(new GuiLabel(0, 0, "I'm a test label!", game.getFont()));
    }

    public void update(double delta) {
        updateEditor(delta);
        super.update(delta);
    }

    public void render(double delta) {
        RenderEngine engine = game.getRenderEngine();
        Camera oldCam = engine.getCurrentCamera();

        engine.setCurrentCamera(camera);
        engine.clearDepth();
        t += delta / 40f;
        t %= 1f;
        camera.setPos(new Vec3f(5, 0, (float) (t * 5f - 8f)));
        renderEditor(delta);
        engine.clearDepth();
        engine.setCurrentCamera(oldCam);
        super.render(delta);
    }

    private void updateEditor(double delta) {
        for (ShipEditorComponent c : components) {
            c.update(delta);
        }
    }

    private void renderEditor(double delta) {
        int mx = Mouse.getX();
        int my = Mouse.getY();
        float projectedX = ((float) mx / (float) game.getDisplayWidth()) * 2f - 1f;
        float projectedY = ((float) my / (float) game.getDisplayHeight()) * 2f - 1f;
        Vec3f mouseWorldPos = raycast(projectedX, projectedY);

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    ShipWall wall = grid[x][y][z];
                    if (wall != null) {
                        wall.render(delta);
                    }
                }
            }
        }
        for (ShipEditorComponent c : components) {
            c.render(delta);
        }
    }

    private Vec3f raycast(float projectedX, float projectedY) {
        Vec3f mousePos = new Vec3f(projectedX, projectedY, 0);
        camera.getTransform().transform(mousePos);
        Vec3f forward = camera.getTransform().rot().forward();

        return null;
    }

}
