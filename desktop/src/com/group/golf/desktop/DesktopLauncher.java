package com.group.golf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.group.golf.Golf;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Golf.GAME_NAME;
		config.width = Golf.VIRTUAL_WIDTH;
		config.height = Golf.VIRTUAL_HEIGHT;
		new LwjglApplication(new Golf(), config);
	}
}
