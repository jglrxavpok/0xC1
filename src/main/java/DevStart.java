import java.io.*;
import java.util.*;

import org.c1.*;

public abstract class DevStart
{

    public void start(String[] args) throws ReflectiveOperationException, IOException
    {
        Map<String, String> properties = Startup.argsToMap(args);

        applyDefaults(properties);
        Startup.applyArguments(properties);

        preInit(properties);
        Class<?> clazz = Class.forName(getStartupClassName());
        clazz.getMethod("main", String[].class).invoke(null, new Object[]
        {
                args
        });
    }

    public abstract String getStartupClassName();

    public void preInit(Map<String, String> properties)
    {
        ;
    }

    public void applyDefaults(Map<String, String> properties)
    {
        ;
    }

    public static void applyDefault(Map<String, String> properties, String key, String value)
    {
        if(!properties.containsKey(key))
            properties.put(key, value);
    }
}
