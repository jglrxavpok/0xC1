package org.c1.client.gui.layout;

import org.c1.client.gui.*;
import org.c1.maths.*;

public class DirectionLayout implements GuiLayout {

    public static enum Directions {
        VERTICAL_DOWNWARDS, HORIZONTAL, VERTICAL_UPWARDS
    }

    private float accumulator;
    private Directions dir;
    private float spacing;

    public DirectionLayout(Directions dir) {
        this.dir = dir;
        this.spacing = 4f;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public float getSpacing() {
        return spacing;
    }

    public Directions getDirection() {
        return dir;
    }

    @Override
    public void onAdded(GuiComponent component, GuiComponent parent) {
        float x = component.getPos().x();
        float y = component.getPos().y();
        float w = component.getWidth();
        float h = component.getHeight();
        switch (dir) {
        case VERTICAL_DOWNWARDS:
            component.setPos(new Vec2f(x, y + accumulator));
            accumulator += h + spacing;
            break;

        case VERTICAL_UPWARDS:
            component.setPos(new Vec2f(x, y + accumulator));
            accumulator -= h + spacing;
            break;

        case HORIZONTAL:
            component.setPos(new Vec2f(x + accumulator, y));
            accumulator += w + spacing;
            break;
        }
    }

}
