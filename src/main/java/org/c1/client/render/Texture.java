package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.c1.*;
import org.c1.utils.*;
import org.lwjgl.opengl.*;

public class Texture implements IDisposable {

    private static Map<String, TextureData> resources = Maps.newHashMap();
    private int width;
    private int height;

    private boolean isRenderTarget;
    private int texID;
    private int filter;
    private Framebuffer framebuffer;
    private int target;
    private int internalFormat;
    private TextureData data;

    public Texture(String classpath) throws IOException {
        this(classpath, GL_NEAREST);
    }

    public Texture(String classpath, int filter) throws IOException {
        this.filter = filter;
        if (resources.containsKey(classpath)) {
            data = resources.get(classpath);
        } else {
            data = new TextureData(classpath);
            resources.put(classpath, data);
        }
        width = data.getWidth();
        height = data.getHeight();
        this.target = GL_TEXTURE_2D;
        this.internalFormat = GL_RGBA;
        init();
    }

    public Texture(int w, int h, int[] pixels) {
        this(w, h, pixels, GL_NEAREST);
    }

    public Texture(int w, int h, int[] pixels, int filter) {
        this(w, h, pixels, filter, GL_TEXTURE_2D);
    }

    public Texture(int w, int h, int[] pixels, int filter, int target) {
        this(w, h, pixels, filter, target, GL_RGBA);
    }

    public Texture(int w, int h, int[] pixels, int filter, int target, int internalFormat) {
        this.width = w;
        this.height = h;
        this.filter = filter;
        this.target = target;
        this.internalFormat = internalFormat;
        data = new TextureData(w, h, pixels);
        init();
    }

    private void init() {
        if (!data.isInit()) {
            data.init(target, filter, internalFormat);
        }
        this.texID = data.getTextureID();
    }

    public void bind() {
        glBindTexture(target, texID);
    }

    public void unbind() {
        glBindTexture(target, 0);
    }

    public void setupRenderTarget(boolean clampUV) {
        if (isRenderTarget)
            return;
        bind();
        if (clampUV) {
            glTexParameterf(target, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            glTexParameterf(target, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        }
        framebuffer = new Framebuffer(width, height, this);
        isRenderTarget = true;
    }

    public void bindAsRenderTarget() {
        if (!isRenderTarget)
            throw new UnsupportedOperationException("Texture hasn't been set up to be a render target");

        framebuffer.bind();
    }

    public boolean isRenderTarget() {
        return isRenderTarget;
    }

    public int getTextureID() {
        return texID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void dispose() {
        // TODO Implement Texture.dispose
        throw new RuntimeException("IDisposable.dispose is not implemented yet");
    }

}
