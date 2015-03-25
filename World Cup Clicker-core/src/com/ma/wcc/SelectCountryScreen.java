package com.ma.wcc;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SelectCountryScreen extends ScreenClass {
	WorldCupClicker game;
	int width;
	int height;
	OrthographicCamera camera;
	Vector3 touchPoint;
	SpriteBatch batch;
	public boolean favorite;
	public ArrayList<Rectangle> flagsPositions;
	public Rectangle flagsRandom;
	public Rectangle flagsUnknown;
	public float flagSize;
	float xStep;
	float yStep;
	MainMenuScreen mms;

	public SelectCountryScreen(WorldCupClicker g, boolean f, MainMenuScreen mms) {
		game = g;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		batch = new SpriteBatch();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		touchPoint = new Vector3();
		batch = new SpriteBatch();
		favorite = f;
		flagsPositions = new ArrayList<Rectangle>();
		int fsX = (int) (0.2 * width);
		int fxY = (int) (((0.8 * height) / 8) * 0.8);
		flagSize = Math.min(fsX, fxY);
		xStep = (width * 0.8f - 4 * flagSize) / 3;
		yStep = (height * 0.75f - 8 * flagSize) / 7;

		for (int i = 0; i < 32; i++) {
			int line = i / 4;
			int col = i % 4;
			Rectangle r = new Rectangle(
					(float) ((col + 0.5) * 0.25 * width - flagSize / 2),
					(float) (0.9 * height - (line + 1) * flagSize * 1.25),
					flagSize, flagSize * 1.25f);
			flagsPositions.add(r);
		}
		flagsRandom = new Rectangle((float) (width * 0.33 - flagSize / 2),
				(float) (0.9 * height - 9 * (flagSize) * 1.25) - 3, flagSize,
				flagSize);
		flagsUnknown = new Rectangle((float) (width * 0.66 - flagSize / 2),
				(float) (0.9 * height - 9 * (flagSize) * 1.25) - 3, flagSize,
				flagSize);

		this.mms = mms;
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void draw() {
		drawBackground();
		drawFlagsAndTitle();

	}

	private void drawFlagsAndTitle() {
		batch.begin();

		if (favorite) {
			TextBounds t1 = Assets.infoFont.getBounds(Assets.favorite);
			Assets.infoFont.draw(batch, Assets.favorite, width * 0.49f
					- t1.width, (float) (0.95 * height));
		} else {
			TextBounds t1 = Assets.infoFont.getBounds(Assets.hated);
			Assets.infoFont.draw(batch, Assets.hated, width * 0.49f - t1.width,
					(float) (0.95 * height));
		}
		Assets.infoFont.draw(batch, Assets.team, width * 0.51f,
				(float) (0.95 * height));

		for (int i = 0; i < 32; i++) {
			batch.draw(Assets.teamsFlags.get(i), flagsPositions.get(i).x,
					flagsPositions.get(i).y + flagSize * 0.25f,
					flagsPositions.get(i).width,
					flagsPositions.get(i).height / 1.25f);
			Assets.smallTimeFont.draw(
					batch,
					Assets.teams.get(i).nameTMP,
					flagsPositions.get(i).x + flagSize / 2
							- Assets.teams.get(i).X / 2,
					flagsPositions.get(i).y + flagSize * 0.25f);
		}
		batch.draw(Assets.cubeRandom, flagsRandom.x, flagsRandom.y + flagSize
				* 0.25f, flagsRandom.width, flagsRandom.height);
		batch.draw(Assets.question, flagsUnknown.x, flagsUnknown.y + flagSize
				* 0.25f, flagsUnknown.width, flagsUnknown.height);

		batch.end();

	}

	private void drawBackground() {
		batch.begin();
		batch.disableBlending();
		batch.draw(Assets.backgroundPole, 0, 0, width, height);
		batch.end();
		batch.begin();
		batch.enableBlending();
		batch.setColor(1, 1, 1, 0.4f);
		batch.draw(Assets.cells, 0, 0, width, height);
		batch.setColor(1, 1, 1, 1);
		batch.end();

	}

	private void update(float delta) {
		backgroundUpdate(delta);
		if (Gdx.input.justTouched()) {
			for (Rectangle r : flagsPositions) {
				camera.unproject(touchPoint.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));
				if (r.contains(new Vector2(touchPoint.x, touchPoint.y))) {
					if (favorite)
						game.player.favorite = flagsPositions.indexOf(r);
					else
						game.player.hated = flagsPositions.indexOf(r);
				}
			}
			if (flagsRandom.contains(new Vector2(touchPoint.x, touchPoint.y))) {
				if (favorite)
					game.player.favorite = Assets.r.nextInt(32);
				else
					game.player.hated = Assets.r.nextInt(32);
			}
			if (flagsUnknown.contains(new Vector2(touchPoint.x, touchPoint.y))) {
				if (favorite)
					game.player.favorite = -1;
				else
					game.player.hated = -1;
			}
			Settings.saveSettings(game.player);
			game.setScreen(mms);
		}

	}

	private void backgroundUpdate(float delta) {
		// TODO Auto-generated method stub

	}

}
