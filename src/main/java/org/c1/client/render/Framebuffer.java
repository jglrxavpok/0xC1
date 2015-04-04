package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.c1.client.*;
import org.lwjgl.*;

public class Framebuffer {

    private int width;
    private int height;
    private Texture colorBuffer;
    private int depthBuffer;
    private int framebufferId;

    public Framebuffer(int w, int h) {
        this(w, h, null);
    }

    public Framebuffer(int width, int height, Texture texture) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width (" + width + ") or height(" + height + ")" + " is null or negative");
        }
        this.width = width;
        this.height = height;
        if (texture == null) {
            init();
        } else {
            init(texture);
        }
    }

    private void init() {
        colorBuffer = new Texture(width, height, null);
        init(colorBuffer);
    }

    private void init(Texture colorBuffer) {
        glBindTexture(GL_TEXTURE_2D, 0);
        framebufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBuffer.getTextureID(), 0);

        depthBuffer = glGenRenderbuffers();
        OpenGLUtils.printIfError("initing");
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        int max = glGetInteger(GL_MAX_RENDERBUFFER_SIZE);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, Math.min(width, max), Math.min(height, max));
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        OpenGLUtils.printIfError("initing2");
        glDrawBuffers((IntBuffer) BufferUtils.createIntBuffer(2).put(GL_COLOR_ATTACHMENT0).put(GL_NONE).flip());

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (status != GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("Framebuffer could not be created, status code: " + status);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
        glViewport(0,0,width,height);
    }

    public Texture getColorBuffer() {
        return colorBuffer;
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
