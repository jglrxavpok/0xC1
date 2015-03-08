package org.c1.client.gui.layout;

import org.c1.client.gui.widgets.*;

public class GuiAbsoluteLayout implements IGuiLayout
{
    @Override
    public void onAdd(GuiWidget widget, GuiPanel container)
    {
        widget.setLocation(widget.getX() + container.getX(), widget.getY() + container.getY());
    }

}
