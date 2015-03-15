package org.c1.client.gui;

import org.c1.C1Game;

public class GuiDebug extends Gui {

    private GuiLabel playerPos;
    private GuiLabel playerHitbox;
    private C1Game game;
    
    public GuiDebug(C1Game gameInstance) {
        super(gameInstance);
        this.game = gameInstance;
        playerPos = new GuiLabel(10, 400, "Player pos: " + gameInstance.player.getPos().toString(), gameInstance.getFont());
        playerHitbox = new GuiLabel(10, 380, "Pl Hitbox : ", gameInstance.getFont());
    }

    @Override
    public void init() {
        
    }
    
    @Override
    public void update(double delta) {
        playerPos.setText("Player pos: " + this.game.player.getPos().toString());
        playerHitbox.setText("Pl Hitbox : " + game.player.hitbox.toString());
        super.update(delta);
    }

    @Override
    public void render(double delta) {
        playerPos.render(delta);
        playerHitbox.render(delta);
        super.render(delta);
    }

}
