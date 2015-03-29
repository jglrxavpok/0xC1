package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.opengl.*;

public class TextureData {

    private int width;
    private int height;
    private int[] pixels;
    private int texID;
    private boolean init;

    public TextureData(String path) throws IOException {
        BufferedImage image = ImageIO.read(Texture.class.getResourceAsStream("/" + path));
        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
    }

    public TextureData(int w, int h, int[] pixelData) {
        width = w;
        height = h;
        pixels = pixelData;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void init(int target, int filter, int internalFormat) {
        texID = glGenTextures();
        glBindTexture(target, texID);
        glTexParameteri(target, GL12.GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(target, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, filter);

        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
        if (pixels != null) {
            for (int y = height - 1; y >= 0; y--) {
                for (int x = 0; x < width; x++) {
                    int color = pixels[x + y * width];
                    int alpha = (color >> 24) & 0xFF;
                    int red = (color >> 16) & 0xFF;
                    int green = (color >> 8) & 0xFF;
                    int blue = (color >> 0) & 0xFF;

                    // We swap composites to go from ARGB to RGBA
                    buffer.put((byte) red);
                    buffer.put((byte) green);
                    buffer.put((byte) blue);
                    buffer.put((byte) alpha);
                }
            }
            buffer.flip();
        }
        glTexImage2D(target, 0, GL30.GL_RGBA32F, width, height, 0, internalFormat, GL_UNSIGNED_BYTE, buffer);
        init = true;
    }

    public boolean isInit() {
        return init;
    }

    public int getTextureID() {
        return texID;
    }
}
