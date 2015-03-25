package com.ma.wcc;

import java.util.concurrent.TimeUnit;

import magicaround.BrainPlayer;
import magicaround.GameResult;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ClickerScreen extends ScreenClass implements IWorldListener {

	public static final int READY = 0;
	public static final int INFO1 = -1;
	public static final int INFO2 = -2;
	public static final int INFO3 = -3;
	public static final int RUNNING = 1;
	public static final int PAUSED = 2;
	public static final int STOPPED = 21;
	public static final int NEXT_LEVEL = 3;
	public static final int NEXT_LEVEL_MAX = 31;
	public static final int CURRENT_LEVEL = 4;
	public static final int GAME_OVER = 5;
	public static final int GAME_OVER_MAX = 51;
	int state;
	float readyTime;

	WorldCupClicker game;
	BrainPlayer player;
	ClickerWorld world;
	ClickerRenderer renderer;
	OrthographicCamera camera;
	Vector3 touchPoint;
	Vector2 touch;
	SpriteBatch batch;

	Rectangle mainMenu;
	Rectangle pause;
	Rectangle results;

	public int width;
	public int height;
	public int diff;
	int goTouch;

	public ClickerScreen(WorldCupClicker g) {
		if (Settings.needReload) {
			Assets.reloadGraph();
		}
		game = g;
		player = game.player;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		touchPoint = new Vector3();
		batch = new SpriteBatch();
		if (player.results.size() >= 1)
			world = new ClickerWorld(width, height, player.getLevel(2), this);
		else
			world = new ClickerWorld(width, height, 1, this);
		renderer = new ClickerRenderer(this);
		readyTime = 1;
		goTouch = 0;
		mainMenu = new Rectangle(0.2f * width, 0.7f * height, 0.6f * width,
				0.1f * height);
		pause = new Rectangle(0.01f * width, 0.99f * height - 0.09f * width,
				0.09f * width, 0.09f * width);
		results = new Rectangle(0.95f * width, height - 0.05f * width,
				0.05f * width, 0.05f * width);
		if (Settings.info)
			state = READY;
		else
			state = INFO1;
	}

	@Override
	public void render(float delta) {
		if (delta > 0.1)
			delta = 0.1f;
		update(delta);
		draw(delta);
	}

	private void update(float delta) {
		switch (state) {
		case INFO1:
			if (Gdx.input.justTouched())
				state = INFO2;
			break;
		case INFO2:
			if (Gdx.input.justTouched())
				state = INFO3;
			break;
		case INFO3:
			if (Gdx.input.justTouched()) {
				state = READY;
				Settings.info = true;
				Settings.saveSettings(game.player);
			}
			break;

		case READY:
			if (Gdx.input.justTouched()) {
				camera.unproject(touchPoint.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));
				touch = new Vector2(touchPoint.x, touchPoint.y);
				if (pause.contains(touch)) {
				}
			}
			readyTime -= delta;
			if (readyTime <= 0) {
				state = RUNNING;
				readyTime = 1;
			}
			break;

		case RUNNING:
			world.update(delta);
			if (Gdx.input.justTouched()) {
				camera.unproject(touchPoint.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));
				updateTouch();
				Gdx.app.log("t", touchPoint.toString());
			}
			break;

		case NEXT_LEVEL:
		case NEXT_LEVEL_MAX:
		case CURRENT_LEVEL:
		case PAUSED:
			if (Gdx.input.justTouched()) {
				boolean t = true;
				if (!t) {
				} else {

					goTouch++;
					if (goTouch >= 2) {
						state = READY;
						if (Settings.needReload) {
							try {
								TimeUnit.SECONDS.sleep(3);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Assets.load(game.platform);
						}
						world.startNewLevel();
					}
				}
			}
			break;
		case STOPPED:
			if (Gdx.input.justTouched()) {
				camera.unproject(touchPoint.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));
				touch = new Vector2(touchPoint.x, touchPoint.y);
				if (pause.contains(touch)) {
					game.setScreen(new MainMenuScreen(game));
				} else {
					goTouch++;
					if (goTouch >= 2) {
						state = RUNNING;
					}
				}
			}
			break;

		case GAME_OVER:
		case GAME_OVER_MAX:
			if (Gdx.input.justTouched()) {
				goTouch++;
				if (goTouch >= 2) {
					game.platform.showAdMob(false);
					game.setScreen(new MainMenuScreen(game));
				}
			}
			break;
		default:
			break;
		}
	}

	private void updateTouch() {
		touch = new Vector2(touchPoint.x, touchPoint.y);
		if (pause.contains(touch)) {
			state = STOPPED;
		} else if (results.contains(touch)) {
			game.platform.submitScoreGPGS(new GameResult(1, Assets.r
					.nextInt(500), world.level, player.favorite, player.hated,
					""));
		} else
			world.touch(touchPoint);
	}

	private void draw(float delta) {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderer.draw();
		switch (state) {
		case INFO1:
			batch.begin();
			batch.setColor(1, 1, 1, 0.5f);
			batch.draw(Assets.cells, 0, 0, width, height);
			batch.setColor(1, 1, 1, 1);
			Assets.infoFont.draw(batch, Assets.info1, width * 0.5f
					- Assets.info1X / 2, height * 0.75f);
			batch.draw(Assets.ball, width * 0.40f, height * 0.55f,
					width * 0.15f, width * 0.15f);
			batch.draw(Assets.hand, width * 0.45f, height * 0.55f,
					width * 0.1f, width * 0.1f);
			Assets.infoFont.draw(batch, Assets.info1a, width * 0.5f
					- Assets.info1aX / 2, height * 0.5f);
			Assets.scoreFont.draw(batch, "50", width * 0.49f, height * 0.44f);
			Assets.infoFont.draw(batch, Assets.info1b, width * 0.5f
					- Assets.info1bX / 2, height * 0.3f);
			batch.draw(Assets.cup, width * 0.3f, height * 0.15f, width * 0.1f,
					width * 0.1f);
			batch.draw(Assets.cup, width * 0.5f, height * 0.15f, width * 0.1f,
					width * 0.1f);
			batch.draw(Assets.cup, width * 0.7f, height * 0.15f, width * 0.1f,
					width * 0.1f);

			batch.end();

			break;
		case INFO2:
			batch.begin();
			batch.setColor(1, 1, 1, 0.5f);
			batch.draw(Assets.cells, 0, 0, width, height);
			batch.setColor(1, 1, 1, 1);
			Assets.infoFont.draw(batch, Assets.info2, width * 0.5f
					- Assets.info2X / 2, height * 0.77f);
			Assets.infoFont.draw(batch, Assets.info2x, width * 0.5f
					- Assets.info2xX / 2, height * 0.7f);
			batch.draw(Assets.stars.get(0), width * 0.43f, height * 0.55f,
					width * 0.15f, width * 0.15f);

			Assets.infoFont.draw(batch, Assets.info2a, width * 0.5f
					- Assets.info2aX / 2, height * 0.4f);
			batch.draw(Assets.stars.get(0), width * 0.1f, height * 0.25f,
					width * 0.15f, width * 0.15f);
			Assets.scoreFont.draw(batch, "x2", width * 0.25f, height * 0.3f);

			batch.draw(Assets.stars.get(0), width * 0.5f, height * 0.25f,
					width * 0.15f, width * 0.15f);
			batch.draw(Assets.stars.get(1), width * 0.65f, height * 0.25f,
					width * 0.15f, width * 0.15f);
			Assets.scoreFont.draw(batch, "x3", width * 0.8f, height * 0.3f);

			batch.end();

			break;
		case INFO3:
			batch.begin();
			batch.setColor(1, 1, 1, 0.5f);
			batch.draw(Assets.cells, 0, 0, width, height);
			batch.setColor(1, 1, 1, 1);
			Assets.infoFont.draw(batch, Assets.info3, width * 0.5f
					- Assets.info3X / 2, height * 0.75f);
			batch.draw(Assets.ball, width * 0.10f, height * 0.55f,
					width * 0.15f, width * 0.15f);
			batch.draw(Assets.hand, width * 0.30f, height * 0.55f,
					width * 0.1f, width * 0.1f);

			batch.draw(Assets.arrow, width * 0.45f, height * 0.55f,
					width * 0.1f, width * 0.1f);

			batch.draw(Assets.stars.get(0), width * 0.70f, height * 0.55f,
					width * 0.15f, width * 0.15f);
			batch.draw(Assets.no, width * 0.70f, height * 0.55f, width * 0.15f,
					width * 0.15f);

			Assets.infoFont.draw(batch, Assets.info3a, width * 0.5f
					- Assets.info3aX / 2, height * 0.4f);
			batch.draw(Assets.ball, width * 0.10f, height * 0.20f,
					width * 0.15f, width * 0.15f);
			batch.draw(Assets.ball, width * 0.25f, height * 0.20f,
					width * 0.10f, width * 0.10f);
			batch.draw(Assets.ball, width * 0.35f, height * 0.20f,
					width * 0.05f, width * 0.05f);
			batch.draw(Assets.ball, width * 0.40f, height * 0.20f,
					width * 0.01f, width * 0.01f);

			batch.draw(Assets.arrow, width * 0.45f, height * 0.25f,
					width * 0.1f, width * 0.1f);

			batch.draw(Assets.stars.get(0), width * 0.60f, height * 0.25f,
					width * 0.1f, width * 0.1f);
			batch.draw(Assets.no, width * 0.60f, height * 0.25f, width * 0.1f,
					width * 0.1f);
			batch.draw(Assets.stars.get(1), width * 0.70f, height * 0.25f,
					width * 0.1f, width * 0.1f);
			batch.draw(Assets.no, width * 0.70f, height * 0.25f, width * 0.1f,
					width * 0.1f);
			batch.draw(Assets.stars.get(2), width * 0.80f, height * 0.25f,
					width * 0.1f, width * 0.1f);
			batch.draw(Assets.no, width * 0.80f, height * 0.25f, width * 0.1f,
					width * 0.1f);

			batch.end();

			break;
		case READY:
			drawReady();
			break;
		case RUNNING:
			batch.begin();
			batch.draw(Assets.pause, pause.x, pause.y, pause.width,
					pause.height);
			batch.end();
			break;
		case STOPPED:
			drawNeedTap();
			batch.begin();
			// Assets.scoreFont.draw(batch, "Maind menu", mainMenu.x,
			// mainMenu.y);
			batch.setColor(1, 1, 1, 1);
			batch.draw(Assets.mainMenu, pause.x, pause.y, pause.width,
					pause.height);
			batch.end();
			break;
		case NEXT_LEVEL:
			drawNeedTap();
			batch.begin();
			Assets.scoreFont.draw(batch,
					Assets.next_level + " " + String.valueOf(world.level),
					width * 0.45f - Assets.next_levelX / 2, height * 0.55f);
			batch.end();
			break;
		case NEXT_LEVEL_MAX:
			drawNeedTap();
			batch.begin();
			Assets.infoFont.draw(batch, Assets.max_level, width * 0.5f
					- Assets.max_levelX / 2, height * 0.7f);
			Assets.infoFont.draw(batch, Assets.max_level1, width * 0.5f
					- Assets.max_level1X / 2, height * 0.55f);
			Assets.scoreFont.draw(batch,
					Assets.level + " " + String.valueOf(world.level), width
							* 0.45f - Assets.levelX / 2, height * 0.4f);
			batch.end();

			break;
		case CURRENT_LEVEL:
			drawNeedTap();
			batch.begin();
			Assets.infoFont.draw(batch, Assets.need50, width * 0.5f
					- Assets.need50X / 2, height * 0.7f);
			Assets.infoFont.draw(batch, Assets.need50_a, width * 0.5f
					- Assets.need50_aX / 2, height * 0.55f);
			Assets.scoreFont.draw(batch,
					Assets.cur_level + String.valueOf(world.level), width
							* 0.45f - Assets.cur_levelX / 2, height * 0.4f);
			batch.end();

			break;
		case GAME_OVER:
			drawNeedTap();
			batch.begin();
			Assets.scoreFont.draw(batch, Assets.game_over, width * 0.5f
					- Assets.game_overX / 2, height * 0.7f);
			Assets.scoreFont.draw(batch, String.valueOf((int) world.score),
					width * 0.45f, height * 0.4f);
			batch.end();

			break;
		case GAME_OVER_MAX:
			drawNeedTap();
			batch.begin();
			Assets.infoFont.draw(batch, Assets.max_level, width * 0.5f
					- Assets.max_levelX / 2, height * 0.7f);
			Assets.infoFont.draw(batch, Assets.max_level1, width * 0.5f
					- Assets.max_level1X / 2, height * 0.55f);
			Assets.scoreFont.draw(batch, Assets.game_over, width * 0.5f
					- Assets.game_overX / 2, height * 0.4f);
			batch.end();

			break;
		default:
			break;
		}

	}

	private void drawPause() {
		batch.begin();
		batch.setColor(1, 1, 1, 0.5f);
		batch.draw(Assets.cells, 0, 0, width, height * 0.9f);
		// batch.draw(Assets.pause, pause.x, pause.y, pause.width,
		// pause.height);
		batch.end();
	}

	private void drawReady() {
		drawPause();
		batch.begin();
		batch.setColor(1, 1, 1, 1f);
		// batch.setColor(1, 0, 0, 1f);
		Assets.scoreFont.draw(batch,
				Assets.level + String.valueOf(world.level), width * 0.5f
						- Assets.levelX / 2, height * 0.7f);
		String s = String.format("%1$,.2f", readyTime);
		if (readyTime < 0)
			s = "0";
		Assets.timeFont.draw(batch, s, width * 0.45f, height * 0.5f);
		batch.draw(Assets.pause, pause.x, pause.y, pause.width, pause.height);
		batch.end();
	}

	private void drawNeedTap() {
		if (world.score > 100000)//
			drawReloading(); // NB! убрать это отсюда
		drawPause();
		batch.begin();
		batch.setColor(1, 0, 0, 1f);
		// batch.setColor(1, 0, 0, 1f);
		if (goTouch == 1)
			Assets.infoFont.setColor(Color.RED);
		Assets.infoFont.draw(batch, Assets.need_tap, width * 0.5f
				- Assets.need_tapX / 2, height * 0.15f);
		if (goTouch == 1)
			Assets.infoFont.setColor(Color.WHITE);
		batch.end();
	}

	private void drawReloading() {
		drawPause();
		batch.begin();
		batch.setColor(1, 1, 1, 1f);
		// batch.setColor(1, 0, 0, 1f);
		if (goTouch == 1)
			Assets.infoFont.setColor(Color.RED);
		Assets.infoFont.draw(batch, Assets.need_tap, width * 0.5f
				- Assets.need_tapX / 2, height * 0.15f);
		if (goTouch == 1)
			Assets.infoFont.setColor(Color.WHITE);
		batch.end();
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
		if (state == RUNNING) {
			state = STOPPED;
			goTouch = 1;
			drawPause();
		}
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void timeOver() {
	}

	@Override
	public void gameOver(boolean mLvl) {
		if (mLvl)
			state = GAME_OVER_MAX;
		else
			state = GAME_OVER;
		goTouch = 0;
		GameResult r = new GameResult(1, (int) world.score, world.level,
				player.favorite, player.hated, "");
		game.platform.saveResult(r);
		player.results.add(r);
		game.platform.submitScoreGPGS(r);
		game.platform.showAdMob(true);
		if (Settings.needReload) {
			Assets.reloadGraph();
		}
	}

	@Override
	public void nextLevel(boolean mLvl) {
		if (mLvl)
			state = NEXT_LEVEL_MAX;
		else
			state = NEXT_LEVEL;
		goTouch = 0;
		win();

	}

	@Override
	public void currentLevel() {
		state = CURRENT_LEVEL;
		goTouch = 0;
		same_lvl();
	}

	@Override
	public void hit() {
		Assets.hit.play(0.5f);
	}

	@Override
	public void miss() {
		Assets.miss2.play(0.5f);
	}

	@Override
	public void lose_ball() {
		Assets.lose_ball.play(0.5f);

	}

	@Override
	public void win() {
		Assets.appl.play(0.6f);
	}

	@Override
	public void same_lvl() {
		// TODO Auto-generated method stub

	}
}