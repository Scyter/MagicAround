package com.magicaround.lines.simple;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.magicaround.lines.Ball;
import com.magicaround.lines.Cell;
import com.magicaround.lines.LinesPlayer;
import com.magicaround.lines.WorldProperties;

/**
 * @author ScyTer Класс обычной игры в Lines
 * 
 * 
 */
public class SimpleLinesWorld implements IBallListener, IWorld {

	public static final int READY = 0;
	public static final int RUNNING = 1;
	public static final int PAUSED = 2;
	public static final int TASK_END = 3;
	public static final int LEVEL_END = 4;
	public static final int OVER = 5;
	public static final int BALL_MOVING = 6;
	public static final int SPELLING = 7;

	public final int MAXIMUM_COLOR_SIZE = 20;

	public WorldProperties properties;
	public int[][] pole;
	public int score;
	public int worldState;

	public Cell selectedCell;
	public Cell newSelectedCell;
	public boolean cellIsSelected;

	public IWorldListener listener;
	public int weight;
	public int height;

	public ArrayList<Ball> balls;
	public ArrayList<Ball> ballsToRemove;
	public ArrayList<Ball> ballsNextTern;
	public ArrayList<Cell> cellsFree;
	public ArrayList<Cell> cellsForMove;
	public ArrayList<Cell> cellsWay;
	public boolean additionalBall;

	private int Nk;
	private int[][] waveAlg;

	public int tempValue;
	public Random rand;
	// public String scoreFile = "fmlscore.scyele";
	// public String poleFile = "fmlpole.scyele";

	public LinesPlayer player;

	public SimpleLinesWorld(IWorldListener l, int w, int h, WorldProperties p,
			LinesPlayer player) {
		this.properties = p;
		pole = new int[properties.poleXSize][properties.poleYSize];
		rand = new Random();
		this.listener = l;
		this.weight = w;
		this.height = h;
		score = 0;
		this.player = player;

	}

	/**
	 * Метод начала новой игры
	 */
	@Override
	public void startGame() {
		listener.startNewGame();
		newPole();
		generateNextTern();
		nextTern(0);

	}

	@Override
	public void update(float deltaTime) {
		ArrayList<Ball> t = new ArrayList<Ball>();
		for (Ball b : balls)
			t.add(b);
		for (Ball b : t)
			b.update(deltaTime);
		for (Ball b : ballsNextTern) {
			b.update(deltaTime);
		}

		// ball.stateTime += deltaTime;

	}

	/**
	 * Следующий ход. Вырост старых и генерация новых
	 */

	protected void nextTern(int i) {
		// Gdx.app.log("slw", "nextTern");
		checkAddBalls();
		resolveNextTern(i);
		generateNextTern();
		// Gdx.app.log(
		// "slw",
		// String.valueOf(balls.size()) + " "
		// + String.valueOf(cellsFree.size()));
		// poleToString();
		// ballsToString();
		// cellsToString();
	}

	protected void checkAddBalls() {
	}

	protected void newPole() {
		clearPole();
		tempValue = 0;
		Nk = properties.poleYSize * properties.poleXSize;
		waveAlg = new int[properties.poleXSize + 2][properties.poleYSize + 2];

	}

	protected void resolveNextTern(int i) {
		// poleToString();
		for (Ball b : ballsNextTern) {
			if ((b.polePosition.color > MAXIMUM_COLOR_SIZE)) {
				b.polePosition.color -= MAXIMUM_COLOR_SIZE;
				pole[b.polePosition.x][b.polePosition.y] -= MAXIMUM_COLOR_SIZE;
				if (pole[b.polePosition.x][b.polePosition.y] < 0) {
					// Gdx.app.log("error", "pole < 0");
				}
			}
			b.place(balls, null);
			b.setState(Ball.GROWING);
			if (cellInFreeCells(b.polePosition.x, b.polePosition.y) != null)
				cellInFreeCells(b.polePosition.x, b.polePosition.y).place(null,
						cellsFree);

		}
		ballsNextTern.clear();
		// poleToString();
		checkBallsForRemove();
		checkBalls();

	}

