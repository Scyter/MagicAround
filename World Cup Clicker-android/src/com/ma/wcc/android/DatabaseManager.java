package com.ma.wcc.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import magicaround.GameResult;

import com.ma.wcc.Assets;
import com.ma.wcc.FootballTeam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseManager {
	public static final int DB_VERSION = 1;

	public static final String TABLE = "Result";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_GLOBAL_ID = "GlobalID";
	public static final String COLUMN_GAME = "Game";
	public static final String COLUMN_RESULT = "Result";
	public static final String COLUMN_LEVEL = "Level";
	public static final String COLUMN_TIME = "Time";
	public static final String COLUMN_FAVORITE = "Favorite";
	public static final String COLUMN_HATED = "Hated";
	public static final String COLUMN_INFO = "Info";

	private static final String DB_CREATE_Q = "CREATE TABLE " + TABLE + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_GLOBAL_ID
			+ " INTEGER, " + COLUMN_GAME + " INTEGER, " + COLUMN_RESULT
			+ " DOUBLE, " + COLUMN_LEVEL + " INTEGER, " + COLUMN_TIME
			+ " DOUBLE, " + COLUMN_FAVORITE + " INTEGER, " + COLUMN_HATED
			+ " INTEGER, " + COLUMN_INFO + " TEXT " + ")";

	public static final String TABLE_T = "Results";
	public static final String COLUMN_TID = "I";
	public static final String COLUMN_TR = "R";
	private static final String DB_CREATE_R = "CREATE TABLE " + TABLE_T + " ("
			+ COLUMN_TID + " INTEGER, " + COLUMN_TR + " DOUBLE " + ")";

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

	public void addResult(int game, double result, int f, int h, String info) {
		sqliteDB = getInstance().openDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_GAME, game);
		cv.put(COLUMN_RESULT, result);
		Date now = new Date();
		double dt = now.getTime();
		cv.put(COLUMN_TIME, dt);
		cv.put(COLUMN_FAVORITE, f);
		cv.put(COLUMN_HATED, h);
		cv.put(COLUMN_INFO, info);

		sqliteDB.insert(TABLE, null, cv);
		getInstance().closeDatabase();
	}

	public double addResult(GameResult r, double dt) {
		sqliteDB = getInstance().openDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_GAME, r.game);
		cv.put(COLUMN_RESULT, r.result);
		cv.put(COLUMN_LEVEL, r.level);
		cv.put(COLUMN_TIME, dt);
		cv.put(COLUMN_FAVORITE, r.favorite);
		cv.put(COLUMN_HATED, r.hated);
		cv.put(COLUMN_INFO, r.info);

		long localID = sqliteDB.insert(TABLE, null, cv);
		getInstance().closeDatabase();
		return localID;
	}

	public ArrayList<GameResult> getResutls(int game) {
		ArrayList<GameResult> results = new ArrayList<GameResult>();
		try {
			sqliteDB = getInstance().openDatabase();
			Cursor c = sqliteDB.query(TABLE, null, COLUMN_GAME + " = ?",
					new String[] { String.valueOf(game) }, null, null, null);
			if (c.moveToFirst()) {
				int IDIndex = c.getColumnIndex(COLUMN_ID);
				int globalIDIndex = c.getColumnIndex(COLUMN_GLOBAL_ID);
				int gameIndex = c.getColumnIndex(COLUMN_GAME);
				int resultIndex = c.getColumnIndex(COLUMN_RESULT);
				int levelIndex = c.getColumnIndex(COLUMN_LEVEL);
				int timeIndex = c.getColumnIndex(COLUMN_TIME);
				int favoriteIndex = c.getColumnIndex(COLUMN_FAVORITE);
				int hatedIndex = c.getColumnIndex(COLUMN_HATED);
				int infoIndex = c.getColumnIndex(COLUMN_INFO);
				do {
					GameResult r = new GameResult();
					r.ID = c.getInt(IDIndex);
					r.globalID = c.getInt(globalIDIndex);
					r.game = c.getInt(gameIndex);
					r.result = c.getDouble(resultIndex);
					r.level = c.getInt(levelIndex);
					r.time = c.getDouble(timeIndex);
					r.favorite = c.getInt(favoriteIndex);
					r.hated = c.getInt(hatedIndex);
					r.info = c.getString(infoIndex);
					results.add(r);

				} while (c.moveToNext());
			}
			getInstance().closeDatabase();
			for (GameResult r : results) {
				Log.d("aa", String.valueOf(r.result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	public void updateVote(ContentValues cv, double localID) {
		sqliteDB = getInstance().openDatabase();
		Log.d("aa", "Vote updated");
		sqliteDB.update(TABLE, cv, COLUMN_ID + "=" + String.valueOf(localID),
				null);
		getInstance().closeDatabase();
	}

	public void addTeam(int id, double res) {
		ContentValues cv = new ContentValues();
		try {
			cv.put(COLUMN_TID, id);
			cv.put(COLUMN_TR, res);
			int up = sqliteDB.update(TABLE_T, cv,
					COLUMN_TID + " = " + String.valueOf(id), null);
			if (up <= 0) {
				sqliteDB.insert(TABLE_T, null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<FootballTeam> getTeams() {
		ArrayList<FootballTeam> teams = new ArrayList<FootballTeam>();
		try {
			sqliteDB = getInstance().openDatabase();
			Cursor c = sqliteDB.query(TABLE_T, null, null, null, null, null,
					null);
			if (c.moveToFirst()) {
				int TIndex = c.getColumnIndex(COLUMN_TID);
				int RIndex = c.getColumnIndex(COLUMN_TR);
				do {
					FootballTeam t = new FootballTeam(c.getInt(TIndex),
							c.getDouble(RIndex));
					teams.add(t);

				} while (c.moveToNext());
			}
			getInstance().closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teams;
	}

	public void setTeamsForAssets() {
		try {
			sqliteDB = getInstance().openDatabase();
			Cursor c = sqliteDB.query(TABLE_T, null, null, null, null, null,
					null);
			if (c.moveToFirst()) {
				int TIndex = c.getColumnIndex(COLUMN_TID);
				int RIndex = c.getColumnIndex(COLUMN_TR);
				do {
					Assets.teams.get(c.getInt(TIndex)).result = c
							.getDouble(RIndex);

				} while (c.moveToNext());
			}
			getInstance().closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
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
			db.execSQL(DB_CREATE_R);
			try {
				sqliteDB = DatabaseManager.getInstance().openDatabase();
				sqliteDB.beginTransaction();
				for (int i = 0; i < 32; i++) {
					addTeam(i, 0);
				}
				sqliteDB.setTransactionSuccessful();
				sqliteDB.endTransaction();
				DatabaseManager.getInstance().closeDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

}