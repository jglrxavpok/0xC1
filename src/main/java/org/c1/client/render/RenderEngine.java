package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.util.*;

import org.c1.client.gui.*;
import org.c1.level.*;
import org.c1.level.lights.*;
import org.c1.maths.*;
import org.lwjgl.opengl.*;

public class RenderEngine {

    private Vec3f ambientColor;
    private int displayWidth;
    private int displayHeight;
    private Texture renderTarget;
    private Shader ambientShader;
    private Texture[] shadowMaps;
    private Texture[] shadowMapsTmp;
    private Texture currentShadowMap;
    private Texture currentTmpShadowMap;
    private Shader currentShader;
    private float shadowLightBleedingReduction;
    private float shadowVarianceMin;
    private Mat4f initMatrix;
    private Vec3f shadowTexelSize;
    private Camera altCamera;
    private Camera currentCamera;
    private Shader shadowMapShader;
    private Texture renderTargetTmp;
    private Texture filterTexture;
    private Mat4f lightMatrix;
    private Shader renderToTextShader;
    private Shader nullFilterShader;
    private VertexArray planeObject;
    private Light activeLight;
    private Camera guiCam;
    private Mat4f modelview;

    private static final Mat4f bias = new Mat4f().scale(0.5f, 0.5f, 0.5f).mul(new Mat4f().translation(1, 1, 1));
    public static final int SHADOW_MAP_SLOT = 1;

    public RenderEngine(int w, int h) {
        renderTarget = new Texture(w, h, null, GL_LINEAR);
        renderTargetTmp = new Texture(w, h, null, GL_LINEAR);

        this.displayWidth = w;
        this.displayHeight = h;

        glClampColor(GL_CLAMP_READ_COLOR, GL_FALSE);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL32.GL_DEPTH_CLAMP);

        glShadeModel(GL_SMOOTH);

        renderTarget.setupRenderTarget(false);
        renderTargetTmp.setupRenderTarget(false);

