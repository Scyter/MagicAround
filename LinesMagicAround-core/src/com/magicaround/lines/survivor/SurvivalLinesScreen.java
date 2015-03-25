package com.magicaround.lines.survivor;

import java.text.NumberFormat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.magicaround.lines.LinesAssets;
import com.magicaround.lines.MALinesGame;
import com.magicaround.lines.MovingText;
import com.magicaround.lines.WorldProperties;
import com.magicaround.lines.simple.SimpleLinesScreen;

public class SurvivalLinesScreen extends SimpleLinesScreen {

	public Rectangle skillsBounds;
	public Rectangle s1Bounds;
	public Rectangle s2Bounds;
	public Rectangle s3Bounds;
	public Rectangle s4Bounds;
	public Rectangle s5Bounds;

	public Vector2 levelPosition;
	public Vector2 magicPosition;
	public Vector2 playerInfoPosition;

	public Vector2 gameTypePositionSurvival;

	public SurvivalLinesScreen(MALinesGame g, WorldProperties p) {
		super(g, p);
		skillsBounds = new Rectangle(width - 0.32f * diff, -0.32f * diff,
				diff * 0.3f, diff * 0.3f);
		s1Bounds = new Rectangle(width - 0.6f * diff, -0.22f * diff,
				diff * 0.2f, diff * 0.2f);

		s2Bounds = new Rectangle(width - 0.6f * diff, -0.44f * diff,
				diff * 0.2f, diff * 0.2f);

		// new Rectangle(width - 0.90f * diff, -0.22f * diff,
		// diff * 0.2f, diff * 0.2f);

		s3Bounds = new Rectangle(width - 0.82f * diff, -0.16f * diff,
				diff * 0.15f, diff * 0.15f);
		s4Bounds = new Rectangle(width - 0.48f * diff, -0.35f * diff,
				diff * 0.15f, diff * 0.15f);
		s5Bounds = new Rectangle(width - 0.65f * diff, -0.35f * diff,
				diff * 0.15f, diff * 0.15f);
		// sInfoBounds = new Rectangle(width - 0.82f * diff, -0.35f * diff,
		// diff * 0.15f, diff * 0.15f);//NB!
		magicPosition = new Vector2(width - 0.16f * diff, -0.35f * diff);
		levelPosition = new Vector2(width * 0.5f, -0.02f * diff);
		playerInfoPosition = new Vector2(width - 0.45f * diff, -0.25f * diff);

		gameTypePositionSurvival = new Vector2(width - 0.02f * diff, width
				+ 0.10f * diff);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(1);
		String ts = format.format(properties.ternSize);
		ternSizeBounds = LinesAssets.nextTernSizeFont.getBounds(ts);
	}

	@Override
	protected void newWorld() {
		super.newWorld();
		properties.colorSize = 7;
		properties.lineSize = 5;
		properties.ternSize = 2;
		world = new SurvivalLinesWorld(this, width, height, properties,
				game.player);
		renderer = new SurvivalLinesRenderer(this);
		screenState = S_GAME_RUNNING;
		if (world.loadGame() == 0)
			world.startGame();
	}

	@Override
	protected void updateTouch(float deltaTime) {
		super.updateTouch(deltaTime);
		// if (skillsBounds.contains(new Vector2(touchPoint.x, touchPoint.y))) {
		// world.removeBallSelection();
		// world.removeSkillSelection();
		// if (screenState != S_GAME_SELECT_SKILL) {
		// screenState = S_GAME_SELECT_SKILL;
		// world.moveBallToEnd();
		// } else {
		// screenState = S_GAME_RUNNING;
		// }
		// }
		if (s1Bounds.contains(new Vector2(touchPoint.x, touchPoint.y))) {
			world.moveBallToEnd();
			world.selectSkill(1);
			screenState = S_GAME_RUNNING;
		} else if (s2Bounds.contains(new Vector2(touchPoint.x, touchPoint.y))) {
			world.moveBallToEnd();
			world.selectSkill(5);
			screenState = S_GAME_RUNNING;
		}

	}

	@Override
	protected void updateTouchSelectSkill() {
		// if (screenState == S_GAME_SELECT_SKILL) {
		// if (s1Bounds.contains(touch)) {
		// world.selectSkill(1);
		// screenState = S_GAME_RUNNING;
		// }
		// if (s2Bounds.contains(touch)) {
		// world.selectSkill(2);
		// screenState = S_GAME_RUNNING;
		// }
		// if (s3Bounds.contains(touch)) {
		// world.selectSkill(3);
		// screenState = S_GAME_RUNNING;
		// }
		// if (s4Bounds.contains(touch)) {
		// world.selectSkill(4);
		// screenState = S_GAME_RUNNING;
		// }
		// if (s5Bounds.contains(touch)) {
		// world.selectSkill(5);
		// screenState = S_GAME_RUNNING;
		// }
		// if ((touchPoint.x < 0) || (touchPoint.y < 0)
		// || (touchPoint.x >= cellSize * properties.poleXSize)
		// || (touchPoint.y >= cellSize * properties.poleYSize))
		// return;
		// else
		// screenState = S_GAME_RUNNING;
		// }
	}

