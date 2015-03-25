package com.ma.wcc;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Ball {

	public final static int NORMAL = 1;
	public final static int DOUBLE = 2;
	public final static int FAKE = 3;

	public Circle c;
	float grow;
	boolean up;
	float size;
	boolean rotationDir;
	float rotation;
	int rotationSpeed;
	public boolean remove;
	public boolean miss;
	int maxBallSize;
	int dirX;
	int dirY;
	public float speed;
	int type;
	int clicks;

	public Ball() {
		c = new Circle();
		c.x = 0;
		c.y = 0;
		up = true;
		grow = 1;
		c.radius = 0;
		size = 0;
		remove = false;
		miss = false;
		maxBallSize = 0;
		dirX = 0;
		dirY = 0;
		speed = 0;
		rotation = 0;
		rotationDir = true;
		rotationSpeed = 1;
		type = 1;
		clicks = 0;

	}

	public Ball(Vector2 v) {
		this();
		c.x = v.x;
		c.y = v.y;

	}

	public void generate(int w, int h, int level) {
		int lvl = level;
		if (level > Levels.maxLevel)
			lvl = Levels.maxLevel;
		int lvlAbove10 = 0;
		if (lvl > 10) {
			lvlAbove10 = lvl - 10;
			lvl = 10;
		}
		c.x = Assets.r.nextInt((int) (w * 0.8)) + (int) (w * 0.1);
		c.y = Assets.r.nextInt((int) (h * 0.9 - w * 0.1)) + (int) (w * 0.1);
		maxBallSize = (int) (w * 0.1);
		int gRand = 10 - Assets.r.nextInt(20);
		grow = (float) (Math.pow(lvl, 0.6)) * (1 + gRand / 20);
		int dRand = Assets.r.nextInt(100);
		dirX = dRand;
		dirY = (int) Math.sqrt(10000 - dRand * dRand);
		gRand = 10 - Assets.r.nextInt(20);
		if (gRand < 0)
			dirX *= (-1);
		gRand = 10 - Assets.r.nextInt(20);
		if (gRand < 0)
			dirY *= (-1);
		int sRand = 10 - Assets.r.nextInt(20);
		speed = (float) (Math.pow(lvl - 1, 1.3)) * (1 + sRand / 20) * 2;
		if (lvl == 1)
			size += 0.3;
		if (lvl == 2)
			size += 0.2;
		if (lvl == 3)
			size += 0.1;
		if (sRand * gRand >= 0)
			rotationDir = true;
		else
			rotationDir = false;
		rotation = Assets.r.nextInt(180);
		rotationSpeed = Assets.r.nextInt(180);

		sRand = Assets.r.nextInt(100);
		switch (lvlAbove10) {
		case 0:
			break;
		case 1:
			if (sRand >= 90)
				type = 2;
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		default:
			break;
		}

	}

	public void update(float delta) {
		// delta /= 6; // NB! Для тестов уменьшал скорость
		if (up) {
			size += delta * grow / 2;
			move(delta);
			if (size >= 1) {
				up = false;
				changeRotation();
			}
		} else {
			size -= delta * grow / 2;
			move(delta);
			if (size <= 0)
				miss = true;
		}
		c.radius = size * maxBallSize;
		rotation = rotationDir ? rotation + delta * rotationSpeed : rotation
				- delta * rotationSpeed;
	}

	private void changeRotation() {
		rotationDir = rotationDir ? false : true;
	}

	private void move(float delta) {
		c.x += delta * speed * dirX / 10;
		c.y += delta * speed * dirY / 10;

		if (c.x - c.radius <= 0) {
			c.x = c.radius;
			dirX *= -1;
			changeRotation();
		}
		if (c.x + c.radius >= ClickerWorld.w) {
			c.x = ClickerWorld.w - c.radius;
			dirX *= -1;
			changeRotation();
		}
		if (c.y - c.radius <= 0) {
			c.y = c.radius;
			dirY *= -1;
			changeRotation();
		}
		if (c.y + c.radius >= ClickerWorld.h) {
			c.y = ClickerWorld.h - c.radius;
			dirY *= -1;
			changeRotation();
		}
	}
}
