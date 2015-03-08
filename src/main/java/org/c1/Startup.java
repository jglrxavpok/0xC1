package org.c1;

import java.io.*;
import java.util.*;

import org.c1.utils.*;

public final class Startup
{
    private Startup()
    {
        throw new IllegalAccessError("No instance for you!");
    }

    public static Map<String, String> argsToMap(String[] args)
    {
        String current = null;
        HashMap<String, String> properties = new HashMap<String, String>();
        for(int i = 0; i < args.length; i++ )
        {
            String arg = args[i];
            if(arg.startsWith("--"))
            {
                if(current != null && !properties.containsKey(current))
                {
                    properties.put(current, "");
                }
                current = arg.substring(2);
            }
            else
            {
                properties.put(current, arg);
            }
        }
        if(current != null && !properties.containsKey(current))
        {
            properties.put(current, "");
        }

        return properties;
    }

    public static void applyArguments(Map<String, String> properties)
    {
        boolean debug = properties.get("debug") != null && !properties.get("debug").equalsIgnoreCase("false");
        Log.useFullClassNames = debug;
        Log.showCaller = debug;
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        Dev.debug(debug);
    }
}
