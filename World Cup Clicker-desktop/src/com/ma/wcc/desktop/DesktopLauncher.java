package com.ma.wcc.desktop;

import magicaround.BrainPlayer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ma.wcc.WorldCupClicker;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "Clicker";
		cfg.useGL30 = true;
		cfg.width = 400;
		cfg.height = 680;
		DPlatform pd = new DPlatform();
		BrainPlayer player = new BrainPlayer();
		WorldCupClicker wcc = new WorldCupClicker(pd, player);

		new LwjglApplication(wcc, cfg);
	}
}
