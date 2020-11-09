package com.mygdx.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Editor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.backgroundFPS = 120;
		config.foregroundFPS = 120;
		config.width = 1366;
		config.height = 768;
		config.resizable = false;
		config.samples = 0;
		//config.fullscreen = true;
		new LwjglApplication(new Editor(), config);
	}
}
