package org.c1.client.gui;

import org.c1.*;
import org.c1.client.gui.widgets.*;

public class GuiDebug extends Gui {

    private GuiLabel playerPos;
    private GuiLabel playerHitbox;
    private C1Game game;

    public GuiDebug(C1Game gameInstance) {
        super(gameInstance);
        this.game = gameInstance;
        playerPos = new GuiLabel(10, 400, "Player pos: " + gameInstance.getPlayer().getPos().toString(), gameInstance.getFont());
        playerHitbox = new GuiLabel(10, 380, "Pl Hitbox : ", gameInstance.getFont());
    }

    @Override
    public void init() {
        addComponent(playerHitbox);
        addComponent(playerPos);
    }

    @Override
    public void update(double delta) {
        playerPos.setText("Player pos: " + this.game.getPlayer().getPos().toString());
        playerHitbox.setText("Pl Hitbox : " + game.getPlayer().getBoundingBox().toString());
        super.update(delta);
    }

    public boolean locksMouse() {
        return true;
    }
}
