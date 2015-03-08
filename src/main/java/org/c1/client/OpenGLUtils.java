package org.c1.client;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLUtils {

    public static void printIfError(String header) {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.err.println("OpenGL error while " + header + ": " + error);
        }
    }
}