        lightMatrix = new Mat4f().scale(0, 0, 0);
        initMatrix = new Mat4f().identity();
        altCamera = new Camera((float) Math.toRadians(90), 16f / 9f, 0.001f, 10000f);
        guiCam = new Camera(new Mat4f().orthographic(0, w, 0, h, -1f, 1f));
        ambientColor = new Vec3f(0.5f, 0.5f, 0.5f);
        loadShaders();
        initShadowMaps();

    }

    private void initShadowMaps() {
        shadowMaps = new Texture[ShadowMapSize.values().length];
        shadowMapsTmp = new Texture[shadowMaps.length];

        for (int i = 0; i < shadowMaps.length; i++) {
            ShadowMapSize mapSize = ShadowMapSize.values()[i];
            int size = mapSize.size();
            Texture shadowMap = new Texture(size, size, null, GL_LINEAR, GL_TEXTURE_2D, GL_RGBA,GL_RG32F);
            Texture shadowMapTmp = new Texture(size, size, null, GL_LINEAR, GL_TEXTURE_2D, GL_RGBA,GL_RG32F);
            shadowMap.setupRenderTarget(false);
            shadowMapTmp.setupRenderTarget(false);

            shadowMaps[i] = shadowMap;
            shadowMapsTmp[i] = shadowMapTmp;
        }
    }

    private void loadShaders() {
        try {
            ambientShader = new Shader("shaders/blit.vsh", "shaders/lights/ambient.fsh");
            shadowMapShader = new Shader("shaders/blit.vsh", "shaders/lights/shadowMap.fsh");
            renderToTextShader = new Shader("shaders/blit.vsh", "shaders/renderToText.fsh");
            nullFilterShader = new Shader("shaders/blit");

            planeObject = new VertexArray();
            planeObject.addIndex(1);
            planeObject.addIndex(0);
            planeObject.addIndex(2);

            planeObject.addIndex(2);
            planeObject.addIndex(0);
            planeObject.addIndex(3);

            float left = -1f;
            float right = 1f;
            float top = 1f;
            float bottom = -1f;
            planeObject.addVertex(new Vec3f(left, bottom, 0), new Vec2f(0, 0), new Vec3f(0, 0, -1));
            planeObject.addVertex(new Vec3f(right, bottom, 0), new Vec2f(1, 0), new Vec3f(0, 0, -1));
            planeObject.addVertex(new Vec3f(right, top, 0), new Vec2f(1, 1), new Vec3f(0, 0, -1));
            planeObject.addVertex(new Vec3f(left, top, 0), new Vec2f(0, 1), new Vec3f(0, 0, -1));
            planeObject.upload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAmbientColor(Vec3f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vec3f getAmbientColor() {
        return ambientColor;
    }

    public void renderLevel(Level level, double delta, Camera renderCamera) {
        enableAlphaBlending();
        int lightCount = Math.max(1, level.getLights().size() + 1);
        renderToTextShader.bind();
        renderToTextShader.getUniform("lightNumber").setValuei(lightCount);

        ambientShader.bind();
        ambientShader.getUniform("ambientColor").setValue3f(ambientColor);

        renderTarget.bindAsRenderTarget();

        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        glClear(GL_DEPTH_BUFFER_BIT);

        renderObjects(ambientShader, level, delta, renderCamera);
        List<Light> lights = level.getLights();

        for (Light l : lights) {
            if (!l.isActive())
                continue;
            activeLight = l;

            int mapIndex = 0;
            ShadowingData shadowingData = l.getShadowingData();
            if (shadowingData != null) {
                mapIndex = shadowingData.getShadowMapSize().ordinal();
            }

            currentShadowMap = shadowMaps[mapIndex];
            currentTmpShadowMap = shadowMapsTmp[mapIndex];

            setLightMatrix(initMatrix);
            currentShadowMap.bindAsRenderTarget();
            glViewport(0,0,currentShadowMap.getWidth(),currentShadowMap.getHeight());
            glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (shadowingData != null) {
                shadowLightBleedingReduction = shadowingData.getLightBleedingReduction();
                shadowVarianceMin = shadowingData.getVarianceMin();
                shadowTexelSize = new Vec3f(1.0f / (float) ShadowMapSize.values()[mapIndex].size(), 1.0f / (float) ShadowMapSize.values()[mapIndex].size(), 0f);
                altCamera.setProjection(shadowingData.getProjectionMatrix());
                altCamera.getTransform().pos(l.getPos());
                altCamera.getTransform().rot(l.getRotation());

                setLightMatrix(bias.mul(altCamera.getViewProjection()));

                if (shadowingData.flipsCullFace())
                    glCullFace(GL_FRONT);
                renderObjects(shadowMapShader, level, delta, altCamera);
                if (shadowingData.flipsCullFace())
                    glCullFace(GL_BACK);
            } else {
                setLightMatrix(new Mat4f().scale(0, 0, 0));

                shadowLightBleedingReduction = 0f;
                shadowVarianceMin = 0.0002f;
            }
            renderTarget.bindAsRenderTarget();
            glViewport(0, 0, renderTarget.getWidth(), renderTarget.getHeight());

            bindTexture(currentShadowMap, SHADOW_MAP_SLOT);
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glDepthFunc(GL_EQUAL);
            if (l.getShader() != null) {
                renderObjects(l.getShader(), level, delta, renderCamera);
            }
            glDepthFunc(GL_LESS);
            glDisable(GL_BLEND);
        }

        applyFilter(renderToTextShader, renderTarget, renderTargetTmp);
        applyFilter(nullFilterShader, renderTargetTmp, renderTarget);

        applyFilter(nullFilterShader, renderTarget, null);

        if (currentShadowMap != null) {
//            applyFilter(nullFilterShader, currentShadowMap, null);
        }
    }

    public void applyFilter(Shader filter, Texture source, Texture dest) {
        if (dest != null)
            dest.bindAsRenderTarget();
        else
            glBindFramebuffer(GL_FRAMEBUFFER, 0);

        altCamera.getTransform().pos(Vec3f.NULL.copy());
        altCamera.getTransform().rot(Quaternion.NULL.copy());
        altCamera.setProjection(initMatrix);
        glClear(GL_DEPTH_BUFFER_BIT);

        setCurrentCamera(altCamera);
        setCurrentShader(filter);
        bindTexture(source);
        planeObject.bind();
        planeObject.render();
    }

    private void bindTexture(Texture text) {
        bindTexture(text, 0);
    }

    private void bindTexture(Texture text, int slot) {
        text.bind(slot);
    }

    private void setLightMatrix(Mat4f mat) {
        lightMatrix.set(mat);
    }

    public void setCurrentShader(Shader shader) {
        currentShader = shader;
        shader.bind();
        updateShader();
    }

    private void renderObjects(Shader shader, Level level, double delta, Camera camera) {
        setCurrentCamera(camera);
        setCurrentShader(shader);
        level.getGameObjects().forEach(object -> {
            setModelview(object.getTransform().getTransformationMatrix());
            object.render(delta);
        });
    }

    public void setCurrentCamera(Camera camera) {
        currentCamera = camera;
        updateShader();
    }

    public float getShadowLightBleedingReduction() {
        return shadowLightBleedingReduction;
    }

    public float getShadowVarianceMin() {
        return shadowVarianceMin;
    }

    public Light getLight() {
        return activeLight;
    }

    public Mat4f getLightMatrix() {
        return lightMatrix;
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }

    public void clearColorBuffer(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void clearDepth() {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public void enableAlphaBlending() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void disableBlending() {
        glDisable(GL_BLEND);
    }

    public void enableDepthTesting() {
        glEnable(GL_DEPTH_TEST);
    }

    public void disableDepthTesting() {
        glDisable(GL_DEPTH_TEST);
    }

    public void enableTextures() {
        glEnable(GL_TEXTURE_2D);
    }

    public Vec3f getShadowTexelSize() {
        return shadowTexelSize;
    }

    public void renderGui(Gui gui, double deltaTime) {
        setCurrentCamera(guiCam);
        setCurrentShader(nullFilterShader);
        setModelview(initMatrix);
        gui.render(deltaTime);
    }

    public void updateShader() {
        if (currentShader == null)
            return;
        currentShader.bind();
        if (currentCamera != null) {
            currentShader.getUniform("projection").setValueMat4(currentCamera.getViewProjection());
            setModelview(initMatrix);
        }
        currentShader.update(this);
    }

    public void setModelview(Mat4f mat) {
        if (currentShader != null) {
            currentShader.getUniform("modelview").setValueMat4(mat);
        }
        this.modelview = mat;
    }

    public Mat4f getModelview() {
        return modelview;
    }

}
