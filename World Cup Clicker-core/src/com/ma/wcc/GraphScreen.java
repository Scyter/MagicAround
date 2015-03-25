package com.ma.wcc;

import java.util.ArrayList;

import magicaround.GameResult;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GraphScreen extends ScreenClass {
	public static final int axesSize = 1;
	public static final float lineSize = 3;

	WorldCupClicker game;
	float width;
	float height;
	OrthographicCamera camera;
	Vector3 touchPoint;
	Vector2 touch;
	SpriteBatch batch;
	float drawen;

	ShapeRenderer shapeRenderer;

	float gStartX;
	float gStartY;
	float pointerDiff;

	float xSize;
	float ySize;
	// private boolean running = false;

	int littlePosition;
	int bigPosition;
	int positionSize;
	float numDiffer;

	int stepSize;
	ArrayList<Integer> data;

	Rectangle back;
	Rectangle leaderboard;

	public GraphScreen(WorldCupClicker g) {
		game = g;

		data = new ArrayList<Integer>();
		for (GameResult r : g.player.results) {
			data.add((int) r.result);
		}
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		touchPoint = new Vector3();
		touch = new Vector2();
		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		touchPoint = new Vector3();
		batch = new SpriteBatch();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		drawen = 0;
		if (data.size() == 0) {
			data.add(0);
			data.add(0);
		} else if (data.size() == 1)
			data.add(data.get(0));
		numDiffer = 0.08f;
		calculate();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		back = new Rectangle(0.25f * width, 0.87f * height, 0.1f * height,
				0.1f * height);
		leaderboard = new Rectangle(0.65f * width, 0.87f * height,
				0.1f * height, 0.1f * height);

	}

	private void calculate() {
		xSize = width * (0.95f - numDiffer);
		ySize = height * 0.80f;
		gStartX = width * numDiffer;
		gStartY = height * 0.05f;

		littlePosition = data.get(0);
		bigPosition = data.get(0);
		positionSize = 1;
		for (int d : data) {
			if (d < littlePosition)
				littlePosition = d;
			if (d > bigPosition)
				bigPosition = d;
		}
		littlePosition--;
		bigPosition++;
		positionSize = bigPosition - littlePosition;
		if (bigPosition >= 10000) {
			numDiffer = 0.10f;
			xSize = width * (0.95f - numDiffer);
			gStartX = width * numDiffer;
		}
		if (bigPosition >= 100000) {
			numDiffer = 0.12f;
			xSize = width * (0.95f - numDiffer);
			gStartX = width * numDiffer;
		}
		pointerDiff = (float) ((xSize > ySize) ? (ySize * 0.02)
				: (xSize * 0.02));
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void update(float delta) {
		if (data.size() < 10)
			drawen += delta * 10;
		else
			drawen += delta * 10 * ((int) data.size() / 10);
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			touch = new Vector2(touchPoint.x, touchPoint.y);
			if (leaderboard.contains(touch)) {
				game.platform.getLeaderboardGPGS(1);
			} else if (back.contains(touch)) {
				game.setScreen(new MainMenuScreen(game));
			}
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.enableBlending();
		batch.begin();
		batch.draw(Assets.rating, leaderboard.x, leaderboard.y,
				leaderboard.width, leaderboard.height);
		batch.setColor(1, 1, 1, 0.75f);
		batch.draw(Assets.stars.get(1), leaderboard.x, leaderboard.y,
				leaderboard.width, leaderboard.height);
		batch.setColor(1, 1, 1, 1);
		batch.draw(Assets.arrow, back.x + back.width, back.y, -1 * back.width,
				back.height);
		batch.end();
		drawAxes();
		drawScale();
		drawGraph();

	}

	private void drawAxes() {
		batch.begin();
		batch.draw(Assets.axe, gStartX, gStartY, xSize, 5);
		batch.draw(Assets.axe, gStartX, gStartY, 0, 0, ySize, 5, 1, 1, 90);
		batch.draw(Assets.nullPoint, gStartX, gStartY, 5, 5);
		batch.draw(Assets.axeEnd, gStartX + xSize, gStartY, 5, 5);
		batch.draw(Assets.axeEnd, gStartX, gStartY + ySize, 0, 0, 5, 5, 1, 1,
				90);
		batch.end();

	}

	private void drawScale() {
		float xWay = xSize / (data.size() - 1);
		float yWay = ySize / positionSize;
		batch.begin();
		for (int i = 1; i < positionSize;) {
			Assets.graphFont.draw(batch, getShortString(littlePosition + i),
					gStartX - (float) ((numDiffer - 0.01) * width), gStartY + i
							* yWay + 0.01f * height);
			if (positionSize <= 10)
				i++;
			else
				i += (int) positionSize / 10;
		}
		batch.end();
		setAxesPaint();
		for (int i = 1; i <= positionSize;) {
			shapeRenderer.line(gStartX, gStartY + i * yWay, gStartX + xSize,
					gStartY + i * yWay);
			if (positionSize <= 10)
				i++;
			else
				i += (int) positionSize / 10;
		}
		for (int i = 1; i < data.size() - 1;) {
			shapeRenderer.line(gStartX + i * xWay, gStartY + 5, gStartX + i
					* xWay, gStartY + ySize - 5);
			if (data.size() <= 10)
				i++;
			else
				i += (int) data.size() / 10;
		}
		endPaint();

	}

	private void drawGraph() {
		ArrayList<Integer> d = data;
		float lineXS;
		float lineYS;
		float lineXE;
		float lineYE;
		float colorNum = 0;

		float xWay = xSize / (d.size() - 1);
		float yWay = ySize / positionSize;
		// lineXS = gStartX;
		// lineYS = gStartY + (bigPosition - d.get(0)) * yWay;
		// lineXE = gStartX + xWay;
		// lineYE = gStartY + (bigPosition - d.get(1)) * yWay;
		setGraphPaint();
		// shapeDebugger.line(lineXS, lineYS, lineXE, lineYE,
		// getColorByNum(colorNum), getColorByNum(colorNum + 1));
		// colorNum++;
		for (int i = 0; (i < d.size() - 1) && (i <= drawen); i++) {
			lineXS = gStartX + i * xWay;
			lineYS = gStartY + (d.get(i) - littlePosition) * yWay;
			lineXE = gStartX + i * xWay + xWay;
			lineYE = gStartY + (d.get(i + 1) - littlePosition) * yWay;
			shapeRenderer.line(lineXS, lineYS, lineXE, lineYE,
					getColorByNum(colorNum), getColorByNum(colorNum + 1));
			colorNum++;
		}
		endPaint();

	}

	private Color getColorByNum(float num) {
		if (num % 2 == 0)
			return Color.YELLOW;
		else
			return Color.GREEN;
	}

	private void setAxesPaint() {
		Gdx.gl20.glLineWidth(axesSize);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.begin(ShapeType.Line);
	}

	private void setGraphPaint() {
		Gdx.gl20.glLineWidth(lineSize);
		shapeRenderer.setColor(Color.GREEN);
		// shapeRanderer.
		shapeRenderer.begin(ShapeType.Line);
	}

	private void endPaint() {
		shapeRenderer.end();
	}

	private String getShortString(int res) {
		String resString = "";
		if (res < 1000) {
			resString = String.valueOf((int) res);
		} else if ((res >= 1000) && (res < 1000000)) {
			String t1 = String.valueOf((int) (res / 1000));
			String t2 = String.valueOf((int) (((int) (res)) % 1000) / 100);
			resString = t1 + "," + t2 + "K";
		} else if ((res >= 1000000) && (res < 1000000000)) {
			resString = String.valueOf((int) (res / 1000000)) + ","
					+ String.valueOf((int) (((int) (res)) % 1000000) / 100000)
					+ "M";
		} else
			resString = String.valueOf((int) (res / 1000000000)) + "MM";
		return resString;
	}
}
