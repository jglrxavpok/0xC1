package org.c1.client.gui.widgets;

import org.c1.client.gui.*;
import org.c1.client.render.*;
import org.c1.client.render.fonts.*;

public class GuiSmallButton extends GuiButton
{

    public GuiSmallButton(int id, int x, int y, String title, FontRenderer fontRenderer)
    {
        super(id, x, y, 32, 32, "", fontRenderer);
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            engine.bindLocation(Gui.widgetsTexture);
            float minU = isPressed() ? 120f / 256f : 100f / 256f;
            Gui.drawTexturedRect(engine, getX(), getY(), getWidth(), getHeight(), minU, 20f / 256f, minU + 20f / 256f, 40f / 256f);
        }
    }
}
