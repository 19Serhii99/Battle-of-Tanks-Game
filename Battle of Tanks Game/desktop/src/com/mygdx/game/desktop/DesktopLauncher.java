package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Game;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
		config.height = 768;
		config.samples = 16;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.fullscreen = true;
		// config.resizable = false;
		// System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		new LwjglApplication(new Game(), config);
	}
}