package com.magicaround.lines;

import java.util.Calendar;

public class LinesPlayer {
	public String name;
	public int level;
	public int experience;
	public long date;
	public String skills;
	public boolean published;

	public long c_date;
	public int c_psX;
	public int c_psY;
	public int c_c;
	public int c_l;
	public float c_t;
	public String c_pole;
	public int c_score;
	public int c_h_score;

	public long h_date;
	public int h_psX;
	public int h_psY;
	public int h_c;
	public int h_l;
	public float h_t;
	public String h_pole;
	public int h_score;
	public int h_h_score;

	public int m_curLevel;
	public String m_levels;
	public String m_tasks;
	public long m_date;
	public int m_psX;
	public int m_psY;
	public int m_c;
	public int m_l;
	public float m_t;
	public String m_pole;
	public int m_score;
	public int m_h_score;

	public float s_curMagic;
	public int s_curLevel;
	public long s_date;
	public int s_psX;
	public int s_psY;
	public int s_c;
	public int s_l;
	public float s_t;
	public String s_pole;
	public int s_score;
	public int s_h_score;
	public int s_tern;
	public int s_level;
	public float s_addBall;
	public boolean s_addBallNextTern;

	public LinesPlayer() {
		level = 1;
		experience = 0;
		s_curMagic = 0;
		c_h_score = 0;
		s_h_score = 0;
		h_h_score = 0;
		name = "Guest";
		date = Calendar.getInstance().getTimeInMillis();

	}

	public int addExp(int e) {
		int addMagic = 0;
		experience += e;
		if (experience >= Constants.nextExpForLevel[level]) {
			if (level < 99)
				addMagic = nextLevel();
		}
		return addMagic;
	}

	public int nextLevel() {
		int addMagic = 0;
		experience = 0;
		s_curMagic += Constants.magicMax[level + 1] - Constants.magicMax[level];
		addMagic += Constants.magicMax[level + 1] - Constants.magicMax[level];
		level++;
		return addMagic;

	}

}
