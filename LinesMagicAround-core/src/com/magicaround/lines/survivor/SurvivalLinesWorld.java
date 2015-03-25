package com.magicaround.lines.survivor;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.magicaround.lines.Constants;
import com.magicaround.lines.Ball;
import com.magicaround.lines.Cell;
import com.magicaround.lines.LinesPlayer;
import com.magicaround.lines.WorldProperties;
import com.magicaround.lines.simple.IWorldListener;
import com.magicaround.lines.simple.SimpleLinesWorld;

public class SurvivalLinesWorld extends SimpleLinesWorld {

	public int skill;
	private boolean skillIsSelected;
	public ArrayList<Cell> cellsToActivateSkill;
	public ArrayList<Cell> cellsToGo;
	public ArrayList<Ball> ballsForLightning;
	public ArrayList<Ball> ballsForRain;
	public ArrayList<Ball> ballsForEarthshake;
	public Lightning lightning;
	public int tern;
	public int level;
	public float curMagic;
	public float addBall;

	public SurvivalLinesWorld(IWorldListener l, int w, int h,
			WorldProperties p, LinesPlayer player) {
		super(l, w, h, p, player);
		skill = 0;
		skillIsSelected = false;
		cellsToActivateSkill = new ArrayList<Cell>();
		cellsToGo = new ArrayList<Cell>();
		ballsForLightning = new ArrayList<Ball>();
		ballsForRain = new ArrayList<Ball>();
		ballsForEarthshake = new ArrayList<Ball>();
		lightning = new Lightning();
		level = 1;
		addBall = 0;
		additionalBall = false;
		setTern();
		curMagic = Constants.magicMax[player.level];

	}

	private void setTern() {
		tern = Constants.nextLevel[level];

	}

	/**
	 * Метод начала новой игры
	 */
	@Override
	public void startGame() {
		listener.startNewGame();
		newPole();
		for (int i = 0; i <= 12; i++) { // NB 12 вместо 3 сделать
			generateNextTern();
			nextTern(0);
		}

	}

	@Override
	public void selectSkill(int skillID) {
		if (skillIsSelected) {
			removeSkillSelection();
			newSelectedCell.unselect();
			selectedCell.unselect();
			return;
		}
		if (Constants.skillMagic[skillID] > curMagic) {
			listener.noEnoughMagic();
			return;
		}
		skill = skillID;
		skillIsSelected = true;
		removeBallSelection();
		cellsToActivateSkill.clear();
		switch (skill) {
		case 1:
			for (Ball b : balls) {
				Cell c = new Cell(b.polePosition.x, b.polePosition.y,
						b.polePosition.color);
				cellsToActivateSkill.add(c);
			}
			break;
		case 2:
			for (Ball b : balls) {
				Cell c = new Cell(b.polePosition.x, b.polePosition.y,
						b.polePosition.color);
				cellsToActivateSkill.add(c);
			}
			break;

		case 3:
			for (Ball b : balls) {
				Cell c = new Cell(b.polePosition.x, b.polePosition.y,
						b.polePosition.color);
				cellsToActivateSkill.add(c);
			}

			break;
		case 4:
			rain();
			break;

		case 5:
			ballsNextTern.clear();
			generateNextTern();
			curMagic -= Constants.skillMagic[5];
			player.addExp(Constants.addXPForSkill[5]);
			skillIsSelected = false;
			skill = 0;

		default:
			break;
		}
	}

	@Override
	public void removeSkillSelection() {
		skill = 0;
		skillIsSelected = false;
		this.cellsForMove.clear();
		cellsToActivateSkill.clear();
	}

	@Override
	protected void resolveNextTern(int i) {
		if (i == 1) {
			tern--;
			if (tern <= 0)
				nextLevel();
		}

		super.resolveNextTern(i);
	}

	@Override
	protected void checkAddBalls() {
		addBall += properties.ternSize - (int) properties.ternSize;
		if (addBall >= 1) {
			additionalBall = true;
			addBall -= 1;
			// for (Ball b : ballsNextTern) {
			// listener.addTernSize(b.polePosition.x, b.polePosition.y,
			// b.polePosition.color - MAXIMUM_COLOR_SIZE);
			// }
		}
		super.checkAddBalls();
	}

	private void nextLevel() {
		addTernSize();
		level++;
		setTern();
		listener.nextLevel(properties.ternSize);
	}

