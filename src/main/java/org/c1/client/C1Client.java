package org.c1.client;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import org.c1.*;
import org.c1.client.gui.*;
import org.c1.client.render.*;
import org.c1.client.render.entity.*;
import org.c1.client.render.fonts.*;
import org.c1.client.sound.*;
import org.c1.entity.*;
import org.c1.level.*;
import org.c1.maths.*;
import org.c1.resources.*;
import org.c1.sound.*;
import org.c1.utils.*;
import org.c1.utils.CollisionInfos.CollisionType;
import org.c1.utils.Log.NonLoggable;
import org.c1.utils.crash.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.*;

public class C1Client implements Runnable, C1Instance
{

    private int                      displayWidth                = 960;
    private int                      displayHeight               = 540;
    private boolean                  running                     = true;
    private RenderEngine             renderEngine                = null;
    private AssetLoader              assetsLoader;
    private MouseHandler             mouseHandler;
    private static C1Client          instance;
    private CollisionInfos           objectInFront               = null;
    private OpenGLBuffer             crosshairBuffer;
    private ResourceLocation         crosshairLocation;
    private Runtime                  runtime;
    private FontRenderer             fontRenderer;
    private String                   username;

    private Gui                      currentMenu;
    private Gui                      newMenu;
    private DiskSimpleResourceLoader gameFolderLoader;
    private int                      frame;
    private int                      fps;
    private double                   expectedFrameRate           = 60.0;
    private double                   timeBetweenUpdates          = 1000000000 / expectedFrameRate;
    private final int                maxUpdatesBeforeRender      = 3;
    private double                   lastUpdateTime              = System.nanoTime();
    private double                   lastRenderTime              = System.nanoTime();

    // If we are able to get as high as this FPS, don't render again.
    private final double             TARGET_FPS                  = 60;
    private final double             TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    private int                      lastSecondTime              = (int) (lastUpdateTime / 1000000000);
    private GameSettings             settings;
    private ParticleRenderer         particleRenderer;
    private DirectSoundProducer      sndProducer;
    private boolean                  fullscreen;
    private JFrame                   renderWindow;
    private int                      oldWidth;
    private int                      oldHeight;
    private EntityPlayer             player;
    private PlayerController         playerController;
    private Level                    clientWorld;
    private OpenGLBuffer             planeBuffer;

    public C1Client()
    {
        instance = this;
        this.assetsLoader = new AssetLoader(new ClasspathSimpleResourceLoader("assets"));
        this.gameFolderLoader = new DiskSimpleResourceLoader(SystemUtils.getGameFolder().getAbsolutePath());
        runtime = Runtime.getRuntime();
        crosshairLocation = new ResourceLocation("ourcraft", "textures/crosshair.png");
    }

    public void start(Map<String, String> properties)
    {
        username = properties.get("username");
        I18n.setCurrentLanguage(properties.get("lang"));
        run();
    }

