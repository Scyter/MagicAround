package com.ma.wcc;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ClickerRenderer {

	OrthographicCamera cam;
	SpriteBatch batch;
	Screen s;
	ClickerWorld world;

	float w;
	float h;
	int maxBallSize;

	public ClickerRenderer(ClickerScreen gameScreen) {
		s = gameScreen;
		world = gameScreen.world;
		cam = gameScreen.camera;
		batch = gameScreen.batch;
		w = gameScreen.width;
		h = gameScreen.height;
		maxBallSize = (int) (w * 0.1);
	}

	public void draw() {
		cam.update();
		renderBackground();
		renderObjects();
	}

	private void renderObjects() {
		batch.enableBlending();
		batch.begin();
		batch.setColor(1, 1, 1, 0.4f);
		batch.draw(Assets.cells, 0, 0.9f * h, w, 0.1f * h);
		batch.setColor(1, 1, 1, 1);
		renderStars();
		renderTouches();
		renderMissedBalls();
		renderBalls();
		renderScore();
		renderTime();
		batch.end();

	}

	private void renderStars() {
		batch.setColor(1, 1, 1, 0.4f);
		batch.draw(Assets.stars.get(0), w * 0.45f, h * 0.91f, w * 0.1f,
				w * 0.1f);
		batch.draw(Assets.stars.get(1), w * 0.55f, h * 0.91f, w * 0.1f,
				w * 0.1f);
		batch.draw(Assets.stars.get(2), w * 0.65f, h * 0.91f, w * 0.1f,
				w * 0.1f);
		batch.draw(Assets.progress.get(10), w * 0.25f, h * 0.91f, w * 0.15f,
				w * 0.15f);

		batch.setColor(1, 1, 1, 1);
		if (world.stars >= 1)
			batch.draw(Assets.stars.get(0), w * 0.45f, h * 0.91f, w * 0.1f,
					w * 0.1f);
		if (world.stars >= 2)
			batch.draw(Assets.stars.get(1), w * 0.55f, h * 0.91f, w * 0.1f,
					w * 0.1f);
		if (world.stars >= 3)
			batch.draw(Assets.stars.get(2), w * 0.65f, h * 0.91f, w * 0.1f,
					w * 0.1f);
		int num = (int) world.starProgress;
		if (world.starProgress - num >= 0.5)
			num++;
		if (num > 0)
			batch.draw(Assets.progress.get(num), w * 0.25f, h * 0.91f, w
					* 0.15f / 10 * num, w * 0.15f);

	}

	private void renderTime() {
		String s = String.format("%1$,.2f", world.time);
		if (world.time <= 0)
			s = "0";
		Assets.timeFont.draw(batch, s, w * 0.81f, h * 0.97f);
	}

	private void renderBalls() {
		for (Ball b : world.balls) {
			if (b.type == Ball.DOUBLE) {
				if (b.clicks == 0) {
					batch.setColor(1, 0, 0, 1);
					batch.draw(Assets.ball, b.c.x - b.c.radius, b.c.y
							- b.c.radius, b.c.radius, b.c.radius,
							b.c.radius * 2, b.c.radius * 2, 1, 1, b.rotation);
					batch.setColor(1, 1, 1, 1);
				} else
					batch.draw(Assets.ball, b.c.x - b.c.radius, b.c.y
							- b.c.radius, b.c.radius, b.c.radius,
							b.c.radius * 2, b.c.radius * 2, 1, 1, b.rotation);
			} else
				batch.draw(Assets.ball, b.c.x - b.c.radius, b.c.y - b.c.radius,
						b.c.radius, b.c.radius, b.c.radius * 2, b.c.radius * 2,
						1, 1, b.rotation);
		}
	}

	private void renderTouches() {
		for (TouchingPoint t : world.touchingPoints) {
			switch (t.type) {
			case 1:
				batch.draw(Assets.stars.get(1), t.position.x - 0.02f * w,
						t.position.y - 0.02f * w, 0.04f * w, 0.04f * w);
				break;
			case 2:
				batch.draw(Assets.stars.get(2), t.position.x - 0.02f * w,
						t.position.y - 0.02f * w, 0.04f * w, 0.04f * w);
				break;
			default:
				break;
			}

		}
	}

	private void renderMissedBalls() {
		if (world.missedBalls.size() > 0) {
			batch.setColor(1, 0, 0, 0.3f);
			for (Ball b : world.missedBalls) {
				batch.draw(Assets.ball, b.c.x - maxBallSize, b.c.y
						- maxBallSize, maxBallSize * 2, maxBallSize * 2);
			}
			batch.setColor(1, 1, 1, 1);
		}

	}

	private void renderScore() {
		Assets.totalScoreFont.draw(batch, String.valueOf((int) world.score),
				w * 0.15f, h * 0.94f);
		Assets.scoreFont.draw(batch, String.valueOf((int) world.levelScore),
				w * 0.15f, h * 0.99f);
	}

	public void renderBackground() {
		batch.disableBlending();
		batch.begin();
		batch.setColor(1, 1, 1, 1);
		batch.draw(Assets.background, 0, 0, w, h);
		batch.end();
	}
}