	private boolean addTernSize() {
		if (properties.ternSize < 9) {
			properties.ternSize += 0.1f;
			if (properties.ternSize > 9)
				properties.ternSize = 9;
		}
		return false;

	}

	@Override
	protected void addScore(int sumTernScore, int addTernScore) {
		super.addScore(sumTernScore, addTernScore);
		curMagic += sumTernScore + addTernScore;
		if (curMagic > Constants.magicMax[player.level]) {
			curMagic = Constants.magicMax[player.level];
		}

	}

	@Override
	public void selectCell(int x, int y) {
		if (skillIsSelected) {
			Gdx.app.log("slw", "selectCell in survival skillIsSelected:"
					+ ((Integer) x).toString() + " " + ((Integer) y).toString()
					+ "-" + String.valueOf(worldState));
			poleToString();
			ballsToString();
			cellsToString();
			switch (skill) {
			case 1:
				ballBroker(x, y);
				break;
			case 2:
				// if (cellIsSelected) {
				// selectCellForTeleportation();
				// } else {
				// super.selectCell(x, y);
				// }
				earthShake(x, y);
				break;
			case 3:
				chainLightning(x, y);
			default:
				break;
			}
			poleToString();
			ballsToString();
			cellsToString();
		} else
			super.selectCell(x, y);
	}

	// private void selectCellForTeleportation() {
	//
	//
	// }

	/**
	 * Скилл 1. Уничтожение шарика
	 * 
	 * @param x
	 *            координата по оси Х
	 * @param y
	 *            координата по оси У
	 */
	private void ballBroker(int x, int y) {
		if (ballInBalls(x, y) != null) {
			listener.removeBall(x, y, pole[x][y]);
			listener.useMagic(1);
			Ball b = ballInBalls(x, y);
			pole[x][y] = 0;
			b.place(null, balls);
			cellsFree.add(new Cell(x, y, 0));
			skill = 0;
			skillIsSelected = false;
			cellsToActivateSkill.clear();
			checkBalls();
			curMagic -= Constants.skillMagic[1];
			player.addExp(Constants.addXPForSkill[1]);
		} else {
			skill = 0;
			skillIsSelected = false;
			cellsToActivateSkill.clear();
		}
	}

	// /**
	// * Скилл 2. Телепортация шарика
	// *
	// * @param x
	// * координата по оси Х
	// * @param y
	// * координата по оси У
	// */
	// private void ballTeleportation(int x, int y) {
	// }
	/**
	 * Скилл 2. Землятресение
	 * 
	 * @param x
	 *            координата по оси Х
	 * @param y
	 *            координата по оси У
	 */
	private void earthShake(int x, int y) {
		if (ballInBalls(x, y) != null) {
			findBallForEarthshake(x, y);
			listener.useMagic(2);
			for (Ball b : ballsForEarthshake) {
				listener.removeBallLightning(b.polePosition.x,
						b.polePosition.y, b.polePosition.color);
				pole[b.polePosition.x][b.polePosition.y] = 0;
				b.place(null, balls);
				cellsFree.add(new Cell(b.polePosition.x, b.polePosition.y, 0));
			}

			skill = 0;
			skillIsSelected = false;
			cellsToActivateSkill.clear();
			checkBalls();
			curMagic -= Constants.skillMagic[2];
			curMagic += player.addExp(Constants.addXPForSkill[2]);
		} else {
			skill = 0;
			skillIsSelected = false;
			cellsToActivateSkill.clear();
		}
	}