	protected void generateNextTern() {
		// Gdx.app.log("slw", "generateNextTern");
		int g = 0;
		int a = additionalBall ? 1 : 0;
		while ((g < (int) properties.ternSize + a) && (g < cellsFree.size())) {
			boolean same = false;
			Cell tmpCell = cellsFree.get(rand.nextInt(cellsFree.size()));
			// Gdx.app.log("b", tmpCell.toString());
			for (Ball b : ballsNextTern) {
				// Gdx.app.log("nb", b.polePosition.toString());
				if ((b.polePosition.x == tmpCell.x)
						&& (b.polePosition.y == tmpCell.y)) {
					same = true;
					break;
				}
			}
			if (same)
				continue;
			Ball newBall = new Ball(tmpCell, this);
			newBall.polePosition.color = rand.nextInt(properties.colorSize) + 1
					+ MAXIMUM_COLOR_SIZE;
			pole[newBall.polePosition.x][newBall.polePosition.y] = newBall.polePosition.color;
			newBall.place(ballsNextTern, null);
			newBall.setState(Ball.APPEARING);
			g++;
		}
		additionalBall = false;
		// poleToString();
	}

	protected void clearPole() {
		for (int i = 0; i < properties.poleXSize; i++)
			for (int j = 0; j < properties.poleYSize; j++)
				pole[i][j] = 0;
		this.worldState = RUNNING;
		balls = new ArrayList<Ball>();
		ballsToRemove = new ArrayList<Ball>();
		ballsNextTern = new ArrayList<Ball>();
		cellsFree = new ArrayList<Cell>();
		for (int i = 0; i < properties.poleXSize; i++)
			for (int j = 0; j < properties.poleYSize; j++)
				cellsFree.add(new Cell(i, j));
		cellsForMove = new ArrayList<Cell>();
		cellsWay = new ArrayList<Cell>();
		cellIsSelected = false;
		selectedCell = new Cell();
		newSelectedCell = new Cell();
	}

	@Override
	public void selectCell(int x, int y) {
		Gdx.app.log("slw", "selectCell:" + ((Integer) x).toString() + " "
				+ ((Integer) y).toString() + "-" + String.valueOf(worldState));
		poleToString();
		ballsToString();
		cellsToString();
		switch (worldState) {
		case BALL_MOVING:
			moveBallToEnd();
			selectCellRunning(x, y);
			// selectCellBallMoving(x, y);
			return;
		case OVER:
			if (pole[x][y] == 0)
				return;
			listener.removeBall(x, y, pole[x][y]);
			pole[x][y] = 0;
			if (ballInBalls(x, y) != null)
				ballInBalls(x, y).place(null, balls);

			return;
		case RUNNING:
			selectCellRunning(x, y);

			break;
		default:
			break;
		}
	}

	@Override
	public void moveBallToEnd() {
		for (Ball b : balls) {
			if ((b.state == Ball.MOVED) || (b.state == Ball.MOVING)
					|| (b.state == Ball.MOVING1) || (b.state == Ball.MOVING2)
					|| (b.state == Ball.MOVING3) || (b.state == Ball.MOVING4)
					|| (b.state == Ball.MOVING5)) {
				b.moveToEnd();
				return;
			}
		}

	}

	protected void selectCellBallMoving(int x, int y) {
		newSelectedCell.x = x;
		newSelectedCell.y = y;
		// Gdx.app.log("cell", String.valueOf(x) + " " + String.valueOf(y));
		if (cellIsSelected) {
			return;
		} else {

			if (cellInCellsWay(x, y) != null)
				return;
			else {
				selectCellRunningNoSelected(x, y, true);
			}
		}

	}

	protected void selectCellRunning(int x, int y) {
		newSelectedCell.x = x;
		newSelectedCell.y = y;
		// Gdx.app.log("cell", String.valueOf(x) + " " + String.valueOf(y));
		if (!cellIsSelected) {
			selectCellRunningNoSelected(x, y, false);

		} else {
			if ((newSelectedCell.x == selectedCell.x)
					&& (newSelectedCell.y == selectedCell.y)) {
				removeBallSelection();
				return;
			}
			if ((pole[newSelectedCell.x][newSelectedCell.y] == 0)
					|| (pole[newSelectedCell.x][newSelectedCell.y] > properties.colorSize)) {
				checkTernAvailability();
			} else {// добавить еще проверку на новые
					// шары
				selectCellRunningAnotherBall(x, y);
			}
		}
	}

	@Override
	public void removeBallSelection() {
		selectedCell.unselect();
		newSelectedCell.unselect();
		for (Ball b : balls) {
			if (b.state == Ball.SELECTED)
				b.setState(Ball.NORMAL);
		}
		cellIsSelected = false;
		cellsForMove.clear();

	}

	@Override
	public void removeSkillSelection() {
	}

