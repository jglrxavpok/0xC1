package org.c1.client;

import org.c1.client.render.StaticRegion;
import org.c1.client.render.TextureRegion;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLUtils {

    public static void printIfError(String header) {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.err.println("OpenGL error while " + header + ": " + error);
        }
    }

    /**
     * Creates a TextureRegion instance based on given parameters. The coordinates are expressed in image space meaning (0,0) is the top left corner and (textureWidth,textureHeight) is the bottom right
     * @param x
     *         The X coordinate of the region of the texture to create
     * @param y
     *         The Y coordinate of the region of the texture to create
     * @param w
     *         The width of the region of the texture to create
     * @param h
     *         The height of the region of the texture to create
     * @param textureWidth
     *         The texture <b>usual</b> width
     * @param textureHeight
     *         The texture <b>usual</b> height
     * @return
     *        A TextureRegion instance going from (x,y) to (x+w,y+h)
     */
    public static TextureRegion createRegion(int x, int y, int w, int h, int textureWidth, int textureHeight) {
        int openglH = textureHeight-y;
        float minU = (float)x/(float)textureWidth;
        float maxU = (float)(x+w)/(float)textureWidth;
        float minV = (float)(openglH-h)/(float)textureHeight;
        float maxV = (float)openglH/(float)textureHeight;
        return new StaticRegion(minU, minV, maxU, maxV);
    }
}
