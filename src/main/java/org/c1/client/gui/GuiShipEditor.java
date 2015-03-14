package org.c1.client.gui;

import org.c1.*;

public class GuiShipEditor extends Gui {

    public GuiShipEditor(C1Game gameInstance) {
        super(gameInstance);
    }

    @Override
    public void init() {
        addComponent(new GuiLabel(0, 0, "I'm a test label!", game.getFont()));
    }

    public void render(double delta) {
        renderEditor();
        super.render(delta);
    }

    private void renderEditor() {
        ;
    }

}
