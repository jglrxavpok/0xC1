package org.c1;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class C1Game {

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		while (!Display.isCloseRequested()) {

			Display.update();
		}

		Display.destroy();
	}

	public static void main(String[] args) {
		C1Game game = new C1Game();
		game.start();
	}

}