	protected void selectCellRunningAnotherBall(int x, int y) {

		ballInBalls(selectedCell.x, selectedCell.y).setState(Ball.NORMAL);
		ballInBalls(newSelectedCell.x, newSelectedCell.y).setState(
				Ball.SELECTED);
		selectCellRunningNoSelected(x, y, false);

	}

	// protected void selectCellRunningSameSell(int x, int y) {
	//
	//
	// }

	protected void selectCellRunningNoSelected(int x, int y, boolean moving) {
		if ((pole[newSelectedCell.x][newSelectedCell.y] > 0)
				&& (pole[newSelectedCell.x][newSelectedCell.y] <= MAXIMUM_COLOR_SIZE)) {
			cellIsSelected = true;
			// Gdx.app.log("t", Integer
			// .toString(pole[newSelectedCell.x][newSelectedCell.y]));

			selectedCell.x = newSelectedCell.x;
			selectedCell.y = newSelectedCell.y;
			Ball t = ballInBalls(x, y);
			if (t != null)
				t.setState(Ball.SELECTED);
			newSelectedCell.unselect();
			if (!calculateAvaliableMovement(moving)) {
				listener.noMoveAvaliable();
				if (ballInBalls(x, y) != null)
					ballInBalls(x, y).setState(Ball.NO_WAY);
				cellIsSelected = false;
				newSelectedCell.unselect();
				selectedCell.unselect();
			}
		}

	}

	/**
	 * Алгоритм взят из checkTernAvailibility только для поиска доступных клеток
	 */
	protected Boolean calculateAvaliableMovement(boolean moving) {
		cellsForMove.clear();
		int i, j, Ni = 0;
		for (i = 0; i < properties.poleXSize + 2; i++)
			for (j = 0; j < properties.poleYSize + 2; j++) {
				waveAlg[i][j] = 255;
			}
		for (i = 1; i < properties.poleXSize + 1; i++)
			for (j = 1; j < properties.poleYSize + 1; j++) {
				if ((pole[i - 1][j - 1] == 0)
						|| ((pole[i - 1][j - 1] > MAXIMUM_COLOR_SIZE)))
					waveAlg[i][j] = 254;
				if (moving)
					if (pole[i - 1][j - 1] > MAXIMUM_COLOR_SIZE)
						waveAlg[i][j] = 255;
				if (pole[i - 1][j - 1] < 0)
					waveAlg[i][j] = 255;
			}
		waveAlg[selectedCell.x + 1][selectedCell.y + 1] = 0;

		for (Ni = 0; Ni <= Nk; Ni++) {
			for (i = 1; i < properties.poleXSize + 1; i++)
				for (j = 1; j < properties.poleYSize + 1; j++) {
					if (waveAlg[i][j] == Ni) {
						if (waveAlg[i + 1][j] == 254)
							waveAlg[i + 1][j] = Ni + 1;
						if (waveAlg[i - 1][j] == 254)
							waveAlg[i - 1][j] = Ni + 1;
						if (waveAlg[i][j + 1] == 254)
							waveAlg[i][j + 1] = Ni + 1;
						if (waveAlg[i][j - 1] == 254)
							waveAlg[i][j - 1] = Ni + 1;
					}
				}
		}

		for (i = 1; i < properties.poleXSize + 1; i++)
			for (j = 1; j < properties.poleYSize + 1; j++) {
				if ((waveAlg[i][j] < 253) && (waveAlg[i][j] > 0)) {
					Cell c = new Cell(i - 1, j - 1, waveAlg[i][j]);
					c.place(cellsForMove, null);
				}
			}

		if (cellsForMove.size() == 0)
			return false;
		else
			return true;

	}

	/**
	 * Алгоритм проверки хода. Создается массив, который со всех сторон на 1
	 * клетку больше поля и выполняется проверка, куда можно попасть из
	 * выбранной клетки В алгоритме 255 - клетка занята; 254 - на клетку пожно
	 * пойти; 253 - выбранная клетка; 0 - целевая клетка; 1+ - число ходов из
	 * целевой клетки до выбранной
	 */

	protected void checkTernAvailability() {
		if (cellInCellsForMove(newSelectedCell.x, newSelectedCell.y) != null) {
			findWay();
		} else {
			calculateAvaliableMovement(false);
			listener.noMoveAvaliable();
		}
	}

