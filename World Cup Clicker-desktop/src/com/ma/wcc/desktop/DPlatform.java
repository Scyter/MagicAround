package com.ma.wcc.desktop;

import java.util.ArrayList;

import magicaround.GameResult;

import com.badlogic.gdx.Gdx;
import com.ma.wcci.Platform;

public class DPlatform implements Platform {
	public DPlatform() {
	}

	@Override
	public void showAdMob(boolean show) {

	}

	@Override
	public ArrayList<GameResult> results() {
		return new ArrayList<GameResult>();

	}

	@Override
	public void saveResult(GameResult r) {

	}

	@Override
	public void showTeamResutls() {

	}

	@Override
	public String getDir() {
		return "C:";
	}

	@Override
	public boolean getSignedInGPGS() {
		return false;
	}

	@Override
	public void loginGPGS() {
		Gdx.app.log("aa", "Го логин");
	}

	@Override
	public void submitScoreGPGS(GameResult r) {
	}

	@Override
	public void getLeaderboardGPGS(int type) {
		// TODO Auto-generated method stub

	}

}
