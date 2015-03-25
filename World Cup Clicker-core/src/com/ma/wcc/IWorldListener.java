package com.ma.wcc;

public interface IWorldListener {
	public void timeOver();

	public void gameOver(boolean maxLvl);

	public void nextLevel(boolean maxLvl);

	public void currentLevel();

	public void hit();

	public void miss();

	public void lose_ball();

	public void win();

	public void same_lvl();

}
