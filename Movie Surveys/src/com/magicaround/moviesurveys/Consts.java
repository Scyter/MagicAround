package com.magicaround.moviesurveys;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class Consts {
	public static final String DB_NAME = "TM7";
	public static final String SITE_ADDRESS = "http://scyter.zz.vc/";
	public static final String PICTERES_ADDRESS = "http://scyter.zz.vc/pictures/";
	public static final String SURVEYS_SQRIPT = "select_surveys.php";
	public static final String ANSWERS_SQRIPT = "select_answers.php";
	public static final String GET_GRAPH_SQRIPT = "get_graph.php";
	public static final String ANSWERS_ID_PARAM = "q";
	public static final String USER_SQRIPT = "get_user.php";
	public static final String ADD_VOTE_SQRIPT = "add_vote.php";
	public static final String GET_IMAGE_SIZE_SQRIPT = "get_image_size.php";
	public static final String IMAGE_SIZE_PARAM = "i";
	public static final String VOTE_QUESTION_PARAM = "q";
	public static final String VOTE_USER_PARAM = "u";
	public static final String VOTE_A1_PARAM = "a1";
	public static final String VOTE_A2_PARAM = "a2";
	public static final String VOTE_RESULT_PARAM = "r";
	public static final String GRAPH_QUESTION_PARAM = "q";
	public static final String GRAPH_ANSWER_PARAM = "a";

	public static final String SETTINGS = "Settings";
	public static final String LANGUAGE_SETTED = "LanguageSetted";
	public static final String LANGUAGE = "Language";
	public static final String LANGUAGE_NUMBER = "LanguageNumber";
	public static final String USER_ID = "UserID";
	public static final String[] languages = { "ru", "en" };
	public static final int defaulLanguage = 1;

	public final static int STATUS_START = 100;
	public final static int STATUS_FINISH = 200;
	public final static String PARAM_TIME = "time";
	public final static String PARAM_TASK = "task";
	public final static String PARAM_RESULT = "result";
	public final static String PARAM_STATUS = "status";
	public final static String BROADCAST_ACTION = "com.magicaround.movie.getsurveys";

	public final static String GLOBAL_DIFF = "GlobDiff";

	public static int language = 1;

	public static int getLanguage(Context c) {
		SharedPreferences sPref = c.getSharedPreferences(Consts.SETTINGS,
				Context.MODE_PRIVATE);
		language = 1;
		if (sPref != null) {
			boolean setted = sPref.getBoolean(Consts.LANGUAGE_SETTED, false);
			if (setted)
				language = sPref.getInt(Consts.LANGUAGE_NUMBER, 1);
			else {
				String l = c.getResources().getConfiguration().locale
						.getLanguage();
				for (int i = 0; i < languages.length; i++) {
					if (languages[i].equalsIgnoreCase(l))
						language = i;
				}
			}
		}
		if (language < 0) {
			Toast.makeText(c, R.string.no_language, Toast.LENGTH_LONG).show();
			language = 1;
		}
		return language;
	}

	public static void setLanguage(Context c, boolean set, int lNum) {
		SharedPreferences sPref = c.getSharedPreferences(Consts.SETTINGS,
				Context.MODE_PRIVATE);
		Editor ed = sPref.edit();
		if (set) {
			ed.putInt(Consts.LANGUAGE_NUMBER, lNum);
			language = lNum;
		} else
			ed.putInt(Consts.LANGUAGE_NUMBER, -1);
		ed.putBoolean(Consts.LANGUAGE_SETTED, set);
		ed.commit();

	}
}