	private void findBallForEarthshake(int x, int y) {
		ballsForEarthshake = new ArrayList<Ball>();
		Ball curBall = ballInBalls(x, y);
		ballsForEarthshake.add(curBall);
		int x1 = x + 1;
		int y1 = y;
		{
			int x2 = x1 + 1;
			int y2 = y1;
			Ball tmpBall = ballInBalls(x1, y1);
			if (tmpBall != null) {
				if (x1 >= properties.poleXSize - 1) {
					ballsForEarthshake.add(tmpBall);
				} else if (ballInBalls(x2, y2) == null) {
					Cell c1 = cellInFreeCells(x2, y2);
					if (c1 != null) {
						Cell c2 = new Cell(x1, y1);
						ArrayList<Cell> way = new ArrayList<Cell>();
						way.add(c1);
						way.add(c2);
						moveBallByEarthshake(way, x1, y1, x2, y2);
					}
				} else
					tmpBall.setState(Ball.NO_WAY);
			}
		}
		x1 = x - 1;
		y1 = y;
		{
			int x2 = x1 - 1;
			int y2 = y1;
			Ball tmpBall = ballInBalls(x1, y1);
			if (tmpBall != null) {
				if (x1 <= 0) {

					ballsForEarthshake.add(tmpBall);

				} else if (ballInBalls(x2, y2) == null) {

					Cell c1 = cellInFreeCells(x2, y2);
					if (c1 != null) {
						Cell c2 = new Cell(x1, y1);
						ArrayList<Cell> way = new ArrayList<Cell>();
						way.add(c1);
						way.add(c2);
						moveBallByEarthshake(way, x1, y1, x2, y2);
					}
				} else
					tmpBall.setState(Ball.NO_WAY);
			}
		}

		x1 = x;
		y1 = y + 1;
		{
			int x2 = x1;
			int y2 = y1 + 1;
			Ball tmpBall = ballInBalls(x1, y1);
			if (tmpBall != null) {
				if (y1 >= properties.poleYSize - 1) {
					ballsForEarthshake.add(tmpBall);

				} else if (ballInBalls(x2, y2) == null) {

					Cell c1 = cellInFreeCells(x2, y2);
					if (c1 != null) {
						Cell c2 = new Cell(x1, y1);
						ArrayList<Cell> way = new ArrayList<Cell>();
						way.add(c1);
						way.add(c2);
						moveBallByEarthshake(way, x1, y1, x2, y2);
					}
				} else
					tmpBall.setState(Ball.NO_WAY);
			}
		}
		x1 = x;
		y1 = y - 1;
		{
			int x2 = x1;
			int y2 = y1 - 1;
			Ball tmpBall = ballInBalls(x1, y1);
			if (tmpBall != null) {
				if (y1 <= 0) {
					ballsForEarthshake.add(tmpBall);
				} else if (ballInBalls(x2, y2) == null) {

					Cell c1 = cellInFreeCells(x2, y2);
					if (c1 != null) {
						Cell c2 = new Cell(x1, y1);
						ArrayList<Cell> way = new ArrayList<Cell>();
						way.add(c1);
						way.add(c2);
						moveBallByEarthshake(way, x1, y1, x2, y2);
					}
				} else
					tmpBall.setState(Ball.NO_WAY);
			}
		}
	}

	protected void moveBallByEarthshake(ArrayList<Cell> way, int x1, int y1,
			int x2, int y2) {
		Gdx.app.log(
				"slw",
				"moveBallByEarthshake:" + ((Integer) x1).toString() + "-"
						+ ((Integer) y1).toString() + ":"
						+ ((Integer) x2).toString() + "-"
						+ ((Integer) y2).toString());
		listener.moveBall();
		this.worldState = BALL_MOVING;

		poleToString();
		ballsToString();
		cellsToString();

		tempValue = pole[x2][y2];
		if (ballInNextTernBalls(x2, y2) != null) {
			ballInNextTernBalls(x2, y2).setStateSmallSwap(way);
		}
		if (ballInBalls(x1, y1) != null) {
			ballInBalls(x1, y1).setStateMoving(way, 1, 1);
		} else {
		}
		Cell c = cellInFreeCells(x2, y2);
		if (c != null)
			c.place(null, cellsFree);
		c = new Cell(x1, y1, 0);
		c.place(cellsFree, null);
		pole[x2][y2] = pole[x1][y1];
		pole[x1][y1] = tempValue;
		selectedCell.unselect();
		newSelectedCell.unselect();
		cellIsSelected = false;
		cellsForMove.clear();

		poleToString();
		ballsToString();
		cellsToString();

	}

	/**
	 * Скилл 3. Цепная молния
	 * 
	 * @param x
	 *            координата по оси Х
	 * @param y
	 *            координата по оси У
	 */
	private void chainLightning(int x, int y) {
		if (ballInBalls(x, y) != null) {
			findBallForLightning(x, y);
			listener.useMagic(3);
			lightning = new Lightning();
			for (Ball b : ballsForLightning) {
				listener.removeBallLightning(b.polePosition.x,
						b.polePosition.y, b.polePosition.color);
				lightning.addTarget(b.polePosition.x, b.polePosition.y);
				pole[b.polePosition.x][b.polePosition.y] = 0;
				b.place(null, balls);
				cellsFree.add(new Cell(b.polePosition.x, b.polePosition.y, 0));
			}

			skill = 0;
			skillIsSelected = false;
			cellsToActivateSkill.clear();
			checkBalls();
			curMagic -= Constants.skillMagic[3];
			curMagic += player.addExp(Constants.addXPForSkill[3]);
		} else {
			skill = 0;
			skillIsSelected = false;
			cellsToActivateSkill.clear();
		}
	}

