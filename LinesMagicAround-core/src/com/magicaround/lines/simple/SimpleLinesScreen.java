package com.magicaround.lines.simple;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.magicaround.lines.Constants;
import com.magicaround.lines.IElementListner;
import com.magicaround.lines.LinesAssets;
import com.magicaround.lines.LinesSettings;
import com.magicaround.lines.MALinesGame;
import com.magicaround.lines.MainMenuScreen;
import com.magicaround.lines.MovingElement;
import com.magicaround.lines.MovingPicture;
import com.magicaround.lines.MovingText;
import com.magicaround.lines.WorldProperties;

public class SimpleLinesScreen implements Screen, IElementListner,
		IWorldListener {

	public static final int S_GAME_READY = 0;
	public static final int S_GAME_RUNNING = 1;
	public static final int S_GAME_MENU = 2;
	public static final int S_GAME_TASK_END = 3;
	public static final int S_GAME_LEVEL_END = 4;
	public static final int S_GAME_OVER = 5;
	public static final int S_GAME_OVER_HIGHT_SCORE = 6;
	public static final int S_GAME_SPELLING = 8;
	public static final int S_GAME_INFO1 = 9;
	public static final int S_GAME_INFO2 = 10;
	public static final int S_GAME_INFO3 = 11;
	public static final int S_GAME_INFO4 = 12;
	public static final int S_GAME_INFO5 = 13;
	public static final int S_GAME_INFO6 = 14;
	public static final int S_GAME_INFO7 = 15;
	public static final int S_GAME_INFO8 = 16;
	public static final int S_GAME_INFO9 = 17;

	public OrthographicCamera camera;
	public SpriteBatch batch;
	public Vector3 touchPoint;
	public int width;
	public int height;
	public int diff;
	public float cellSize;
	public float ballSize;
	public float infoBallSize;
	// public float ballSize;

	public Rectangle menuBounds;
	public Rectangle musicBounds;
	public Rectangle soundBounds;

	public Rectangle mainMenuBounds;
	public Rectangle newGameBounds;
	public Rectangle otherBounds;
	public Rectangle gameOverBounds;
	public Rectangle newGameOverBounds;
	public Rectangle ratingBounds;
	public Rectangle achivesBounds;

	public Rectangle playBounds;

	public Rectangle sInfoBounds;

	public MALinesGame game;
	public IRenderer renderer;
	public int screenState;
	public int screenLastState;
	public IWorld world;
	public WorldProperties properties;
	public ArrayList<MovingElement> movingElements;
	public Random rand;
	public int renderCount;
	public float times = 0;
	public int[] clearedBalls = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	public int addClearedBalls;
	int gr;

	public Vector2 scorePosition;
	public Vector2 hScorePosition;
	public Vector2 heroLevelPosition;
	protected Vector2 colorSizePosition;
	protected Vector2 lineSizePosition;
	public Vector2 ternSizePosition;
	public TextBounds ternSizeBounds;
	public Vector2 expPosition;
	public Vector2 gameTypePosition;
	protected Vector2 touch;

	public int xO;
	public int yO;

	int submit;
	int randomBall1;
	int randomBall2;
	int randomBall3;
	int randomBall4;

	public SimpleLinesScreen(MALinesGame g, WorldProperties p) {
		movingElements = new ArrayList<MovingElement>();
		submit = 0;
		properties = p;
		renderCount = 0;
		gr = 0;
		game = g;

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		diff = (height / width <= 1.6f) ? (height - width)
				: ((int) (width * 0.75));
		// diff = height - weight;
		cellSize = (int) width / properties.poleXSize;// //NB
		ballSize = width / properties.poleXSize - 3;// ///NB
		infoBallSize = (ballSize < diff * 0.2f) ? ballSize : (diff * 0.2f);
		yO = (int) ((diff) * 0.6);
		xO = (int) (width - cellSize * properties.poleXSize) / 2 + 1;// ///////////NB

		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2 - xO, height / 2 - yO, 0);
		batch = new SpriteBatch();
		touchPoint = new Vector3();

		rand = new Random();
		menuBounds = new Rectangle(width - (0.21f * diff), width
				+ (0.2f * (diff)), (diff) * 0.2f, (diff) * 0.2f);
		musicBounds = new Rectangle(width - (0.4f * diff), width
				+ (0.2f * (diff)), (diff) * 0.2f, (diff) * 0.2f);
		soundBounds = new Rectangle(width - (0.6f * diff), width
				+ (0.2f * (diff)), (diff) * 0.2f, (diff) * 0.2f);

		newGameBounds = new Rectangle(0.15f * width, 0.65f * width,
				width * 0.7f, width * 0.3f);
		mainMenuBounds = new Rectangle(0.15f * width, 0.35f * width,
				width * 0.7f, width * 0.3f);
		otherBounds = new Rectangle(0.15f * width, 0.05f * width, width * 0.7f,
				width * 0.3f);
		gameOverBounds = new Rectangle(0, -0.6f * diff, 0.03f * width,
				0.03f * width);
		newGameOverBounds = new Rectangle(0, width + 0.36f * diff,
				0.06f * width, 0.06f * width);

		gameTypePosition = new Vector2(width - 0.02f * diff, width + 0.02f
				* diff);
		scorePosition = new Vector2(0.01f * diff, -0.02f * (diff));
		hScorePosition = new Vector2(0.01f * diff, -0.28f * (diff));
		heroLevelPosition = new Vector2(0.01f * diff, -0.4f * (diff));
		expPosition = new Vector2(0.01f * diff, -0.59f * (diff));

		ratingBounds = new Rectangle(0.01f * diff, -0.26f * diff, 0.1f * diff,
				0.1f * diff);
		achivesBounds = new Rectangle(0.21f * diff, -0.26f * diff, 0.1f * diff,
				0.1f * diff);

		playBounds = new Rectangle(0, -0.6f * (diff), 0.1f * diff, 0.1f * diff);

		colorSizePosition = new Vector2((float) (3 * diff / 100),
				(float) (cellSize * properties.poleYSize + 0.1f * (diff)));
		lineSizePosition = new Vector2((float) (3 * diff / 100),
				(float) (cellSize * properties.poleYSize + 0.02f * (diff)));
		ternSizePosition = new Vector2((float) (3 * diff / 100),
				(float) (cellSize * properties.poleYSize + 0.2f * (diff)));

		ternSizeBounds = LinesAssets.nextTernSizeFont.getBounds("");
		newWorld();

		addClearedBalls = 0;

		sInfoBounds = new Rectangle(width - 0.90f * diff, -0.22f * diff,
				diff * 0.2f, diff * 0.2f);

		randomBall1 = rand.nextInt(7) + 1;
		randomBall2 = rand.nextInt(7) + 1;
		randomBall3 = rand.nextInt(7) + 1;
		randomBall4 = rand.nextInt(7) + 1;

	}

	protected void newWorld() {
		world = new SimpleLinesWorld(this, width, height, properties,
				game.player);
		renderer = new SimpleLinesRenderer(this);
		screenState = S_GAME_RUNNING;
		screenLastState = S_GAME_RUNNING;
		game.platform.loadPlayer();
		if (world.loadGame() == 0)
			world.startGame();

	}

	@Override
	public void render(float deltaTime) {
		renderCount++;
		if (renderCount == 1) {
			for (int i = 0; i <= 10; i++)
				clearedBalls[i] = 0;
		}
		times += deltaTime;
		if (submit > 0)
			submit--;
		update(deltaTime);
		draw(deltaTime);
	}

	protected void update(float deltaTime) {
		// if (deltaTime > 0.1f)
		// deltaTime = 0.1f;
		world.update(deltaTime);
		updateEmelents(deltaTime);
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			touch = new Vector2(touchPoint.x, touchPoint.y);
			updateTouch(deltaTime);
			Gdx.app.log("t", touchPoint.toString());

		}

	}

	protected void updateTouch(float deltaTime) {
		boolean updTouch = false;
		if (gameOverBounds.contains(touch)) {
			sendResult();
		}
		if (newGameOverBounds.contains(touch)) {
			sendResultNew();
		}
		if (LinesSettings.loggedIn) {
			if (ratingBounds.contains(touch)) {
				game.actionResolver.getLeaderboardGPGS(properties.worldType);
			}
			if (achivesBounds.contains(touch)) {
				game.actionResolver.getAchievementsGPGS();
			}
		}
		if ((screenState != S_GAME_OVER)
				&& (screenState != S_GAME_OVER_HIGHT_SCORE)
				&& (screenState != S_GAME_MENU)) {
			if (sInfoBounds.contains(touch)) {
				if ((screenState != S_GAME_INFO1)
						&& (screenState != S_GAME_INFO2)
						&& (screenState != S_GAME_INFO3)
						&& (screenState != S_GAME_INFO4)
						&& (screenState != S_GAME_INFO5)
						&& (screenState != S_GAME_INFO6)
						&& (screenState != S_GAME_INFO7)
						&& (screenState != S_GAME_INFO8)
						&& (screenState != S_GAME_INFO9)) {
					screenLastState = screenState;
					screenState = S_GAME_INFO1;
					updTouch = true;
				} else
					screenState = screenLastState;
			}
		}
		switch (screenState) {
		case S_GAME_OVER:
		case S_GAME_OVER_HIGHT_SCORE:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = screenState;
				screenState = S_GAME_MENU;
			}
			if (sInfoBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = screenState;
				screenState = S_GAME_MENU;
			}
			convertPoint();
			break;
		case S_GAME_INFO1:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = S_GAME_MENU;
				screenState = S_GAME_MENU;
				break;
			}
			if (!updTouch)
				screenState = S_GAME_INFO2;

			break;
		case S_GAME_INFO2:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = S_GAME_MENU;
				screenState = S_GAME_MENU;
				break;
			}
			if (!updTouch)
				screenState = S_GAME_INFO3;
			if (properties.worldType == IWorld.CLASSIC) {
				screenState = screenLastState;
			} else if (properties.worldType == IWorld.HARD) {
				screenState = screenLastState;
			} else if (properties.worldType == IWorld.CAMPAIGN) {
				screenState = screenLastState;
			}

			break;
		case S_GAME_INFO3:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = S_GAME_MENU;
				screenState = S_GAME_MENU;
				break;
			}
			if (!updTouch)
				screenState = S_GAME_INFO4;
			break;
		case S_GAME_INFO4:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = S_GAME_MENU;
				screenState = S_GAME_MENU;
				break;
			}
			if (!updTouch)
				screenState = S_GAME_INFO5;
			break;
		case S_GAME_INFO5:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = S_GAME_MENU;
				screenState = S_GAME_MENU;
				break;
			}
			if (!updTouch)
				screenState = screenLastState;
			break;
		case S_GAME_INFO6:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = S_GAME_MENU;
				screenState = S_GAME_MENU;
				break;
			}
			if (!updTouch)
				screenState = screenLastState;
			break;
		case S_GAME_RUNNING:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = screenState;
				screenState = S_GAME_MENU;
			}
			convertPoint();
			break;
		case S_GAME_MENU:
			if (menuBounds.contains(touch)) {
				renderer.backNumber(rand.nextInt(LinesAssets.backs.size()));
				LinesAssets.loadBackTexrure();
				screenLastState = screenState;
				screenState = S_GAME_RUNNING;
			}
			if (newGameBounds.contains(touch)) {
				world.clearSavedGame();
				// world.saveGame();
				game.platform.savePlayer();
				if (properties.worldType == SimpleLinesWorld.CLASSIC)
					game.platform.saveClassic();
				else if (properties.worldType == SimpleLinesWorld.HARD)
					game.platform.saveHardcore();
				else if (properties.worldType == SimpleLinesWorld.SURVIVAL)
					game.platform.saveSurvival();
				else if (properties.worldType == SimpleLinesWorld.CAMPAIGN)
					game.platform.saveCampaign();

				newWorld();

			} else if (mainMenuBounds.contains(touch)) {
				Screen tmp = new MainMenuScreen(game);
				pause();
				game.platform.showAdMob(false);
				game.setScreen(tmp);
			} else if (otherBounds.contains(touch)) {
				screenState = screenLastState;
			}
			break;

		default:
			break;
		}

	}

	private void sendResultNew() {
		Gdx.app.log("aa", "new");
		if (game.actionResolver.getSignedInGPGS()) {
			game.actionResolver.incrementAchievementGPGS(
					IWorld.A_LINES_DESTROER, 500);
		}

	}

	private void sendResult() {
		Gdx.app.log("aa", "old");
		submit = 60;
		// gameOver(SimpleLinesWorld.CLASSIC, false);
		if (game.actionResolver.getSignedInGPGS()) {
			Random r = new Random();
			game.actionResolver.submitScoreGPGS(1000 + r.nextInt(1000),
					IWorld.CLASSIC);
			game.actionResolver.submitScoreGPGS(10 + r.nextInt(200),
					IWorld.HARD);
			game.actionResolver.submitScoreGPGS(5 + r.nextInt(100),
					IWorld.CAMPAIGN);
			game.actionResolver.submitScoreGPGS(10 + r.nextInt(300),
					IWorld.SURVIVAL);
		}

	}

	protected void updateTouchSelectSkill() {

	}

	protected void updateEmelents(float deltaTime) {
		ArrayList<MovingElement> tmpElem = new ArrayList<MovingElement>();

		for (MovingElement elem : movingElements) {
			tmpElem.add(elem);
		}
		for (MovingElement elem : tmpElem) {
			elem.update(deltaTime);
		}
	}

	private void convertPoint() {
		if ((touchPoint.x < 0) || (touchPoint.y < 0)
				|| (touchPoint.x >= cellSize * properties.poleXSize)
				|| (touchPoint.y >= cellSize * properties.poleYSize))
			return;
		int x = (int) ((int) touchPoint.x / (cellSize));
		int y = (int) ((int) touchPoint.y / (cellSize));

		// Gdx.app.log("a", String.valueOf(x) + " " + String.valueOf(y) + "  " +
		// touchPoint.toString());

		world.selectCell(x, y);
	}

	/********************************* DRAW ****************************/
	/*************************************************************/
	protected void draw(float delta) {

		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderer.render();
		batch.enableBlending();

		batch.begin();
		if (submit > 0)
			LinesAssets.hScoreFont.draw(batch, "Засабмитилиasdfasdasdfas",
					0.1f * width, 0.85f * width);
		drawElements(delta);
		drawDefault(delta);
		switch (screenState) {
		case S_GAME_RUNNING:
			drawRunning(delta);
			break;
		case S_GAME_OVER:
		case S_GAME_OVER_HIGHT_SCORE:
			drawGameOver(delta);
			break;
		case S_GAME_MENU:
			drawMenu();
			break;
		case S_GAME_INFO1:
			drawPaused(1);
			drawInfo1();
			break;
		case S_GAME_INFO2:
			drawPaused(1);
			drawInfo2();
			break;
		case S_GAME_INFO3:
			drawPaused(1);
			drawInfo3();
			break;
		case S_GAME_INFO4:
			drawPaused(1);
			drawInfo4();
			break;
		case S_GAME_INFO5:
			drawPaused(1);
			drawInfo5();
			break;
		default:
			break;
		}

		batch.end();
	}

	protected void drawDefault(float delta) {
		batch.setColor(1, 1, 1, 1);
		batch.draw(LinesAssets.balls[0], menuBounds.x, menuBounds.y,
				menuBounds.width, menuBounds.height);
		//
		// batch.draw(LinesAssets.musicRegion, musicBounds.x, musicBounds.y,
		// musicBounds.width, musicBounds.height); if
		// (!LinesSettings.musicEnabled) { batch.draw(LinesAssets.noRegion,
		// musicBounds.x, musicBounds.y, musicBounds.width, musicBounds.height);
		// }
		//
		// batch.draw(LinesAssets.soundRegion, soundBounds.x, soundBounds.y,
		// soundBounds.width, soundBounds.height);
		// if (!LinesSettings.soundEnabled) {
		// batch.draw(LinesAssets.noRegion, soundBounds.x, soundBounds.y,
		// soundBounds.width, soundBounds.height);
		// }/NB!
		if (LinesSettings.loggedIn) {
			batch.draw(LinesAssets.ratingRegion, ratingBounds.x,
					ratingBounds.y, ratingBounds.width, ratingBounds.height);
			batch.draw(LinesAssets.achiveRegion, achivesBounds.x,
					achivesBounds.y, achivesBounds.width, achivesBounds.height);
		}
		if ((screenState != S_GAME_OVER)
				&& (screenState != S_GAME_OVER_HIGHT_SCORE)
				&& (screenState != S_GAME_MENU))
			batch.draw(LinesAssets.sInfo, sInfoBounds.x, sInfoBounds.y,
					sInfoBounds.width, sInfoBounds.height);

	}

	private void drawInfo3() {
		LinesAssets.hScoreFont.draw(batch, LinesAssets.i7, 0.5f * width
				- LinesAssets.i7X / 2, 0.9f * width);
		batch.draw(LinesAssets.magicRegion[100], 0.5f * width - cellSize,
				0.5f * width, cellSize * 2, cellSize * 2);

	}

	private void drawInfo4() {
		LinesAssets.levelFont.draw(batch, LinesAssets.i8, 0.5f * width
				- LinesAssets.i8X / 2, 0.9f * width);
		batch.draw(LinesAssets.skill1, 0.3f * width - cellSize, 0.8f * width
				- cellSize * 2, cellSize * 2, cellSize * 2);
		LinesAssets.hScoreFont.draw(batch, LinesAssets.i9, 0.5f * width
				- LinesAssets.i9X / 2, 0.8f * width - cellSize * 3);
		LinesAssets.hScoreFont.draw(batch,
				String.valueOf(Constants.skillMagic[1]) + LinesAssets.i10,
				0.55f * width, 0.8f * width - cellSize);

	}

	private void drawInfo5() {
		LinesAssets.levelFont.draw(batch, LinesAssets.i11, 0.5f * width
				- LinesAssets.i11X / 2, 0.9f * width);
		batch.draw(LinesAssets.skill2, 0.3f * width - cellSize, 0.8f * width
				- cellSize * 2, cellSize * 2, cellSize * 2);
		LinesAssets.hScoreFont.draw(batch, LinesAssets.i12, 0.5f * width
				- LinesAssets.i12X / 2, 0.8f * width - cellSize * 3);
		LinesAssets.hScoreFont.draw(batch,
				String.valueOf(Constants.skillMagic[2]) + LinesAssets.i10,
				0.55f * width, 0.8f * width - cellSize);
	}

	private void drawInfo2() {
		if (properties.worldType == IWorld.CLASSIC)
			LinesAssets.hScoreFont.draw(batch, LinesAssets.i3, 0.5f * width
					- LinesAssets.i3X / 2, 0.9f * width);
		else
			LinesAssets.hScoreFont.draw(batch, LinesAssets.i5, 0.5f * width
					- LinesAssets.i5X / 2, 0.9f * width);
		if (properties.worldType == IWorld.SURVIVAL)
			LinesAssets.hScoreFont.draw(batch, LinesAssets.i6, 0.5f * width
					- LinesAssets.i6X / 2, 0.8f * width);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 1f,
				cellSize * 6f, cellSize / 5, cellSize / 5);
		batch.draw(LinesAssets.balls[randomBall2], cellSize * 1.5f,
				cellSize * 5f, cellSize / 5, cellSize / 5);
		batch.draw(LinesAssets.balls[randomBall3], cellSize * 2f,
				cellSize * 5.5f, cellSize / 5, cellSize / 5);

		batch.draw(LinesAssets.arrow, width / 2 - cellSize / 2,
				cellSize * 5.5f, cellSize, cellSize);

		batch.draw(LinesAssets.balls[randomBall1], cellSize * 6f,
				cellSize * 6f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall2], cellSize * 6.5f,
				cellSize * 5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall3], cellSize * 7f,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize * 7f,
				cellSize * 6.5f, cellSize / 5, cellSize / 5);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 5.5f,
				cellSize * 6f, cellSize / 5, cellSize / 5);
		batch.draw(LinesAssets.balls[randomBall3], cellSize * 6f,
				cellSize * 5.5f, cellSize / 5, cellSize / 5);

		LinesAssets.hScoreFont.draw(batch, LinesAssets.i4, 0.5f * width
				- LinesAssets.i4X / 2, 0.3f * width);

	}

	private void drawInfo1() {
		LinesAssets.hScoreFont.draw(batch, LinesAssets.i1, 0.5f * width
				- LinesAssets.i1X / 2, 0.9f * width);
		LinesAssets.hScoreFont.draw(batch, LinesAssets.i1a, 0.5f * width
				- LinesAssets.i1aX / 2, 0.8f * width);

		batch.draw(LinesAssets.balls[randomBall2], cellSize, cellSize * 5.5f,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize, cellSize * 5,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall3], cellSize * 1.5f,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize * 2,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 2,
				cellSize * 4.5f, cellSize / 2, cellSize / 2);

		batch.draw(LinesAssets.balls[randomBall1], cellSize * 3, cellSize * 6,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 3,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 3, cellSize * 5,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 3,
				cellSize * 4.5f, cellSize / 2, cellSize / 2);

		batch.draw(LinesAssets.arrow, width / 2 - cellSize / 2, cellSize * 5,
				cellSize, cellSize);

		batch.draw(LinesAssets.balls[randomBall2], cellSize * 6,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize * 6, cellSize * 5,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall3], cellSize * 6.5f,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize * 7,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 8, cellSize * 4,
				cellSize / 2, cellSize / 2);

		batch.draw(LinesAssets.balls[randomBall1], cellSize * 8, cellSize * 6,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 8,
				cellSize * 5.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 8,
				cellSize * 4.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall1], cellSize * 8, cellSize * 5,
				cellSize / 2, cellSize / 2);

		LinesAssets.hScoreFont.draw(batch, LinesAssets.i2, 0.5f * width
				- LinesAssets.i2X / 2, 0.4f * width);

		batch.draw(LinesAssets.arrow, cellSize, cellSize * 2, cellSize,
				cellSize);

		batch.draw(LinesAssets.balls[randomBall2], cellSize * 3,
				cellSize * 2.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize * 3, cellSize * 2,
				cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall3], cellSize * 3.5f,
				cellSize * 2.5f, cellSize / 2, cellSize / 2);
		batch.draw(LinesAssets.balls[randomBall4], cellSize * 4,
				cellSize * 2.5f, cellSize / 2, cellSize / 2);

		LinesAssets.heroFont.draw(batch, "+5", 0.65f * width, 0.3f * width);
	}

	private void drawMenu() {
		drawPaused(0);
		batch.draw(LinesAssets.balls[0], menuBounds.x, menuBounds.y,
				menuBounds.width, menuBounds.height);
		// NB! Рисуется шире, т.к. есть пустое место в menuRegion
		batch.draw(LinesAssets.menuRegion, otherBounds.x - 0.15f * width,
				otherBounds.y, otherBounds.width + 0.3f * width,
				otherBounds.height + mainMenuBounds.height
						+ newGameBounds.height);

	}

	protected void drawGameOver(float delta) {
		gr++;

		drawDefault(delta);
		batch.draw(LinesAssets.balls[0], sInfoBounds.x, sInfoBounds.y,
				sInfoBounds.width, sInfoBounds.height);
		batch.setColor(1, 1f, 1f, 1f);

		LinesAssets.scoreFont.draw(batch, LinesAssets.s1, width / 2
				- LinesAssets.s1X / 2 - xO, height * 0.6f - LinesAssets.s1Y
				- diff + yO);
		TextBounds t2 = new TextBounds();
		String scoreStr = "";

		if (screenState == S_GAME_OVER) {
			scoreStr = LinesAssets.s2 + String.valueOf(world.score());
			t2 = LinesAssets.gametypeFont.getBounds(scoreStr);

		} else if (screenState == S_GAME_OVER_HIGHT_SCORE) {
			scoreStr = LinesAssets.s3 + String.valueOf(world.score());
			t2 = LinesAssets.gametypeFont.getBounds(scoreStr);
		}
		LinesAssets.scoreFont.draw(batch, scoreStr, width / 2 - t2.width / 2
				- xO, height * 0.4f - t2.height - diff + yO);

	}

	protected void drawRunning(float delta) {
		drawDefault(delta);
	}

	protected void drawPaused(int i) {
		if (i == 1)
			batch.setColor(1, 1, 1, 0.75f);
		else
			batch.setColor(1, 1, 1, 0.5f);
		batch.draw(LinesAssets.cellsRegion, 0 - xO, 0 - xO, width, width);
		batch.setColor(1, 1, 1, 1);
	}

	private void drawElements(float delta) {
		for (ArrayList<ParticleEffect> array : LinesAssets.ballClearEffects) {
			for (ParticleEffect p : array) {
				p.draw(batch, delta);
			}
		}
		for (ParticleEffect p : LinesAssets.addClearEffects) {
			p.draw(batch, delta);
		}
		for (MovingElement elem : movingElements) {
			if (elem.getClass() == MovingText.class) {
				MovingText t = (MovingText) elem;
				Color curColor = t.font.getColor();
				t.font.setColor(t.color);
				t.font.draw(batch, t.text, t.position.x, t.position.y);
				t.font.setColor(curColor);
			} else if (elem.getClass() == MovingElement.class) {
				batch.setColor(1, 1, 1, elem.transparency);
				batch.draw(LinesAssets.buttonNewBack, elem.position.x,
						elem.position.y, menuBounds.width * elem.size,
						menuBounds.height * elem.size);
			} else if (elem.getClass() == MovingPicture.class) {
				MovingPicture p = (MovingPicture) elem;
				batch.setColor(1, 1, 1, elem.transparency);
				batch.draw(p.picture, p.position.x, p.position.y, p.weight
						* elem.size, p.height * elem.size);
			}
		}
		batch.setColor(1, 1, 1, 1);
	}

	/******************************************************************************************/
	/******************** SCREEN ******************************************************/
	@Override
	public void resize(int width, int height) {
		// Gdx.app.log("aa", "resize");

	}

	@Override
	public void show() {
		// world.loadGame();
		// Gdx.app.log("aa", "show");
	}

	@Override
	public void hide() {
		// Gdx.app.log("aa", "hide");
	}

	@Override
	public void pause() {
		// Gdx.app.log("aa", "pause");
		world.moveBallToEnd();
		world.saveGame();
		game.platform.savePlayer();
		if (properties.worldType == SimpleLinesWorld.CLASSIC)
			game.platform.saveClassic();
		else if (properties.worldType == SimpleLinesWorld.HARD)
			game.platform.saveHardcore();
		else if (properties.worldType == SimpleLinesWorld.CAMPAIGN)
			game.platform.saveCampaign();
		batch.begin();
		drawPaused(0);
		batch.end();
	}

	@Override
	public void resume() {

		// Gdx.app.log("aa", "resume");
	}

	@Override
	public void dispose() {
		// Gdx.app.log("aa", "dispose");
		batch.dispose();

	}

	/******************************************************************************************/
	/******************** ELEMENT LISTENER ******************************************************/
	@Override
	public void richTarget(MovingElement elem) {
		movingElements.remove(elem);

	}

	@Override
	public void richStep(MovingElement elem) {
	}

	/******************************************************************************************/
	/******************** WORLD LISTENER ******************************************************/
	@Override
	public void gameOver(int worldType, boolean h) {
		game.platform.showAdMob(true);
		// Gdx.app.log("a", "GAMEOVER");
		if (h)
			screenState = S_GAME_OVER_HIGHT_SCORE;
		else
			screenState = S_GAME_OVER;
		world.clearSavedGame();
		if (game.actionResolver.getSignedInGPGS()) {
			submitScore();
		}

	}

	public void submitScore() {
		game.actionResolver
				.submitScoreGPGS(world.score(), properties.worldType);
		switch (properties.worldType) {
		case IWorld.CLASSIC:
			if (world.score() >= 1000)
				game.actionResolver
						.unlockAchievementGPGS(IWorld.A_CLASSIC_MASTER);
			if (world.score() >= 10000)
				game.actionResolver
						.unlockAchievementGPGS(IWorld.A_CLASSIC_HERO);
			break;
		case IWorld.HARD:
			if (world.score() >= 250)
				game.actionResolver
						.unlockAchievementGPGS(IWorld.A_HARDCORE_MASTER);
			if (world.score() >= 1000)
				game.actionResolver
						.unlockAchievementGPGS(IWorld.A_HARDCORE_HERO);
			break;
		case IWorld.SURVIVAL:
			if (world.score() >= 1000)
				game.actionResolver
						.unlockAchievementGPGS(IWorld.A_SURVIVAL_MASTER);
			if (world.score() >= 10000)
				game.actionResolver
						.unlockAchievementGPGS(IWorld.A_SURVIVAL_HERO);
			break;

		default:
			break;
		}
	}

	@Override
	public void addScore(int ternScore, int addScore, int lines, float x,
			float y, int colorNum) {
		if (game.actionResolver.getSignedInGPGS())
			game.actionResolver.incrementAchievementGPGS(
					IWorld.A_LINES_DESTROER, lines);
		Color color = renderer.getColor(colorNum);
		MovingText mText = new MovingText((x) * cellSize, (y) * cellSize,
				scorePosition.x, scorePosition.y, String.valueOf(ternScore),
				color, 60, this);
		movingElements.add(mText);
		if (addScore > 0) {
			MovingText addText = new MovingText((x) * cellSize, (y) * cellSize,
					"+" + String.valueOf(addScore), Color.WHITE, this);
			addText.addTarget((x) * cellSize, (y) * cellSize, 1, 1.5f, 30);
			addText.addTarget(scorePosition.x, scorePosition.y, 60);
			movingElements.add(addText);
		}

	}

	@Override
	public void moveBall() {
		// Gdx.app.log("a", "move");

	}

	@Override
	public void noWay() {
		// Gdx.app.log("a", "noWay");

	}

	@Override
	public void removeBall(int x, int y, int color) {
		clearedBalls[color]++;
		if (clearedBalls[color] >= LinesAssets.MAX_EFFECTS)
			clearedBalls[color] = 0;
		ParticleEffect p = LinesAssets.ballClearEffects.get(color).get(
				clearedBalls[color]);
		if (p.isComplete()) {
			p.setPosition(x * cellSize + 0.4f * ballSize, y * cellSize + 0.4f
					* ballSize);
			p.start();
		} else {
			for (ParticleEffect pe : LinesAssets.addClearEffects) {
				if (pe.isComplete()) {
					pe.setPosition(x * cellSize + 0.4f * ballSize, y * cellSize
							+ 0.4f * ballSize);
					pe.start();
					break;
				}
			}
		}
	}

	@Override
	public void removeBallLightning(int x, int y, int color) {
		for (int i = 0; i <= 10; i++) {
			MovingPicture mPic = new MovingPicture((x) * cellSize, (y)
					* cellSize, LinesAssets.balls[color], cellSize / 10,
					cellSize / 10, this);
			mPic.addTarget((x) * cellSize * rand.nextInt(3), (y) * cellSize
					* rand.nextInt(3), 1, 1, 60);
			movingElements.add(mPic);
		}

	}

	@Override
	public void startNewGame() {
		movingElements.clear();
		game.platform.showAdMob(false);

	}

	@Override
	public void noMoveAvaliable() {
		// TODO Auto-generated method stub

	}

	@Override
	public int state() {
		return screenState;
	}

	@Override
	public void armageddon(int x, int y) {
		// ParticleEffect p = LinesAssets.armageddon;
		// p.setPosition(x * cellSize + 0.5f * renderer.ballSize(), y * cellSize
		// + 0.5f * renderer.ballSize());
		// p.start();
	}

	@Override
	public void addTernSize(int x, int y, int color) {

	}

	@Override
	public void noEnoughMagic() {

	}

	@Override
	public void useMagic(int i) {
		if (game.actionResolver.getSignedInGPGS())
			game.actionResolver.incrementAchievementGPGS(IWorld.A_MAGIC_MASTER,
					1);
	}

	@Override
	public void nextLevel(float ternSize) {
		// TODO Auto-generated method stub
	}

	@Override
	public void startRain(int x, int y) {
	}
}