package org.c1;

public final class CommonHandler
{

    private static C1Instance instance;

    private CommonHandler()
    {
        throw new IllegalAccessError("Why are you trying to access an instance of static-only class?? Plz tel me");
    }

    public static C1Instance getCurrentInstance()
    {
        return instance;
    }

    public static void setInstance(C1Instance instance)
    {
        CommonHandler.instance = instance;
    }
}
