package com.ma.wcc;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ClickerWorld implements IBallListner {
	public static int w;
	public static int h;
	int level;
	ArrayList<Ball> balls;
	ArrayList<Ball> missedBalls;
	ArrayList<TouchingPoint> touchingPoints;
	float timeForBall;
	float time;
	float ballTime;
	float score;
	float levelScore;
	int state;
	IWorldListener listener;
	int stars;
	float starProgress;
	int lives;
	int addStars;

	public ClickerWorld() {
		this(1);
	}

	public ClickerWorld(int l) {
		level = l;
		score = 0;
		startNewLevel();
	}

	public ClickerWorld(int weight, int height, int lvl, IWorldListener listener) {
		this.listener = listener;
		level = lvl;
		if (level < 1)
			level = 1;
		// level = 11;
		w = weight;
		h = (int) (height * 0.9);
		lives = 3;
		score = 0;
		if (level > Levels.maxLevel) {
			stars = level - Levels.maxLevel;
			level = Levels.maxLevel;
		}
		addStars = 0;
		startNewLevel();

	}

	public void startNewLevel() {
		balls = new ArrayList<Ball>();
		missedBalls = new ArrayList<Ball>();
		touchingPoints = new ArrayList<TouchingPoint>();
		timeForBall = 0;
		// time = Levels.time[level];
		// ballTime = Levels.ballTime[level];
		// ballTime = (float) (1 / (Math.pow(level, 0.2f)));
		// time = 15 + (int) (level / 5);
		time = 15 + level - (int) (level / 2) - 1;
		ballTime = (float) Math.pow(1 / (double) level, 0.4);
		stars = addStars;
		addStars = 0;
		starProgress = 0;
		levelScore = 0;
		generateNewBall();
	}

	private void generateNewBall() {
		Ball b = new Ball();
		b.generate(w, h, level);
		balls.add(b);
		timeForBall = 0;
	}

	public void update(float delta) {
		timeForBall += delta;
		if (timeForBall >= ballTime) {
			generateNewBall();
		}
		updateBalls(delta);
		updateTouchs(delta);
		updateMissedBalls(delta);
		time -= delta;
		if (time <= 0)
			timeOver();
	}

	private void timeOver() {
		lives--;
		score += levelScore * (1 + (level - 1) * 0.2f);
		if (level >= 9)
			score += levelScore * (level - 8) * 0.2f;
		boolean maxLvl = false;
		if (lives == 0) {
			if (checkResult()) {
				level++;
				if (level >= Levels.maxLevel) {
					level = Levels.maxLevel + stars;
					maxLvl = true;
				}
			}
			listener.gameOver(maxLvl);
		} else {
			if (checkResult())
				nextLevel();
			else
				currentLevel();
		}
	}

	private void currentLevel() {
		listener.currentLevel();

	}

	private void nextLevel() {
		boolean nearMax = false;
		boolean maxLvl = false;
		if (level >= Levels.maxLevel)
			nearMax = true;
		level++;
		if (level >= Levels.maxLevel) {
			addStars = stars;
			level = Levels.maxLevel;
			if (nearMax)
				maxLvl = true;
		}
		listener.nextLevel(maxLvl);

	}

	private boolean checkResult() {
		if (levelScore >= 50)
			return true;
		return false;
	}

	public void updateBalls(float delta) {
		int ballsAfterUpdate = 0;
		for (Ball b : balls) {
			b.update(delta);
			if (!b.miss)
				ballsAfterUpdate++;
			else
				loseBall(b);
		}
		if (ballsAfterUpdate < balls.size()) {
			ArrayList<Ball> tBalls = new ArrayList<Ball>();
			for (Ball b : balls) {
				if (!b.miss)
					tBalls.add(b);
			}
			balls = tBalls;
			if (balls.size() == 0)
				generateNewBall();
		}
	}

	public void updateTouchs(float delta) {
		int touchesAfterUpdate = 0;
		for (TouchingPoint t : touchingPoints) {
			t.update(delta);
			if (!t.remove)
				touchesAfterUpdate++;
		}
		if (touchesAfterUpdate < touchingPoints.size()) {
			ArrayList<TouchingPoint> tTouches = new ArrayList<TouchingPoint>();
			for (TouchingPoint t : touchingPoints) {
				if (!t.remove)
					tTouches.add(t);
			}
			touchingPoints = tTouches;
		}
	}

	private void updateMissedBalls(float delta) {
		boolean miss = false;
		for (Ball b : missedBalls) {
			b.size += delta;
			if (b.size >= 0.5f)
				miss = true;
		}
		if (miss) {
			ArrayList<Ball> newMissedBalls = new ArrayList<Ball>();
			for (Ball b : missedBalls) {
				if (b.size < 0.5f)
					newMissedBalls.add(b);
			}
			missedBalls = newMissedBalls;
		}

	}

	private void loseBall(Ball b) {
		stars = 0;
		starProgress = 0;
		listener.lose_ball();
		missedBalls.add(new Ball(new Vector2(b.c.x, b.c.y)));

	}

	private void checkBalls() {
		int ballsAfterUpdate = 0;
		for (Ball b : balls) {
			if (!b.remove)
				ballsAfterUpdate++;
		}
		if (ballsAfterUpdate < balls.size()) {
			ArrayList<Ball> tBalls = new ArrayList<Ball>();
			for (Ball b : balls) {
				if (!b.remove)
					tBalls.add(b);
			}
			balls = tBalls;
			if (balls.size() == 0)
				generateNewBall();
		}
	}

	public void touch(Vector3 t) {
		boolean hit = false;
		Vector2 touch = new Vector2(t.x, t.y);
		Circle tc = null;
		if (t.y > h)
			return;
		for (Ball b : balls) {
			tc = new Circle(b.c.x, b.c.y, b.c.radius);
			tc.radius += 0.02f * w;
			if (tc.contains(touch)) {
				if (b.type == Ball.NORMAL) {
					addScore(b);
					b.remove = true;
					hit = true;
					break;
				} else if (b.type == Ball.DOUBLE) {
					b.clicks++;
					addScore(b);
					hit = true;
					break;
				}
			}
		}
		if (hit) {
			touchingPoints.add(new TouchingPoint(touch, 1));
			checkBalls();
			listener.hit();
		} else {
			touchingPoints.add(new TouchingPoint(touch, 2));
			missClick();
		}
	}

	private void missClick() {
		if (stars >= 1)
			stars--;
		else
			starProgress = 0;
		listener.miss();
	}

	private void addScore(Ball b) {
		float add = 0;
		if (b.up) {
			add += 1 + (1 - Math.pow(b.size, 0.8));
		} else {
			add += b.size;
		}
		if (b.type == Ball.DOUBLE) {
			if (b.clicks > 1)
				add += 5;
		}
		// score += add * (stars + 1);
		levelScore += add * (stars + 1);
		starProgress += add;
		if (starProgress >= 10) {
			starProgress -= 10;
			stars++;
		}
	}

	@Override
	public void removeBall(Ball b) {
	}

}
