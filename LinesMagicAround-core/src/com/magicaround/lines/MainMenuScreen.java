package com.magicaround.lines;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.magicaround.lines.campaign.CampaignLinesScreen;
import com.magicaround.lines.simple.IWorld;
import com.magicaround.lines.simple.SimpleLinesScreen;
import com.magicaround.lines.survivor.SurvivalLinesScreen;

public class MainMenuScreen implements Screen {
	MALinesGame game;
	public int weight;
	public int height;

	public ArrayList<Ball> balls;

	OrthographicCamera guiCam;
	SpriteBatch batch;
	Rectangle classicGame;
	Rectangle hardGame;
	Rectangle compainGame;
	Rectangle survivalGame;
	Rectangle menuButton;
	Rectangle musicBounds;
	Rectangle soundBounds;
	// Rectangle playBounds;
	// Rectangle highscoresBounds;
	// Rectangle helpBounds;
	Vector3 touchPoint;
	int t = 0;
	TextureRegion back;

	Random r;
	float w;
	float h;
	SimpleLinesScreen nextScreen;
	public WorldProperties prop;
	Music music;

	public MainMenuScreen(MALinesGame game) {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		this.game = game;
		weight = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		guiCam = new OrthographicCamera(weight, height);
		guiCam.position.set(weight / 2, height / 2, 0);
		batch = new SpriteBatch();
		touchPoint = new Vector3();
		balls = new ArrayList<Ball>();
		r = new Random();
		generateBalls();
		prop = new WorldProperties();
		back = LinesAssets.backs.get(r.nextInt(LinesAssets.backs.size()));

		if (w >= 500) {
			classicGame = new Rectangle(0, 400, 300, 100);
			hardGame = new Rectangle(0, 300, 300, 100);
			compainGame = new Rectangle(0, 200, 300, 100);
			survivalGame = new Rectangle(0, 100, 300, 100);
			menuButton = new Rectangle(90, 10, 120, 80);
			musicBounds = new Rectangle(10, 10, 80, 80);
			soundBounds = new Rectangle(210, 10, 80, 80);
		} else {
			classicGame = new Rectangle(0, 400 * h / 680, 300 * w / 400,
					100 * h / 680);
			hardGame = new Rectangle(0, 300 * h / 680, 300 * w / 400,
					100 * h / 680);
			compainGame = new Rectangle(0, 200 * h / 680, 300 * w / 400,
					100 * h / 680);
			survivalGame = new Rectangle(0, 100 * h / 680, 300 * w / 400,
					100 * h / 680);

			menuButton = new Rectangle(90 * w / 400, 10 * h / 680,
					120 * w / 400, 80 * h / 680);
			musicBounds = new Rectangle(10 * w / 400, 10 * h / 680,
					80 * w / 400, 80 * h / 680);
			soundBounds = new Rectangle(210 * w / 400, 10 * h / 680,
					80 * w / 400, 80 * h / 680);
		}

	}

	private void generateBalls() {
		Random r = new Random();
		int m = r.nextInt(100);
		m += 10;
		for (int i = 0; i <= m; i++) {
			balls.add(new Ball(weight, height));
		}
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	public void update(float deltaTime) {
		t++;
		if (Gdx.input.justTouched()) {
			game.platform.connectBtn(true);
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			Vector2 touch = new Vector2(touchPoint.x, touchPoint.y);
			// Gdx.app.log("c", String.valueOf(touchPoint.toString()));
			if (classicGame.contains(touch)) {
				prop.colorSize = 7;
				prop.worldType = IWorld.CLASSIC;
				prop.lineSize = 5;
				prop.poleXSize = 9;
				prop.poleYSize = 9;
				prop.ternSize = 3;
				nextScreen = new SimpleLinesScreen(game, prop);
				nextScreen.game.setScreen(nextScreen);
			}
			if (hardGame.contains(touch)) {
				prop.colorSize = 9;
				prop.worldType = IWorld.HARD;
				prop.lineSize = 5;
				prop.poleXSize = 9;
				prop.poleYSize = 9;
				prop.ternSize = 3;
				nextScreen = new SimpleLinesScreen(game, prop);
				nextScreen.game.setScreen(nextScreen);
			}
			if (compainGame.contains(touch)) {
				prop.colorSize = 5;
				prop.lineSize = 4;
				prop.poleXSize = 7;
				prop.poleYSize = 7;
				prop.ternSize = 3;
				prop.worldType = IWorld.CAMPAIGN;
				nextScreen = new CampaignLinesScreen(game, prop);
				nextScreen.game.setScreen(nextScreen);
			}
			if (survivalGame.contains(touch)) {
				prop.colorSize = 7;
				prop.lineSize = 5;
				prop.poleXSize = 9;
				prop.poleYSize = 9;
				prop.ternSize = 2;
				prop.worldType = IWorld.SURVIVAL;
				nextScreen = new SurvivalLinesScreen(game, prop);
				nextScreen.game.setScreen(nextScreen);
			}
			// if (musicBounds.contains(touch)) {
			// LinesAssets.music();
			// }
			// if (soundBounds.contains(touch)) {
			// LinesAssets.sound();
			// }//NB!

			back = LinesAssets.backs.get(r.nextInt(LinesAssets.backs.size()));

		}
		for (Ball b : balls) {
			b.updateMenu(deltaTime, weight, height);
		}
	}

	public void draw(float deltaTime) {

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

		guiCam.update();
		batch.setProjectionMatrix(guiCam.combined);

		batch.disableBlending();
		batch.begin();
		batch.setColor(1, 1, 1, 0.1f);
		batch.draw(back, -2, -2, this.weight + 5, this.height + 5);
		batch.end();

		batch.enableBlending();
		batch.begin();
		batch.setColor(1, 1, 1, 1);

		for (Ball b : balls) {
			batch.draw(b.picture, b.xDelta + b.mDelta, b.yDelta, b.size, b.size);
		}
		drawMenu();
		batch.end();

	}

	private void drawMenu() {
		batch.draw(LinesAssets.mmClassic, classicGame.x, classicGame.y,
				classicGame.width, classicGame.height);
		batch.draw(LinesAssets.mmHard, hardGame.x, hardGame.y, hardGame.width,
				hardGame.height);
		batch.draw(LinesAssets.mmCom, compainGame.x, compainGame.y,
				compainGame.width, compainGame.height);
		batch.draw(LinesAssets.mmSur, survivalGame.x, survivalGame.y,
				survivalGame.width, survivalGame.height);
		// batch.draw(LinesAssets.musicRegion, musicBounds.x, musicBounds.y,
		// musicBounds.width, musicBounds.height);
		// if (!LinesSettings.musicEnabled) {
		// batch.draw(LinesAssets.noRegion, musicBounds.x, musicBounds.y,
		// musicBounds.width, musicBounds.height);
		// }
		// batch.draw(LinesAssets.soundRegion, soundBounds.x, soundBounds.y,
		// soundBounds.width, soundBounds.height);
		// if (!LinesSettings.soundEnabled) {
		// batch.draw(LinesAssets.noRegion, soundBounds.x, soundBounds.y,
		// soundBounds.width, soundBounds.height);
		// }// NB!
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}