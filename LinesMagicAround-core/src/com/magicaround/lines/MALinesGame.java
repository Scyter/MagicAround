package com.magicaround.lines;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.magicaround.linesf.android.ActionResolver;

public class MALinesGame extends Game {
	boolean firstTimeCreate = true;
	public final IPlatform platform;
	public final ActionResolver actionResolver;
	public LinesPlayer player;
	FPSLogger fps;

	public MALinesGame(IPlatform handler, ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
		this.platform = handler;
		fps = new FPSLogger();
		player = new LinesPlayer();
	}

	public LinesPlayer getPlayer() {
		return this.player;
	}

	public void setPlayer(LinesPlayer p) {
		player = p;
	}

	public void updatePlayer(LinesPlayer p) {

	}

	@Override
	public void create() {
		// Settings.load();
		// Assets.load();
		setScreen(new LoadingScreen(this));

	}

	@Override
	public void render() {
		super.render();
		// fps.log();

	}

	@Override
	public void resize(int width, int height) {
		// Gdx.app.log("aa", "g resize");
		super.resize(width, height);
	}

	@Override
	public void pause() {
		// Gdx.app.log("aa", "g pause");
		super.pause();
	}

	@Override
	public void resume() {
		// Gdx.app.log("aa", "g resume");
		super.resume();
	}

	@Override
	public void dispose() {
		// Gdx.app.log("aa", "g dispose");
		super.dispose();
	}

}
