package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.util.*;

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

    private static final Mat4f bias = new Mat4f().scale(0.5f, 0.5f, 0.5f).mul(new Mat4f().translation(1, 1, 1));
    public static final int SHADOW_MAP_SLOT = 1;

    public RenderEngine(int w, int h) {
        renderTarget = new Texture(w, h, null);
        renderTargetTmp = new Texture(w, h, null);
        this.displayWidth = w;
        this.displayHeight = h;

        glClampColor(GL_CLAMP_READ_COLOR, GL_FALSE);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        //  glEnable(GL_CULL_FACE);
        glEnable(GL32.GL_DEPTH_CLAMP);

        glShadeModel(GL_SMOOTH);

        renderTarget.setupRenderTarget(false);
        renderTargetTmp.setupRenderTarget(false);

        lightMatrix = new Mat4f().scale(0, 0, 0);
        initMatrix = new Mat4f().identity();
        altCamera = new Camera((float) Math.toRadians(90), 16f / 9f, -1f, 10000f);
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
            Texture shadowMap = new Texture(size, size, null, GL_LINEAR);
            Texture shadowMapTmp = new Texture(size, size, null, GL_LINEAR);
            shadowMap.setupRenderTarget(true);
            shadowMapTmp.setupRenderTarget(true);

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
        int lightCount = Math.max(1, level.getLights().size() + 1);
        renderToTextShader.bind();
        renderToTextShader.getUniform("lightNumber").setValuei(lightCount);

        ambientShader.bind();
        ambientShader.getUniform("ambientColor").setValue3f(ambientColor);

        renderTarget.bindAsRenderTarget();

        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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

            currentShadowMap.bindAsRenderTarget();
            bindTexture(currentShadowMap, SHADOW_MAP_SLOT);
            glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            setLightMatrix(initMatrix);
            if (shadowingData != null) {
                shadowLightBleedingReduction = shadowingData.getLightBleedingReduction();
                shadowVarianceMin = shadowingData.getVarianceMin();
                shadowTexelSize = new Vec3f(1.0f / (float) ShadowMapSize.values()[mapIndex].size(), 1.0f / (float) ShadowMapSize.values()[mapIndex].size(), 0f);
                altCamera.setProjection(shadowingData.getProjectionMatrix());
                altCamera.getTransform().set(l.getTransform());

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

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glDepthMask(false);
            glDepthFunc(GL_EQUAL);
            if (l.getShader() != null) {
                renderObjects(l.getShader(), level, delta, renderCamera);
            }
            glDepthFunc(GL_LESS);
            glDepthMask(true);
            glDisable(GL_BLEND);
        }

        applyFilter(renderToTextShader, renderTarget, renderTargetTmp);
        applyFilter(nullFilterShader, renderTargetTmp, renderTarget);

        glEnable(GL_BLEND);
        GL14.glBlendFuncSeparate(GL_SRC_ALPHA, GL_SRC_ALPHA, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        applyFilter(nullFilterShader, renderTarget, null);

        //        if (currentShadowMap != null) {
        //            applyFilter(nullFilterShader, currentShadowMap, null);
        //        }
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
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
        text.bind();
    }

    private void setLightMatrix(Mat4f mat) {
        lightMatrix = mat;
    }

    public void setCurrentShader(Shader shader) {
        currentShader = shader;
        shader.bind();
        if (currentCamera != null) {
            shader.getUniform("projection").setValueMat4(currentCamera.getViewProjection());
            shader.getUniform("modelview").setValueMat4(initMatrix);
        }
        shader.update(this);
    }

    private void renderObjects(Shader shader, Level level, double delta, Camera camera) {
        setCurrentCamera(camera);
        setCurrentShader(shader);
        for (GameObject o : level.getGameObjects()) {
            currentShader.getUniform("modelview").setValueMat4(o.getTransform().getTransformationMatrix());
            o.render(delta);
        }
    }

    public void setCurrentCamera(Camera camera) {
        currentCamera = camera;
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
}
