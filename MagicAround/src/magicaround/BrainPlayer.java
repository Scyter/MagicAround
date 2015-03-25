package magicaround;

import java.util.ArrayList;
import java.util.Collections;

public class BrainPlayer {
	public ArrayList<GameResult> results;
	public int favorite;
	public int hated;

	// gameTypes:
	// 1 - Кликер
	// 2 - Кликер УЕФА
	// 11 - Оффсайд

	public BrainPlayer() {
		results = new ArrayList<GameResult>();
		favorite = -1;
		hated = -1;
	}

	public int getLevel(int gameType) {
		try {
			return getLevelNoCatch(gameType);
		} catch (Exception e) {
			return 1;
		}

	}

	public int getLevelNoCatch(int gameType) {
		if (results.size() <= 0)
			return 1;
		Collections.sort(results);
		switch (gameType) {
		case 1:
		case 2:
			return results.get(results.size() - 1).level - 1;
		case 11:
			return results.get(results.size() - 1).level - 1;
		case 15:

			break;

		default:
			break;
		}
		return 1;
	}

}
