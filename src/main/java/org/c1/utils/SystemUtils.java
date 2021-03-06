package org.c1.utils;

import java.io.*;

public final class SystemUtils {

    public enum OperatingSystem {
        WINDOWS, LINUX, MACOSX, SOLARIS, UNKNOWN;
    }

    private static File gameFolder;

    public static OperatingSystem getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("sunos") || os.contains("solaris")) {
            return OperatingSystem.SOLARIS;
        } else if (os.contains("unix") || os.contains("linux")) {
            return OperatingSystem.LINUX;
        } else if (os.contains("mac")) {
            return OperatingSystem.MACOSX;
        }
        return OperatingSystem.UNKNOWN;
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Returns the folder where game data is saved
     */
    public static File getGameFolder(String gameID) {
        if (gameFolder == null) {
            String appdata = System.getenv("APPDATA");
            if (appdata != null)
                gameFolder = new File(appdata, gameID);
            else
                gameFolder = new File(System.getProperty("user.home"), gameID);
        }
        return gameFolder;
    }

    public static void deleteRecursivly(File file) {
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null)
                for (File f : list) {
                    deleteRecursivly(f);
                    f.delete();
                }
        }
        file.delete();
    }

    public static void setGameFolder(File file) {
        gameFolder = file;
    }
}
