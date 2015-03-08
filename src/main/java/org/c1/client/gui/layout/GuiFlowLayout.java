package org.c1.client.gui.layout;

import org.c1.client.gui.widgets.*;

public class GuiFlowLayout implements IGuiLayout
{

    private float x;
    private float y;

    @Override
    public void onAdd(GuiWidget widget, GuiPanel container)
    {
        if(x + widget.getWidth() >= container.getWidth())
        {
            y += widget.getHeight() + 10;
            x = 0;
        }
        widget.setLocation(container.getX() + (int) x, container.getY() + (int) y);
        x += widget.getWidth() + 10;
    }

}
