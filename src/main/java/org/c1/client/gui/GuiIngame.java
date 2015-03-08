package org.c1.client.gui;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.client.render.fonts.*;
import org.c1.entity.*;
import org.c1.utils.*;

public class GuiIngame extends Gui
{

    private float scale;

    public GuiIngame(C1Client game)
    {
        super(game);
    }

    @Override
    public void init()
    {
        scale = 1.85f;
    }

    @Override
    public void render(int mx, int my, RenderEngine renderEngine)
    {
        super.render(mx, my, renderEngine);
        getFontRenderer().drawString("Playing as \"" + oc.getClientUsername() + "\" and password is " + TextFormatting.OBFUSCATED + "LOL_THERE'S_NO_PASSWORD_HERE", 0xFFFFFFFF, 2, 0, renderEngine);
        EntityPlayer player = oc.getClientPlayer();

        CollisionInfos infos = oc.getObjectInFront();

        if(player != null)
        {
            org.c1.inventory.Stack stack = player.getHeldItem();
            if(stack != null)
            {
                String s = I18n.format(stack.getStackable().getUnlocalizedID());
                getFontRenderer().setScale(scale);
                getFontRenderer().drawShadowedString(s, 0xFFFFFFFF, (int) (oc.getDisplayWidth() / 2 - (int) getFontRenderer().getTextWidth(s) / 2), (int) oc.getDisplayHeight() - 40, renderEngine);
                getFontRenderer().setScale(1);
            }
        }

    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public boolean requiresMouse()
    {
        return false;
    }

}
