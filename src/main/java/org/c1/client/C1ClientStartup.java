package org.c1.client;

import java.io.*;
import java.util.*;

import org.c1.*;
import org.c1.utils.*;

public class C1ClientStartup
{

    public static void main(String[] args)
    {
        Thread.currentThread().setName("Client");
        Map<String, String> properties = Startup.argsToMap(args);
        if(!properties.containsKey("username"))
            properties.put("username", "Player_" + (int) (Math.random() * 100000L));
        if(!properties.containsKey("lang"))
            properties.put("lang", "en_US");
        if(!properties.containsKey("gamefolder"))
            properties.put("gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
        if(!properties.containsKey("nativesFolder"))
            properties.put("nativesFolder", new File(SystemUtils.getGameFolder(), "natives").getAbsolutePath());

        Startup.applyArguments(properties);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        System.setProperty("net.java.games.input.librarypath", properties.get("nativesFolder"));
        System.setProperty("org.lwjgl.librarypath", properties.get("nativesFolder"));

        I18n.setCurrentLanguage(properties.get("lang"));
        C1Client instance = new C1Client();
        CommonHandler.setInstance(instance);
        instance.start(properties);
    }
}
