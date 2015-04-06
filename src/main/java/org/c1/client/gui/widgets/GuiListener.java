package org.c1.client.gui.widgets;

import org.c1.client.gui.GuiComponent;

@FunctionalInterface
public interface GuiListener {

    void onComponentClicked(GuiComponent comp);
}
