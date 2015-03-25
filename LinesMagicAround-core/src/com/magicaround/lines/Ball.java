package com.magicaround.lines;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.magicaround.lines.simple.IBallListener;

public class Ball {
	public static final int NO_STATE = -1;
	public static final int APPEARING = 0;
	public static final int SMALL = 1;
	public static final int GROWING = 2;
	public static final int NORMAL = 3;
	public static final int SELECTED = 4;
	public static final int MOVING = 5;
	public static final int DISAPPEARING = 6;
	public static final int DISAPPEARING_SMALL = 6;
	public static final int NO_WAY = 7;
	public static final int MOVED = 8;
	public static final int MOVING1 = 11;
	public static final int MOVING2 = 12;
	public static final int MOVING3 = 13;
	public static final int MOVING4 = 14;
	public static final int MOVING5 = 15;
	public static final int RAIN = 16;
	public static final int END_RAIN = 17;
	public static final int JUMPING = 18;
	public float stateTime;
	public Cell polePosition;
	public int state;
	public TextureRegion picture;
	public Random rnd;
	public ArrayList<Integer> removeSign;
	public ArrayList<Cell> way;
	private Cell currentCell;
	public Cell nextCell;
	public int step;
	public IBallListener listener;
	public boolean looping;
	public boolean xMoving;
	public int tmpColor;
	public float xDelta;
	public float yDelta;
	public float mDelta;
	public float size;
	public float visibility;
	public boolean rain;
	public float rainTime;
	public float selectedDelta[][] = Constants.selectedDelta;
	public boolean movingByEarthshake;
	public boolean needSwipe;

	// {0,1,2,3,4,5,4,3,2,1,0,-1,-2,-3,-4,-5,-4,-3,-2,-1,0};

	public Ball() {
		movingByEarthshake = false;
		state = NO_STATE;
		stateTime = 0;
		polePosition = new Cell();
		tmpColor = 0;
		rnd = new Random();
		removeSign = new ArrayList<Integer>();
		picture = new TextureRegion();
		way = new ArrayList<Cell>();
		looping = false;
		currentCell = new Cell();
		nextCell = new Cell();
		step = 0;
		xDelta = 0;
		yDelta = 0;
		xMoving = true;
		mDelta = 0;
		rain = false;
		rainTime = 0;
		needSwipe = false;

	}

	public Ball(Cell pos, IBallListener l) {
		this();
		polePosition = pos;
		listener = l;

	}

	public Ball(int w, int h) {
		this();
		int t = rnd.nextInt(100);
		picture = LinesAssets.balls[t % (LinesAssets.balls.length - 3) + 1];
		if (picture == null) {
			// Gdx.app.log("berr", String.valueOf(t) + "sdafasdf");
		}
		xDelta = rnd.nextInt(w * 2) - w;
		yDelta = rnd.nextInt(h * 2) - h;
		size = (rnd.nextInt(2500) + 1) % 128 + 1;
		mDelta = rnd.nextInt(100);

		if ((rnd.nextInt(10)) % 2 == 1)
			looping = true;
		else
			looping = false;
		if ((rnd.nextInt(10)) % 2 == 1)
			xMoving = true;
		else
			xMoving = false;

	}

	public void updateMenu(float t, int w, int h) {
		if (looping)
			size += t * 50;
		else
			size -= t * 50;
		if (size > 54) {
			size = 54;
			looping = false;
		}
		if (size < 10) {
			size = 10;
			looping = true;
		}
		yDelta -= t * 50;
		if (yDelta <= -1 * h)
			yDelta = h + 50;
		if (xMoving)
			mDelta += t * 50;
		else
			mDelta -= t * 50;
		if (Math.abs(mDelta) >= 100)
			if (xMoving)
				xMoving = false;
			else
				xMoving = true;

	}

	public void place(ArrayList<Ball> where, ArrayList<Ball> from) {
		if (where != null) {
			where.add(this);
			if (from != null)
				from.remove(this);
		} else {
			if (from != null)
				from.remove(this);
		}
	}

	public void setState(int s) {

		this.state = s;
		stateTime = 0;
		xDelta = 0;
		yDelta = 0;
		visibility = 100;
		mDelta = 0;
		if (s == SMALL) {
			stateTime += rnd.nextInt(100);
			size = 100;
		}
		if (s == NORMAL) {
			stateTime += rnd.nextInt(1000);
			size = 100;
		}
		if (s == GROWING) {
			size = 20;
		}
		if (s == NO_WAY) {
			size = 100;
		}
		if (s == SELECTED) {
			size = 100;
		}
		if (s == MOVED) {
			size = 100;
		}
		if (s == RAIN) {
			size = 100;
			rainTime = 0;
		}
		if (s == END_RAIN) {
			rainTime = 0;
		}
	}

	public int getState() {
		return this.state;
	}

