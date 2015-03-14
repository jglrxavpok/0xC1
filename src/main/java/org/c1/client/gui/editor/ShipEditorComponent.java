package org.c1.client.gui.editor;

public abstract class ShipEditorComponent {

    public abstract void render(double delta);

    public abstract void update(double delta);

    public abstract int getWidth();

    public abstract int getHeight();
}