	/**
	 * Метод нахождения пути к цели
	 */
	protected void findWay() {
		cellsWay.clear();
		cellsWay.add(new Cell(newSelectedCell.x, newSelectedCell.y));
		int x = newSelectedCell.x + 1;
		int y = newSelectedCell.y + 1;
		int currentValue = waveAlg[x][y];
		int t = 255;
		while (currentValue > 0) {
			Cell nextCell = new Cell(x + 1, y);
			t = waveAlg[x + 1][y];
			if (t < currentValue) {
				currentValue = waveAlg[x + 1][y];
				nextCell.x = x + 1;
				nextCell.y = y;
			}
			t = waveAlg[x - 1][y];
			if (t < currentValue) {
				currentValue = waveAlg[x - 1][y];
				nextCell.x = x - 1;
				nextCell.y = y;
			}
			t = waveAlg[x][y + 1];
			if (t < currentValue) {
				currentValue = waveAlg[x][y + 1];
				nextCell.x = x;
				nextCell.y = y + 1;
			}
			t = waveAlg[x][y - 1];
			if (t < currentValue) {
				currentValue = waveAlg[x][y - 1];
				nextCell.x = x;
				nextCell.y = y - 1;
			}
			x = nextCell.x;
			y = nextCell.y;
			nextCell.x--;
			nextCell.y--;
			cellsWay.add(nextCell);
		}
		String sway = "";
		for (Cell c : cellsWay) {
			sway += String.valueOf(c.x) + "," + String.valueOf(c.y) + " ";
		}
		sway += "\r\n";
		Gdx.app.log("way", sway);
		moveBall();
	}

	/**
	 * Метод перемещения шара
	 * 
	 * @param way
	 */
	protected void moveBall() {
		Gdx.app.log("slw", "moveBall");
		listener.moveBall();
		this.worldState = BALL_MOVING;

		// poleToString();
		// ballsToString();
		// cellsToString();

		tempValue = pole[newSelectedCell.x][newSelectedCell.y];
		if (ballInNextTernBalls(newSelectedCell.x, newSelectedCell.y) != null) {
			ballInNextTernBalls(newSelectedCell.x, newSelectedCell.y)
					.setStateSmallSwap(cellsWay);
		}
		if (ballInBalls(selectedCell.x, selectedCell.y) != null) {
			ballInBalls(selectedCell.x, selectedCell.y).setStateMoving(
					cellsWay, 0, 0);
		} else {
			// Gdx.app.log("err",
			// "Нужно двигать несуществующий мяч, чекай алгоритмы");
		}
		// Gdx.app.log("error",
		// "Прошлая ошибка - не ошибка, а просто отсутсвие шара в целевой точке");
		Cell c = cellInFreeCells(newSelectedCell.x, newSelectedCell.y);
		if (c != null)
			c.place(null, cellsFree);
		c = new Cell(selectedCell.x, selectedCell.y, 0);
		c.place(cellsFree, null);
		pole[newSelectedCell.x][newSelectedCell.y] = pole[selectedCell.x][selectedCell.y];
		pole[selectedCell.x][selectedCell.y] = tempValue;
		selectedCell.unselect();
		newSelectedCell.unselect();
		cellIsSelected = false;
		cellsForMove.clear();

		// poleToString();
		// ballsToString();
		// cellsToString();

	}

	/**
	 * Проверка на 0 шаров или на число шаров, больше поля
	 */

	protected void checkBalls() {
		// Gdx.app.log("slw", "checkBalls");
		if (balls.size() == 0) {
			if (ballsNextTern.size() == 0) {
				generateNextTern();
			}
			resolveNextTern(0);
		} else if (balls.size() == properties.poleXSize * properties.poleYSize)
			gameOver();

		// poleToString();
		// ballsToString();
		// cellsToString();

	}

	/**
	 * Конец игры
	 */
	protected void gameOver() {
		// Gdx.app.log("slw", "gameOver");

		worldState = OVER;
		boolean h = false;
		switch (properties.worldType) {
		case IWorld.CLASSIC:
			if (score > player.c_h_score) {
				player.c_h_score = score;
				h = true;
			}
			break;
		case IWorld.HARD:
			if (score > player.h_h_score) {
				player.h_h_score = score;
				h = true;
			}
			break;
		case IWorld.SURVIVAL:
			if (score > player.s_h_score) {
				player.s_h_score = score;
				h = true;
			}
			break;
		case IWorld.CAMPAIGN:
			if (score > player.m_h_score) {
				player.m_h_score = score;
				h = true;
			}
			break;
		default:
			break;
		}
		listener.gameOver(this.properties.worldType, h);

	}

	/**
	 * Проверяет, надо ли убирать какие-либо шары (т.е. собрана ли линия)
	 * 
	 * @return true, если что-то убирать надо; false, если нет
	 */

