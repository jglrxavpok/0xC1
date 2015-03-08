import java.io.*;
import java.util.*;

import org.c1.utils.*;

public class ServerDevStart extends DevStart
{

    public static void main(String[] args) throws IOException, ReflectiveOperationException
    {
        new ServerDevStart().start(args);
    }

    @Override
    public String getStartupClassName()
    {
        return "org.c1.server.OurCraftServerStartup";
    }

    @Override
    public void applyDefaults(Map<String, String> properties)
    {
        applyDefault(properties, "gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
    }

}
