package com.magicaround.db;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.magicaround.moviesurveys.Answer;
import com.magicaround.moviesurveys.Consts;
import com.magicaround.moviesurveys.Survey;
import com.magicaround.task.SetVoteTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseManager {
	public static final int DB_VERSION = 1;

	public static final int ANSWER_SORT_VOTES_LOCAL = 1;
	public static final int ANSWER_SORT_RATING_LOCAL = 2;
	public static final int ANSWER_SORT_RATING_GLOBAL = 3;
	public static final int ANSWER_SORT_FOR_QUESTION = 4;

	public static final String ID = "ID";

	public static final String TABLE_Q = "Question";
	public static final String COLUMN_Q_ID = "Q_ID";
	public static final String COLUMN_Q_TYPE = "Type";
	public static final String COLUMN_Q_STATE = "State";
	public static final String COLUMN_Q_QUESTION = "Question";
	public static final String COLUMN_Q_PICTURE = "Picture";
	public static final String COLUMN_Q_INFO = "Info";
	public static final String COLUMN_Q_USERS = "Users";
	public static final String COLUMN_Q_VOTES = "Votes";

	private static final String DB_CREATE_Q = "CREATE TABLE " + TABLE_Q + " ("
			+ COLUMN_Q_ID + " INTEGER PRIMARY KEY, " + COLUMN_Q_TYPE
			+ " INTEGER, " + COLUMN_Q_STATE + " INTEGER, " + COLUMN_Q_QUESTION
			+ " TEXT, " + COLUMN_Q_PICTURE + " TEXT, " + COLUMN_Q_INFO
			+ " TEXT, " + COLUMN_Q_USERS + " INTEGER, " + COLUMN_Q_VOTES
			+ " DOUBLE " + ")";

	public static final String TABLE_QL = "QL";
	public static final String COLUMN_QL_QID = "Q_ID";
	public static final String COLUMN_QL_LID = "L_ID";
	public static final String COLUMN_QL_QUESTION = "Question";
	public static final String COLUMN_QL_PICTURE = "Picture";
	public static final String COLUMN_QL_INFO = "Info";

	private static final String DB_CREATE_QL = "CREATE TABLE " + TABLE_QL
			+ " (" + COLUMN_QL_QID + " INTEGER, " + COLUMN_QL_LID
			+ " INTEGER, " + COLUMN_QL_QUESTION + " TEXT, " + COLUMN_QL_PICTURE
			+ " TEXT, " + COLUMN_QL_INFO + " TEXT " + ")";

	public static final String TABLE_A = "Answer";
	public static final String COLUMN_A_ID = "A_ID";
	public static final String COLUMN_A_QID = "Q_ID";
	public static final String COLUMN_A_STATE = "State";
	public static final String COLUMN_A_TEXT = "Text";
	public static final String COLUMN_A_YEAR = "Year";
	public static final String COLUMN_A_PICTURE = "Picture";
	public static final String COLUMN_A_INFO = "Info";
	public static final String COLUMN_A_RATING = "Rating";
	public static final String COLUMN_A_VOTES = "Votes";
	public static final String COLUMN_A_LAST_POSITION = "LastPosition";
	public static final String COLUMN_A_PRE_LAST_POSITION = "PreLastPosition";
	public static final String COLUMN_A_RATING_LOCAL = "RatingLocal";
	public static final String COLUMN_A_VOTES_LOCAL = "VotesLocal";

	private static final String DB_CREATE_A = "CREATE TABLE " + TABLE_A + " ("
			+ COLUMN_A_ID + " INTEGER PRIMARY KEY, " + COLUMN_A_QID
			+ " INTEGER, " + COLUMN_A_STATE + " INTEGER, " + COLUMN_A_TEXT
			+ " TEXT, " + COLUMN_A_YEAR + " INTEGER, " + COLUMN_A_PICTURE
			+ " TEXT, " + COLUMN_A_INFO + " TEXT, " + COLUMN_A_RATING
			+ " DOUBLE," + COLUMN_A_VOTES + " BIGINT, "
			+ COLUMN_A_LAST_POSITION + " INTEGER, "
			+ COLUMN_A_PRE_LAST_POSITION + " INTEGER, " + COLUMN_A_RATING_LOCAL
			+ " DOUBLE DEFAULT 400, " + COLUMN_A_VOTES_LOCAL
			+ " INT DEFAULT 0 " + ")";

	public static final String TABLE_AL = "AL";
	public static final String COLUMN_AL_AID = "A_ID";
	public static final String COLUMN_AL_QID = "Q_ID";
	public static final String COLUMN_AL_LID = "L_ID";
	public static final String COLUMN_AL_TEXT = "Text";
	public static final String COLUMN_AL_PICTURE = "Picture";

	private static final String DB_CREATE_AL = "CREATE TABLE " + TABLE_AL
			+ " (" + COLUMN_AL_AID + " INTEGER, " + COLUMN_AL_QID
			+ " INTEGER, " + COLUMN_AL_LID + " INTEGER, " + COLUMN_AL_TEXT
			+ " TEXT, " + COLUMN_QL_PICTURE + " TEXT " + ")";

	public static final String TABLE_V = "Vote";
	public static final String COLUMN_V_LOCAL_ID = "V_ID_local";
	public static final String COLUMN_V_ID = "V_ID";
	public static final String COLUMN_V_QID = "Q_ID";
	private static final String DB_CREATE_V = "CREATE TABLE " + TABLE_V + " ("
			+ COLUMN_V_LOCAL_ID + " INTEGER PRIMARY KEY, " + COLUMN_V_ID
			+ " DOUBLE DEFAULT 0, " + COLUMN_V_QID + " INTEGER)";

	public static final String TABLE_R = "Result";
	public static final String COLUMN_R_V_LOCAL_ID = "V_ID_local";
	public static final String COLUMN_R_AID = "A_ID";
	public static final String COLUMN_R_RESULT = "Result";
	private static final String DB_CREATE_R = "CREATE TABLE " + TABLE_R + " ("
			+ COLUMN_R_V_LOCAL_ID + " INTEGER, " + COLUMN_R_AID + " INTEGER, "
			+ COLUMN_R_RESULT + " INTEGER)";

	public static final String TABLE_G = "Graph";
	public static final String COLUMN_G_ID = "ID";
	public static final String COLUMN_G_QID = "Q_ID";
	public static final String COLUMN_G_AID = "A_ID";
	public static final String COLUMN_G_POSITION = "Position";
	public static final String COLUMN_G_DATE = "Date";

	private static final String DB_CREATE_G = "CREATE TABLE " + TABLE_G + " ("
			+ COLUMN_G_ID + " DOUBLE, " + COLUMN_G_QID + " INTEGER, "
			+ COLUMN_G_AID + " INTEGER, " + COLUMN_G_POSITION + " INTEGER, "
			+ COLUMN_G_DATE + " DATETIME)";

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private static DatabaseManager instance;
	public static DBHelper mDatabaseHelper;
	public SQLiteDatabase sqliteDB;
	static Context cont;

	public DatabaseManager(Context cnt) {
		cont = cnt;
		mDatabaseHelper = new DBHelper(cnt, Consts.DB_NAME, null,
				DatabaseManager.DB_VERSION);
	}

	public static synchronized void initializeInstance(DBHelper helper) {
		if (instance == null) {
			instance = new DatabaseManager(cont);
			mDatabaseHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					DatabaseManager.class.getSimpleName()
							+ " is not initialized"
							+ ", call initializeInstance(..) method first.");
		}
		return instance;
	}

	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			sqliteDB = mDatabaseHelper.getWritableDatabase();
		}
		return sqliteDB;
	}

	public synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			sqliteDB.close();
		}
	}

	// ///////////////////////////////////QUESTIONS//////////////////////////////////////
	public ArrayList<Survey> getAllQuestionsArray() {
		ArrayList<Survey> surveys = new ArrayList<Survey>();
		sqliteDB = getInstance().openDatabase();
		Cursor c = getAllQuestionsForActivity();
		// int surveysCount = c.getCount();
		if (c.moveToFirst()) {
			int IDIndex = c.getColumnIndex(COLUMN_Q_ID);
			int typeIndex = c.getColumnIndex(COLUMN_Q_TYPE);
			int questionIndex = c.getColumnIndex(COLUMN_Q_QUESTION);
			int pictureIndex = c.getColumnIndex(COLUMN_Q_PICTURE);
			int infoIndex = c.getColumnIndex(COLUMN_Q_INFO);
			int usersIndex = c.getColumnIndex(COLUMN_Q_USERS);
			int votesIndex = c.getColumnIndex(COLUMN_Q_VOTES);
			int stateIndex = c.getColumnIndex(COLUMN_Q_STATE);
			do {
				Survey s = new Survey();
				s.id = c.getInt(IDIndex);
				s.type = c.getInt(typeIndex);
				s.question = c.getString(questionIndex);
				s.picture = c.getString(pictureIndex);
				s.info = c.getString(infoIndex);

				Cursor cl = getQuestionLanguage(s.id);
				int lID = Consts.defaulLanguage;
				String q = "";
				String p = "";
				String i = "";
				if (cl.moveToFirst()) {
					int lIDIndex_l = cl.getColumnIndex(COLUMN_QL_LID);
					int questionIndex_l = cl.getColumnIndex(COLUMN_QL_QUESTION);
					int pictureIndex_l = cl.getColumnIndex(COLUMN_QL_PICTURE);
					int infoIndex_l = cl.getColumnIndex(COLUMN_QL_INFO);
					do {
						lID = cl.getInt(lIDIndex_l);
						if (lID == Consts.language) {
							q = cl.getString(questionIndex_l);
							p = cl.getString(pictureIndex_l);
							i = cl.getString(infoIndex_l);
							break;
						} else if (lID == Consts.defaulLanguage) {
							q = cl.getString(questionIndex_l);
							p = cl.getString(pictureIndex_l);
							i = cl.getString(infoIndex_l);
						}
					} while (cl.moveToNext());
				}
				if (!q.equalsIgnoreCase("")) {
					s.question = q;
				}
				if (!p.equalsIgnoreCase("")) {
					s.picture = p;
				}
				if (!i.equalsIgnoreCase("")) {
					s.info = i;
				}

				s.users = c.getInt(usersIndex);
				s.votes = c.getInt(votesIndex);
				s.state = c.getInt(stateIndex);

				surveys.add(s);
			} while (c.moveToNext());
		}
		getInstance().closeDatabase();
		return surveys;
	}

	public Cursor getAllQuestions() {
		return sqliteDB.query(TABLE_Q, null, null, null, null, null, null);
	}

	public Cursor getAllQuestionsForActivity() {
		return sqliteDB.query(TABLE_Q, null, COLUMN_Q_STATE + " <= 2", null,
				null, null, COLUMN_Q_STATE + " ASC");
	}

	public Cursor getQuestionByID(int id) {
		return sqliteDB.query(TABLE_Q, new String[] { COLUMN_Q_ID },
				COLUMN_Q_ID + " = ?", new String[] { String.valueOf(id) },
				null, null, COLUMN_Q_ID + " DESC", "1");
	}

	public Cursor getQuestionLanguage(int id) {
		return sqliteDB.query(TABLE_QL, null, COLUMN_QL_QID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
	}

	public void addQuestion(int id, String txt, String img) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_Q_ID, id);
		cv.put(COLUMN_Q_QUESTION, txt);
		cv.put(COLUMN_Q_PICTURE, img);
		sqliteDB.insert(TABLE_Q, null, cv);
	}

	public void addQuestions(JSONArray jArray, JSONArray lArray) {
		try {
			sqliteDB = DatabaseManager.getInstance().openDatabase();
			sqliteDB.beginTransaction();

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("aa", "id: " + json_data.getInt("Q_ID") + ", text: "
						+ json_data.getString("Question") + ", pic: "
						+ json_data.getString("Picture"));
				addQuestion(json_data);
			}
			for (int i = 0; i < lArray.length(); i++) {
				JSONObject json_data = lArray.getJSONObject(i);
				Log.i("aa",
						"id: " + json_data.getInt("Q_ID") + ", text: "
								+ json_data.getString("Question") + ", pic: "
								+ json_data.getString("Picture") + ", l: "
								+ json_data.getString("L_ID"));
				addQuestionLanguage(json_data);
			}
			sqliteDB.setTransactionSuccessful();
			sqliteDB.endTransaction();
			DatabaseManager.getInstance().closeDatabase();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void addQuestion(JSONObject jsonQuestion) {
		ContentValues cv = new ContentValues();
		try {
			int id = jsonQuestion.getInt("Q_ID");
			cv.put(COLUMN_Q_ID, id);
			cv.put(COLUMN_Q_TYPE, jsonQuestion.getInt(COLUMN_Q_TYPE));
			cv.put(COLUMN_Q_STATE, jsonQuestion.getInt(COLUMN_Q_STATE));
			cv.put(COLUMN_Q_QUESTION, jsonQuestion.getString(COLUMN_Q_QUESTION));
			cv.put(COLUMN_Q_PICTURE, jsonQuestion.getString(COLUMN_Q_PICTURE));
			cv.put(COLUMN_Q_INFO, jsonQuestion.getString(COLUMN_Q_INFO));
			cv.put(COLUMN_Q_USERS, jsonQuestion.getInt(COLUMN_Q_USERS));
			cv.put(COLUMN_Q_VOTES, jsonQuestion.getDouble(COLUMN_Q_VOTES));

			int up = sqliteDB.update(TABLE_Q, cv,
					COLUMN_Q_ID + "=" + String.valueOf(id), null);
			if (up <= 0) {
				sqliteDB.insert(TABLE_Q, null, cv);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addQuestionLanguage(JSONObject jsonQuestion) {
		ContentValues cv = new ContentValues();
		try {
			int qID = jsonQuestion.getInt(COLUMN_QL_QID);
			int lID = jsonQuestion.getInt(COLUMN_QL_LID);
			cv.put(COLUMN_QL_QID, qID);
			cv.put(COLUMN_QL_LID, lID);
			cv.put(COLUMN_QL_QUESTION,
					jsonQuestion.getString(COLUMN_QL_QUESTION));
			cv.put(COLUMN_QL_PICTURE, jsonQuestion.getString(COLUMN_QL_PICTURE));
			cv.put(COLUMN_QL_INFO, jsonQuestion.getString(COLUMN_QL_INFO));

			int up = sqliteDB.update(TABLE_QL, cv,
					COLUMN_QL_QID + "=" + String.valueOf(qID) + " AND "
							+ COLUMN_QL_LID + "=" + String.valueOf(lID), null);
			if (up <= 0) {
				sqliteDB.insert(TABLE_QL, null, cv);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delQuestion(long id) {
		sqliteDB.delete(TABLE_Q, COLUMN_Q_ID + " = " + id, null);
	}

	// ////////////////////////////////////ANSWERS//////////////////////////////////////////////
	public Cursor getAnswersByQuestionID(int id) {
		return sqliteDB.query(TABLE_A, null, COLUMN_A_QID + " = ?",
				new String[] { String.valueOf(id) }, null, null, COLUMN_A_ID
						+ " ASC");
	}

	public Cursor getAnswersByQIDSort(int id, String sort) {
		return sqliteDB.query(TABLE_A, null, COLUMN_A_QID + " = ?",
				new String[] { String.valueOf(id) }, null, null, sort);
	}

	public Cursor getAnswersByQIDForResults(int id, String sort) {
		return sqliteDB.query(TABLE_A, null, COLUMN_A_QID + " = ? AND "
				+ COLUMN_A_STATE + " <= 1",
				new String[] { String.valueOf(id) }, null, null, sort);
	}

	public Cursor getAnswersByQIDForResultsEliminated(int id, String sort) {
		return sqliteDB.query(TABLE_A, null, COLUMN_A_QID + " = ? AND "
				+ COLUMN_A_STATE + " = 2", new String[] { String.valueOf(id) },
				null, null, sort);
	}

	public Cursor getAnswersByQIDForQuestion(int id) {
		return sqliteDB.query(TABLE_A, null, COLUMN_A_QID + " = ? AND "
				+ COLUMN_A_STATE + " <= 1",
				new String[] { String.valueOf(id) }, null, null, COLUMN_A_STATE
						+ " DESC, " + COLUMN_A_VOTES_LOCAL + " ASC, "
						+ COLUMN_A_VOTES + " ASC");
	}

	public ArrayList<Answer> getAnswersByQID(int questionID, int sortType) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		Log.d("aa", "GetAnswerFromBase Start");
		sqliteDB = getInstance().openDatabase();
		Cursor c;
		switch (sortType) {
		case ANSWER_SORT_VOTES_LOCAL:
			c = getAnswersByQIDSort(questionID, COLUMN_A_VOTES_LOCAL + " ASC");
			answers.addAll(getAnswerFromCursor(c));
			break;
		case ANSWER_SORT_RATING_GLOBAL:
			c = getAnswersByQIDForResults(questionID, COLUMN_A_RATING + " DESC");
			answers.addAll(getAnswerFromCursor(c));
			c = getAnswersByQIDForResultsEliminated(questionID, COLUMN_A_RATING
					+ " DESC");
			answers.addAll(getAnswerFromCursor(c));
			break;
		case ANSWER_SORT_RATING_LOCAL:
			c = getAnswersByQIDForResults(questionID, COLUMN_A_RATING_LOCAL
					+ " DESC");
			answers.addAll(getAnswerFromCursor(c));
			c = getAnswersByQIDForResultsEliminated(questionID,
					COLUMN_A_RATING_LOCAL + " DESC");
			answers.addAll(getAnswerFromCursor(c));
			break;
		case ANSWER_SORT_FOR_QUESTION:
			c = getAnswersByQIDForQuestion(questionID);
			answers.addAll(getAnswerFromCursor(c));
			break;
		default:
			c = getAnswersByQIDSort(questionID, COLUMN_A_RATING + " DESC");
			break;
		}
		getInstance().closeDatabase();
		Log.d("aa", "GetAnswerFromBase Ends");
		return answers;
	}

	public ArrayList<Answer> getAnswerFromCursor(Cursor c) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		int idIndex = c.getColumnIndex(COLUMN_A_ID);
		int qIDIndex = c.getColumnIndex(COLUMN_A_QID);
		int stateIndex = c.getColumnIndex(COLUMN_A_STATE);
		int textIndex = c.getColumnIndex(COLUMN_A_TEXT);
		int yearIndex = c.getColumnIndex(COLUMN_A_YEAR);
		int pictureIndex = c.getColumnIndex(COLUMN_A_PICTURE);
		int infoIndex = c.getColumnIndex(COLUMN_A_INFO);
		int ratingIndex = c.getColumnIndex(COLUMN_A_RATING);
		int votesIndex = c.getColumnIndex(COLUMN_A_VOTES);
		int lpIndex = c.getColumnIndex(COLUMN_A_LAST_POSITION);
		int plpIndex = c.getColumnIndex(COLUMN_A_PRE_LAST_POSITION);
		int ratingLocalIndex = c.getColumnIndex(COLUMN_A_RATING_LOCAL);
		int votesLocalIndex = c.getColumnIndex(COLUMN_A_VOTES_LOCAL);
		if (c.moveToFirst()) {
			do {
				Answer a = new Answer();
				a.id = c.getInt(idIndex);
				a.q_id = c.getInt(qIDIndex);
				a.state = c.getInt(stateIndex);
				a.text = c.getString(textIndex);
				a.year = c.getInt(yearIndex);
				a.picture = c.getString(pictureIndex);
				a.info = c.getString(infoIndex);

				Cursor al = getAnswerLanguage(a.id);
				int lID = Consts.defaulLanguage;
				String t = "";
				String p = "";
				if (al.moveToFirst()) {
					int lIDIndex_l = al.getColumnIndex(COLUMN_AL_LID);
					int textIndex_l = al.getColumnIndex(COLUMN_AL_TEXT);
					int pictureIndex_l = al.getColumnIndex(COLUMN_AL_PICTURE);
					do {
						lID = al.getInt(lIDIndex_l);
						if (lID == Consts.language) {
							t = al.getString(textIndex_l);
							p = al.getString(pictureIndex_l);
							break;
						} else if (lID == Consts.defaulLanguage) {
							t = al.getString(textIndex_l);
							p = al.getString(pictureIndex_l);
						}
					} while (al.moveToNext());
				}
				if (!t.equalsIgnoreCase("")) {
					a.text = t;
				}
				if (!p.equalsIgnoreCase("")) {
					a.picture = p;
				}
				if (a.info.equalsIgnoreCase(a.text))
					a.info = "";
				a.rating = c.getDouble(ratingIndex);
				a.votes = c.getInt(votesIndex);
				a.lastPosition = c.getInt(lpIndex);
				a.preLastPosition = c.getInt(plpIndex);
				a.ratingLocal = c.getDouble(ratingLocalIndex);
				a.votesLocal = c.getInt(votesLocalIndex);
				answers.add(a);
			} while (c.moveToNext());
		}
		return answers;

	}

	public Cursor getAnswerByID(int id) {
		return sqliteDB.query(TABLE_A, null, COLUMN_A_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, COLUMN_A_ID,
				"1");
	}

	public Cursor getAnswerLanguage(int id) {
		return sqliteDB.query(TABLE_AL, null, COLUMN_AL_AID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
	}

	// public void addAnswerOld(JSONObject jsonAnswer) {
	// ContentValues cv = new ContentValues();
	// try {
	// int id = jsonAnswer.getInt("A_ID");
	// Cursor c = getAnswerByID(id);
	// int i = c.getCount();
	// cv.put(COLUMN_A_ID, id);
	// cv.put(COLUMN_A_QID, jsonAnswer.getInt(COLUMN_A_QID));
	// cv.put(COLUMN_A_STATE, jsonAnswer.getInt(COLUMN_A_STATE));
	// cv.put(COLUMN_A_TEXT, jsonAnswer.getString(COLUMN_A_TEXT));
	// cv.put(COLUMN_A_YEAR, jsonAnswer.getInt(COLUMN_A_YEAR));
	// cv.put(COLUMN_A_PICTURE, jsonAnswer.getString(COLUMN_A_PICTURE));
	// cv.put(COLUMN_A_INFO, jsonAnswer.getString(COLUMN_A_INFO));
	// cv.put(COLUMN_A_RATING, jsonAnswer.getDouble(COLUMN_A_RATING));
	// cv.put(COLUMN_A_VOTES, jsonAnswer.getLong(COLUMN_A_VOTES));
	// cv.put(COLUMN_A_LAST_POSITION,
	// jsonAnswer.getInt(COLUMN_A_LAST_POSITION));
	// cv.put(COLUMN_A_PRE_LAST_POSITION,
	// jsonAnswer.getInt(COLUMN_A_PRE_LAST_POSITION));
	// if (i < 1) {
	// sqliteDB.insert(TABLE_A, null, cv);
	// } else {
	// updateAnswer(cv, id);
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void addAnsewrs(JSONArray jArray, JSONArray lArray) {
		try {
			sqliteDB = DatabaseManager.getInstance().openDatabase();
			sqliteDB.beginTransaction();
			for (int i = 0; i < jArray.length(); i++)
				addAnswer(jArray.getJSONObject(i));

			for (int i = 0; i < lArray.length(); i++)
				addAnswerLanguage(lArray.getJSONObject(i));

			Log.d("aa", "Answers adding ends");
			sqliteDB.setTransactionSuccessful();
			sqliteDB.endTransaction();
			DatabaseManager.getInstance().closeDatabase();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addAnswer(JSONObject jsonAnswer) {
		ContentValues cv = new ContentValues();
		try {
			int id = jsonAnswer.getInt(COLUMN_A_ID);
			// Cursor c = getAnswerByID(id);
			// int i = c.getCount();
			cv.put(COLUMN_A_ID, id);
			cv.put(COLUMN_A_QID, jsonAnswer.getInt(COLUMN_A_QID));
			cv.put(COLUMN_A_STATE, jsonAnswer.getInt(COLUMN_A_STATE));
			cv.put(COLUMN_A_TEXT, jsonAnswer.getString(COLUMN_A_TEXT));
			cv.put(COLUMN_A_YEAR, jsonAnswer.getInt(COLUMN_A_YEAR));
			cv.put(COLUMN_A_PICTURE, jsonAnswer.getString(COLUMN_A_PICTURE));
			cv.put(COLUMN_A_INFO, jsonAnswer.getString(COLUMN_A_INFO));
			cv.put(COLUMN_A_RATING, jsonAnswer.getDouble(COLUMN_A_RATING));
			cv.put(COLUMN_A_VOTES, jsonAnswer.getLong(COLUMN_A_VOTES));
			cv.put(COLUMN_A_LAST_POSITION,
					jsonAnswer.getInt(COLUMN_A_LAST_POSITION));
			cv.put(COLUMN_A_PRE_LAST_POSITION,
					jsonAnswer.getInt(COLUMN_A_PRE_LAST_POSITION));
			int up = sqliteDB.update(TABLE_A, cv,
					COLUMN_A_ID + "=" + String.valueOf(id), null);
			if (up <= 0) {
				sqliteDB.insert(TABLE_A, null, cv);
			}
			// sqliteDB.
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAnswerLanguage(JSONObject jsonQuestion) {
		ContentValues cv = new ContentValues();
		try {
			int aID = jsonQuestion.getInt(COLUMN_AL_AID);
			int lID = jsonQuestion.getInt(COLUMN_AL_LID);
			cv.put(COLUMN_AL_AID, aID);
			cv.put(COLUMN_AL_QID, jsonQuestion.getString(COLUMN_AL_QID));
			cv.put(COLUMN_AL_LID, lID);
			cv.put(COLUMN_AL_TEXT, jsonQuestion.getString(COLUMN_AL_TEXT));
			cv.put(COLUMN_AL_PICTURE, jsonQuestion.getString(COLUMN_AL_PICTURE));

			int up = sqliteDB.update(TABLE_AL, cv,
					COLUMN_AL_AID + "=" + String.valueOf(aID) + " AND "
							+ COLUMN_AL_LID + "=" + String.valueOf(lID), null);
			if (up <= 0) {
				sqliteDB.insert(TABLE_AL, null, cv);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateAnswer(ContentValues cv, int id) {
		sqliteDB.update(TABLE_A, cv, COLUMN_A_ID + "=" + String.valueOf(id),
				null);
	}

	// /////////////////////////////GRAPH///////////////////////////////////////
	public Cursor getGraphByID(int id) {
		return sqliteDB.query(TABLE_G, null, COLUMN_G_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, COLUMN_G_ID,
				"1");
	}

	public Cursor getGraphsByAnswerID(int id) {
		return sqliteDB.query(TABLE_G, null, COLUMN_G_AID + " = ?",
				new String[] { String.valueOf(id) }, null, null, COLUMN_G_ID);
	}

	public void addGraph(JSONObject jsonGraph) {
		ContentValues cv = new ContentValues();
		try {
			int id = jsonGraph.getInt("ID");
			Cursor c = getGraphByID(id);
			int i = c.getCount();
			cv.put(COLUMN_G_ID, id);
			cv.put(COLUMN_G_QID, jsonGraph.getInt(COLUMN_G_QID));
			cv.put(COLUMN_G_AID, jsonGraph.getInt(COLUMN_G_AID));
			cv.put(COLUMN_G_POSITION, jsonGraph.getInt(COLUMN_G_POSITION));
			cv.put(COLUMN_G_DATE, jsonGraph.getString(COLUMN_G_DATE));
			if (i < 1) {
				sqliteDB.insert(TABLE_G, null, cv);
			} else {
				updateGraph(cv, id);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateGraph(ContentValues cv, int id) {
		sqliteDB.update(TABLE_G, cv, COLUMN_G_ID + "=" + String.valueOf(id),
				null);
	}

	// ////////////////////////////VOTE//////////////////////////

	public long addVote(int questionID) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_V_QID, questionID);
		return sqliteDB.insert(TABLE_V, null, cv);
	}

	public void addVoteToAll(int questionID, int userID, int id1, int id2, int r) {
		if ((r < 0) && (r > 10))
			return;
		AddVoteRunnable avr = new AddVoteRunnable(questionID, userID, id1, id2,
				r);
		avr.run();
	}

	public void updateVote(ContentValues cv, long voteLocalID) {
		sqliteDB = getInstance().openDatabase();
		Log.d("aa", "Vote updated");
		sqliteDB.update(TABLE_V, cv,
				COLUMN_V_LOCAL_ID + "=" + String.valueOf(voteLocalID), null);
		getInstance().closeDatabase();
	}

	public Cursor getVotesForUpdate() {
		return sqliteDB.query(TABLE_V, null, COLUMN_V_ID + " <= 0 OR "
				+ COLUMN_V_ID + " = NULL", null, null, null, null);
	}

	// ////////////////////////////RESULT///////////////////////////////
	public void addResult(long localVoteID, int answerID, int r) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_R_V_LOCAL_ID, localVoteID);
		cv.put(COLUMN_R_AID, answerID);
		cv.put(COLUMN_R_RESULT, r);
		sqliteDB.insert(TABLE_R, null, cv);
	}

	public Cursor getResult(int localID) {
		return sqliteDB.query(TABLE_R, null, COLUMN_R_V_LOCAL_ID + " = ?",
				new String[] { String.valueOf(localID) }, null, null, null,
				null);
	}

	// //////////////////////////OTHER//////////////////
	public void checkVotes(int userID) {
		sqliteDB = getInstance().openDatabase();
		Cursor c = getVotesForUpdate();
		if (c.moveToFirst()) {
			do {
				int localID = c.getInt(c.getColumnIndex(COLUMN_V_LOCAL_ID));
				int questionID = c.getInt(c.getColumnIndex(COLUMN_V_QID));
				Cursor cResult = getResult(localID);
				if (cResult.moveToFirst()) {
					int id1 = cResult.getInt(cResult
							.getColumnIndex(COLUMN_R_AID));
					int r = cResult.getInt(cResult
							.getColumnIndex(COLUMN_R_RESULT));
					if (cResult.moveToNext()) {
						final int id2 = cResult.getInt(cResult
								.getColumnIndex(COLUMN_R_AID));

						SetVoteDB svt = new SetVoteDB(questionID, userID, id1,
								id2, r, localID);
						svt.execute();

					} else {
						Log.d("aa",
								"Что-то в базе явно не то");
						break;
					}
				} else {
					Log.d("aa", "Что-то в базе явно не то");
					break;
				}

			} while (c.moveToNext());
		}
		getInstance().closeDatabase();
	}

	public double newLocalRating(double r1, double r2, int res, int v1) {
		double nRes = res / 10;
		int k = 10;
		// if (v1 <= 10)
		// k = 30;
		// else if (r1 < 1000)
		// k = 15;

		double diff = r2 - r1;
		double e1 = 1 / (1 + Math.pow(10, diff / 400));
		double r_d = nRes - e1;
		double newRate = 400;
		if (r_d >= 0)
			newRate = r1 + k * (r_d);
		else
			newRate = r1 + 10 * (r_d);
		return newRate;
	}

	// ///////////////////////CLASS/////////////////////////////
	class AddVoteRunnable implements Runnable {
		int r1;
		int r2;
		int questionID;
		int userID;
		int id1;
		int id2;
		int r;

		public AddVoteRunnable(int questionID, int userID, int id1, int id2,
				int r) {
			this.r = r;
			r1 = r;
			r2 = 10 - r;
			this.questionID = questionID;
			this.userID = userID;
			this.id1 = id1;
			this.id2 = id2;
		}

		@Override
		public void run() {
			sqliteDB = getInstance().openDatabase();
			Cursor c1 = getAnswerByID(id1);
			Answer a1 = new Answer();
			if (c1.moveToFirst()) {
				a1.ratingLocal = c1.getDouble(c1
						.getColumnIndex(COLUMN_A_RATING_LOCAL));
				a1.votesLocal = c1.getInt(c1
						.getColumnIndex(COLUMN_A_VOTES_LOCAL));
			} else
				return;
			Cursor c2 = getAnswerByID(id2);
			Answer a2 = new Answer();
			if (c2.moveToFirst()) {
				a2.ratingLocal = c2.getDouble(c2
						.getColumnIndex(COLUMN_A_RATING_LOCAL));
				a2.votesLocal = c2.getInt(c2
						.getColumnIndex(COLUMN_A_VOTES_LOCAL));
			} else
				return;
			long localID = addVote(questionID);
			if (localID < 0)
				return;
			addResult(localID, id1, r1);
			addResult(localID, id2, r2);
			getInstance().closeDatabase();
			SetVoteDB svt = new SetVoteDB(questionID, userID, id1, id2, r,
					localID);
			svt.execute();
			a1.ratingLocal = newLocalRating(a1.ratingLocal, a2.ratingLocal, r1,
					a1.votesLocal);
			a2.ratingLocal = newLocalRating(a2.ratingLocal, a1.ratingLocal, r2,
					a2.votesLocal);
			a1.votesLocal++;
			a2.votesLocal++;
			sqliteDB = getInstance().openDatabase();
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_A_RATING_LOCAL, a1.ratingLocal);
			cv.put(COLUMN_A_VOTES_LOCAL, a1.votesLocal);
			updateAnswer(cv, id1);
			cv.put(COLUMN_A_RATING_LOCAL, a2.ratingLocal);
			cv.put(COLUMN_A_VOTES_LOCAL, a2.votesLocal);
			updateAnswer(cv, id2);
			getInstance().closeDatabase();
		}
	}

	class SetVoteDB extends SetVoteTask {
		long localID;

		public SetVoteDB(int q, int u, int a1, int a2, int r, long locID) {
			super(q, u, a1, a2, r);
			localID = locID;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (result == 0)
				return;
			super.onPostExecute(result);
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_V_ID, result);
			// cv.put(COLUMN_A_VOTES_LOCAL, a1.votesLocal);
			updateVote(cv, localID);
		}

	}

	public class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_Q);
			db.execSQL(DB_CREATE_QL);
			db.execSQL(DB_CREATE_A);
			db.execSQL(DB_CREATE_AL);
			db.execSQL(DB_CREATE_V);
			db.execSQL(DB_CREATE_R);
			db.execSQL(DB_CREATE_G);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}