	protected boolean checkBallsForRemove() {
		// Gdx.app.log("slw", "checkBallsForRemove");
		int row = 0;
		int k, t = 0;
		int curColor = 0;
		boolean hit = false;
		// HashMap<Integer, Integer> ternScore = new HashMap<Integer,
		// Integer>();
		int ternScore[] = { 0, 0, 0, 0 };
		for (int i = 0; i < properties.poleXSize; i++) {
			for (int j = 0; j < properties.poleYSize; j++) {
				// проверка каждой ячейки
				if ((pole[i][j] > 0) && (pole[i][j] <= properties.colorSize)) {
					curColor = pole[i][j];
					// проверка по горизонтали
					row = 1;
					for (k = i + 1; k < properties.poleXSize; k++) {
						if (inRemove(i, j, 1))
							break;
						if (pole[k][j] == curColor) {
							row++;
						} else {
							if (row >= properties.lineSize) {
								ternScore[0] = row;
								hit = true;
								for (int i1 = i; i1 <= k - 1; i1++) {
									if (ballInBalls(i1, j) != null) {
										ballInBalls(i1, j).removeSign.add(1);
										ballInBalls(i1, j).place(ballsToRemove,
												null);

									} else {
										// Gdx.app.log("error",
										// "нет шара");
									}
								}
							}
							row = 0;
							break;
						}
					}
					if (row >= properties.lineSize) {
						ternScore[0] = row;
						hit = true;
						for (int i1 = i; i1 <= k - 1; i1++) {
							Ball tBall = ballInBalls(i1, j);
							if (tBall == null) {
								// Gdx.app.log("error", "нет шара");
							}
							tBall.removeSign.add(1);
							tBall.place(ballsToRemove, null);

						}
					}
					row = 0;
					// проверка по вертикали
					row = 1;
					for (k = j + 1; k < properties.poleYSize; k++) {
						if (inRemove(i, j, 2))
							break;
						if (pole[i][k] == curColor) {
							row++;
						} else {
							if (row >= properties.lineSize) {
								ternScore[1] = row;
								hit = true;
								for (int j1 = j; j1 <= k - 1; j1++) {
									ballInBalls(i, j1).removeSign.add(2);
									ballInBalls(i, j1).place(ballsToRemove,
											null);

								}
							}
							row = 0;
							break;
						}
					}
					if (row >= properties.lineSize) {
						ternScore[1] = row;
						hit = true;
						for (int j1 = j; j1 <= k - 1; j1++) {
							ballInBalls(i, j1).removeSign.add(2);
							ballInBalls(i, j1).place(ballsToRemove, null);

						}
					}
					row = 0;
					// проверка по диагонали вверх
					// вправо
					row = 1;

					for (k = i + 1, t = j + 1; (k < properties.poleXSize)
							&& (t < properties.poleYSize); t++, k++) {
						if (inRemove(i, j, 3))
							break;
						if (pole[k][t] == curColor) {
							row++;
						} else {
							if (row >= properties.lineSize) {
								ternScore[2] = row;
								hit = true;
								for (int i1 = i, j1 = j; (i1 <= k - 1)
										&& (j1 <= t - 1); i1++, j1++) {
									ballInBalls(i1, j1).removeSign.add(3);
									ballInBalls(i1, j1).place(ballsToRemove,
											null);

								}
							}
							row = 0;
							break;
						}
					}
					if (row >= properties.lineSize) {
						ternScore[2] = row;
						hit = true;
						for (int i1 = i, j1 = j; (i1 <= k - 1) && (j1 <= t - 1); i1++, j1++) {
							ballInBalls(i1, j1).removeSign.add(3);
							ballInBalls(i1, j1).place(ballsToRemove, null);

						}
					}
					row = 0;
					// проверка по диагонали вниз
					// вправо
					row = 1;

					for (k = i + 1, t = j - 1; (k < properties.poleXSize)
							&& (t >= 0); k++, t--) {
						if (inRemove(i, j, 4))
							break;
						if (pole[k][t] == curColor) {
							row++;
						} else {
							if (row >= properties.lineSize) {
								ternScore[3] = row;
								hit = true;
								for (int i1 = i, j1 = j; (i1 < k) && (j1 > t); i1++, j1--) {
									ballInBalls(i1, j1).removeSign.add(4);
									ballInBalls(i1, j1).place(ballsToRemove,
											null);

								}
							}
							row = 0;
							break;
						}
					}
					if (row >= properties.lineSize) {
						ternScore[3] = row;
						hit = true;
						for (int i1 = i, j1 = j; (i1 < k) && (j1 > t); i1++, j1--) {
							ballInBalls(i1, j1).removeSign.add(4);
							ballInBalls(i1, j1).place(ballsToRemove, null);

						}
					}
					row = 0;
				}
			}
		}

		for (int i = 0; i < 4; i++) {
			if (ternScore[i] > 0) {
				selectedCell.unselect();
				newSelectedCell.unselect();
				cellIsSelected = false;
				cellsForMove.clear();
				for (Ball b : balls)
					if (b.state == Ball.SELECTED)
						b.setState(Ball.NORMAL);
				calculateScore(ternScore);
				return hit;
			}
		}

		return hit;

	}

