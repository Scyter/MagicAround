package com.ma.wcc;

import magicaround.BrainPlayer;

import com.badlogic.gdx.Game;
import com.ma.wcci.Platform;

public class WorldCupClicker extends Game {
	public BrainPlayer player;
	public Platform platform;

	public WorldCupClicker(Platform p, BrainPlayer pl) {
		platform = p;
		player = pl;
	}

	public void create() {
		Assets.load(platform);
		player.results = platform.results();
		Settings.loadSettings();
		player.favorite = Settings.favorite;
		player.hated = Settings.hated;
		setScreen(new MainMenuScreen(this));

		// setScreen(new GraphScreen(this));
	}

	public void resume() {
		Settings.loadSettings();
		player.favorite = Settings.favorite;
		player.hated = Settings.hated;
	}

	public void render() {
		super.render();
	}
}
