package com.lukzdev.grow.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lukzdev.grow.G;
import com.lukzdev.grow.Grow;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = G.TARGET_WIDTH;
		config.height = G.TARGET_HEIGHT;

		new LwjglApplication(new Grow(), config);
	}
}
