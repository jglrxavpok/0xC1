package org.c1;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.c1.client.*;
import org.c1.client.gui.*;
import org.c1.client.model.ModelCube;
import org.c1.client.render.*;
import org.c1.level.*;
import org.c1.level.lights.*;
import org.c1.maths.*;
import org.c1.tests.*;
import org.c1.utils.*;
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
    private ModelCube modelCube;
    private Gui newGui;
    private Gui currentGui;

    private FontRenderer font;
    private FontRenderer computerFont;

    private boolean isDebugEnabled;
    private List<String> loadingScreenLines;
    private static C1Game instance;

    public void start() {
        instance = this;
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

        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        initGame();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1_000_000_000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        running = true;
        while (running) { // Thanks to TheCherno for the code of this loop, check him out on GitHub, he does a lot of cool stuff
            long now = System.nanoTime();
            boolean polledInput = false;
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1.0) {
                double deltaTime = ns / 1_000_000_000.0;
                if (!polledInput) {
                    pollEvents(deltaTime);
                    polledInput = true;
                }
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
                Display.setTitle("0xC1 " + getVersion() + "- " + fps + " fps");
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
            isDebugEnabled = true;
            loadingScreenLines = Lists.newArrayList();
            prepareLoadingScreen();
            font = new FontRenderer(new TextureAtlas("textures/font.png", 16, 16));
            computerFont = new FontRenderer(new TextureAtlas("textures/font.png", 16, 16)); // TODO: Custom font for the computers
            shader = new Shader("shaders/blit");
            shader.bind();
            shader.getUniform("modelview").setValueMat4(new Mat4f().identity());

            updateLoadingScreen("Loading render engine");
            renderEngine = new RenderEngine(displayWidth, displayHeight);
            renderEngine.setAmbientColor(new Vec3f(0.5f, 0.5f, 0.5f));
            updateLoadingScreen();

            updateLoadingScreen("Loading camera");
            Mat4f projection = new Mat4f().perspective((float) (Math.toRadians(90)), 16f / 9f, 0.001f, 100000f);
            shader.getUniform("projection").setValueMat4(projection);
            player = new PlayerController(projection);
            camera = player.getCamera();
            updateLoadingScreen();

            updateLoadingScreen("Loading texture");
            model = new TestModel();
            modelCube = new ModelCube(new Vec3f(-1,-1,-1), new Vec3f(2,2,2));
            texture = new Texture("textures/logo.png");
            updateLoadingScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateLoadingScreen("Loading level");
        level = new Level();

        GameObject testObject = new TestObject(texture, model);
        testObject.setPos(new Vec3f(0, 0, 10f));

        GameObject testObject2 = new TestObject(texture, model);
        testObject2.setPos(new Vec3f(0.5f, 0, 11f));

        GameObject testObject3 = new TestObject(texture, model);
        testObject3.setPos(new Vec3f(0.5f, 0, 10.5f));

        GameObject collideableObject = new TestCollideableObject(modelCube, texture);
        collideableObject.setPos(new Vec3f(5.0f, 0, 5.0f));
        level.addGameObject(testObject);
        level.addGameObject(testObject2);
        level.addGameObject(testObject3);
        level.addGameObject(collideableObject);
        level.addGameObject(player);

        light = new SpotLight(new Vec3f(10, 0, 0), 0.8f, new Vec3f(1f, 1f, 0.0005f), (float) Math.toRadians(90));
        level.addLight(light);

        level.update(0);
        updateLoadingScreen();

        updateLoadingScreen("Loading ship editor");
        openGui(new GuiDebug(this));
        updateLoadingScreen();
    }

    private void prepareLoadingScreen() {}

    private void updateLoadingScreen() {
        String oldLine = loadingScreenLines.get(loadingScreenLines.size() - 1);
        String newLine = oldLine + " done";
        loadingScreenLines.set(loadingScreenLines.size() - 1, newLine);
        refreshLoadingScreen();
    }

    private void updateLoadingScreen(String line) {
        loadingScreenLines.add(line + "...");
        refreshLoadingScreen();
    }

    private void refreshLoadingScreen() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(0, 0, 0.75f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        shader.bind();
        shader.getUniform("modelview").setValueMat4(new Mat4f().identity());
        float h = 250f;
        shader.getUniform("projection").setValueMat4(new Mat4f().orthographic(0, h * (16f / 9f), 0, h, -1, 1f));
        for (int i = 0; i < loadingScreenLines.size(); i++) {
            String line = loadingScreenLines.get(i);
            float x = 2;
            float y = (loadingScreenLines.size() - i - 1) * font.getCharHeight('A');
            computerFont.renderString(line, x, y, 0xFFFFFFFF);
        }
        Display.update();
    }

    private void pollEvents(double deltaTime) {

        int dx = Mouse.getDX();
        int dy = Mouse.getDY();

        if(currentGui == null || currentGui.locksMouse())
            player.mouseInput((float) dx * 0.005f, -(float) dy * 0.005f);

        if (currentGui != null) {
            if (currentGui.locksMouse() && !Mouse.isGrabbed()) {
                Mouse.setGrabbed(true);
            }
            if (!currentGui.locksMouse() && Mouse.isGrabbed()) {
                Mouse.setGrabbed(false);
            }
        }
        else if (!Mouse.isGrabbed())
            Mouse.setGrabbed(true);
        // Keyboard input

        //Move forward
        float speed = (float) (deltaTime * 5f);
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            player.walkForward(speed, deltaTime);
        }

        //Move backward
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            player.walkBackwards(speed, deltaTime);
        }

        //Move right
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            player.walkRight(speed, deltaTime);
        }

        //Move left
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            player.walkLeft(speed, deltaTime);
        }

        while (Keyboard.next()) {
            boolean pressed = Keyboard.getEventKeyState();
            int keycode = Keyboard.getEventKey();
            char eventchar = Keyboard.getEventCharacter();

            if (currentGui != null) {
                if (pressed)
                    currentGui.onKeyPressed(keycode, eventchar);
                else
                    currentGui.onKeyReleased(keycode, eventchar);
            }
            if (keycode == Keyboard.KEY_F1 && !pressed) {
                if (!isDebugEnabled) {
                    openGui(new GuiDebug(this));
                    isDebugEnabled = true;
                } else {
                    openGui(new GuiShipEditor(this));
                    isDebugEnabled = false;
                }
            }
        }

        while (Mouse.next()) {
            int button = Mouse.getEventButton();
            int x = Mouse.getEventX();
            int y = Mouse.getEventY();
            dx = Mouse.getEventDX();
            dy = Mouse.getEventDY();
            int dwheel = Mouse.getEventDWheel();
            boolean pressedButton = Mouse.getEventButtonState();

            if (currentGui != null) {
                if (button != -1) {
                    if (pressedButton) {
                        currentGui.onMousePressed(x, y, button);
                    } else {
                        currentGui.onMouseReleased(x, y, button);
                    }
                } else {
                    if (dwheel != 0) {
                        currentGui.onScroll(x, y, (int) Math.signum(dwheel));
                    } else {
                        currentGui.onMouseMoved(x, y, dx, dy);
                    }
                }
            }
        }
    }

    private void update(double deltaTime) {
        if (newGui != currentGui) {
            currentGui = newGui;
            if (currentGui != null) {
                currentGui.clearComponents();
                currentGui.init();
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            light.setRotation(player.getCamera().getRotation());
            light.setPos(player.getCamera().getPos());
        }
        level.update(deltaTime);

        if (currentGui != null)
            currentGui.update(deltaTime);
    }

    private void render(double deltaTime) {
        renderEngine.enableTextures();

        renderEngine.clearColorBuffer(0f, 0f, 1f, 1f);

        renderEngine.clearDepth();
        renderEngine.enableDepthTesting();

        renderEngine.renderLevel(level, deltaTime, camera);

        renderEngine.clearDepth();
        renderEngine.enableAlphaBlending();
        if (currentGui != null)
            renderEngine.renderGui(currentGui, deltaTime);
    }

    public void openGui(Gui newGui) {
        this.newGui = newGui;
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

    public FontRenderer getFont() {
        return font;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public PlayerController getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public static C1Game getInstance() {
        return instance;
    }

    public static String getVersion() {
        return "0xC1:BuildNumber";
    }
}