    @Override
    public void run()
    {
        try
        {
            objectInFront = new CollisionInfos();
            objectInFront.type = CollisionType.NONE;
            //LWJGL Properties
            System.setProperty("org.lwjgl.util.Debug", "true");
            System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");

            //Init OpenGL context and settings up the display
            ContextAttribs context = new ContextAttribs(3, 2).withProfileCompatibility(true).withDebug(true);
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            Display.setIcon(new ByteBuffer[]
            {
                    ImageUtils.getPixels(ImageUtils.getFromClasspath("/assets/ourcraft/textures/favicon_128.png")),
                    ImageUtils.getPixels(ImageUtils.getFromClasspath("/assets/ourcraft/textures/favicon_32.png"))
            });
            Display.setResizable(true);
            Display.setTitle("C1Client - " + getVersion());
            try
            {
                Display.create(new PixelFormat(), context);
            }
            catch(LWJGLException e)
            {
                if(e.getMessage().contains("Could not create context"))
                {
                    Log.error("Failing to create LWJGL context, falling back to classic context");
                    Display.create();
                }
            }
            if(!GLContext.getCapabilities().GL_ARB_vertex_buffer_object)
            {
                Log.fatal("Sorry, but this game only works with Vertex Buffer Objects and it seems your graphical card can't support it. :(");
            }

            //Init the RenderEngine
            renderEngine = new RenderEngine(assetsLoader);
            renderEngine.enableGLCap(GL_BLEND);
            renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            renderEngine.switchToOrtho();
            renderEngine.renderSplashScreen();
            Display.update();

            mouseHandler = new MouseHandler();
            //Init OpenGL CapNames for crash report system
            OpenGLHelper.loadCapNames();

            //Init Game Content
            sndProducer = new DirectSoundProducer();
            settings = new GameSettings();
            File propsFile = new File(SystemUtils.getGameFolder(), "properties.txt");
            settings.loadFrom(propsFile);
            settings.saveTo(propsFile);

            AudioRegistry.init();
            if(settings.font.getValue().equals("default"))
                fontRenderer = new BaseFontRenderer();
            else
                fontRenderer = new TrueTypeFontRenderer(settings.font.getValue());

            EntityRegistry.init();
            I18n.init(assetsLoader);
            I18n.setCurrentLanguage(settings.lang.getValue());
            ParticleRegistry.init();
            AbstractRender.registerVanillaRenderers();

            particleRenderer = new ParticleRenderer(20000);
            openMenu(new GuiMainMenu(this));

            loadCrosshairBuffer();

            running = true;
            while(running)
            {
                tick();
            }
            cleanup();
            System.exit(0);
        }
        catch(Exception e)
        {
            crash(new CrashReport(e));
        }
    }