	@SuppressWarnings("unchecked")
	public void setStateMoving(ArrayList<Cell> w, int t, int sign) {
		if (t == 0) {
			if (w.size() > 15) {
				t = 5;
			} else {
				t = rnd.nextInt(5) + 1;
			}
		}
		if (sign == 1)
			movingByEarthshake = true;
		switch (t) {
		case 1:
			setState(Ball.MOVING1);
			break;
		case 2:
			setState(Ball.MOVING2);
			break;
		case 3:
			setState(Ball.MOVING3);
			break;
		case 4:
			setState(Ball.MOVING4);
			break;
		case 5:
			setState(Ball.MOVING5);
			break;

		default:
			setState(Ball.MOVING1);
			break;
		}
		visibility = 100;
		step = 1;
		tmpColor = polePosition.color;

		way = (ArrayList<Cell>) w.clone();
		currentCell = way.get(way.size() - step);
		nextCell = way.get(way.size() - step - 1);

		this.polePosition.x = currentCell.x;
		this.polePosition.y = currentCell.y;
		polePosition.color = tmpColor;

	}

	public void setStateSmallSwap(ArrayList<Cell> w) {
		way = w;
		needSwipe = true;

	}

	public void SmallSwap() {
		tmpColor = polePosition.color;
		this.polePosition = way.get(way.size() - 1);
		polePosition.color = tmpColor;
		needSwipe = false;
		setState(Ball.APPEARING);
	}

