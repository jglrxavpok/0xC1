package org.c1;

import java.io.*;

import org.c1.client.*;
import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.level.lights.*;
import org.c1.maths.*;
import org.c1.tests.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

public class C1Game {

    private int fps;
    private boolean running;
    private Logger logger;
    private File gameFolder;
    private Texture texture;
    private Shader shader;
    private RenderEngine renderEngine;
    private int displayWidth;
    private int displayHeight;
    private Level level;
    private Camera camera;
    private PlayerController player;
    private PointLight light;
    private TestModel model;

    public void start() {
        try {
            float ratio = 16f / 9f;
            displayWidth = 940;
            displayHeight = (int) (displayWidth / ratio);
            LWJGLSetup.load(new File(getGameFolder(), "natives"));
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            Display.create();
        } catch (LWJGLException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        logger = LoggerFactory.getLogger("0xC1");

        initGame();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1_000_000_000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        running = true;
        while (running) // Thanks to TheCherno for the code of this loop,
                        // check him out on GitHub, he does a lot of cool
                        // stuff
        {
            long now = System.nanoTime();
            boolean polledInput = false;
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                if (!polledInput) {
                    pollEvents();
                    polledInput = true;
                }
                double deltaTime = ns / 1_000_000_000.0;
                update(deltaTime);
                updates++;
                delta--;
            }
            render(ns / 1_000_000_000.0);
            Display.update();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                logger.trace(updates + " ups, " + frames + " fps");
                Display.setTitle("0xC1 - " + fps + " fps");
                fps = frames;
                updates = 0;
                frames = 0;
            }

            if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                running = false;
        }

        Display.destroy();
    }

    private void initGame() {
        try {
            texture = new Texture("textures/logo.png");
            shader = new Shader("shaders/blit");
            shader.bind();
            shader.getUniform("modelview").setValueMat4(new Mat4f().identity());
            Mat4f projection = new Mat4f().perspective((float) (Math.toRadians(90)), 16f / 9f, 0.001f, 100000f);
            shader.getUniform("projection").setValueMat4(projection);
            player = new PlayerController(projection);
            camera = player.getCamera();
            model = new TestModel();
            renderEngine = new RenderEngine(displayWidth, displayHeight);
            renderEngine.setAmbientColor(new Vec3f(0.5f, 0.5f, 0.5f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        level = new Level();

        GameObject testObject = new TestObject(texture, model);
        testObject.setPos(new Vec3f(0, 0, 10f));

        GameObject testObject2 = new TestObject(texture, model);
        testObject2.setPos(new Vec3f(0.5f, 0, 11f));

        GameObject testObject3 = new TestObject(texture, model);
        testObject3.setPos(new Vec3f(0.5f, 0, 10.5f));
        level.addGameObject(testObject);
        level.addGameObject(testObject2);
        level.addGameObject(testObject3);

        light = new SpotLight(new Vec3f(10, 0, 0), 0.8f, new Vec3f(1f, 1f, 0.0005f), (float) Math.toRadians(90));
        level.addLight(light);

        level.update(0);

    }

    float dx = 0.0f;
    float dy = 0.0f;

    private void pollEvents() {

        dx = Mouse.getDX();
        dy = Mouse.getDY();

        player.mouseInput(dx * 0.005f, -dy * 0.005f);
        Vec3f translationForward = player.playerCam.getRotation().forward();
        Vec3f translationRight = player.playerCam.getRotation().right();
        translationForward.div(2);
        translationRight.div(2);

        Mouse.setGrabbed(true);
        // Keyboard input

        //Move forward
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            player.getTransform().translate(translationForward);
            player.playerCam.getTransform().translate(translationForward);
        }

        //Move backward
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            translationForward.mul(-1);
            player.getTransform().translate(translationForward);
            player.playerCam.getTransform().translate(translationForward);
        }

        //Move right
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            player.getTransform().translate(translationRight);
            player.playerCam.getTransform().translate(translationRight);
        }

        //Move left
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            translationRight.mul(-1);
            player.getTransform().translate(translationRight);
            player.playerCam.getTransform().translate(translationRight);
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                // int key = Keyboard.getEventKey();
                //
                // if (key == Keyboard.KEY_Z) {
                // player.getTransform().translate(translation);
                // player.playerCam.getTransform().translate(translation);
                // } else if (key == Keyboard.KEY_S) {
                // translation.mul(-1);
                // player.getTransform().translate(translation);
                // player.playerCam.getTransform().translate(translation);
                // } else if (key == Keyboard.KEY_ESCAPE) {
                // this.running = false;
                // }
            }
        }

    }

    private void update(double deltaTime) {
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            light.setRotation(player.playerCam.getRotation());
            light.setPos(player.playerCam.getPos());
        }
        level.update(deltaTime);
    }

    private void render(double deltaTime) {
        renderEngine.clearColorBuffer(0f, 0f, 1f, 1f);

        renderEngine.clearDepth();

        renderEngine.enableAlphaBlending();
        renderEngine.enableDepthTesting();

        renderEngine.enableTextures();

        renderEngine.renderLevel(level, deltaTime, camera);
    }

    public File getGameFolder() {
        if (gameFolder == null) {
            String appdata = System.getenv("APPDATA");
            if (appdata == null)
                gameFolder = new File(System.getProperty("user.home"), "0xC1");
            else
                gameFolder = new File(appdata, "0xC1");
            if (!gameFolder.exists())
                gameFolder.mkdirs();
        }
        return gameFolder;
    }

    public static void main(String[] args) {
        C1Game game = new C1Game();
        game.start();
    }

}
