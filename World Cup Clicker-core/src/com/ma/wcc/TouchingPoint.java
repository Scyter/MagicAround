package com.ma.wcc;

import com.badlogic.gdx.math.Vector2;

public class TouchingPoint {
	public Vector2 position;
	public int type;
	public float live;
	public boolean remove;

	public TouchingPoint(Vector2 p) {
		position = p;
		live = 0.2f;
		remove = false;
	}

	public TouchingPoint(Vector2 p, int t) {
		this(p);
		type = t;
	}

	public void update(float delta) {
		live -= delta;
		if (live <= 0)
			remove = true;
	}
}
