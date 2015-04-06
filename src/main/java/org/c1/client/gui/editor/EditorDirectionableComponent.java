package org.c1.client.gui.editor;

import org.c1.client.gui.GuiShipEditor;
import org.c1.utils.CardinalDirection;

public abstract class EditorDirectionableComponent extends ShipEditorComponent {

    private CardinalDirection direction;

    public EditorDirectionableComponent(float x, float y) {
        super(x, y);
        direction = CardinalDirection.NORTH;
    }

    @Override
    public CardinalDirection getDirection() {
        return direction;
    }

    @Override
    public void setDirection(CardinalDirection dir) {
        this.direction = dir;
    }
}