	public void update(float deltaTime) {
		stateTime++;
		switch (state) {
		case SELECTED:
			xDelta = Constants.selectedDelta[(int) (stateTime)
					% Constants.selectedDelta.length][0];
			yDelta = Constants.selectedDelta[(int) (stateTime)
					% Constants.selectedDelta.length][1];
			break;
		case SMALL:
			size = Constants.smallDelta[(int) (stateTime)
					% Constants.smallDelta.length];
			break;

		case NO_WAY:
			if (stateTime >= Constants.noWayDelta.length) {
				setState(NORMAL);
				break;
			}
			xDelta = Constants.noWayDelta[(int) (stateTime)
					% Constants.noWayDelta.length][0];
			yDelta = Constants.noWayDelta[(int) (stateTime)
					% Constants.noWayDelta.length][1];
			break;
		case APPEARING:
			if (stateTime >= Constants.appearingDelta.length) {
				setState(SMALL);
				size = Constants.appearingDelta[Constants.appearingDelta.length - 1];
				break;
			}
			size = Constants.appearingDelta[(int) (stateTime)];
			break;
		case GROWING:
			if (stateTime >= Constants.growingDelta.length) {
				setState(NORMAL);
				size = Constants.growingDelta[Constants.growingDelta.length - 1];
				break;
			}
			size = Constants.growingDelta[(int) (stateTime)];
			break;
		case MOVING1:
			stateTime++;
			// Gdx.app.log("moving1",
			// String.valueOf(stateTime) + " " + String.valueOf(xDelta)
			// + " " + String.valueOf(yDelta));
			if (stateTime < Constants.movingDelta.length) {
				if (currentCell.x < nextCell.x)
					xDelta = Constants.movingDelta[(int) (stateTime)
							% Constants.movingDelta.length];
				if (currentCell.x > nextCell.x)
					xDelta = -1
							* Constants.movingDelta[(int) (stateTime)
									% Constants.movingDelta.length];
				if (currentCell.y < nextCell.y)
					yDelta = Constants.movingDelta[(int) (stateTime)
							% Constants.movingDelta.length];
				if (currentCell.y > nextCell.y)
					yDelta = -1
							* Constants.movingDelta[(int) (stateTime)
									% Constants.movingDelta.length];

			} else {
				polePosition = nextCell;
				polePosition.color = tmpColor;
				step++;

				if (step > way.size() - 1) {
					setState(Ball.MOVED);
					break;

				}
				xDelta = 0;
				yDelta = 0;
				stateTime = 0;
				currentCell = way.get(way.size() - step);
				nextCell = way.get(way.size() - step - 1);
			}
			break;

		case MOVING3:
		case MOVING4:
			stateTime++;

			if (stateTime < Constants.movingDelta.length) {
				visibility = Constants.visibility[(int) (stateTime)
						% Constants.visibility.length];

			} else {
				polePosition = nextCell;
				polePosition.color = tmpColor;
				step++;

				if (step > way.size() - 1) {
					setState(Ball.MOVED);
					break;

				}
				xDelta = 0;
				yDelta = 0;
				stateTime = 0;
				visibility = 100;
				currentCell = way.get(way.size() - step);
				nextCell = way.get(way.size() - step - 1);
			}
			// Gdx.app.log(
			// "moving" + String.valueOf(state),
			// String.valueOf(stateTime) + " "
			// + String.valueOf(visibility));
			break;
		case MOVING2:
			stateTime++;
			if (stateTime < Constants.movingDelta.length) {
				if (currentCell.x < nextCell.x) {
					xDelta = Constants.movingDelta1[(int) (stateTime)
							% Constants.movingDelta1.length][0];
					yDelta = Constants.movingDelta1[(int) (stateTime)
							% Constants.movingDelta1.length][1];
				}
				if (currentCell.x > nextCell.x) {
					xDelta = -1
							* Constants.movingDelta1[(int) (stateTime)
									% Constants.movingDelta1.length][0];
					yDelta = Constants.movingDelta1[(int) (stateTime)
							% Constants.movingDelta1.length][1];
				}
				if (currentCell.y < nextCell.y) {
					yDelta = Constants.movingDelta1[(int) (stateTime)
							% Constants.movingDelta1.length][1];
					yDelta += Constants.movingDelta1[(int) (stateTime)
							% Constants.movingDelta1.length][0];
				}
				if (currentCell.y > nextCell.y) {
					yDelta = -1
							* Constants.movingDelta1[(int) (stateTime)
									% Constants.movingDelta1.length][0];
					yDelta += Constants.movingDelta1[(int) (stateTime)
							% Constants.movingDelta1.length][1];

				}

			} else {
				polePosition = nextCell;
				polePosition.color = tmpColor;
				step++;

				if (step > way.size() - 1) {
					setState(Ball.MOVED);
					break;

				}
				xDelta = 0;
				yDelta = 0;
				stateTime = 0;
				currentCell = way.get(way.size() - step);
				nextCell = way.get(way.size() - step - 1);
			}
			// Gdx.app.log("moving2",
			// String.valueOf(stateTime) + " " + String.valueOf(xDelta)
			// + " " + String.valueOf(yDelta));
			break;

		case MOVING5:
			stateTime++;
			if (step == 1) {
				if (stateTime < Constants.movingDelta4.length) {

					visibility = Constants.movingDelta4[(int) (stateTime)
							% Constants.movingDelta4.length][1];
					mDelta = Constants.movingDelta4[(int) (stateTime)
							% Constants.movingDelta4.length][0];

				} else {
					stateTime = 0;
					polePosition = way.get(0);
					polePosition.color = tmpColor;
					step++;
					xDelta = 0;
					yDelta = 0;
					stateTime = 0;

				}
			} else {
				if (stateTime < Constants.movingDelta4.length) {
					visibility = Constants.movingDelta4[(int) (Constants.movingDelta4.length - stateTime)
							% Constants.movingDelta4.length][1];
					mDelta = Constants.movingDelta4[(int) (Constants.movingDelta4.length - stateTime)
							% Constants.movingDelta4.length][0];

				} else {
					setState(Ball.MOVED);
					xDelta = 0;
					yDelta = 0;
					stateTime = 0;
					visibility = 100;
					break;

				}
			}
			// Gdx.app.log(
			// "moving" + String.valueOf(state),
			// String.valueOf(stateTime) + " "
			// + String.valueOf(visibility) + " "
			// + String.valueOf(mDelta));
			break;
		case MOVED:
			if (stateTime >= Constants.movedDelta.length) {
				setState(NORMAL);
				listener.movingFinished(movingByEarthshake ? 1 : 0);
				movingByEarthshake = false;
				break;
			}
			size = Constants.movedDelta[(int) stateTime];

			break;
		case NORMAL:
			xDelta = Constants.normalPositionX[(int) (stateTime)
					% Constants.normalPositionX.length];
			yDelta = Constants.normalPositionY[(int) (stateTime)
					% Constants.normalPositionY.length];
			size = Constants.normalSize[(int) (stateTime)
					% Constants.normalSize.length];

			break;

		case RAIN:
			rainTime += deltaTime;
			// Gdx.app.log("b", toString() + " " + String.valueOf(stateTime));
			if (stateTime >= Constants.rainTime) {
				setState(END_RAIN);
			}
			break;
		case END_RAIN:
			rainTime += deltaTime;
			// Gdx.app.log("be", toString() + " " + String.valueOf(stateTime));
			if (stateTime >= Constants.rainTime) {
				// Gdx.app.log("be", "Ball:RainFinished");
				listener.rainFinished(this);
			}
			break;
		case 355:
			if (stateTime >= 2) {
				setState(Ball.NORMAL);
				listener.movingFinished(movingByEarthshake ? 1 : 0);
			}
			break;

		default:
			break;
		}
	}

	public void moveToEnd() {
		polePosition = way.get(0);
		polePosition.color = tmpColor;
		xDelta = 0;
		yDelta = 0;
		stateTime = 0;
		mDelta = 0;
		visibility = 100;
		setState(Ball.NORMAL);
		listener.movingFinished(movingByEarthshake ? 1 : 0);

	}

	@Override
	public String toString() {
		return (String.valueOf(this.polePosition.x) + ":"
				+ String.valueOf(polePosition.y) + "("
				+ String.valueOf(polePosition.color) + ")");
	}
}
