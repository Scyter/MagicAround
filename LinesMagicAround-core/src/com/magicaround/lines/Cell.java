package com.magicaround.lines;

import java.util.ArrayList;

public class Cell {
	public int x;
	public int y;
	public int color;

	public Cell() {
		x = 0;
		y = 0;
		color = 0;

	}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		color = 0;
	}

	public Cell(int x, int y, int clr) {
		this(x, y);
		color = clr;

	}

	@Override
	public String toString() {
		return (String.valueOf(x) + "" + String.valueOf(y));
	}

	public void unselect() {
		x = -1;
		y = -1;
		color = 0;
	}

	public void place(ArrayList<Cell> where, ArrayList<Cell> from) {
		if (where != null) {
			where.add(this);
			if (from != null)
				from.remove(this);
		} else {
			if (from != null)
				from.remove(this);
		}
	}
}