	protected void calculateScore(int[] ternScore) {
		int sumTernScore = 0;
		int addTernScore = 0;

		ArrayList<Integer> lines = new ArrayList<Integer>();
		for (int i : ternScore) {
			sumTernScore += i;
			if (i > 0) {
				lines.add(i);
				if (i == 6)
					addTernScore += 1;
				if (i == 7)
					addTernScore += 3;
				if (i == 8)
					addTernScore += 5;
				if (i == 9)
					addTernScore += 10;
				if (i > 9)
					addTernScore += (i - properties.lineSize + 1) * 2;

			}

		}
		if (lines.size() == 2)
			addTernScore += 10;
		if (lines.size() == 3)
			addTernScore += 20;
		if (lines.size() == 4)
			addTernScore += 40;
		float sX = 0, sY = 0;
		for (Ball b : ballsToRemove) {
			sX += b.polePosition.x;
			sY += b.polePosition.y;
		}
		sX = sX / ballsToRemove.size();
		sY = sY / ballsToRemove.size();
		listener.addScore(sumTernScore, addTernScore, lines.size(), sX, sY,
				ballsToRemove.get(0).polePosition.color);
		addScore(sumTernScore, addTernScore);
		clearBalls();

	}

	protected void addScore(int sumTernScore, int addTernScore) {
		score += sumTernScore;
		score += addTernScore;
		player.addExp(sumTernScore + addTernScore * 2);

	}

