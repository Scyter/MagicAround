package com.magicaround.lines.campaign;

import com.magicaround.lines.LinesPlayer;
import com.magicaround.lines.WorldProperties;
import com.magicaround.lines.simple.IWorldListener;
import com.magicaround.lines.simple.SimpleLinesWorld;

public class CampaignLinesWorld extends SimpleLinesWorld {

	public int level;

	public CampaignLinesWorld(IWorldListener l, int w, int h,
			WorldProperties p, LinesPlayer player) {
		super(l, w, h, p, player);
		level = 1;
	}

}
