package com.magicaround.lines;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class MovingElement {
	public Vector2 position;
	public Vector2 startPosition;
	public float transparency;
	public float startTransparency;

	public float size;
	public float startSize;
	public ArrayList<Target> targets;
	private int step;
	private IElementListner listner;

	public MovingElement(float x, float y, IElementListner l) {
		this.position = new Vector2(x, y);
		this.startPosition = new Vector2(x, y);
		step = 0;
		targets = new ArrayList<Target>();
		listner = l;
		transparency = 1;
		startTransparency = 1;
		size = 1;
		startSize = 1;
	}

	public MovingElement(float x, float y, float targetX, float targetY,
			int steps, IElementListner l) {
		this(x, y, l);
		Target t = new Target(targetX, targetY, steps);
		targets.add(t);

	}

	public MovingElement(float x, float y, float targetX, float targetY,
			float transparency, float targetTransparency, float size,
			float targetS, int steps, IElementListner l) {
		this(x, y, targetX, targetY, steps, l);
		targets.clear();
		Target target = new Target(targetX, targetY, targetTransparency,
				targetS, steps);
		targets.add(target);
		this.transparency = transparency;
		startTransparency = transparency;
		this.size = size;
		startSize = size;
	}

	public void update(float deltaTime) {
		step++;
		position.x = startPosition.x
				+ (targets.get(0).targetPosition.x - startPosition.x)
				/ targets.get(0).steps * step;
		position.y = startPosition.y
				+ (targets.get(0).targetPosition.y - startPosition.y)
				/ targets.get(0).steps * step;
		if (targets.size() == 0)
			return;
		if (step >= targets.get(0).steps) {
			startPosition = targets.get(0).targetPosition;
			startTransparency = targets.get(0).transparency;
			startSize = targets.get(0).size;
			step = 0;
			targets.remove(0);
			if (targets.size() != 0) {
				listner.richStep(this);
			} else
				listner.richTarget(this);
			return;
		}
		transparency = startTransparency
				+ (targets.get(0).transparency - startTransparency)
				/ targets.get(0).steps * step;
		size = startSize + (targets.get(0).size - startSize)
				/ targets.get(0).steps * step;

	};

	public void addTarget(float x, float y, int steps) {
		Target newTarget = new Target(x, y, steps);
		targets.add(newTarget);
	}

	public void addTarget(float x, float y, float transparency, float size,
			int steps) {
		Target newTarget = new Target(x, y, transparency, size, steps);
		targets.add(newTarget);
	}

	private class Target {
		public Vector2 targetPosition;
		public float transparency;
		public float size;
		public int steps;

		public Target(float targetX, float targetY, int s) {
			targetPosition = new Vector2(targetX, targetY);
			this.steps = s;
			this.transparency = 1;
			this.size = 1;
		}

		public Target(float targetX, float targetY, float transparency,
				float size, int steps) {
			this(targetX, targetY, steps);
			this.transparency = transparency;

			this.size = size;
		}

	}

}
