package org.c1;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import org.c1.client.render.*;
import org.c1.maths.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.slf4j.*;

public class C1Game {

    private int fps;
    private boolean running;
    private Logger logger;
    private File gameFolder;
    private Texture texture;
    private Shader shader;
    private VertexArray vertexArray;

    public void start() {
        try {
            float ratio = 16f / 9f;
            int width = 940;
            LWJGLSetup.load(new File(getGameFolder(), "natives"));
            Display.setDisplayMode(new DisplayMode(width, (int) (width / ratio)));
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
            shader.getUniform("projection").setValueMat4(new Mat4f().orthographic(0, 940f, 0, 400f, -1, 1f));
            vertexArray = new VertexArray();
            vertexArray.addVertex(new Vec3f(0, 0, 0), new Vec2f(0, 0), new Vec3f(0, 0, 1));
            vertexArray.addVertex(new Vec3f(940, 0, 0), new Vec2f(1, 0), new Vec3f(0, 0, 1));
            vertexArray.addVertex(new Vec3f(940, 400, 0), new Vec2f(1, 1), new Vec3f(0, 0, 1));
            vertexArray.addVertex(new Vec3f(0, 400, 0), new Vec2f(0, 1), new Vec3f(0, 0, 1));

            vertexArray.addIndex(0);
            vertexArray.addIndex(1);
            vertexArray.addIndex(2);

            vertexArray.addIndex(2);
            vertexArray.addIndex(0);
            vertexArray.addIndex(3);
            vertexArray.upload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pollEvents() {
        // TODO Implement

    }

    private void update(double deltaTime) {
        // TODO Implement

    }

    private void render(double deltaTime) {
        // TODO Implement
        glClearColor(1, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        glClear(GL_DEPTH_BUFFER_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_TEXTURE_2D);

        glColor4f(1, 1, 1, 1);
        shader.bind();

        texture.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        vertexArray.bind();

        vertexArray.render();
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
