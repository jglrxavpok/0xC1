package org.c1.utils;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.google.common.collect.*;

import org.c1.utils.*;
import org.c1.utils.SystemUtils.*;
import org.slf4j.*;

public class LWJGLSetup {

    private static boolean loaded;

    /**
     * Load LWJGL in given folder
     */
    public static void load(File folder) throws IOException {
        Logger logger = LoggerFactory.getLogger("LWJGLSetup");
        if (!loaded) {
            if (!folder.exists())
                folder.mkdirs();
            if (folder.isDirectory()) {
                Map<OperatingSystem, String[]> nativesMap = createNativesMap();
                String arch = System.getProperty("os.arch");
                boolean is64bits = !arch.equals("x86");
                OperatingSystem os = SystemUtils.getOS();

                String[] arch64Variants = new String[] { "_64", "64", "" };
                String[] nativesList = nativesMap.get(os);
                if (nativesList == null) {
                    logger.error("OS " + os.name() + " is not supported, sorry :(");
                } else {
                    nativesLoop: for (String f : nativesList) {
                        for (String variant : arch64Variants) {
                            String fileName = is64bits ? f.replace("32", "") : f;
                            String[] parts = fileName.split(Pattern.quote("."));
                            String name = sum(parts, 0, parts.length - 1);
                            name += variant + "." + parts[parts.length - 1];
                            if (exists(name)) {
                                if (!new File(folder, name).exists()) {
                                    extractFromClasspath(name, folder);
                                    logger.info("Successfully extracted native from classpath: " + name);
                                }
                                continue nativesLoop;
                            }
                        }
                    }
                }
                System.setProperty("net.java.games.input.librarypath", folder.getAbsolutePath());
                System.setProperty("org.lwjgl.librarypath", folder.getAbsolutePath());
            }
            loaded = true;
        }
    }

    private static String sum(String[] parts, int offset, int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = offset; i < offset + length; i++) {
            buffer.append(parts[i]);
        }
        return buffer.toString();
    }

    private static boolean exists(String fileName) {
        InputStream stream = LWJGLSetup.class.getResourceAsStream("/" + fileName);
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private static Map<OperatingSystem, String[]> createNativesMap() {
        Map<OperatingSystem, String[]> nativesMap = Maps.newHashMap();
        String[] win = new String[] { "jinput-dx8.dll", "jinput-raw.dll", "lwjgl.dll", "OpenAL32.dll" };
        nativesMap.put(OperatingSystem.WINDOWS, win);

        String[] macosx = new String[] { "liblwjgl.jnilib", "liblwjgl-osx.jnilib", "openal.dylib" };
        nativesMap.put(OperatingSystem.MACOSX, macosx);

        String[] unix = new String[] { "liblwjgl.so", "libopenal.so" };
        nativesMap.put(OperatingSystem.LINUX, unix);
        nativesMap.put(OperatingSystem.SOLARIS, unix);
        return nativesMap;
    }

    /**
     * Extract given file from classpath into given folder
     */
    private static void extractFromClasspath(String fileName, File folder) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(folder, fileName));
        IOUtils.copy(LWJGLSetup.class.getResourceAsStream("/" + fileName), out);
        out.flush();
        out.close();
    }
}
