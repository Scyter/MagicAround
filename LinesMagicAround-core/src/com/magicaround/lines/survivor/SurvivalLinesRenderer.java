package com.magicaround.lines.survivor;

import java.text.NumberFormat;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.magicaround.lines.Ball;
import com.magicaround.lines.Constants;
import com.magicaround.lines.Cell;
import com.magicaround.lines.LinesAssets;
import com.magicaround.lines.simple.SimpleLinesRenderer;

public class SurvivalLinesRenderer extends SimpleLinesRenderer {
	SurvivalLinesWorld newWorld;
	SurvivalLinesScreen newScreen;

	public SurvivalLinesRenderer(SurvivalLinesScreen screen) {
		super(screen);
		newScreen = screen;

	}

	@Override
	protected void setWorld() {
		super.setWorld();
		this.newWorld = (SurvivalLinesWorld) screen.world;
	}

	@Override
	protected void renderGameType() {
		String s = "";
		s += LinesAssets.s9 + " " + String.valueOf(newWorld.level);
		TextBounds t1 = LinesAssets.gametypeFont.getBounds(s);
		LinesAssets.gametypeFont.draw(batch, s,
				newScreen.gameTypePositionSurvival.x - t1.width,
				newScreen.gameTypePositionSurvival.y + t1.height);
		s = LinesAssets.s10 + " " + String.valueOf(newWorld.tern) + " "
				+ LinesAssets.s11;
		t1 = LinesAssets.hScoreFont.getBounds(s);
		LinesAssets.hScoreFont
				.draw(batch, s,
						newScreen.gameTypePositionSurvival.x - t1.width,
						newScreen.gameTypePositionSurvival.y + t1.height
								- 0.08f * diff);
	}

	@Override
	protected void renderMagic() {
		batch.setColor(1, 1, 1, 0.3f);
		batch.draw(LinesAssets.magicRegion[100], newScreen.skillsBounds.x
				+ newScreen.skillsBounds.width, newScreen.skillsBounds.y
				+ newScreen.skillsBounds.height, (-1)
				* newScreen.skillsBounds.width, (-1)
				* newScreen.skillsBounds.height);
		batch.setColor(1, 1, 1, 1);
		int i = (int) newWorld.curMagic * 100
				/ Constants.magicMax[world.player.level];
		if (i < 0)
			i = 0;
		if (i > 100)
			i = 100;
		batch.draw(LinesAssets.magicRegion[i], newScreen.skillsBounds.x
				+ newScreen.skillsBounds.width, newScreen.skillsBounds.y
				+ newScreen.skillsBounds.height * i / 100, (-1)
				* newScreen.skillsBounds.width, (-1)
				* newScreen.skillsBounds.height * i / 100);
		String magicString = String.valueOf((int) newWorld.curMagic) + "/"
				+ String.valueOf(Constants.magicMax[newWorld.player.level]);
		TextBounds t1 = LinesAssets.hScoreFont.getBounds(magicString);
		LinesAssets.hScoreFont.draw(batch, magicString,
				newScreen.magicPosition.x - t1.width / 2,
				newScreen.magicPosition.y);
		// if (newScreen.screenState == SurvivalLinesScreen.S_GAME_SELECT_SKILL)
		// {
		// renderSkills();
		// }
	}

	@Override
	protected void renderSkills() {
		batch.setColor(1, 1, 1, 1);
		batch.draw(LinesAssets.skill1, newScreen.s1Bounds.x,
				newScreen.s1Bounds.y, newScreen.s1Bounds.width,
				newScreen.s1Bounds.height);
		batch.draw(LinesAssets.skill2, newScreen.s2Bounds.x,
				newScreen.s2Bounds.y, newScreen.s2Bounds.width,
				newScreen.s2Bounds.height);
		batch.draw(LinesAssets.skill3, newScreen.s3Bounds.x,
				newScreen.s3Bounds.y, newScreen.s3Bounds.width,
				newScreen.s3Bounds.height);
		batch.draw(LinesAssets.skill4, newScreen.s4Bounds.x,
				newScreen.s4Bounds.y, newScreen.s4Bounds.width,
				newScreen.s4Bounds.height);
		batch.draw(LinesAssets.skill5, newScreen.s5Bounds.x,
				newScreen.s5Bounds.y, newScreen.s5Bounds.width,
				newScreen.s5Bounds.height);
		batch.draw(LinesAssets.sInfo, newScreen.sInfoBounds.x,
				newScreen.sInfoBounds.y, newScreen.sInfoBounds.width,
				newScreen.sInfoBounds.height);

	}

