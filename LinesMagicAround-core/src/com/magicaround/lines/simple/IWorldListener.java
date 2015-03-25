package com.magicaround.lines.simple;

public interface IWorldListener {

	public void gameOver(int worldType, boolean h);

	public void addScore(int ternScore, int addScore, int lines, float x,
			float y, int colorNumber);

	public void moveBall();

	public void noWay();

	public void removeBall(int x, int y, int color);

	public void removeBallLightning(int x, int y, int color);

	public void startNewGame();

	public void noMoveAvaliable();

	public int state();

	public void armageddon(int x, int y);

	// public void addColorSize(int x, int y);

	public void addTernSize(int x, int y, int color);

	// public void addLineSize(int x, int y, int color);

	public void nextLevel(float ternSize);

	public void noEnoughMagic();

	public void useMagic(int i);

	public void startRain(int x, int y);
}
