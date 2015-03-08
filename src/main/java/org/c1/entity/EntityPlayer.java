package org.c1.entity;

import java.util.*;

import com.mojang.nbt.*;

import org.c1.inventory.*;
import org.c1.level.*;
import org.c1.utils.*;

public class EntityPlayer extends EntityLiving
{
    private String name;
    private String displayName;

    public EntityPlayer(Level world, UUID uuid)
    {
        super(world);
        this.stepHeight = 0.75f;
        this.uuid = uuid;
        this.name = SessionManager.getInstance().getName(uuid);
        this.displayName = SessionManager.getInstance().getDisplayName(uuid);
        this.inventory = new PlayerInventory(name, 36);
        setSize(0.75f, 1.80f, 0.75f);
    }

    @Override
    public float getEyeOffset()
    {
        return 1.7f;
    }

    public String getName()
    {
        return name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public org.c1.inventory.Stack getHeldItem()
    {
        return inventory.getStackInSlot(((PlayerInventory) inventory).getSelectedIndex());
    }

    public void sendMessage(String message)
    {
        // TODO: Implementation
    }

    @Override
    public void readFromNBT(NBTCompoundTag compound)
    {
        super.readFromNBT(compound);
        displayName = compound.getString("displayName");
    }

    @Override
    public void writeToNBT(NBTCompoundTag compound)
    {
        super.writeToNBT(compound);
        compound.putString("displayName", displayName);
    }

}
