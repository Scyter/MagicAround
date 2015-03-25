package com.magicaround.lines;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MovingText extends MovingElement {
	public String text;
	public BitmapFont font;
	public Color color;

	public MovingText(float x, float y, float targetX, float targetY,
			float transparency, float targetTransparency, float size,
			float targetS, int steps, IElementListner l) {
		super(x, y, targetX, targetY, transparency, targetTransparency, size,
				targetS, steps, l);

	}

	public MovingText(float x, float y, float targetX, float targetY,
			String text, Color color, int steps, IElementListner l) {
		super(x, y, targetX, targetY, steps, l);
		this.text = text;
		this.color = color;
		font = LinesAssets.scoreFont;

	}

	public MovingText(float x, float y, String text, Color color,
			IElementListner l) {
		super(x, y, l);
		this.text = text;
		this.color = color;
		font = LinesAssets.scoreFont;

	}
}
