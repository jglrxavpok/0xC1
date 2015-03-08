package org.c1.inventory;


public class Stack
{
    private int     stackSize;
    private short   damage;
    private Item    stackable;
    private boolean hasCustomMaxStackSize;
    private int     customMaxStackSize;

    public Stack(Item stackable)
    {
        this(stackable, 1);
    }

    public Stack(Item stackable, int size)
    {
        this(stackable, size, (short) 0);
    }

    public Stack(Item stackable, int size, short damage)
    {
        this.stackable = stackable;
        this.stackSize = size;
        this.damage = damage;

        if(this.damage < 0)
            this.damage = 0;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Item getStackable()
    {
        return stackable;
    }

    public int compareTo(Stack o)
    {
        return Integer.compare(stackSize, o.getQuantity());
    }

    public short getDamage()
    {
        return damage;
    }

    public void setDamage(short damage)
    {
        this.damage = damage;
    }

    public int getQuantity()
    {
        return stackSize;
    }

    public void setQuantity(int quantity) throws IllegalArgumentException
    {
        this.stackSize = quantity;
    }

    public int getMaxStackQuantity()
    {
        if(hasCustomMaxStackSize)
            return customMaxStackSize;
        return 1;
    }

    public void setMaxStackQuantity(int quantity)
    {
        hasCustomMaxStackSize = true;
        customMaxStackSize = quantity;
    }
}
