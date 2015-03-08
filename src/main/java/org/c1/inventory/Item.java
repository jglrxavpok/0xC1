package org.c1.inventory;

public class Item
{

    private String id;

    public Item(String id)
    {
        this.id = id;
    }

    public String getUnlocalizedID()
    {
        return "item." + id;
    }

}
