package com.magicaround.lines;

public interface IPlatform {

	public void showAdMob(boolean show);

	public void go();

	public void connectBtn(boolean b);

	public void savePlayer();

	public void saveClassic();

	public void saveHardcore();

	public void saveCampaign();

	public void saveSurvival();
	
	public void clearGame(int type);

	public void loadPlayer();

}