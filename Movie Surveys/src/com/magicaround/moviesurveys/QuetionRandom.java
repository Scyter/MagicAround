package com.magicaround.moviesurveys;

import java.util.Random;

public class QuetionRandom extends Random {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int nextQuestion(int size) {
		int result = nextInt(size);

		int rnd = nextInt(100);
		// Log.d("rnd", "?" + String.valueOf(result) + "/" +
		// String.valueOf(size)
		// + ":" + String.valueOf(rnd));
		if (rnd < (int) (result * 100 / size)) {
			result = nextInt(size);
			// Log.d("rnd",
			// "!" + String.valueOf(result) + "/" + String.valueOf(size));
		}
		return result;
	}
}
