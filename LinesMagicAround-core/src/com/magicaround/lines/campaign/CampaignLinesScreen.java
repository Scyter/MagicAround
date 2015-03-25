package com.magicaround.lines.campaign;

import com.magicaround.lines.MALinesGame;
import com.magicaround.lines.WorldProperties;
import com.magicaround.lines.simple.SimpleLinesScreen;

public class CampaignLinesScreen extends SimpleLinesScreen {

	public CampaignLinesScreen(MALinesGame g, WorldProperties p) {
		super(g, p);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void newWorld() {
		properties.colorSize = 5;
		properties.lineSize = 4;
		properties.poleXSize = 7;
		properties.poleYSize = 7;
		properties.ternSize = 3;
		world = new CampaignLinesWorld(this, width, height, properties,
				game.player);
		renderer = new CampaignLinesRenderer(this);
		screenState = S_GAME_RUNNING;
		if (world.loadGame() == 0)
			world.startGame();
	}

}
