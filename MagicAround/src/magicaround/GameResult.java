package magicaround;

public class GameResult implements Comparable<Object> {

	public int ID;
	public double globalID;
	public int game;
	public double result;
	public double time;
	public int level;
	public int favorite;
	public int hated;
	public String info;

	public GameResult() {
	}

	public GameResult(double r) {
		result = r;
	}

	public GameResult(int g, double res, int l, int f, int h, String info) {
		game = g;
		level = l;
		result = res;
		favorite = f;
		hated = h;
		this.info = info;
	}

	@Override
	public int compareTo(Object o) {
		int res = 0;
		GameResult compared = (GameResult) o;
		if (compared.result < this.result) {
			res = 1;
		}
		if (compared.result > this.result) {
			res = -1;
		}
		return res;
	}

}
