package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.opengl.*;

public class Texture {

    private int width;
    private int height;
    private int[] pixels;

    private boolean isRenderTarget;
    private int texID;
    private int filter;

    public Texture(String classpath) throws IOException {
        this(classpath, GL_NEAREST);
    }

    public Texture(String classpath, int filter) throws IOException {
        this.filter = filter;
        BufferedImage image = ImageIO.read(Texture.class.getResourceAsStream("/" + classpath));
        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
        init();
    }

    public Texture(int w, int h, int[] pixels) {
        this(w, h, pixels, GL_NEAREST);
    }

    public Texture(int w, int h, int[] pixels, int filter) {
        this.width = w;
        this.height = h;
        this.pixels = pixels;
        this.filter = filter;

        init();
    }

    private void init() {
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);
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

        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexImage2D(GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void setupRenderTarget() {
        throw new RuntimeException("Texture.setupRenderTarget is not yet implemented");
    }

    public boolean isRenderTarget() {
        return isRenderTarget;
    }

}
