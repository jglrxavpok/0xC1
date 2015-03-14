package org.c1.client.gui;

import java.util.*;

import com.google.common.collect.*;

import org.c1.*;
import org.c1.client.gui.editor.*;
import org.c1.level.*;
import org.c1.maths.*;
import org.lwjgl.input.*;

public class GuiShipEditor extends Gui {

    private List<ShipEditorComponent> components;
    private boolean[][][] grid;
    private Vec2f firstSelector;
    private Vec2f secondSelector;
    private Camera camera;

    public GuiShipEditor(C1Game gameInstance) {
        super(gameInstance);
    }

    @Override
    public void init() {
        components = Lists.newArrayList();
        grid = new boolean[50][50][5];

        firstSelector = new Vec2f(-1, -1);
        secondSelector = new Vec2f(-1, -1);

        camera = new Camera(new Mat4f().perspective((float) Math.toRadians(90), 16f / 9f, 0.001f, 1000f));
        addComponent(new GuiLabel(0, 0, "I'm a test label!", game.getFont()));
    }

    public void update(double delta) {
        updateEditor(delta);
        super.update(delta);
    }

    public void render(double delta) {
        renderEditor(delta);
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
        for (ShipEditorComponent c : components) {
            c.render(delta);
        }
    }

    private Vec3f raycast(float projectedX, float projectedY) {
        return null;
    }

}
