package org.c1.client.gui.editor;

import org.c1.client.models.*;
import org.c1.maths.*;

public class ShipWall extends ShipEditorComponent {

    private static ShipWallModel[] models;
    private ShipWallModel wallModel;
    private Mat4f transformMatrix;

    public ShipWall(float x, float y, float z) {
        if (models == null) {
            initModels();
        }
        wallModel = models[(int) ((x + z) % 4)];
        transformMatrix = new Mat4f().translation(x * 0.25f, y, z * 0.25f);
    }

    private void initModels() {
        models = new ShipWallModel[4];
        models[0] = new ShipWallModel(0);
        models[1] = new ShipWallModel(1);
        models[2] = new ShipWallModel(2);
        models[3] = new ShipWallModel(3);
    }

    @Override
    public void render(double delta) {
        wallModel.render();
    }

    @Override
    public void update(double delta) {
        ;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    public Mat4f getTransformationMatrix() {
        return transformMatrix;
    }

}
