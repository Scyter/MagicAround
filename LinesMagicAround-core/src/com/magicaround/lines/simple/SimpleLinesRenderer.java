package com.magicaround.lines.simple;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.magicaround.lines.Ball;
import com.magicaround.lines.Cell;
import com.magicaround.lines.LinesAssets;

public class SimpleLinesRenderer implements IRenderer {

	protected SimpleLinesScreen screen;
	// public OrthographicCamera camera;
	public SpriteBatch batch;
	public int height;
	public int weight;
	public int diff;
	public SimpleLinesWorld world;

	public int backNumber;
	protected float cellSize;
	protected float ballSize;
	protected float infoBallSize;

	public SimpleLinesRenderer(SimpleLinesScreen screen) {
		this.screen = screen;
		// this.camera = screen.camera;
		this.batch = screen.batch;
		this.weight = screen.width;
		this.height = screen.height;
		this.diff = (height / weight <= 1.7f) ? (height - weight) : weight;
		setWorld();
		this.cellSize = screen.cellSize;
		this.ballSize = screen.ballSize;
		this.infoBallSize = screen.infoBallSize;
		backNumber = screen.rand.nextInt(LinesAssets.backs.size());
	}

	protected void setWorld() {
		this.world = (SimpleLinesWorld) screen.world;

	}

	@Override
	public void render() {

		renderBackground();
		batch.enableBlending();
		batch.begin();
		renderGameType();
		renderScore();
		renderPole();
		renderSeceltedCell();
		renderSellsForMove();
		renderSkillSells();
		renderSpelling();
		renderNextTern();
		renderLineSize();
		renderColorSize();
		renderBalls();
		renderLightning();
		renderMagic();
		renderLevelInfo();
		batch.end();
		batch.enableBlending();
	}

	protected void renderGameType() {
		String s = "";
		if (world.properties.worldType == SimpleLinesWorld.CLASSIC)
			s += LinesAssets.s4;
		else if (world.properties.worldType == SimpleLinesWorld.HARD)
			s += LinesAssets.s5;
		else if (world.properties.worldType == SimpleLinesWorld.SURVIVAL) {
			s += LinesAssets.s6;
		} else if (world.properties.worldType == SimpleLinesWorld.CAMPAIGN)
			s += LinesAssets.s7;
		TextBounds t1 = LinesAssets.scoreFont.getBounds(s);
		LinesAssets.scoreFont.draw(batch, s, screen.gameTypePosition.x
				- t1.width, screen.gameTypePosition.y + t1.height);

	}

	protected void renderColorSize() {
		for (int i = 1; i <= world.properties.colorSize; i++) {
			batch.draw(LinesAssets.balls[i],
					(float) (screen.colorSizePosition.x + (i - 1)
							* (infoBallSize / 5 + 1)),
					screen.colorSizePosition.y, infoBallSize / 5,
					infoBallSize / 5);
		}
	}

	protected void renderLineSize() {
		for (int i = 1; i <= world.properties.lineSize; i++) {
			batch.draw(LinesAssets.magicTexture,
					(float) (screen.lineSizePosition.x + (i - 1)
							* (infoBallSize / 5 + 1)),
					screen.lineSizePosition.y, infoBallSize / 5,
					infoBallSize / 5);
		}
	}

	protected void renderNextTern() {
		batch.setColor(1, 1, 1, 0.2f);
		for (int j = 0; j < world.properties.ternSize; j++) {
			batch.draw(LinesAssets.balls[9],
					(float) (screen.ternSizeBounds.width
							+ screen.ternSizePosition.x + j
							* (infoBallSize + 1)), screen.ternSizePosition.y,
					infoBallSize, infoBallSize);
		}
		renderPartTern();
		batch.setColor(1, 1, 1, 1f);
		for (int i = 0; i < world.ballsNextTern.size(); i++) {
			Ball b = world.ballsNextTern.get(i);
			batch.draw(LinesAssets.balls[b.polePosition.color
					- world.MAXIMUM_COLOR_SIZE], (b.polePosition.x + 0.5f)
					* this.cellSize - cellSize * b.size / 200,
					(b.polePosition.y + 0.5f) * this.cellSize - cellSize
							* b.size / 200, ballSize * b.size / 100, ballSize
							* b.size / 100);
			batch.draw(LinesAssets.balls[b.polePosition.color
					- world.MAXIMUM_COLOR_SIZE],
					(float) (screen.ternSizeBounds.width
							+ screen.ternSizePosition.x + i
							* (infoBallSize + 1)), screen.ternSizePosition.y,
					infoBallSize, infoBallSize);
		}

	}

	protected void renderPartTern() {
	}

	protected void renderSpelling() {
	}

	protected void renderSellsForMove() {
		batch.setColor(0.5f, 0.5f, 0.5f, 0.3f);
		for (Cell c : world.cellsForMove) {
			batch.draw(LinesAssets.cellsRegion, (int) c.x * this.cellSize, c.y
					* this.cellSize, ballSize, ballSize);
		}

	}

