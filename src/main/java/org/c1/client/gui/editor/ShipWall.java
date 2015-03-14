package org.c1.client.gui.editor;

import org.c1.client.models.*;

public class ShipWall extends ShipEditorComponent {

    private ShipWallModel wallModel;

    public ShipWall(float x, float y, float z) {
        wallModel = new ShipWallModel(x, y, z);
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

}