	private void findBallForLightning(int x, int y) {
		ballsForLightning = new ArrayList<Ball>();
		Ball curBall = ballInBalls(x, y);
		ballsForLightning.add(curBall);
		int i = 1;
		while (i < 5) {
			Ball b = findNearBall(curBall);
			if (b == null)
				break;
			curBall = b;
			ballsForLightning.add(b);
			i++;
		}

	}

	private Ball findNearBall(Ball curBall) {
		Ball nearBall = null;
		int x1 = 0;
		int y1 = 0;
		for (int t = 1; t <= 18; t++) {
			for (int i = 0; i <= t; i++) {
				for (int j = 0; j <= t - i; j++) {
					if ((i == 0) & (j == 0))
						continue;
					x1 = curBall.polePosition.x + i;
					y1 = curBall.polePosition.y + j;
					if (!((x1 < 0) || (y1 < 0)) || (x1 > properties.poleXSize)
							|| (y1 > properties.poleYSize)) {
						Ball tBall = ballInBalls(x1, y1);
						if (tBall != null) {
							if (tBall.polePosition.color == curBall.polePosition.color) {
								if (ballInBallForLightning(
										tBall.polePosition.x,
										tBall.polePosition.y) == null) {
									nearBall = tBall;
									return nearBall;
								}
							}
						}
					}
					x1 = curBall.polePosition.x + i;
					y1 = curBall.polePosition.y - j;
					if (!((x1 < 0) || (y1 < 0))) {
						Ball tBall = ballInBalls(x1, y1);
						if (tBall != null) {
							if (tBall.polePosition.color == curBall.polePosition.color) {
								if (ballInBallForLightning(
										tBall.polePosition.x,
										tBall.polePosition.y) == null) {
									nearBall = tBall;
									return nearBall;
								}
							}
						}
					}
					x1 = curBall.polePosition.x - i;
					y1 = curBall.polePosition.y + j;
					if (!((x1 < 0) || (y1 < 0))) {
						Ball tBall = ballInBalls(x1, y1);
						if (tBall != null) {
							if (tBall.polePosition.color == curBall.polePosition.color) {
								if (ballInBallForLightning(
										tBall.polePosition.x,
										tBall.polePosition.y) == null) {
									nearBall = tBall;
									return nearBall;
								}
							}
						}
					}
					x1 = curBall.polePosition.x - i;
					y1 = curBall.polePosition.y - j;
					if (!((x1 < 0) || (y1 < 0))) {
						Ball tBall = ballInBalls(x1, y1);
						if (tBall != null) {
							if (tBall.polePosition.color == curBall.polePosition.color) {
								if (ballInBallForLightning(
										tBall.polePosition.x,
										tBall.polePosition.y) == null) {
									nearBall = tBall;
									return nearBall;
								}
							}
						}
					}
				}
			}
		}
		return nearBall;
	}

	/**
	 * Скилл 4. Дождь
	 */
	private void rain() {
		for (Ball b : balls) {
			if (rand.nextInt(100) <= 30 + (int) (player.level / 10)) {
				ballsForRain.add(b);
				b.setState(Ball.RAIN);
			}
		}
		worldState = SPELLING;

		curMagic -= Constants.skillMagic[4];
		player.addExp(Constants.addXPForSkill[4]);
		skillIsSelected = false;
		// ArrayList<Ball> t = new ArrayList<Ball>();
		// for (Ball b : ballsForRain) {
		// t.add(b);
		// }
		//
		// for (Ball b : t) {
		// rainFinished(b);
		// }
	}

	@Override
	public void spellEnds(int i) {
		for (Ball b : ballsForRain) {
			listener.removeBallLightning(b.polePosition.x, b.polePosition.y,
					pole[b.polePosition.x][b.polePosition.y]);
			pole[b.polePosition.x][b.polePosition.y] = 0;
			b.place(null, balls);
			cellsFree.add(new Cell(b.polePosition.x, b.polePosition.y, 0));
		}
		ballsForRain.clear();

	}

