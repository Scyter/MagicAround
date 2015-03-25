package com.magicaround.lines.survivor;

import java.util.ArrayList;

import com.magicaround.lines.Cell;

public class Lightning {
	public ArrayList<Cell> position;
	public float time;
	public int up;

	public Lightning() {
		position = new ArrayList<Cell>();
		time = 0;
		up = 0;
	}

	public void addTarget(int x, int y) {
		Cell c = new Cell(x, y);
		position.add(c);
	}

	public void update(float deltaTime) {
		up++;
		time += deltaTime;
	}

}
