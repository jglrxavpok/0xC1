package org.c1.client.gui;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.c1.*;
import org.c1.client.gui.editor.*;
import org.c1.client.gui.layout.*;
import org.c1.client.gui.widgets.*;
import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.maths.*;
import org.lwjgl.input.*;

public class GuiShipEditor extends Gui {

    private enum ShipGridType {
        WALL, VOID
    }

    private List<ShipEditorComponent> components;
    private ShipWall[][] grid;
    private Vec2f firstSelector;
    private Vec2f secondSelector;
    private OrthographicCamera camera;
    private boolean translating;
    private Sprite hoverSprite;
    private float tileX;
    private float tileY;
    private boolean selecting;
    private boolean shiftPressed;

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
        addComponent(new GuiButton(0, game.getDisplayHeight() - 80, 400, 80, "I'm a test button!", game.getFont()));

        addComponent(createToolList());

        try {
            hoverSprite = new Sprite(new Texture("textures/ship/editor/hover.png"));
            hoverSprite.setSize(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GuiComponent createToolList() {
        float w = game.getDisplayWidth() / 4f;
        GuiScrollPane pane = new GuiScrollPane(game.getDisplayWidth() - w, 0, w, game.getDisplayHeight());
        pane.setLayout(new DirectionLayout(DirectionLayout.Directions.VERTICAL_UPWARDS));
        pane.addComponent(new GuiLabel(0, 0, "Test of the scroll pane1", game.getFont()));
        pane.addComponent(new GuiLabel(0, 0, "Test of the scroll pane2", game.getFont()));
        pane.addComponent(new GuiLabel(0, 0, "Test of the scroll pane3", game.getFont()));
        pane.addComponent(new GuiLabel(0, 0, "Test of the scroll pane4", game.getFont()));
        pane.addComponent(new GuiLabel(0, 0, "Test of the scroll pane5", game.getFont()));
        pane.resetScrollToTop();
        return pane;
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

    public boolean onMouseMoved(int x, int y, int dx, int dy) {
        if (super.onMouseMoved(x, y, dx, dy))
            return true;
        if (translating) {
            float translationX = -dx / camera.getZoom();
            float translationY = -dy / camera.getZoom();
            camera.getTransform().translate(translationX, translationY, 0);
        }

        setHoveredTile(x, y);

        if (selecting) {
            secondSelector.x(tileX);
            secondSelector.y(tileY);
        }
        return true;
    }

    private void setHoveredTile(int x, int y) {
        float dcenterX = (x - game.getDisplayWidth() / 2f) / (game.getDisplayWidth() / 2f);
        float dcenterY = (y - game.getDisplayHeight() / 2f) / (game.getDisplayHeight() / 2f);
        dcenterX /= camera.getZoom();
        dcenterY /= camera.getZoom();

        tileX = dcenterX * (game.getDisplayWidth() / 2f) + (game.getDisplayWidth() / 2f);
        tileX += camera.getPos().x();

        tileY = dcenterY * (game.getDisplayHeight() / 2f) + (game.getDisplayHeight() / 2f);
        tileY += camera.getPos().y();
        tileX /= 32f;
        tileY /= 32f;

        tileX = (float) Math.floor(tileX);
        tileY = (float) Math.floor(tileY);
    }

    public boolean onMousePressed(int x, int y, int button) {
        if (super.onMousePressed(x, y, button))
            return true;
        setHoveredTile(x, y);
        if (button == MOUSE_RIGHT) {
            if (shiftPressed) {
                translating = true;
                return true;
            }
        } else if (button == MOUSE_LEFT) {
            resetSelectors();
            firstSelector.x(tileX);
            firstSelector.y(tileY);

            if (shiftPressed) {
                selecting = true;
            }
            return true;
        }
        return false;
    }

    private void resetSelectors() {
        firstSelector.x(-1);
        firstSelector.y(-1);
        secondSelector.x(-1);
        secondSelector.y(-1);
    }

    public boolean onKeyReleased(int keycode, char eventchar) {
        if (super.onKeyReleased(keycode, eventchar))
            return true;
        if (keycode == Keyboard.KEY_LSHIFT || keycode == Keyboard.KEY_RSHIFT) {
            shiftPressed = false;
            return true;
        } else if (!isSelectionEmpty()) {
            if (keycode == Keyboard.KEY_RETURN) {
                fillSelection(ShipGridType.WALL);
                return true;
            } else if (keycode == Keyboard.KEY_BACK || keycode == Keyboard.KEY_DELETE) {
                fillSelection(ShipGridType.VOID);
                return true;
            }
        }
        return false;
    }

    private boolean isSelectionEmpty() {
        return secondSelector.x() == -1 || secondSelector.y() == -1;
    }

    private void fillSelection(ShipGridType type) {
        float minX = (float) Math.floor(Math.min(firstSelector.x(), secondSelector.x()));
        float minY = (float) Math.floor(Math.min(firstSelector.y(), secondSelector.y()));
        float maxX = (float) Math.floor(Math.max(firstSelector.x(), secondSelector.x()));
        float maxY = (float) Math.floor(Math.max(firstSelector.y(), secondSelector.y()));
        for (float x = minX; x <= maxX; x++) {
            for (float y = minY; y <= maxY; y++) {
                int cellX = (int) x;
                int cellY = (int) y;
                if (inBound(cellX, cellY)) {
                    switch (type) {
                    case WALL:
                        grid[cellX][cellY] = new ShipWall(x * 32f, y * 32f);
                        break;
                    case VOID:
                        grid[cellX][cellY] = null;
                        break;
                    }
                }
            }
        }
    }

    public boolean onKeyPressed(int keycode, char eventchar) {
        if (super.onKeyPressed(keycode, eventchar))
            return true;
        if (keycode == Keyboard.KEY_LSHIFT || keycode == Keyboard.KEY_RSHIFT) {
            shiftPressed = true;
            return true;
        }
        return false;
    }

    public boolean onMouseReleased(int x, int y, int button) {
        if (super.onMouseReleased(x, y, button))
            return true;
        setHoveredTile(x, y);
        if (button == MOUSE_RIGHT) {
            if (!translating) {
                int cellX = (int) Math.floor(tileX);
                int cellY = (int) Math.floor(tileY);
                if (inBound(cellX, cellY)) {
                    grid[cellX][cellY] = null;
                    resetSelectors();
                }
            }
            translating = false;
            return true;
        } else if (button == MOUSE_LEFT) {
            if (!selecting) {
                int cellX = (int) Math.floor(tileX);
                int cellY = (int) Math.floor(tileY);
                if (inBound(cellX, cellY)) {
                    grid[cellX][cellY] = new ShipWall(cellX * 32f, cellY * 32f);
                    resetSelectors();
                }
            } else {
                selecting = false;
            }
            return true;
        }
        return false;
    }

    public boolean onScroll(int x, int y, int direction) {
        if (super.onScroll(x, y, direction))
            return true;
        float newZoom = camera.getZoom();
        float zoomFactor = 1.125f;
        if (direction < 0) {
            newZoom /= zoomFactor;
        } else
            newZoom *= zoomFactor;
        camera.setZoom(newZoom);
        return true;
    }

    private void renderEditor(double delta) {
        RenderEngine engine = game.getRenderEngine();

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                ShipWall wall = grid[x][y];
                if (wall != null) {
                    wall.render(delta, engine);
                }
            }
        }

        if (!isSelectionEmpty()) {
            float minX = (float) Math.floor(Math.min(firstSelector.x(), secondSelector.x()));
            float minY = (float) Math.floor(Math.min(firstSelector.y(), secondSelector.y()));
            float maxX = (float) Math.floor(Math.max(firstSelector.x(), secondSelector.x()));
            float maxY = (float) Math.floor(Math.max(firstSelector.y(), secondSelector.y()));
            for (float x = minX; x <= maxX; x++) {
                for (float y = minY; y <= maxY; y++) {
                    hoverSprite.setPos(x * 32f, y * 32f);
                    hoverSprite.render(engine);
                }
            }
        }
        if(tileX >= 0 && tileX <= grid.length - 1 && tileY >= 0 && tileY <= grid.length - 1) {
        	hoverSprite.setPos(tileX * 32f, tileY * 32f);
        	hoverSprite.render(engine);
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
