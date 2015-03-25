package com.magicaround.lines;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.magicaround.lines.simple.SimpleLinesScreen;

public class LoadingScreen implements Screen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	public int clicks;
	public MALinesGame game;
	public SimpleLinesScreen nextScreen;
	int time;
	TextureRegion region;
	int maxTime;
	float w;
	float h;

	public LoadingScreen(MALinesGame mal) {
		this.game = mal;

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		camera.position.set(w / 2, h / 2, 0);
		batch = new SpriteBatch();
		maxTime = 13;
		time = 0;
		Random r = new Random();
		texture = new Texture(Gdx.files.internal("data/ball/!!"
				+ String.valueOf(r.nextInt(7) + 1) + ".png"));
		region = new TextureRegion(texture, 0, 0, texture.getWidth(),
				texture.getHeight());
		sprite = new Sprite(region);
		sprite.setSize(time / maxTime * region.getRegionWidth(), time / maxTime
				* region.getRegionWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(w / 2, h / 2);

	}

	@Override
	public void render(float delta) {
		time++;
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// batch.draw(region, 0, 0, region.getRegionWidth(),
		// region.getRegionHeight());
		sprite.setSize((float) time / maxTime * region.getRegionWidth(),
				(float) time / maxTime * region.getRegionWidth());
		sprite.setPosition(w / 2 - sprite.getWidth() / 2,
				h / 2 - sprite.getHeight() / 2);
		sprite.draw(batch);
		batch.end();

		switch (time) {
		case 2:
			if (game.actionResolver.getSignedInGPGS()) {
				// game.actionResolver.getLeaderboardGPGS();
			} else {
				// game.actionResolver.loginGPGS();
			}
			LinesAssets.load1();
			break;
		case 3:
			LinesAssets.load2();
			break;
		case 4:
			LinesAssets.load3();
			break;
		case 5:
			LinesAssets.load4();
			break;
		case 6:
			LinesSettings.loadSettings();
			break;
		case 7:
			LinesAssets.load5();
			break;
		case 8:
			LinesAssets.load6();
			break;
		case 9:
			LinesAssets.load7();
			break;
		case 10:
			LinesAssets.load8();
			break;
		default:
			break;
		}
		if (time == maxTime) {
			Screen tmp = new MainMenuScreen(game);
			game.setScreen(tmp);
		}

	}

	@Override
	public void resize(int width, int height) {
		// Gdx.app.log("aa", "resize");

	}

	@Override
	public void show() {

		// Gdx.app.log("aa", "show");
	}

	@Override
	public void hide() {

		// Gdx.app.log("aa", "hide");
	}

	@Override
	public void pause() {
		// Gdx.app.log("aa", "pause");

	}

	@Override
	public void resume() {
		// Gdx.app.log("aa", "resume");
	}

	@Override
	public void dispose() {
		// Gdx.app.log("aa", "dispose");
		batch.dispose();
		texture.dispose();

	}

}
