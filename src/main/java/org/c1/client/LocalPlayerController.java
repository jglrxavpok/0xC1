package org.c1.client;

import org.c1.entity.*;
import org.c1.utils.*;

public class LocalPlayerController extends PlayerController
{

    public LocalPlayerController(EntityPlayer player)
    {
        super(player);
    }

    @Override
    public void onSneakRequested()
    {

    }

    @Override
    public void onJumpRequested()
    {
        player.jump();
    }

    @Override
    public void onMoveLeftRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveLeft(speed);
    }

    @Override
    public void onMoveRightRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveRight(speed);
    }

    @Override
    public void onMoveForwardRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveForward(speed);
    }

    @Override
    public void onMoveBackwardsRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveBackwards(speed);
    }

    @Override
    public void update()
    {
        if(!C1Client.getClient().getCurrentMenu().requiresMouse())
        {
            player.yaw += (float) Math.toRadians(C1Client.getClient().getMouseHandler().getDX()) * C1Client.getClient().getGameSettings().sensitivity.getValue();
            player.pitch += (float) Math.toRadians(-C1Client.getClient().getMouseHandler().getDY()) * C1Client.getClient().getGameSettings().sensitivity.getValue();
        }
    }

    @Override
    public void onLeftClick(CollisionInfos infos)
    {
    }

    @Override
    public void onRightClick(CollisionInfos infos)
    {
    }

    @Override
    public void onMouseWheelMoved(int amount)
    {
    }

    @Override
    public void onMouseWheelClicked()
    {
    }
}
