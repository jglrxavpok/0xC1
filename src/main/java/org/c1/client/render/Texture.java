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
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = pixels[x + y * width];
                float alpha = ((color >> 24) & 0xFF) / 255f;
                float red = ((color >> 16) & 0xFF) / 255f;
                float green = ((color >> 8) & 0xFF) / 255f;
                float blue = ((color >> 0) & 0xFF) / 255f;

                // We swap composites to go from ARGB to RGBA
                buffer.put((byte) (red * 255f));
                buffer.put((byte) (green * 255f));
                buffer.put((byte) (blue * 255f));
                buffer.put((byte) (alpha * 255f));
            }
        }
        buffer.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, width, height, 0, GL_RGBA, GL_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
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