	@Override
	public void rainFinished(Ball b) {
		// Gdx.app.log("be", "rainFinished");
		listener.removeBallLightning(b.polePosition.x, b.polePosition.y,
				pole[b.polePosition.x][b.polePosition.y]);
		pole[b.polePosition.x][b.polePosition.y] = 0;
		b.place(null, balls);
		b.place(null, ballsForRain);
		cellsFree.add(new Cell(b.polePosition.x, b.polePosition.y, 0));
		checkBalls();
		if (ballsForRain.size() == 0) {
			worldState = RUNNING;
		}
	}

	@Override
	public void movingFinished(int sign) {
		super.movingFinished(sign);
		curMagic += (float) Constants.magicMax[player.level] / 100;
		if (curMagic > Constants.magicMax[player.level]) {
			curMagic = Constants.magicMax[player.level];
		}
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		for (Ball b : ballsForRain) {
			if (b.rainTime > 0)
				b.rainTime -= deltaTime * 100;
			if (b.rainTime < 0) {
				b.rainTime = 0;
			}
		}
		lightning.update(deltaTime);
		if (lightning.up > Constants.lightningTime) {
			lightning = new Lightning();
		}
	}

	/**
	 * Находит шар в списке шаров на поле
	 * 
	 * @param x
	 *            координата по X
	 * @param y
	 *            координата по Y
	 * @return найденный шар или null, если его нет
	 */
	protected Ball ballInBallForLightning(int x, int y) {
		// Gdx.app.log("ball", Integer.toString(x) + "-" + Integer.toString(y));
		for (Ball b : ballsForLightning) {
			if ((b.polePosition.x == x) && (b.polePosition.y == y)) {
				return b;
			}
		}
		return null;
	}

	@Override
	public void saveGame() {
		if (worldState == SimpleLinesWorld.OVER)
			return;
		if (score == 0) {
			clearSavedGame();
			return;
		}

		player.s_score = score;
		if (score > player.s_h_score) {
			player.s_h_score = score;
			// Submith record
		}
		player.s_psX = properties.poleXSize;
		player.s_psY = properties.poleYSize;
		player.s_c = properties.colorSize;
		player.s_l = properties.lineSize;
		player.s_t = properties.ternSize;
		player.s_addBall = addBall;
		player.s_curMagic = curMagic;
		// Ставится и так само по себе
		// player.s_addBall =
		player.s_addBallNextTern = additionalBall;
		player.s_pole = "";

		for (int i = 0; i < properties.poleXSize; i++)
			for (int j = 0; j < properties.poleYSize; j++) {
				player.s_pole += Integer.toString(pole[i][j]) + "\r\n";
			}
	}

	@Override
	public int loadGame() {
		newPole();
		cellsFree.clear();
		score = player.s_score;
		if (score == 0)
			return score;
		String tmpPole = "";

		properties.poleXSize = player.s_psX;
		properties.poleYSize = player.s_psY;
		properties.colorSize = player.s_c;
		properties.lineSize = player.s_l;
		properties.ternSize = player.s_t;
		addBall = player.s_addBall;
		curMagic = player.s_curMagic;
		additionalBall = player.s_addBallNextTern;
		tmpPole = player.s_pole;
		StringTokenizer st = new StringTokenizer(tmpPole, "\n\r");
		for (int i = 0; i < properties.poleXSize; i++)
			for (int j = 0; j < properties.poleYSize; j++) {
				pole[i][j] = Integer.parseInt(st.nextToken());
				if (pole[i][j] == 0) {
					cellsFree.add(new Cell(i, j, 0));
				}
				if ((pole[i][j] > 0) && (pole[i][j] <= MAXIMUM_COLOR_SIZE)) {
					Ball b = new Ball(new Cell(i, j, pole[i][j]), this);
					b.setState(Ball.NORMAL);
					balls.add(b);
				}
				if (pole[i][j] > MAXIMUM_COLOR_SIZE) {
					Ball b = new Ball(new Cell(i, j, pole[i][j]), this);
					b.setState(Ball.SMALL);
					ballsNextTern.add(b);
					cellsFree.add(new Cell(i, j, pole[i][j]));
				}
			}
		// poleToString();
		return score;
	}

	@Override
	public void clearSavedGame() {
		player.s_score = 0;
		player.s_pole = "";
	}
}
