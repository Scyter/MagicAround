package com.ma.wcc;

public class FootballTeam implements Comparable<Object> {
	public String name;
	public int id;
	public double result;
	public String nameTMP;
	public float X;
	public float Y;
	public int countryID;

	public FootballTeam(String name, int cID) {
		this.name = name;
		countryID = cID;
		setName();
	}

	public FootballTeam(int id, String name, int cID) {
		this.id = id;
		this.name = name;
		countryID = cID;
		setName();
	}

	public FootballTeam(int id, double res) {
		this.id = id;
		result = res;
		countryID = -1;
	}

	public void setBounds(float x, float y) {
		X = x;
		Y = y;
	}

	public void setName() {
		int len;
		String fs = "";
		String s = "";
		s = name;
		len = s.length();
		if (len > 14) {
			fs = s.substring(0, 11);
			fs += "...";
		} else
			fs = s;
		nameTMP = fs;
	}

	public int compareTo(Object o) {
		int res = 0;
		FootballTeam compared = (FootballTeam) o;
		if (compared.result < this.result) {
			res = -1;
		}
		if (compared.result > this.result) {
			res = 1;
		}
		return res;
	}

	public String toString() {
		return String.valueOf(id) + ":" + String.valueOf(result) + " " + name;
	}

}
