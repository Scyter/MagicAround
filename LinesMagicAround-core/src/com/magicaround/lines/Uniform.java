package com.magicaround.lines;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Uniform {
	public TextureRegion shirtTR;
	public TextureRegion chertTR;

	public Color shirtColor;
	public Color chertColor;

	public Uniform() {
		shirtTR = LinesAssets.achiveRegion;
		chertTR = LinesAssets.ratingRegion;
	}

	public Uniform(Color s, Color c) {
		this();
		shirtColor = s;
		chertColor = c;
	}

}