	protected void renderBalls() {

		for (Ball b : world.balls) {
			switch (b.getState()) {
			case Ball.SMALL:
			case Ball.MOVING:
			case Ball.MOVING1:
			case Ball.MOVING2:
			case Ball.MOVING3:
			case Ball.MOVING4:
			case Ball.NORMAL:
			case Ball.GROWING:
			case Ball.NO_WAY:
			case Ball.RAIN:
			case Ball.MOVED:
				batch.setColor(1, 1, 1, (float) b.visibility / 100);
				batch.draw(LinesAssets.balls[b.polePosition.color],
						(b.polePosition.x + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.xDelta * cellSize / 100,
						(b.polePosition.y + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.yDelta * cellSize / 100,
						ballSize * b.size / 100, ballSize * b.size / 100);
				break;

			case Ball.MOVING5:
				batch.setColor(1, 1, 1, (float) b.visibility / 100);
				batch.draw(LinesAssets.balls[b.polePosition.color],
						(b.polePosition.x + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.mDelta * cellSize / 100,
						(b.polePosition.y + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.yDelta * cellSize / 100,
						ballSize * b.size / 100, ballSize * b.size / 100);
				batch.draw(LinesAssets.balls[b.polePosition.color],
						(b.polePosition.x + 0.5f) * this.cellSize - cellSize
								* b.size / 200 - b.mDelta * cellSize / 100,
						(b.polePosition.y + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.yDelta * cellSize / 100,
						ballSize * b.size / 100, ballSize * b.size / 100);
				break;
			default:
				break;
			}
		}
		for (Ball b : world.balls) {
			if (b.getState() == Ball.SELECTED) {
				batch.setColor(1, 1, 1, 1f);
				batch.draw(LinesAssets.balls[b.polePosition.color],
						(b.polePosition.x + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.xDelta * cellSize / 100,
						(b.polePosition.y + 0.5f) * this.cellSize - cellSize
								* b.size / 200 + b.yDelta * cellSize / 100,
						ballSize * b.size / 100, ballSize * b.size / 100);
			}
		}

	}

	protected void renderSkillSells() {
	}

	protected void renderSkills() {
	}

	protected void renderLevelInfo() {
	}

	protected void renderMagic() {
	}

	protected void renderLightning() {
	}

	protected void renderScore() {
		LinesAssets.scoreFont.draw(batch,
				LinesAssets.s2 + " " + String.valueOf(world.score),
				screen.scorePosition.x, screen.scorePosition.y);
		String s = "";
		if (world.properties.worldType == SimpleLinesWorld.CLASSIC)
			s += String.valueOf(world.player.c_h_score);
		else if (world.properties.worldType == SimpleLinesWorld.HARD)
			s += String.valueOf(world.player.h_h_score);
		else if (world.properties.worldType == SimpleLinesWorld.SURVIVAL)
			s += String.valueOf(world.player.s_h_score);
		else if (world.properties.worldType == SimpleLinesWorld.CAMPAIGN)
			s += String.valueOf(world.player.m_h_score);
		LinesAssets.hScoreFont.draw(batch, LinesAssets.s3 + " " + s,
				screen.hScorePosition.x, screen.hScorePosition.y);

	}

	public void renderBackground() {
		batch.disableBlending();
		batch.begin();
		batch.setColor(1, 1, 1, 0.1f);
		// float w = this.weight > LinesAssets.curBackTexture.getRegionWidth() ?
		// this.weight
		// : LinesAssets.curBackTexture.getRegionWidth();
		// float h = this.height > LinesAssets.curBackTexture.getRegionHeight()
		// ? this.height
		// : LinesAssets.curBackTexture.getRegionHeight();
		batch.draw(LinesAssets.curBackTexture, -1 * screen.xO - 1, -1
				* screen.yO - 1, height + 5, height + 5);
		batch.end();

	}

	protected void renderSeceltedCell() {
		if (world.cellIsSelected) {
			batch.setColor(1, 0, 0, 0.7f);
			batch.draw(LinesAssets.cellsRegion, world.selectedCell.x
					* this.cellSize, world.selectedCell.y * this.cellSize,
					ballSize, ballSize);
		}
	}

	public void renderPole() {
		batch.setColor(1, 1, 1, 0.4f);
		for (int i = 0; i < screen.properties.poleXSize; i++)
			for (int j = 0; j < screen.properties.poleXSize; j++) {
				batch.draw(LinesAssets.cellsRegion, i * this.cellSize, j
						* this.cellSize, ballSize, ballSize);
			}

	}

	@Override
	public Color getColor(int i) {
		Color c = new Color();
		switch (i) {
		case 1:
			c = new Color(1, 0, 0, 1);
			break;
		case 2:
			c = new Color(1, 0.5f, 0f, 1);
			break;
		case 3:
			c = new Color(1, 1, 0, 1);
			break;
		case 4:
			c = new Color(0, 1, 0, 1);
			break;
		case 5:
			c = new Color(0, 1, 1, 1);
			break;
		case 6:
			c = new Color(0, 0, 1, 1);
			break;
		case 7:
			c = new Color(1, 0, 1, 1);
			break;
		case 8:
			c = new Color(1f, 1f, 1f, 1);
			break;
		case 9:
			c = new Color(0.4f, 0.4f, 0.4f, 1);
			break;
		case 666:
			return randomColor();
		default:
			c = new Color(1f, 1f, 1f, 1);
			break;
		}
		return c;
	}

	// Неплохо бы привести этот метов в
	// порядок и доработать до ума
	private Color randomColor() {
		int num = 1000 / 20 % 300;
		float r, g, b = 0;
		switch (screen.rand.nextInt(6)) {
		case 0:
			r = num % 10;
			g = num / 10;
			b = num / 100;
			break;
		case 1:
			r = num % 10;
			b = num / 10;
			g = num / 100;
			break;
		case 2:
			g = num % 10;
			r = num / 10;
			b = num / 100;
			break;
		case 3:
			g = num % 10;
			b = num / 10;
			r = num / 100;
			break;
		case 4:
			b = num % 10;
			r = num / 10;
			g = num / 100;
			break;
		case 5:
			b = num % 10;
			g = num / 10;
			r = num / 100;
			break;

		default:
			r = num % 10;
			g = num / 10;
			b = num / 100;
			break;
		}

		return new Color(r / 10, g / 10, b / 10, 1);
	}

	@Override
	public void backNumber(int back) {
		this.backNumber = back;

	}

}
