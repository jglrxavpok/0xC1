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
    private ShipWall[][] grid;
    private Vec2f firstSelector;
    private Vec2f secondSelector;
    private OrthographicCamera camera;

    public GuiShipEditor(C1Game gameInstance) {
        super(gameInstance);
    }

    @Override
    public void init() {
        components = Lists.newArrayList();
        grid = new ShipWall[10][10];

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (x == 0 || x == grid.length - 1 || y == 0 || y == grid[0].length - 1)
                    grid[x][y] = new ShipWall(x * 32f, y * 32f);
            }
        }

        firstSelector = new Vec2f(-1, -1);
        secondSelector = new Vec2f(-1, -1);

        camera = new OrthographicCamera(game.getDisplayWidth(), game.getDisplayHeight());
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
        engine.disableDepthTesting();
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

    public void onScroll(int x, int y, int direction) {
        super.onScroll(x, y, direction);
        float newZoom = camera.getZoom();
        float zoomFactor = 1.125f;
        if (direction < 0) {
            newZoom /= zoomFactor;
        } else
            newZoom *= zoomFactor;
        camera.setZoom(newZoom);
    }

    private void renderEditor(double delta) {
        RenderEngine engine = game.getRenderEngine();
        int mx = Mouse.getX();
        int my = Mouse.getY();

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                ShipWall wall = grid[x][y];
                if (wall != null) {
                    wall.render(delta, engine);
                }
            }
        }
        components.forEach(comp -> comp.render(delta, engine));

    }

    private boolean inBound(int gridX, int gridY) {
        return gridX >= 0 && gridY >= 0 && gridX < grid.length && gridY < grid[0].length;
    }

    public boolean locksMouse() {
        return false;
    }
}
