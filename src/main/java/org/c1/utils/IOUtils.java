package org.c1.utils;

import java.io.*;

public class IOUtils {

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int i;

        while ((i = in.read(buffer)) != -1) {
            out.write(buffer, 0, i);
        }
        out.flush();
    }

}
