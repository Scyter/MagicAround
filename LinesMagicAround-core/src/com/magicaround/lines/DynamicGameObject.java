package com.magicaround.lines;

import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
	public final Vector2 speed;
	public final Vector2 acceleration;
	public float rotationAngle;
	public float rotationSpeed;
	public float rotationAcceleration;

	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		speed = new Vector2();
		acceleration = new Vector2();
		rotationSpeed = 0;
		rotationAcceleration = 0;

	}

	public DynamicGameObject(float x, float y, float width, float height,
			float rotationSpeed, float rotationAcceleration) {
		this(x, y, width, height);
		this.rotationAcceleration = rotationAcceleration;
		this.rotationSpeed = rotationSpeed;
	}

}
