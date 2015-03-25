package com.magicaround.lines;

import com.magicaround.lines.simple.IWorld;

public class WorldProperties {
	public int poleXSize;
	public int poleYSize;
	public int colorSize;
	public int lineSize;
	public float ternSize;
	public int worldType;

	public WorldProperties(int worldXSize, int worldYSize) {
		this.poleXSize = worldXSize;
		this.poleYSize = worldYSize;
		colorSize = 0;
		lineSize = 0;
		ternSize = 0;
		worldType = IWorld.CLASSIC;
	}

	public WorldProperties() {
		this.poleXSize = 0;
		this.poleYSize = 0;
		colorSize = 0;
		lineSize = 0;
		ternSize = 0;
		worldType = IWorld.CLASSIC;
	}

	public WorldProperties(int worldXSize, int worldYSize, int color, int line,
			int tern, int type) {
		this.poleXSize = 0;
		this.poleYSize = 0;
		colorSize = color;
		lineSize = line;
		ternSize = tern;
		worldType = type;
	}
}