	@Override
	protected void drawDefault(float delta) {
		super.drawDefault(delta);
		batch.draw(LinesAssets.skill1, s1Bounds.x, s1Bounds.y, s1Bounds.width,
				s1Bounds.height);
		batch.draw(LinesAssets.skill2, s2Bounds.x, s2Bounds.y, s2Bounds.width,
				s2Bounds.height);
	}

	@Override
	public void pause() {
		batch.begin();
		drawPaused(0);
		batch.end();
		world.saveGame();
		game.platform.savePlayer();
		game.platform.saveSurvival();
	}

	// @Override
	// public void addColorSize(int x, int y) {
	// MovingPicture elem = new MovingPicture(x * cellSize, y * cellSize,
	// LinesAssets.balls[properties.colorSize], this);
	// elem.addTarget(colorSizePosition.x + (properties.colorSize - 1)
	// * (infoBallSize / 5 + 1), colorSizePosition.y, 1, 1, 60);
	// elem.weight = infoBallSize / 5;
	// elem.height = infoBallSize / 5;
	//
	// movingElements.add(elem);
	//
	// MovingPicture elem2 = new MovingPicture(colorSizePosition.x
	// + (properties.colorSize - 1) * (infoBallSize / 5 + 1),
	// colorSizePosition.y, LinesAssets.balls[9], this);
	// elem2.size = 0;
	// elem2.addTarget(colorSizePosition.x + (properties.colorSize - 1)
	// * (infoBallSize / 5 + 1), colorSizePosition.y, 1, 1, 60);
	// elem2.weight = infoBallSize / 5;
	// elem2.height = infoBallSize / 5;
	// movingElements.add(elem2);
	//
	// }
	//
	// @Override
	// public void addTernSize(int x, int y, int color) {
	// MovingPicture elem = new MovingPicture(x * cellSize, y * cellSize,
	// LinesAssets.balls[color], this);
	// elem.addTarget(ternSizePosition.x + (properties.ternSize - 1)
	// * (infoBallSize + 1), ternSizePosition.y, 1, 1, 60);
	// elem.weight = infoBallSize;
	// elem.height = infoBallSize;
	//
	// movingElements.add(elem);
	//
	// MovingPicture elem2 = new MovingPicture(ternSizePosition.x
	// + (properties.ternSize - 1) * (infoBallSize + 1),
	// ternSizePosition.y, LinesAssets.balls[9], this);
	// elem2.size = 0;
	// elem2.addTarget(ternSizePosition.x + (properties.ternSize - 1)
	// * (infoBallSize + 1), ternSizePosition.y, 1, 1, 60);
	// elem2.weight = infoBallSize;
	// elem2.height = infoBallSize;
	// movingElements.add(elem2);
	//
	// }
	//
	// @Override
	// public void addLineSize(int x, int y, int color) {
	// MovingPicture elem = new MovingPicture(x * cellSize, y * cellSize,
	// LinesAssets.balls[color], this);
	// elem.addTarget(lineSizePosition.x + (properties.lineSize - 1)
	// * (infoBallSize / 5 + 1), lineSizePosition.y, 1, 1, 60);
	// elem.weight = infoBallSize / 5;
	// elem.height = infoBallSize / 5;
	//
	// movingElements.add(elem);
	//
	// MovingPicture elem2 = new MovingPicture(lineSizePosition.x
	// + (properties.lineSize - 1) * (infoBallSize / 5 + 1),
	// lineSizePosition.y, LinesAssets.balls[9], this);
	// elem2.size = 0;
	// elem2.addTarget(lineSizePosition.x + (properties.lineSize - 1)
	// * (infoBallSize / 5 + 1), lineSizePosition.y, 1, 1, 60);
	// elem2.weight = infoBallSize / 5;
	// elem2.height = infoBallSize / 5;
	// movingElements.add(elem2);
	//
	// }

	@Override
	public void noEnoughMagic() {

		MovingText nEM = new MovingText(width * 0.5f - LinesAssets.s8X / 2,
				height * 0.4f - LinesAssets.s8Y / 2, LinesAssets.s8, Color.RED,
				this);
		nEM.addTarget(width * 0.5f - LinesAssets.s8X / 2, height * 0.3f
				- LinesAssets.s8Y / 2, 60);
		// nEM.addTarget(weight * 0.6f - t1.width / 2, weight * 0.4f, 10);
		// nEM.addTarget(weight * 0.4f - t1.width / 2, weight * 0.6f, 10);
		// nEM.addTarget(weight * 0.6f - t1.width / 2, weight * 0.6f, 10);
		// nEM.addTarget(weight * 0.5f - t1.width / 2, weight * 0.5f, 10);
		nEM.font = LinesAssets.scoreFont;
		movingElements.add(nEM);
	}

	@Override
	public void nextLevel(float ternSize) {
		super.nextLevel(ternSize);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(1);
		String ts = format.format(properties.ternSize);
		ternSizeBounds = LinesAssets.nextTernSizeFont.getBounds(ts);
		ternSizeBounds.width += diff / 50;

		// ternSizePosition = new Vector2((float) (3 * diff / 100),
		// (float) (cellSize * properties.poleYSize + 0.2f * (diff)));
	}

}