	@Override
	protected void renderSkillSells() {
		batch.setColor(1, 1, 1, 0.3f);
		for (Cell c : newWorld.cellsToActivateSkill) {
			batch.draw(LinesAssets.cellsRegion, (int) c.x * this.cellSize, c.y
					* this.cellSize, ballSize, ballSize);
		}

	}

	@Override
	protected void renderScore() {
		super.renderScore();
		String s = "";
		batch.setColor(1, 1, 1, 1f);
		batch.draw(LinesAssets.levelEndRegion, screen.expPosition.x + 0.1f
				* diff, screen.expPosition.y, diff * (-0.05f), diff * (0.05f));
		batch.draw(LinesAssets.levelEndRegion, screen.expPosition.x + 0.9f
				* weight, screen.expPosition.y, diff * (0.05f), diff * (0.05f));
		batch.setColor(1, 1, 1, 0.1f);
		batch.draw(LinesAssets.levelRegion, screen.expPosition.x + 0.1f * diff,
				screen.expPosition.y, weight * (0.9f) - 0.05f * diff,
				diff * (0.05f));
		batch.setColor(1, 1, 1, 1f);
		batch.draw(LinesAssets.levelRegion, screen.expPosition.x + 0.1f * diff,
				screen.expPosition.y, (weight * (0.9f) - 0.05f * diff)
						* world.player.experience
						/ Constants.nextExpForLevel[world.player.level],
				diff * (0.05f));
		s = String.valueOf(world.player.experience) + "/"
				+ String.valueOf(Constants.nextExpForLevel[world.player.level]);
		TextBounds t1 = LinesAssets.hScoreFont.getBounds(s);
		LinesAssets.hScoreFont.draw(batch, s, screen.expPosition.x + weight / 2
				- t1.width / 2, screen.expPosition.y + 0.09f * diff);
		LinesAssets.heroFont.draw(batch,
				LinesAssets.s12 + " " + String.valueOf(world.player.level),
				screen.heroLevelPosition.x, screen.heroLevelPosition.y);
	}

	@Override
	protected void renderPartTern() {
		batch.draw(
				LinesAssets.magicRegion[((int) (newWorld.addBall * 100)) % 100],
				(float) (screen.ternSizeBounds.width
						+ screen.ternSizePosition.x + ((int) world.properties.ternSize)
						* (infoBallSize + 1))
						+ infoBallSize,
				screen.ternSizePosition.y + infoBallSize
						* ((int) (newWorld.addBall * 100)) / 100, (-1)
						* infoBallSize, (-1) * infoBallSize
						* ((int) (newWorld.addBall * 100)) / 100);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(1);
		String ts = format.format(world.properties.ternSize);
		LinesAssets.nextTernSizeFont.draw(batch, ts, screen.ternSizePosition.x,
				screen.ternSizePosition.y + screen.ternSizeBounds.height + 3);
	}

	@Override
	protected void renderSpelling() {
		batch.setColor(1, 1, 1, 1f);
		for (Ball b : newWorld.ballsForRain) {

			if (b.state == Ball.RAIN) {
				float x = (b.polePosition.x + 0.5f) * this.cellSize - cellSize
						* b.size / 200;
				float y = (b.polePosition.y + 0.5f)
						* this.cellSize
						- cellSize
						* b.size
						/ 200
						+ (height * ((Constants.rainTime - b.stateTime) / Constants.rainTime));
				batch.draw(LinesAssets.firsRain, x, y, ballSize, ballSize);
			} else if (b.state == Ball.END_RAIN) {
				batch.draw(
						LinesAssets.rainAnimation.getKeyFrame(b.stateTime
								/ Constants.rainTime), (b.polePosition.x)
								* this.cellSize, (b.polePosition.y)
								* this.cellSize, ballSize, ballSize);
			}
		}

	}

	@Override
	protected void renderLightning() {
		if (newWorld.lightning.position.size() > 1) {
			Lightning l = newWorld.lightning;
			float time = newWorld.lightning.time;
			TextureRegion tr = LinesAssets.lightning.getKeyFrame(time
					/ Constants.lightningTime);
			batch.draw(tr, l.position.get(0).x * this.cellSize,
					l.position.get(0).y * this.cellSize + cellSize / 2,
					cellSize, 512);

		}
	}
}
