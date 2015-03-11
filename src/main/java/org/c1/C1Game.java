package org.c1;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.File;
import java.io.IOException;

import org.c1.client.PlayerController;
import org.c1.client.render.RenderEngine;
import org.c1.client.render.Shader;
import org.c1.client.render.Texture;
import org.c1.client.render.VertexArray;
import org.c1.level.Camera;
import org.c1.level.GameObject;
import org.c1.level.Level;
import org.c1.level.lights.PointLight;
import org.c1.maths.Mat4f;
import org.c1.maths.Vec2f;
import org.c1.maths.Vec3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C1Game {

    private int fps;
    private boolean running;
    private Logger logger;
    private File gameFolder;
    private Texture texture;
    private Shader shader;
    private VertexArray vertexArray;
    private RenderEngine renderEngine;
    private int displayWidth;
    private int displayHeight;
    private Level level;
    private Camera camera;
    private PlayerController player;

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

            if (Display.isCloseRequested())
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
            Mat4f projection = new Mat4f().orthographic(-1f, 1f, -1f, 1f, -1f, 1f);
            shader.getUniform("projection").setValueMat4(projection);
            player = new PlayerController(projection);
            camera = player.getCamera();
            vertexArray = new VertexArray();
            vertexArray.addVertex(new Vec3f(-1f, -1f, 0), new Vec2f(0, 0), new Vec3f(0, 0, 1));
            vertexArray.addVertex(new Vec3f(1f, -1f, 0), new Vec2f(1, 0), new Vec3f(0, 0, 1));
            vertexArray.addVertex(new Vec3f(1f, 1f, 0), new Vec2f(1, 1), new Vec3f(0, 0, 1));
            vertexArray.addVertex(new Vec3f(-1f, 1f, 0), new Vec2f(0, 1), new Vec3f(0, 0, 1));

            vertexArray.addIndex(1);
            vertexArray.addIndex(0);
            vertexArray.addIndex(2);

            vertexArray.addIndex(2);
            vertexArray.addIndex(0);
            vertexArray.addIndex(3);
            vertexArray.upload();

            renderEngine = new RenderEngine(displayWidth, displayHeight);
            renderEngine.setAmbientColor(new Vec3f(0.5f, 0.5f, 0.5f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        level = new Level();

        GameObject testObject = new GameObject() {

            @Override
            public void update(double delta) {
                ;
            }

            @Override
            public boolean shouldDie() {
                return false;
            }

            @Override
            public void render(double delta) {
                texture.bind();
                vertexArray.bind();
                vertexArray.render();
            }
        };
        level.addGameObject(testObject);
        level.update(0);

        PointLight light = new PointLight(new Vec3f(1, 0, 0), 9.8f, new Vec3f(0, 0, 0.005f));

        light.getTransform().pos(0, 0, 0.1f);
        level.addLight(light);
        light.setActive(true);
    }
    
    
    float dx        = 0.0f;
    float dy        = 0.0f;
    
    private void pollEvents() {

    	dx = Mouse.getDX();
    	dy = Mouse.getDY();
    	
    	player.mouseInput(dy * 0.05f, dx * 0.05f);
    	
    	//Keyboard input
    	while (Keyboard.next()) {
            if(Keyboard.getEventKeyState()){
            	if(Keyboard.getEventKey() == Keyboard.KEY_Z){
            		float moveX = 0, moveZ = 0;
            		moveX -= 3 * (float)Math.sin(Math.toRadians(player.getTransform().rot().x()));
            		moveZ += 3 * (float)Math.cos(Math.toRadians(player.getTransform().rot().x()));
            		player.getTransform().translate(moveX, 0, moveZ);
            		player.playerCam.getTransform().translate(moveX, 0, moveZ);
            	}
            	if(Keyboard.getEventKey() == Keyboard.KEY_S){
            		float moveX = 0, moveZ = 0;
            		moveX += 3 * (float)Math.sin(Math.toRadians(player.getTransform().rot().x()));
            		moveZ -= 3 * (float)Math.cos(Math.toRadians(player.getTransform().rot().x()));
            		player.getTransform().translate(moveX, 0, moveZ);
            		player.playerCam.getTransform().translate(moveX, 0, moveZ);
            	}
            }
            
        }
        

    	
    }

    private void update(double deltaTime) {
        // TODO Implement
    	level.update(deltaTime);
    }

    private void render(double deltaTime) {
        // TODO Implement
        glClearColor(1, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        glClear(GL_DEPTH_BUFFER_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_TEXTURE_2D);

        glColor4f(1, 1, 1, 1);

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
