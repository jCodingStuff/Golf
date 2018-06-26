package com.group.golf.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.group.golf.Golf;

/**
 * Launcher for the Golf game
 */
public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Golf.GAME_NAME;
		config.width = Golf.VIRTUAL_WIDTH;
		config.height = Golf.VIRTUAL_HEIGHT;
		config.resizable = false;
		config.x = -1; // Set in the center
		config.y = -1; // Set in the center
		config.addIcon("icon128.png", Files.FileType.Internal);
		config.addIcon("icon32.png", Files.FileType.Internal);
		config.addIcon("icon16.png", Files.FileType.Internal);
		new LwjglApplication(new Golf(), config);
	}
}
