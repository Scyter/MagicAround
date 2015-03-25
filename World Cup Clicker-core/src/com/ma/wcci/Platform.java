package com.ma.wcci;

import java.util.ArrayList;

import magicaround.GameResult;

public interface Platform {

	public void showAdMob(boolean show);

	public ArrayList<GameResult> results();

	public void saveResult(GameResult r);

	public void showTeamResutls();

	public String getDir();

	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(GameResult r);

	void getLeaderboardGPGS(int type);

}