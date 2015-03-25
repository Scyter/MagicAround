package com.magicaround.linesf.android;

public interface ActionResolver {
	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(int score, int type);

	public void unlockAchievementGPGS(int achievementId);

	public void incrementAchievementGPGS(int achievementId, int n);

	public void getLeaderboardGPGS(int type);

	public void getAchievementsGPGS();

}
