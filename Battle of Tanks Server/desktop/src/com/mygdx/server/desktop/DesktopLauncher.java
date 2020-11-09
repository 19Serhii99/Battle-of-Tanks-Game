package com.mygdx.server.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.server.Server;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.resizable = false;
		config.width = 100;
		config.height = 100;
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
		new LwjglApplication(new Server(), config);
	}
}