	/**
	 * Удалить все шары, подлежащие удалению (собранные в линию)
	 */
	protected void clearBalls() {

		for (Ball b : ballsToRemove) {
			listener.removeBall(b.polePosition.x, b.polePosition.y,
					b.polePosition.color);
		}
		for (Ball b : ballsToRemove) {
			pole[b.polePosition.x][b.polePosition.y] = 0;
			Cell freeCell = new Cell(b.polePosition.x, b.polePosition.y, 0);
			boolean add = true;
			for (Cell c : cellsFree) {
				if ((c.x == freeCell.x) && (c.y == freeCell.y))
					add = false;
			}
			if (add)
				cellsFree.add(freeCell);

			b.place(null, balls);
		}
		ballsToRemove.clear();
		// ArrayList<Ball> tmpBalls = new ArrayList<Ball>();
		// for (Ball b : ballsToRemove) {
		// tmpBalls.add(b);
		// }
		// for (Ball b : tmpBalls) {
		// b.place(null, ballsToRemove);
		// }
		checkBalls();

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
	protected Ball ballInBalls(int x, int y) {
		// Gdx.app.log("ball", Integer.toString(x) + "-" + Integer.toString(y));
		for (Ball b : balls) {
			if ((b.polePosition.x == x) && (b.polePosition.y == y)) {
				return b;
			}
		}
		// Gdx.app.log("error",
		// "Не нашли нужный шар по координатам " +
		// String.valueOf(x) + " "
		// + String.valueOf(y));
		return null;
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
	protected Ball ballInNextTernBalls(int x, int y) {
		// Gdx.app.log("ball", Integer.toString(x) + "-" + Integer.toString(y));
		for (Ball b : ballsNextTern) {
			if ((b.polePosition.x == x) && (b.polePosition.y == y)) {
				return b;
			}
		}
		// Gdx.app.log("error",
		// "Не нашли нужный шар по координатам " +
		// String.valueOf(x) + " "
		// + String.valueOf(y));
		return null;
	}

	/**
	 * Находит клетку в списке клеток для хода
	 * 
	 * @param x
	 *            координата по X
	 * @param y
	 *            координата по Y
	 * @return найденная клетка или null, если его нет
	 */
	protected Cell cellInCellsForMove(int x, int y) {
		// Gdx.app.log("cell", Integer.toString(x) + "-" + Integer.toString(y));
		for (Cell c : cellsForMove) {
			if ((c.x == x) && (c.y == y)) {
				return c;
			}
		}
		// Gdx.app.log("error",
		// "Не нашли клетку для хода по координатам "
		// + String.valueOf(x) + " " + String.valueOf(y));
		return null;
	}

	/**
	 * Находит клетку в списке пустых клеток
	 * 
	 * @param x
	 *            координата по X
	 * @param y
	 *            координата по Y
	 * @return найденная клетка или null, если его нет
	 */
	protected Cell cellInFreeCells(int x, int y) {
		// Gdx.app.log("cell", Integer.toString(x) + "-" + Integer.toString(y));
		for (Cell c : cellsFree) {
			if ((c.x == x) && (c.y == y)) {
				return c;
			}
		}
		// Gdx.app.log("error",
		// "Не нашли такую пустую клетку по координатам "
		// + String.valueOf(x) + " " + String.valueOf(y));
		return null;
	}

	/**
	 * Находит клетку в списке клеток пути
	 * 
	 * @param x
	 *            координата по X
	 * @param y
	 *            координата по Y
	 * @return найденная клетка или null, если его нет
	 */
	protected Cell cellInCellsWay(int x, int y) {
		// Gdx.app.log("cell", Integer.toString(x) + "-" + Integer.toString(y));
		for (Cell c : cellsWay) {
			if ((c.x == x) && (c.y == y)) {
				return c;
			}
		}
		// Gdx.app.log("error",
		// "Не нашли такую пустую клетку по координатам "
		// + String.valueOf(x) + " " + String.valueOf(y));
		return null;
	}

	/**
	 * Проверяет, подлежит ли уже эта ячейка удалению
	 * 
	 * @param i
	 *            координата по Х
	 * @param j
	 *            координата по Y
	 * @param s
	 *            признак удаление
	 * @return true, если эта ячейка уже подлежит удалению по этому признаку
	 *         (направление удаления линии)
	 */
	protected boolean inRemove(int i, int j, int s) {
		for (Ball b : ballsToRemove) {
			if ((b.polePosition.x == i) && (b.polePosition.y == j))
				for (int sign : b.removeSign) {
					if (sign == s)
						return true;
				}

		}

		return false;
	}

	@Override
	public void saveGame() {
		if (worldState == SimpleLinesWorld.OVER)
			return;
		if (score == 0) {
			clearSavedGame();
			return;
		}

		if (properties.worldType == SimpleLinesWorld.CLASSIC) {
			player.c_score = score;
			if (score > player.c_h_score) {
				player.c_h_score = score;
				// Submith record
			}
			player.c_psX = properties.poleXSize;
			player.c_psY = properties.poleYSize;
			player.c_c = properties.colorSize;
			player.c_l = properties.lineSize;
			player.c_t = properties.ternSize;
			player.c_pole = "";
			for (int i = 0; i < properties.poleXSize; i++)
				for (int j = 0; j < properties.poleYSize; j++) {
					player.c_pole += Integer.toString(pole[i][j]) + "\r\n";
				}
		} else if (properties.worldType == SimpleLinesWorld.HARD) {
			player.h_score = score;
			if (score > player.h_h_score) {
				player.h_h_score = score;
				// Submith record
			}
			player.h_psX = properties.poleXSize;
			player.h_psY = properties.poleYSize;
			player.h_c = properties.colorSize;
			player.h_l = properties.lineSize;
			player.h_t = properties.ternSize;
			player.h_pole = "";
			for (int i = 0; i < properties.poleXSize; i++)
				for (int j = 0; j < properties.poleYSize; j++) {
					player.h_pole += Integer.toString(pole[i][j]) + "\r\n";
				}
		
		} else if (properties.worldType == SimpleLinesWorld.CAMPAIGN) {		
			player.m_score = score;
			if (score > player.m_h_score) {
				player.m_h_score = score;
				// Submith record
			}
			player.m_psX = properties.poleXSize;
			player.m_psY = properties.poleYSize;
			player.m_c = properties.colorSize;
			player.m_l = properties.lineSize;
			player.m_t = properties.ternSize;
			player.m_pole = "";
			for (int i = 0; i < properties.poleXSize; i++)
				for (int j = 0; j < properties.poleYSize; j++) {
					player.m_pole += Integer.toString(pole[i][j]) + "\r\n";
				}
		}
	}

	@Override
	public int loadGame() {
		newPole();
		cellsFree.clear();
		if (properties.worldType == SimpleLinesWorld.CLASSIC) {
			score = player.c_score;
		} else if (properties.worldType == SimpleLinesWorld.HARD) {
			score = player.h_score;
		} else if (properties.worldType == SimpleLinesWorld.CAMPAIGN) {
			score = player.m_score;
		}
		if (score == 0)
			return score;
		String tmpPole = "";
		if (properties.worldType == SimpleLinesWorld.CLASSIC) {
			properties.poleXSize = player.c_psX;
			properties.poleYSize = player.c_psY;
			properties.colorSize = player.c_c;
			properties.lineSize = player.c_l;
			properties.ternSize = player.c_t;
			tmpPole = player.c_pole;
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
		} else if (properties.worldType == SimpleLinesWorld.HARD) {
			properties.poleXSize = player.h_psX;
			properties.poleYSize = player.h_psY;
			properties.colorSize = player.h_c;
			properties.lineSize = player.h_l;
			properties.ternSize = player.h_t;
			tmpPole = player.h_pole;
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
		} else if (properties.worldType == SimpleLinesWorld.CAMPAIGN) {
			properties.poleXSize = player.m_psX;
			properties.poleYSize = player.m_psY;
			properties.colorSize = player.m_c;
			properties.lineSize = player.m_l;
			properties.ternSize = player.m_t;
			tmpPole = player.m_pole;
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
		}
		// poleToString();
		return score;
	}

	@Override
	public void clearSavedGame() {
		if (properties.worldType == SimpleLinesWorld.CLASSIC) {
			player.c_score = 0;
			player.c_pole = "";
		} else if (properties.worldType == SimpleLinesWorld.HARD) {
			player.h_score = 0;
			player.h_pole = "";
		} else if (properties.worldType == SimpleLinesWorld.SURVIVAL) {
			player.s_score = 0;
			player.s_pole = "";
		} else if (properties.worldType == SimpleLinesWorld.CAMPAIGN) {
			player.m_score = 0;
			player.m_pole = "";
		}

	}

	@Override
	public void movingStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void movingFinished(int sign) {
		for (Ball b : ballsNextTern) {
			if (b.needSwipe)
				b.SmallSwap();
		}
		if (!checkBallsForRemove()) {
			if (sign != 1)
				nextTern(1);
		}
		if ((listener.state() == SimpleLinesScreen.S_GAME_OVER)
				|| (listener.state() == SimpleLinesScreen.S_GAME_OVER_HIGHT_SCORE))
			worldState = OVER;
		else
			worldState = RUNNING;
		poleToString();

	}

	@Override
	public int score() {
		return this.score;

	}

	@Override
	public int getCellColor(int x, int y) {
		return pole[x][y];
	}

	@Override
	public void selectSkill(int skillID) {
	}

	protected void poleToString() {
		String tmp = worldState + "\r\n";
		for (int j = properties.poleYSize - 1; j >= 0; j--) {
			for (int i = 0; i < properties.poleXSize; i++) {
				if (pole[i][j] < 10)
					tmp += String.valueOf(pole[i][j]) + "   ";
				else if (pole[i][j] < 100)
					tmp += String.valueOf(pole[i][j]) + "  ";
				else
					tmp += String.valueOf(pole[i][j]) + " ";
			}
			tmp += "\r\n";

		}
		tmp += "\r\n";
		Gdx.app.log("", tmp);
	}

	protected void ballsToString() {
		String tmp = String.valueOf(balls.size() + " - Число шаров\r\n");
		for (Ball b : balls)
			tmp += b.polePosition.toString() + ":"
					+ Integer.toString(b.polePosition.color) + "; ";
		tmp += "\r\n";
		tmp += String.valueOf(ballsNextTern.size() + " - Следующий ход\r\n");
		for (Ball b : ballsNextTern)
			tmp += b.polePosition.toString() + ":"
					+ Integer.toString(b.polePosition.color) + "; ";

		Gdx.app.log("", tmp);
	}

	protected void cellsToString() {
		String tmp = "\r\nCells: " + String.valueOf(cellsFree.size()) + "\r\n";

		for (Cell c : cellsFree)
			tmp += c.toString() + ":" + Integer.toString(c.color) + "; ";
		tmp += "\r\n";
		for (Cell c : cellsForMove)
			tmp += c.toString() + ":" + Integer.toString(c.color) + "\r\n";
		Gdx.app.log("", tmp);

	}

	@Override
	public void spellEnds(int i) {
	}

	@Override
	public void rainFinished(Ball b) {
	}

}