    /**
     * Loads crosshair buffer
     */
    private void loadCrosshairBuffer()
    {
        if(crosshairBuffer != null)
            crosshairBuffer.dispose();
        crosshairBuffer = new OpenGLBuffer();
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(displayWidth / 2 - 8, displayHeight / 2 - 8, 0), Vector2.get(0, 0)));
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(displayWidth / 2 + 8, displayHeight / 2 - 8, 0), Vector2.get(1, 0)));
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(displayWidth / 2 + 8, displayHeight / 2 + 8, 0), Vector2.get(1, 1)));
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(displayWidth / 2 - 8, displayHeight / 2 + 8, 0), Vector2.get(0, 1)));

        crosshairBuffer.addIndex(0);
        crosshairBuffer.addIndex(1);
        crosshairBuffer.addIndex(2);

        crosshairBuffer.addIndex(2);
        crosshairBuffer.addIndex(3);
        crosshairBuffer.addIndex(0);
        crosshairBuffer.upload();
        crosshairBuffer.clearAndDisposeVertices();
    }

    /**
     * Opens given gui
     */
    public void openMenu(Gui gui)
    {
        this.newMenu = gui;
    }

    /**
     * Performs one tick of the game
     */
    private final void tick()
    {
        double now = System.nanoTime();
        int updateCount = 0;
        {
            double delta = timeBetweenUpdates / 1000000000;
            while(now - lastUpdateTime > timeBetweenUpdates && updateCount < maxUpdatesBeforeRender)
            {
                update(delta);
                lastUpdateTime += timeBetweenUpdates;
                updateCount++ ;
            }

            if(now - lastUpdateTime > timeBetweenUpdates)
            {
                lastUpdateTime = now - timeBetweenUpdates;
            }

            render(delta);
            Display.update();

            if(Display.isCloseRequested())
                running = false;

            if(Display.wasResized() && !fullscreen)
            {
                int w = Display.getWidth();
                int h = Display.getHeight();
                setDisplayMode(w, h);
            }

            lastRenderTime = now;
            // Update the frames we got.
            int thisSecond = (int) (lastUpdateTime / 1000000000);
            frame++ ;
            if(thisSecond > lastSecondTime)
            {
                fps = frame;
                Display.setTitle("C1Client - " + getVersion() + " - " + fps + " FPS");
                frame = 0;
                lastSecondTime = thisSecond;
            }

            while(now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < timeBetweenUpdates)
            {
                Thread.yield();

                try
                {
                    Thread.sleep(1);
                }
                catch(Exception e)
                {
                }

                now = System.nanoTime();
            }
        }
    }

    private void setDisplayMode(int w, int h)
    {
        oldWidth = displayWidth;
        oldHeight = displayHeight;
        displayWidth = w;
        displayHeight = h;
        renderEngine.loadMatrices();
        try
        {
            renderEngine.loadShaders();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        loadCrosshairBuffer();
        currentMenu.build();
    }

    /**
     * Update the game (world, menus, etc.)
     */
    private void update(final double delta)
    {
        /*  renderEngine.blocksAndItemsMap.tick();
          particleRenderer.getMap().tick();*/
        sndProducer.setMasterVolume(settings.masterVolume.getValue());
        sndProducer.setSoundVolume(settings.soundVolume.getValue());
        sndProducer.setMusicVolume(settings.musicVolume.getValue());
        if(player != null)
        {
            sndProducer.setListenerLocation(player.posX, player.posY, player.posZ);
            sndProducer.setListenerOrientation(player.getQuaternionRotation());
        }
        if(newMenu != currentMenu)
        {
            currentMenu = newMenu;
            fontRenderer.disposeCache();
            if(currentMenu != null)
            {
                currentMenu.build();
            }
        }
        while(Mouse.next())
        {
            int mouseButton = Mouse.getEventButton();
            boolean state = Mouse.getEventButtonState();
            int x = Mouse.getEventX();
            int y = displayHeight - Mouse.getEventY();
            int dx = Mouse.getEventDX();
            int dy = Mouse.getEventDY();
            int deltaWheel = Mouse.getEventDWheel();
            if(currentMenu != null)
            {
                if(mouseButton != -1)
                {
                    if(state)
                        currentMenu.onButtonPressed(x, y, mouseButton);
                    else
                        currentMenu.onButtonReleased(x, y, mouseButton);
                }

                if(deltaWheel != 0)
                {
                    currentMenu.handleMouseWheelMovement(x, y, deltaWheel);
                }
                else
                {
                    currentMenu.handleMouseMovement(x, y, dx, dy);
                }
            }
            if(playerController != null && (currentMenu == null || !currentMenu.requiresMouse()))
            {
                if(!state)
                {
                    if(mouseButton == 0)
                    {
                        playerController.onLeftClick(getObjectInFront());
                    }
                    else if(mouseButton == 1)
                    {
                        playerController.onRightClick(getObjectInFront());
                    }
                }
                if(deltaWheel != 0)
                {
                    playerController.onMouseWheelMoved(deltaWheel);
                }
            }
        }
        while(Keyboard.next())
        {
            int id = Keyboard.getEventKey();
            char c = Keyboard.getEventCharacter();
            boolean state = Keyboard.getEventKeyState();
            if(currentMenu != null)
            {
                if(!state)
                {
                    currentMenu.keyPressed(id, c);
                    if(id == Keyboard.KEY_F2)
                    {
                        File out = new File(SystemUtils.getGameFolder(), "screenshots/" + System.currentTimeMillis() + ".png");
                        try
                        {
                            if(!out.getParentFile().exists())
                                out.getParentFile().mkdirs();
                            out.createNewFile();
                            ImageIO.write(takeScreenshot(), "png", out);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    if(id == Keyboard.KEY_ESCAPE)
                    {
                        if(clientWorld != null && !(currentMenu instanceof GuiPauseMenu))
                        {
                            openMenu(new GuiPauseMenu(this));
                        }
                        else if(clientWorld == null)
                            running = false;
                    }
                    else if(id == Keyboard.KEY_F11)
                    {
                        toggleFullscreen();
                    }
                    currentMenu.keyReleased(id, c);
                }
            }
        }
        if(currentMenu != null)
        {
            currentMenu.update();
            if(currentMenu.requiresMouse())
                mouseHandler.ungrab();
            else
                mouseHandler.grab();
        }
        else
            mouseHandler.grab();
        mouseHandler.update();
        if(playerController != null && (currentMenu == null || !currentMenu.pausesGame()))
        {
            if(Keyboard.isKeyDown(settings.forwardKey.getValue()))
            {
                playerController.onMoveForwardRequested();
            }
            if(Keyboard.isKeyDown(settings.backwardsKey.getValue()))
            {
                playerController.onMoveBackwardsRequested();
            }
            if(Keyboard.isKeyDown(settings.leftKey.getValue()))
            {
                playerController.onMoveLeftRequested();
            }
            if(Keyboard.isKeyDown(settings.rightKey.getValue()))
            {
                playerController.onMoveRightRequested();
            }
            if(Keyboard.isKeyDown(settings.jumpKey.getValue()))
            {
                playerController.onJumpRequested();
            }
            if(Mouse.isButtonDown(2))
            {
                playerController.onMouseWheelClicked();
            }
            playerController.update();
        }
        if(clientWorld != null)
        {
            if(currentMenu != null && !currentMenu.pausesGame())
            {
                clientWorld.update(delta);
                clientWorld.updateAllParticles();
            }
            else if(currentMenu == null)
            {
                clientWorld.update(delta);
                clientWorld.updateAllParticles();
            }
        }
    }

    private void toggleFullscreen()
    {
        fullscreen = !fullscreen;
        EnumFullscreenType type = settings.fullscreenType.getValue();
        if(fullscreen)
        {
            switch(type)
            {
                case NATIVE:
                    try
                    {
                        if(renderWindow != null)
                        {
                            renderWindow.dispose();
                            renderWindow = null;
                        }
                        DisplayMode mode = Display.getDesktopDisplayMode();
                        setDisplayMode(mode.getWidth(), mode.getHeight());
                        Display.setDisplayModeAndFullscreen(mode);
                        Display.setResizable(true);
                    }
                    catch(LWJGLException e1)
                    {
                        e1.printStackTrace();
                    }
                    break;
                case NO_BORDERS:
                    try
                    {
                        if(renderWindow == null)
                        {
                            renderWindow = new JFrame();
                            renderWindow.setUndecorated(true);
                        }
                        else
                            renderWindow.removeAll();
                        Canvas parent = new Canvas();
                        DisplayMode mode = Display.getDesktopDisplayMode();
                        parent.setPreferredSize(new Dimension(mode.getWidth(), mode.getHeight()));
                        renderWindow.add(parent);
                        renderWindow.pack();
                        renderWindow.setVisible(true);
                        renderWindow.requestFocus();
                        Display.setParent(parent);
                        setDisplayMode(mode.getWidth(), mode.getHeight());
                    }
                    catch(LWJGLException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case BORDERS:
                    try
                    {
                        if(renderWindow != null)
                        {
                            renderWindow.dispose();
                            renderWindow = null;
                        }
                        DisplayMode mode = Display.getDesktopDisplayMode();
                        setDisplayMode(mode.getWidth(), mode.getHeight());
                        Display.setDisplayMode(mode);
                        fullscreen = false;
                    }
                    catch(LWJGLException e1)
                    {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
        else
        {
            try
            {
                Display.setParent(null);
                Display.setFullscreen(false);
                Display.setDisplayMode(new DisplayMode(oldWidth, oldHeight));
                setDisplayMode(oldWidth, oldHeight);
                Display.setResizable(true);
            }
            catch(LWJGLException e)
            {
                e.printStackTrace();
            }
            if(renderWindow != null)
            {
                renderWindow.dispose();
                renderWindow = null;
            }
        }
    }

    /**
     * Renders the game
     */
    private void render(double delta)
    {
        render(delta, true);
    }

    /**
     * Renders the game with or without the gui
     */
    private void render(double delta, boolean drawGui)
    {
        glViewport(0, 0, displayWidth, displayHeight);
        glClearColor(0, 0, 0, 1);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        if(clientWorld != null)
        {
            renderEngine.enableGLCap(GL_DEPTH_TEST);
            renderEngine.switchToPerspective();
            renderEngine.beginWorldRendering();
            particleRenderer.renderAll(renderEngine);
            renderWorld(delta, drawGui);
            if(player != null)
            {
                if(player.getHeldItem() != null && player.getHeldItem().getStackable() != null)
                {
                    renderEngine.enableGLCap(GL_DEPTH_TEST);
                    renderEngine.enableGLCap(GL_ALPHA_TEST);
                    renderEngine.setProjectFromEntity(false);
                    Quaternion q = new Quaternion(Vector3.yAxis, (float) Math.toRadians(75));
                    q = q.mul(new Quaternion(Vector3.xAxis, (float) Math.toRadians(10)));
                    q = q.mul(new Quaternion(Vector3.zAxis, (float) Math.toRadians(5)));
                    float ratio = (float) displayWidth / (float) displayHeight;
                    float d = ratio / (16.f / 9.f);
                    Matrix4 m = Matrix4.get().initTranslation(d * 1.05f, -1.05f, 0.5f);
                    m = m.mul(Matrix4.get().initRotation(q.getForward(), q.getUp()));
                    renderEngine.setModelviewMatrix(m);
                    // TODO: render items
                    renderEngine.setProjectFromEntity(true);
                    m.dispose();
                }
            }
            renderEngine.setModelviewMatrix(Matrix4.get().initIdentity());
            renderEngine.flushWorldRendering();
            renderEngine.enableGLCap(GL_BLEND);
            glClear(GL_DEPTH_BUFFER_BIT);
            renderEngine.disableGLCap(GL_DEPTH_TEST);
        }
        printIfGLError("After world rendering");

        if(drawGui)
        {
            renderEngine.beginGuiRendering();
            if(clientWorld != null)
            {
                renderEngine.enableGLCap(GL_COLOR_LOGIC_OP);
                glLogicOp(GL_XOR);
                renderEngine.bindLocation(crosshairLocation);
                renderEngine.renderBuffer(crosshairBuffer);
                renderEngine.disableGLCap(GL_COLOR_LOGIC_OP);
            }

            int mx = Mouse.getX();
            int my = Mouse.getY();
            if(currentMenu != null)
            {
                currentMenu.render(mx, displayHeight - my, renderEngine);
            }

            renderEngine.flushGuiRendering();

            printIfGLError("After gui rendering");
        }
        printIfGLError("After global rendering");
    }

    private void renderWorld(double delta, boolean drawGui)
    {
        // TODO Implement, DO NOT FORGET THAT WE'LL NEED LIGHTING
        if(planeBuffer == null)
        {
            planeBuffer = new OpenGLBuffer();
            planeBuffer.addIndex(0);
            planeBuffer.addIndex(1);
            planeBuffer.addIndex(2);

            planeBuffer.addIndex(2);
            planeBuffer.addIndex(0);
            planeBuffer.addIndex(3);

            planeBuffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(0, 0)));
            planeBuffer.addVertex(Vertex.get(Vector3.get(100, 0, 0), Vector2.get(1, 0)));
            planeBuffer.addVertex(Vertex.get(Vector3.get(100, 100, 0), Vector2.get(1, 1)));
            planeBuffer.addVertex(Vertex.get(Vector3.get(0, 100, 0), Vector2.get(0, 1)));

            planeBuffer.upload();
            planeBuffer.clearAndDisposeVertices();
        }
        Matrix4 modelview = new Matrix4().initIdentity();
        renderEngine.renderBuffer(planeBuffer);
    }

    /**
     * Print an error log only if OpenGL <code>getError()</code> returns an error
     */
    @NonLoggable
    public static void printIfGLError()
    {
        printIfGLError(null);
    }

    @NonLoggable
    public static void printIfGLError(String trailer)
    {
        int errorFlag = glGetError();
        // If an error has occurred...
        if(errorFlag != GL_NO_ERROR)
        {
            // Log the error.
            Log.error("[GL ERROR] " + GLU.gluErrorString(errorFlag) + (trailer == null ? "" : " " + trailer));
        }
    }

    /**
     * Gets the username used by the player
     */
    public String getClientUsername()
    {
        return username;
    }

    /**
     * Returns the instance of the game
     */
    public static C1Client getClient()
    {
        return instance;
    }

    /**
     * Returns the game's assets loader
     */
    public AssetLoader getAssetsLoader()
    {
        return assetsLoader;
    }

    public MouseHandler getMouseHandler()
    {
        return mouseHandler;
    }

    /**
     * Gets the object in front of the player. Updated each tick
     */
    public CollisionInfos getObjectInFront()
    {
        return objectInFront;
    }

    /**
     * Returns true if the game is currently running
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Returns render engine
     */
    public RenderEngine getRenderEngine()
    {
        return renderEngine;
    }

    /**
     * Returns the version of the game attributed during the build process
     */
    public static String getVersion()
    {
        return "C1Client:BuildNumber";
    }

    /**
     * Gets the display width
     */
    public int getDisplayWidth()
    {
        return displayWidth;
    }

    /**
     * Gets the display height
     */
    public int getDisplayHeight()
    {
        return displayHeight;
    }

    public Runtime getRuntimeInfos()
    {
        return runtime;
    }

    public long getFreeMemory()
    {
        return runtime.freeMemory();
    }

    public long getMaxMemory()
    {
        return runtime.maxMemory();
    }

    public long getTotalMemory()
    {
        return runtime.totalMemory();
    }

    public long getUsedMemory()
    {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Gets the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    /**
     * Sets client player
     */
    public void setPlayer(EntityPlayer player)
    {
        this.player = player;
    }

    /**
     * Shutdowns gracefully the game
     */
    public void shutdown()
    {
        running = false;
    }

    /**
     * Convenience method that disposes of the world and change the current screen
     */
    public void quitToMainScreen()
    {
        setPlayer(null);
        openMenu(new GuiMainMenu(this));
    }

    private static int[]     screenshotBufferArray;
    private static IntBuffer pixelBuffer;

    public static BufferedImage takeScreenshot()
    {
        return takeScreenshot(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static BufferedImage takeScreenshot(int x, int y, int w, int h)
    {
        int n = w * h;
        if(pixelBuffer == null || pixelBuffer.capacity() < n)
        {
            pixelBuffer = BufferUtils.createIntBuffer(n);
            screenshotBufferArray = new int[n];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();
        GL11.glReadPixels(x, y, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);
        pixelBuffer.get(screenshotBufferArray);
        int[] finalArray = new int[n];
        for(int index = 0; index < n; index++ )
        {
            int color = screenshotBufferArray[index];
            int alpha = color >> 24 & 0xFF;
            int red = color >> 16 & 0xFF;
            int green = color >> 8 & 0xFF;
            int blue = color >> 0 & 0xFF;
            int x1 = index % w;
            int y1 = h - (index / w) - 1;
            finalArray[x1 + y1 * w] = (alpha << 24) | (blue << 16) | (green << 8) | red; // We invert the colors given by OpenGL
        }
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, w, h, finalArray, 0, w);
        return image;
    }

    public ResourceLoader getGameFolderLoader()
    {
        return gameFolderLoader;
    }

    /**
     * Forces a crash with given crash report
     */
    public void crash(CrashReport crashReport)
    {
        crashReport.printStack();
        cleanup();
        System.exit(-1);
    }

    /**
     * Disposes all resources held by the game
     */
    public void cleanup()
    {
        renderEngine.dispose();
        sndProducer.cleanUp();
        Display.destroy();
    }

    public PlayerController getPlayerController()
    {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController)
    {
        this.playerController = playerController;
    }

    public Gui getCurrentMenu()
    {
        return currentMenu;
    }

    public EntityPlayer getClientPlayer()
    {
        return player;
    }

    public GameSettings getGameSettings()
    {
        return settings;
    }

    public void saveSettings()
    {
        File file = new File(SystemUtils.getGameFolder(), "properties.txt");
        try
        {
            settings.saveTo(file);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public DirectSoundProducer getSoundProducer()
    {
        return sndProducer;
    }

    public void setLevel(Level level)
    {
        clientWorld = level;
    }
}
