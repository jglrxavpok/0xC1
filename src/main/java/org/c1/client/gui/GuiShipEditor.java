package org.c1.client.gui;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.c1.*;
import org.c1.client.OpenGLUtils;
import org.c1.client.gui.editor.*;
import org.c1.client.gui.layout.*;
import org.c1.client.gui.widgets.*;
import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.maths.*;
import org.c1.tests.ModularShipObject;
import org.lwjgl.input.*;

public class GuiShipEditor extends Gui {

    private enum ShipGridType {
        WALL, THIN_WALL, COMPUTER, VOID
    }

    private C1Game gameInstance;
    
    private List<ShipEditorComponent> components;
    private ShipEditorComponent[][] grid;
    private Vec2f firstSelector;
    private Vec2f secondSelector;
    private OrthographicCamera camera;
    private boolean translating;
    private Sprite hoverSprite;
    private float tileX;
    private float tileY;
    private boolean selecting;
    private boolean shiftPressed;
    private ShipGridType currentType;

    public GuiShipEditor(C1Game gameInstance) {
        super(gameInstance);
        this.gameInstance = gameInstance;
        currentType = ShipGridType.WALL;
    }

    @Override
    public void init() {
        components = Lists.newArrayList();
        grid = new ShipEditorComponent[10][10];

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (x == 0 || x == grid.length - 1 || y == 0 || y == grid[0].length - 1)
                    grid[x][y] = new ShipWall(x, y);
            }
        }

        firstSelector = new Vec2f(-1, -1);
        secondSelector = new Vec2f(-1, -1);

        camera = new OrthographicCamera(game.getDisplayWidth(), game.getDisplayHeight());
        addComponent(new GuiLabel(0, 0, "I'm a test label!", game.getFont()));
        addComponent(new GuiButton(0, game.getDisplayHeight() - 80, 400, 80, "I'm a test button!", game.getFont(), 1));

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
        pane.setGuiListener(this);
        pane.setLayout(new DirectionLayout(DirectionLayout.Directions.VERTICAL_UPWARDS));
        try {
            pane.addComponent(new GuiLabel("Walls", game.getFont()));
            pane.addComponent(new GuiIconButton(new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(0,0,32,32,256,256)), 0));
            pane.addComponent(new GuiIconButton(new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(32,0,32,32,256,256)), 1));
            pane.addComponent(new GuiSpacing(20));
            pane.addComponent(new GuiLabel("Special", game.getFont()));
            pane.addComponent(new GuiIconButton(new Sprite(new Texture("textures/ship/editor_ship.png"), OpenGLUtils.createRegion(64,0,32,32,256,256)), 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                fillSelection(currentType);
                return true;
            } else if (keycode == Keyboard.KEY_BACK || keycode == Keyboard.KEY_DELETE) {
                fillSelection(ShipGridType.VOID);
                return true;
            }
        } else if (keycode == Keyboard.KEY_R) {
            if (!selecting) {
                int cellX = (int) Math.floor(tileX);
                int cellY = (int) Math.floor(tileY);
                if (inBound(cellX, cellY)) {
                    if(grid[cellX][cellY] != null) {
                        grid[cellX][cellY].rotate();
                    }
                    resetSelectors();
                }
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
                    grid[cellX][cellY] = create(cellX,cellY, type);
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
                    grid[cellX][cellY] = create(cellX, cellY, currentType);
                    resetSelectors();
                }
            } else {
                selecting = false;
            }
            return true;
        }
        return false;
    }

    private ShipEditorComponent create(int x, int y, ShipGridType type) {
        switch(type) {
            case WALL:
                return new ShipWall(x, y);

            case THIN_WALL:
                return new ShipThinWall(x, y);

            case COMPUTER:
                return new ShipComputer(x, y);

            case VOID:
                return null;
        }
        return null;
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
                ShipEditorComponent component = grid[x][y];
                if (component != null) {
                    component.render(delta, engine);
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
        if(inBound((int) tileX, (int)tileY)) {
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

    @Override
    public void onComponentClicked(GuiComponent component) {
        if(component.getOwner() == this) {
            if (component.getID() == 1) {
                ModularShipObject ship = new ModularShipObject("ship_test");
                for (int x = 0; x < grid.length; x++) {
                    for (int y = 0; y < grid[0].length; y++) {
                        ShipEditorComponent comp = grid[x][y];
                        if (comp != null) {
                            ship.addShipComponent(comp);
                        }
                    }
                }
                for (ShipEditorComponent c : components) {
                    ship.addShipComponent(c);
                }

                ship.createShipModel();
                gameInstance.getLevel().addGameObject(ship);
            }
        } else { // If we are here, that means the owner is the tools pane at the right side of the screen
            switch(component.getID()) {
                case -1:
                default:
                    break;
                case 0:
                    currentType = ShipGridType.WALL;
                    break;

                case 1:
                    currentType = ShipGridType.THIN_WALL;
                    break;

                case 2:
                    currentType = ShipGridType.COMPUTER;
                    break;
            }
        }
    }
}
