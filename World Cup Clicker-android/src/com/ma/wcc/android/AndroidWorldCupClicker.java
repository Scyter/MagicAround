package com.ma.wcc.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import magicaround.BrainPlayer;
import magicaround.GameResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.ma.wcc.Assets;
import com.ma.wcc.FootballTeam;
import com.ma.wcc.Settings;
import com.ma.wcc.WorldCupClicker;
import com.ma.wcci.Platform;

public class AndroidWorldCupClicker extends AndroidApplication implements
		Platform, GameHelperListener {

	DatabaseManager db;
	SimpleCursorAdapter scAdapter;
	private static final String MY_AD_UNIT_ID = "ca-app-pub-7455231204823878/2726083749";

	private GameHelper gameHelper;

	public SharedPreferences sPref;
	int userID;
	boolean addDownload;
	boolean askDownload;
	boolean firstLoad;
	boolean logined;
	BrainPlayer player;
	WorldCupClicker game;
	AdView adView;
	View gameView;
	Context c;
	ImageView imView;
	ImageLoadersTaskUI ilt;

	RelativeLayout layout;

	public AndroidWorldCupClicker() {
		c = this;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Assets.countriesAll = new ArrayList<String>();
		Assets.countries = new ArrayList<String>();
		Consts.countriesImages = new ArrayList<Bitmap>();
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(true, "GPGS");
		gameHelper.setup(this);
		sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
		userID = 0;
		if (sPref != null) {
			userID = sPref.getInt(Consts.USER_ID, 0);
			addDownload = sPref.getBoolean(Consts.ADD_DOWNLOAD, false);
			askDownload = sPref.getBoolean(Consts.ASK_DOWNLOAD, true);
			logined = sPref.getBoolean(Consts.LOGINED, false);
			firstLoad = sPref.getBoolean(Consts.FIRST_LOAD, true);
		}
		Editor ed = sPref.edit();
		ed.putBoolean(Consts.FIRST_LOAD, false);
		ed.commit();

		Log.d("aa", "UserID = " + String.valueOf(userID));
		if (userID == 0) {
			GetUserTask gut = new GetUserTask();
			gut.execute();
		}
		ilt = new ImageLoadersTaskUI(this);
		if (addDownload) {
			ilt.execute(Consts.imName);
		}

		addStringsAndResources();
		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);

		layout = new RelativeLayout(this);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useWakelock = true;
		player = new BrainPlayer();
		game = new WorldCupClicker(this, player);
		// initialize(game, config);

		gameView = initializeForView(game);

		// gameHelper.setup(this);
		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdOpened() {

			}
		});
		adView.setAdUnitId(MY_AD_UNIT_ID);
		adView.setAdSize(AdSize.BANNER);
		adView.setVisibility(View.INVISIBLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		layout.addView(gameView);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		// layout.addView(b, adParams);
		layout.addView(adView, adParams);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		setContentView(layout);
		if ((askDownload) && (!addDownload))
			showDialog(1);
		if ((firstLoad) || (logined))
			loginGPGS();

		// sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
		// Editor ed = sPref.edit();
		// ed.putInt(Consts.USER_ID, userID);
		// ed.commit();

		// b.setVisibility(View.INVISIBLE);
		// adView.setVisibility(View.VISIBLE);
	}

	protected void onResume() {
		game.resume();
		super.onResume();

	}

	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(R.string.title);
			adb.setMessage(R.string.message);
			adb.setIcon(android.R.drawable.ic_dialog_info);
			adb.setPositiveButton(R.string.yes, myDialogClickListener);
			adb.setNegativeButton(R.string.no, myDialogClickListener);
			adb.setNeutralButton(R.string.dont_ask, myDialogClickListener);
			adb.setCancelable(false);
			return adb.create();
		}
		return super.onCreateDialog(id);
	}

	OnClickListener myDialogClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Editor ed;
			switch (which) {
			case Dialog.BUTTON_POSITIVE:
				Log.d("aa", "Да, что-то грузим!");
				if (ilt.getStatus() == Status.RUNNING)
					break;
				ilt.execute(Consts.imName);
				ed = sPref.edit();
				ed.putBoolean(Consts.ADD_DOWNLOAD, true);
				ed.commit();
				break;

			case Dialog.BUTTON_NEGATIVE:
				break;

			case Dialog.BUTTON_NEUTRAL:
				ed = sPref.edit();
				ed.putBoolean(Consts.ASK_DOWNLOAD, false);
				ed.putBoolean(Consts.ADD_DOWNLOAD, false);
				ed.commit();
				break;
			}
		}

	};

	private void addStringsAndResources() {
		Resources r = getResources();
		Assets.countriesAll.add(r.getString(R.string.c0));
		Assets.countriesAll.add(r.getString(R.string.c1));
		Assets.countriesAll.add(r.getString(R.string.c2));
		Assets.countriesAll.add(r.getString(R.string.c3));
		Assets.countriesAll.add(r.getString(R.string.c4));
		Assets.countriesAll.add(r.getString(R.string.c5));
		Assets.countriesAll.add(r.getString(R.string.c6));
		Assets.countriesAll.add(r.getString(R.string.c7));
		Assets.countriesAll.add(r.getString(R.string.c8));
		Assets.countriesAll.add(r.getString(R.string.c9));
		Assets.countriesAll.add(r.getString(R.string.c10));
		Assets.countriesAll.add(r.getString(R.string.c11));
		Assets.countriesAll.add(r.getString(R.string.c12));
		Assets.countriesAll.add(r.getString(R.string.c13));
		Assets.countriesAll.add(r.getString(R.string.c14));
		Assets.countriesAll.add(r.getString(R.string.c15));
		Assets.countriesAll.add(r.getString(R.string.c16));
		Assets.countriesAll.add(r.getString(R.string.c17));
		Assets.countriesAll.add(r.getString(R.string.c18));
		Assets.countriesAll.add(r.getString(R.string.c19));
		Assets.countriesAll.add(r.getString(R.string.c20));
		Assets.countriesAll.add(r.getString(R.string.c21));
		Assets.countriesAll.add(r.getString(R.string.c22));
		Assets.countriesAll.add(r.getString(R.string.c23));
		Assets.countriesAll.add(r.getString(R.string.c24));
		Assets.countriesAll.add(r.getString(R.string.c25));
		Assets.countriesAll.add(r.getString(R.string.c26));
		Assets.countriesAll.add(r.getString(R.string.c27));
		Assets.countriesAll.add(r.getString(R.string.c28));
		Assets.countriesAll.add(r.getString(R.string.c29));
		Assets.countriesAll.add(r.getString(R.string.c30));
		Assets.countriesAll.add(r.getString(R.string.c31));
		Assets.countriesAll.add(r.getString(R.string.c32));
		Assets.countriesAll.add(r.getString(R.string.c33));
		Assets.countriesAll.add(r.getString(R.string.c34));
		Assets.countriesAll.add(r.getString(R.string.c35));
		Assets.countriesAll.add(r.getString(R.string.c36));
		Assets.countriesAll.add(r.getString(R.string.c37));
		Assets.countriesAll.add(r.getString(R.string.c38));
		Assets.countriesAll.add(r.getString(R.string.c39));
		Assets.countriesAll.add(r.getString(R.string.c40));
		Assets.countriesAll.add(r.getString(R.string.c41));
		Assets.countriesAll.add(r.getString(R.string.c42));
		Assets.countriesAll.add(r.getString(R.string.c43));
		Assets.countriesAll.add(r.getString(R.string.c44));
		Assets.countriesAll.add(r.getString(R.string.c45));
		Assets.countriesAll.add(r.getString(R.string.c46));
		Assets.countriesAll.add(r.getString(R.string.c47));
		Assets.countriesAll.add(r.getString(R.string.c48));
		Assets.countriesAll.add(r.getString(R.string.c49));
		Assets.countriesAll.add(r.getString(R.string.c50));
		Assets.countriesAll.add(r.getString(R.string.c51));
		Assets.countriesAll.add(r.getString(R.string.c52));
		Assets.countriesAll.add(r.getString(R.string.c53));

		Assets.countries.add(r.getString(R.string.cc0));
		Assets.countries.add(r.getString(R.string.cc1));
		Assets.countries.add(r.getString(R.string.cc2));
		Assets.countries.add(r.getString(R.string.cc3));
		Assets.countries.add(r.getString(R.string.cc4));
		Assets.countries.add(r.getString(R.string.cc5));
		Assets.countries.add(r.getString(R.string.cc6));
		Assets.countries.add(r.getString(R.string.cc7));
		Assets.countries.add(r.getString(R.string.cc8));
		Assets.countries.add(r.getString(R.string.cc9));
		Assets.countries.add(r.getString(R.string.cc10));
		Assets.countries.add(r.getString(R.string.cc11));
		Assets.countries.add(r.getString(R.string.cc12));
		Assets.countries.add(r.getString(R.string.cc13));
		Assets.countries.add(r.getString(R.string.cc14));
		Assets.countries.add(r.getString(R.string.cc15));
		Assets.countries.add(r.getString(R.string.cc16));
		Assets.countries.add(r.getString(R.string.cc17));

		Assets.teams = new ArrayList<FootballTeam>();
		Assets.teams.add(new FootballTeam(0, r.getString(R.string.t0), 0));
		Assets.teams.add(new FootballTeam(1, r.getString(R.string.t1), 0));
		Assets.teams.add(new FootballTeam(2, r.getString(R.string.t2), 0));
		Assets.teams.add(new FootballTeam(3, r.getString(R.string.t3), 1));
		Assets.teams.add(new FootballTeam(4, r.getString(R.string.t4), 1));
		Assets.teams.add(new FootballTeam(5, r.getString(R.string.t5), 1));
		Assets.teams.add(new FootballTeam(6, r.getString(R.string.t6), 2));
		Assets.teams.add(new FootballTeam(7, r.getString(R.string.t7), 2));
		Assets.teams.add(new FootballTeam(8, r.getString(R.string.t8), 2));
		Assets.teams.add(new FootballTeam(9, r.getString(R.string.t9), 3));
		Assets.teams.add(new FootballTeam(10, r.getString(R.string.t10), 3));
		Assets.teams.add(new FootballTeam(11, r.getString(R.string.t11), 4));
		Assets.teams.add(new FootballTeam(12, r.getString(R.string.t12), 4));
		Assets.teams.add(new FootballTeam(13, r.getString(R.string.t13), 5));
		Assets.teams.add(new FootballTeam(14, r.getString(R.string.t14), 5));
		Assets.teams.add(new FootballTeam(15, r.getString(R.string.t15), 6));
		Assets.teams.add(new FootballTeam(16, r.getString(R.string.t16), 7));
		Assets.teams.add(new FootballTeam(17, r.getString(R.string.t17), 8));
		Assets.teams.add(new FootballTeam(18, r.getString(R.string.t18), 9));
		Assets.teams.add(new FootballTeam(19, r.getString(R.string.t19), 10));
		Assets.teams.add(new FootballTeam(20, r.getString(R.string.t20), 11));
		Assets.teams.add(new FootballTeam(21, r.getString(R.string.t21), 12));
		Assets.teams.add(new FootballTeam(22, r.getString(R.string.t22), 13));
		Assets.teams.add(new FootballTeam(23, r.getString(R.string.t23), 14));
		Assets.teams.add(new FootballTeam(24, r.getString(R.string.t24), 15));
		Assets.teams.add(new FootballTeam(25, r.getString(R.string.t25), 16));
		Assets.teams.add(new FootballTeam(26, r.getString(R.string.t26), 17));
		Assets.teams.add(new FootballTeam(27, r.getString(R.string.t27), 1));
		Assets.teams.add(new FootballTeam(28, r.getString(R.string.t28), 7));
		Assets.teams.add(new FootballTeam(29, r.getString(R.string.t29), 2));
		Assets.teams.add(new FootballTeam(30, r.getString(R.string.t30), 4));
		Assets.teams.add(new FootballTeam(31, r.getString(R.string.t31), 0));

		Assets.info1 = r.getString(R.string.info1);
		Assets.info1a = r.getString(R.string.info1a);
		Assets.info1b = r.getString(R.string.info1b);
		Assets.info2 = r.getString(R.string.info2);
		Assets.info2x = r.getString(R.string.info2x);
		Assets.info2a = r.getString(R.string.info2a);
		Assets.info3 = r.getString(R.string.info3);
		Assets.info3a = r.getString(R.string.info3a);

		Assets.next_level = r.getString(R.string.next_level);
		Assets.max_level = r.getString(R.string.max_level);
		Assets.max_level1 = r.getString(R.string.max_level1);
		Assets.need50 = r.getString(R.string.need50);
		Assets.need50_a = r.getString(R.string.need50_a);
		Assets.cur_level = r.getString(R.string.cur_level);
		Assets.game_over = r.getString(R.string.game_over);
		Assets.level = r.getString(R.string.level);

		Assets.favorite = r.getString(R.string.favorite);
		Assets.hated = r.getString(R.string.hated);
		Assets.team = r.getString(R.string.team);

		// Assets.info1 = r.getString(R.string.info1);

		Consts.countriesImages = new ArrayList<Bitmap>();
		Consts.teamsImages = new ArrayList<Bitmap>();
		try {
			AssetManager am = getAssets();
			Bitmap b = BitmapFactory.decodeStream(am.open("teams_flags.png"));
			for (int i = 0; i < 32; i++) {
				int line = i / 4;
				int col = i % 4;
				Bitmap btm = Bitmap.createBitmap(b, 256 * col, 256 * line, 256,
						256);
				Consts.countriesImages.add(btm);
			}// NB! 2 раза добавлены флаги. Временно
			for (int i = 0; i < 32; i++) {
				int line = i / 4;
				int col = i % 4;
				Bitmap btm = Bitmap.createBitmap(b, 256 * col, 256 * line, 256,
						256);
				Consts.countriesImages.add(btm);
			}
			for (int i = 0; i < 32; i++) {
				int line = i % 8;
				int col = i / 8;
				Bitmap btm = Bitmap.createBitmap(b, 1024 + 256 * col,
						256 * line, 256, 256);
				Consts.teamsImages.add(btm);
			}

		} catch (Exception ex) {
			for (int i = 0; i < 32; i++) {
				Bitmap b = BitmapFactory
						.decodeResource(r, R.drawable.click_ico);
				Consts.countriesImages.add(b);
			}
			for (int i = 0; i < 32; i++) {
				Bitmap b = BitmapFactory
						.decodeResource(r, R.drawable.click_ico);
				Consts.teamsImages.add(b);
			}
			Log.d("aa", "no images set");
		} catch (OutOfMemoryError err) {
			for (int i = 0; i < 32; i++) {
				Bitmap b = BitmapFactory
						.decodeResource(r, R.drawable.click_ico);
				Consts.countriesImages.add(b);
			}
			for (int i = 0; i < 32; i++) {
				Bitmap b = BitmapFactory
						.decodeResource(r, R.drawable.click_ico);
				Consts.teamsImages.add(b);
			}
			Log.d("aa", "no images set");
		}

		Assets.changeCountries(false);

		// Consts.drawables = new ArrayList<Drawable>();
		// Consts.drawables.add(r.getDrawable(R.drawable.a00a));

	}

	class GetUserTask extends AsyncTask<Void, Void, Integer> {
		InputStream is;

		@Override
		protected Integer doInBackground(Void... params) {
			String result = "";
			int id = 0;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
						+ Consts.USER_SQRIPT);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf8"), 8);
				StringBuilder sb = new StringBuilder();
				sb.append(reader.readLine());
				is.close();
				result = sb.toString();
				Log.d("aa", result);
				id = Integer.parseInt(result);
			} catch (ClientProtocolException e) {
				Log.e("aa", "Error in http connection " + e.toString());
			} catch (IOException e) {
				Log.e("aa", "Error in http connection " + e.toString());
			} catch (Exception e) {
				Log.e("aa", "Error parsing data " + e.toString());
			}
			return id;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			userID = result;
			if ((int) userID > 0) {
				Editor ed = sPref.edit();
				ed.putInt(Consts.USER_ID, userID);
				ed.commit();
			}
			Log.d("aa", "Get UserID = " + userID);
		}
	}

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0)
				adView.setVisibility(View.GONE);
			if (msg.what == 1) {
				adView.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	public void showAdMob(boolean show) {
		handler.sendEmptyMessage(show ? 1 : 0);
	}

	@Override
	public ArrayList<GameResult> results() {
		return db.getResutls(1);
	}

	@Override
	public void saveResult(GameResult r) {
		Date now = new Date();
		double dt = now.getTime();

		SetVoteTaskUI svt = new SetVoteTaskUI(userID, r, dt);
		svt.execute();
	}

	public class SetVoteTaskUI extends SetVoteTask {

		public SetVoteTaskUI(int u, GameResult r, double dt) {
			super(u, r, dt);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			this.insertedID = db.addResult(result, dateTime);
			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(Integer resultID) {
			super.onPostExecute(resultID);
			ContentValues cv = new ContentValues();
			cv.put(DatabaseManager.COLUMN_GLOBAL_ID, resultID);
			db.updateVote(cv, insertedID);
		}
	}

	@Override
	public void showTeamResutls() {
		Intent intent = new Intent(this, ResultsActivity.class);
		startActivity(intent);

	}

	@Override
	public String getDir() {
		return getFilesDir().toString() + File.separator;
	}

	@Override
	public void onSignInFailed() {
		Log.d("aa", "SignedIn Failed");
		Settings.loging = false;
		// Toast.makeText(this, getString(R.string.no_interner),
		// Toast.LENGTH_LONG)
		// .show();
	}

	@Override
	public void onSignInSucceeded() {
		Log.d("aa", "Succ");
		Settings.loging = true;
		Editor ed = sPref.edit();
		ed.putBoolean(Consts.LOGINED, true);
		ed.commit();
		Random r = new Random();
		submitScoreGPGS(new GameResult(r.nextInt(100)));

		// googleBtn.setVisibility(View.GONE);
		// loadPlayerFromGoogle();
	}

	@Override
	public void submitScoreGPGS(GameResult r) {
		// if (getSignedInGPGS())
		// gameHelper.getGamesClient().submitScore(
		// getString(R.string.leaderboard_world_cup_clicker),
		// (int) r.result);
		if (getSignedInGPGS())
			Games.Leaderboards.submitScore(gameHelper.getApiClient(),
					getString(R.string.leaderboard_world_cup_clicker),
					(int) r.result);

	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public void getLeaderboardGPGS(int type) {
		Log.d("aaa", String.valueOf(type));
		if (getSignedInGPGS())
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
					gameHelper.getApiClient(),
					getString(R.string.leaderboard_world_cup_clicker)), 100);

		// startActivityForResult(
		// gameHelper.getGamesClient().getLeaderboardIntent(
		// getString(R.string.leaderboard_world_cup_clicker)),
		// 100);
		else
			loginGPGS();
	}

	@Override
	public void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adView.destroy();
	}

	public class ImageLoadersTaskUI extends ImageLoadersTask {

		public ImageLoadersTaskUI(Context cnt) {
			super(cnt);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			imView = new ImageView(c);
			RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			btnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			File f = getFile(Consts.PICTERES_ADDRESS + Consts.imName);
			Bitmap b = Consts.decodeFile(f);
			if (b != null) {
				imView.setImageBitmap(b);
				// layout.addView(imView, btnParams);
			}
			// Toast.makeText(c, R.string.loading_complite, Toast.LENGTH_LONG)
			// .show();

		}
	}

}
