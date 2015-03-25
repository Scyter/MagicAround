package com.magicaround.lines.simple;

public interface IWorld {

	public static final int CLASSIC = 1;
	public static final int HARD = 2;
	public static final int CAMPAIGN = 3;
	public static final int SURVIVAL = 4;

	public static final int A_CLASSIC_MASTER = 1;
	public static final int A_HARDCORE_MASTER = 2;
	public static final int A_SURVIVAL_MASTER = 3;
	public static final int A_LINES_DESTROER = 4;
	public static final int A_MAGIC_MASTER = 5;
	public static final int A_CLASSIC_HERO = 6;
	public static final int A_HARDCORE_HERO = 7;
	public static final int A_SURVIVAL_HERO = 8;

	public void startGame();

	public void selectCell(int x, int y);

	public void update(float deltaTime);

	public void clearSavedGame();

	public int loadGame();

	public void saveGame();

	public int score();

	public int getCellColor(int x, int y);

	public void selectSkill(int skillID);

	public void moveBallToEnd();

	public void removeBallSelection();

	public void removeSkillSelection();

	public void spellEnds(int i);

}
