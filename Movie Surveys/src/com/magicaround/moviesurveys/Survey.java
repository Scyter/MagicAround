package com.magicaround.moviesurveys;

public class Survey {
	public int id;
	public int type;
	public String question;
	public String picture;
	public String info;
	public int users;
	public double votes;
	public int state;
	public int tmp1;
	public String tmp2;

	public Survey() {
		id = 0;
		type = 0;
		question = "";
		picture = "";
		info = "";
		users = 0;
		votes = 0;
		state = 0;
		tmp1 = 0;
		tmp2 = "";
	}

	public Survey(int id) {
		this();
		this.id = id;
	}

	@Override
	public String toString() {
		return "id:" + String.valueOf(id) + "; type:" + String.valueOf(type)
				+ "; question:" + question + "; picture:" + picture + "; info"
				+ info;
	}

}
