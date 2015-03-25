package com.magicaround.linesf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.magicaround.lines.LinesPlayer;
import com.magicaround.lines.MALinesGame;

public class DesktopLauncher {
	public static LinesPlayer player;

	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		cfg.title = "LINES: Magic Around";
		cfg.width = 320;
		cfg.height = 480;

		ActionResolverDesctop ard = new ActionResolverDesctop();
		PlatformAPI p = new PlatformAPI();
		MALinesGame game = new MALinesGame(p, ard);
		player = new LinesPlayer();
		loadPlayer();
		game.setPlayer(player);
		new LwjglApplication(game, cfg);
		// new LwjglApplication(new Clicker(), cfg);

	}

	private static void loadPlayer() {
		player.level = 1;

	}
}
