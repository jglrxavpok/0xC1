package org.c1.client.gui;

import org.c1.client.*;
import org.c1.client.gui.widgets.*;
import org.lwjgl.input.*;

public class GuiPauseMenu extends Gui
{

    public GuiPauseMenu(C1Client game)
    {
        super(game);
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        addWidget(new GuiButton(0, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2, 300, 40, I18n.format("main.play.return"), getFontRenderer()));
        addWidget(new GuiButton(1, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2 + 60, 300, 40, I18n.format("main.settings"), getFontRenderer()));
        addWidget(new GuiButton(2, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2 + 120, 300, 40, I18n.format("main.play.quitToMainScreen"), getFontRenderer()));
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            oc.openMenu(new GuiIngame(oc));
        }
        else if(widget.getID() == 1)
        {
            oc.openMenu(new GuiSettings(oc, this));
        }
        else if(widget.getID() == 2)
        {
            oc.quitToMainScreen();
        }
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public boolean keyReleased(int id, char c)
    {
        super.keyReleased(id, c);
        if(id == Keyboard.KEY_ESCAPE)
        {
            oc.openMenu(new GuiIngame(oc));
        }
        return true;
    }

    @Override
    public boolean pausesGame()
    {
        return true;
    }
}
