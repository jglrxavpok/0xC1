package org.c1.client.gui.editor;

import org.c1.client.render.*;

public abstract class ShipEditorComponent {

    public abstract void render(double delta, RenderEngine engine);

    public abstract void update(double delta);

    public abstract int getWidth();

    public abstract int getHeight();
}
