package com.magicaround.lines;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MovingPicture extends MovingElement {

	public TextureRegion picture;
	public float height;
	public float weight;

	public MovingPicture(float x, float y, float targetX, float targetY,
			TextureRegion picture, int steps, IElementListner l) {
		super(x, y, targetX, targetY, steps, l);
		this.picture = picture;
		height = picture.getRegionHeight();
		weight = picture.getRegionWidth();

	}

	public MovingPicture(float x, float y, TextureRegion textureRegion,
			IElementListner l) {
		super(x, y, l);
		this.picture = textureRegion;
		height = picture.getRegionHeight();
		weight = picture.getRegionWidth();
	}

	public MovingPicture(float x, float y, TextureRegion textureRegion,
			float weight, float heigth, IElementListner l) {
		super(x, y, l);
		this.picture = textureRegion;
		this.height = heigth;
		this.weight = weight;
	}

}
