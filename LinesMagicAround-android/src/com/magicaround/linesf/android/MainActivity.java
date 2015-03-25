package com.magicaround.linesf.android;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.magicaround.lines.IPlatform;
import com.magicaround.lines.LinesAssets;
import com.magicaround.lines.LinesPlayer;
import com.magicaround.lines.LinesSettings;
import com.magicaround.lines.MALinesGame;
import com.magicaround.lines.simple.IWorld;
import com.magicaround.linesf.R;

@SuppressLint("HandlerLeak")
public class MainActivity extends AndroidApplication implements IPlatform,
		GameHelperListener, ActionResolver {
	private GameHelper gameHelper;
	private static final String MY_AD_UNIT_ID = "ca-app-pub-7455231204823878/7860329347";

	Context c;
	AdView adView;
	View gameView;
	com.google.android.gms.common.SignInButton googleBtn;
	public MALinesGame game;
	public LinesPlayer player;
	public SharedPreferences sPref;

	public MainActivity() {
		c = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useWakelock = true;
		game = new MALinesGame(this, this);
		player = new LinesPlayer();
		loadPlayer();
		game.setPlayer(player);
		gameView = initializeForView(game);
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(true, "GPGS");
		gameHelper.setup(this);
		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdOpened() {
				player.nextLevel();
			}
		});
		adView.setAdUnitId(MY_AD_UNIT_ID);
		adView.setAdSize(AdSize.BANNER);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		layout.addView(gameView);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_LEFT);
		
		layout.addView(adView, adParams);
		AdRequest adRequest2 = new AdRequest.Builder().build();
		adView.loadAd(adRequest2);
		googleBtn = new SignInButton(this);
		RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.addView(googleBtn, btnParams);
		OnClickListener googleClicker = new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginGPGS();
				if (getSignedInGPGS())
					v.setVisibility(View.GONE);
			}
		};
		googleBtn.setOnClickListener(googleClicker);
		setContentView(layout);

		adView.setVisibility(View.GONE);
		loginGPGS();
		addStringsAndResources();
	}

	private void addStringsAndResources() {
		LinesAssets.i1 = getResources().getString(R.string.i1);
		LinesAssets.i1a = getResources().getString(R.string.i1a);
		LinesAssets.i2 = getResources().getString(R.string.i2);
		LinesAssets.i3 = getResources().getString(R.string.i3);
		LinesAssets.i4 = getResources().getString(R.string.i4);
		LinesAssets.i5 = getResources().getString(R.string.i5);
		LinesAssets.i6 = getResources().getString(R.string.i6);
		LinesAssets.i7 = getResources().getString(R.string.i7);
		LinesAssets.i8 = getResources().getString(R.string.i8);
		LinesAssets.i9 = getResources().getString(R.string.i9);
		LinesAssets.i10 = getResources().getString(R.string.i10);
		LinesAssets.i11 = getResources().getString(R.string.i11);
		LinesAssets.i12 = getResources().getString(R.string.i12);
		LinesAssets.i13 = getResources().getString(R.string.i13);
		LinesAssets.i14 = getResources().getString(R.string.i14);
		LinesAssets.i15 = getResources().getString(R.string.i15);

		LinesAssets.s1 = getResources().getString(R.string.s1);
		LinesAssets.s2 = getResources().getString(R.string.s2);
		LinesAssets.s3 = getResources().getString(R.string.s3);
		LinesAssets.s4 = getResources().getString(R.string.s4);
		LinesAssets.s5 = getResources().getString(R.string.s5);
		LinesAssets.s6 = getResources().getString(R.string.s6);
		LinesAssets.s7 = getResources().getString(R.string.s7);
		LinesAssets.s8 = getResources().getString(R.string.s8);
		LinesAssets.s9 = getResources().getString(R.string.s9);
		LinesAssets.s10 = getResources().getString(R.string.s10);
		LinesAssets.s11 = getResources().getString(R.string.s11);
		LinesAssets.s12 = getResources().getString(R.string.s12);

		if (getResources().getConfiguration().locale.getLanguage().equals(
				new Locale("ru").getLanguage())) {
			LinesAssets.language = 1;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.menu, menu);
		return false;
		// NB! Óáðàë îòîáðàæåíèå ìåíþ (áûëî true)
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
	public void onSignInFailed() {
		Log.d("aa", "Error sign in");
		googleBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSignInSucceeded() {
		Log.d("aa", "Succ");
		googleBtn.setVisibility(View.GONE);

		ResultsTask rt = new ResultsTask(this);
		rt.execute();
		loadPlayerFromGoogle();
		LinesSettings.loggedIn = true;
	}

	private void loadPlayerFromGoogle() {
		// TODO Auto-generated method stub

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
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
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
	public void submitScoreGPGS(int score, int type) {
		if (game.actionResolver.getSignedInGPGS()) {
			switch (type) {
			case IWorld.CLASSIC:
				Games.Leaderboards.submitScore(gameHelper.getApiClient(),
						getString(R.string.leaderboard_classic_game_score),
						score);
				break;
			case IWorld.HARD:
				Games.Leaderboards.submitScore(gameHelper.getApiClient(),
						getString(R.string.leaderboard_hardcore_game_score),
						score);

			case IWorld.SURVIVAL:
				Games.Leaderboards.submitScore(gameHelper.getApiClient(),
						getString(R.string.leaderboard_survival_game_score),
						score);

			default:
				break;
			}
		}
	}

	@Override
	public void unlockAchievementGPGS(int achievementId) {
		if (game.actionResolver.getSignedInGPGS()) {
			switch (achievementId) {
			case IWorld.A_CLASSIC_HERO:
				Games.Achievements.unlock(gameHelper.getApiClient(),
						getString(R.string.achievement_classic_lines_hero));
				break;
			case IWorld.A_CLASSIC_MASTER:
				Games.Achievements.unlock(gameHelper.getApiClient(),
						getString(R.string.achievement_classic_lines_master));
				break;
			case IWorld.A_HARDCORE_HERO:
				Games.Achievements.unlock(gameHelper.getApiClient(),
						getString(R.string.achievement_hardcore_lines_hero));
				break;
			case IWorld.A_HARDCORE_MASTER:
				Games.Achievements.unlock(gameHelper.getApiClient(),
						getString(R.string.achievement_hard_lines_master));
				break;
			case IWorld.A_SURVIVAL_MASTER:
				Games.Achievements.unlock(gameHelper.getApiClient(),
						getString(R.string.achievement_survival_lines_master));
				break;
			case IWorld.A_SURVIVAL_HERO:
				Games.Achievements.unlock(gameHelper.getApiClient(),
						getString(R.string.achievement_survival_lines_hero));
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void incrementAchievementGPGS(int achievementId, int n) {
		if (game.actionResolver.getSignedInGPGS()) {
			switch (achievementId) {
			case IWorld.A_LINES_DESTROER:
				Games.Achievements.increment(gameHelper.getApiClient(),
						getString(R.string.achievement_lines_destroyer), n);
				// gameHelper.getGamesClient().incrementAchievement(
				// getString(R.string.achievement_lines_destroyer), n);
				// break;
			case IWorld.A_MAGIC_MASTER:
				Games.Achievements.increment(gameHelper.getApiClient(),
						getString(R.string.achievement_magic_master), n);

				break;
			default:
				break;

			}
		}

	}

	@Override
	public void getLeaderboardGPGS(int type) {
		Log.d("aaa", String.valueOf(type));
		if (game.actionResolver.getSignedInGPGS()) {
			switch (type) {
			case IWorld.CLASSIC:
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(),
						getString(R.string.leaderboard_classic_game_score)),
						100);
				break;
			case IWorld.HARD:
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(),
						getString(R.string.leaderboard_hardcore_game_score)),
						100);
				break;
			case IWorld.SURVIVAL:
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(),
						getString(R.string.leaderboard_survival_game_score)),
						100);

				break;

			default:
				break;
			}
		}

	}

	@Override
	public void getAchievementsGPGS() {
		if (game.actionResolver.getSignedInGPGS()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 101);
		}
	}

	@Override
	public void showAdMob(boolean show) {
		handler.sendEmptyMessage(show ? 1 : 0);

	}

	@Override
	public void go() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adView.destroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		adView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		adView.resume();
	}

	@Override
	public void savePlayer() {
		sPref = getSharedPreferences("Player", MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putLong("Date", player.date);
		// Toast t = Toast.makeText(this, String.valueOf(player.date),
		// Toast.LENGTH_LONG);
		// t.show();
		ed.putString("Skills", player.skills);
		ed.putInt("Level", player.level);
		ed.putInt("Exp", player.experience);
		ed.commit();
	}

	@Override
	public void saveClassic() {
		sPref = getSharedPreferences("Classic", MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putLong("Date", player.c_date);
		ed.putInt("PoleSizeX", player.c_psX);
		ed.putInt("PoleSizeY", player.c_psY);
		ed.putInt("Colors", player.c_c);
		ed.putInt("Line", player.c_l);
		ed.putFloat("Tern", player.c_t);
		ed.putInt("Score", player.c_score);
		ed.putInt("HScore", player.c_h_score);
		ed.putString("Pole", player.c_pole);
		ed.commit();
	}

	@Override
	public void saveHardcore() {
		sPref = getSharedPreferences("Hardcore", MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putLong("Date", player.h_date);
		ed.putInt("PoleSizeX", player.h_psX);
		ed.putInt("PoleSizeY", player.h_psY);
		ed.putInt("Colors", player.h_c);
		ed.putInt("Line", player.h_l);
		ed.putFloat("Tern", player.h_t);
		ed.putInt("Score", player.h_score);
		ed.putInt("HScore", player.h_h_score);
		ed.putString("Pole", player.h_pole);
		ed.commit();
	}

	@Override
	public void saveCampaign() {
		sPref = getSharedPreferences("Campaign", MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putLong("Date", player.m_date);
		ed.putInt("PoleSizeX", player.m_psX);
		ed.putInt("PoleSizeY", player.m_psY);
		ed.putInt("Colors", player.m_c);
		ed.putInt("Line", player.m_l);
		ed.putFloat("Tern", player.m_t);
		ed.putInt("Score", player.m_score);
		ed.putInt("HScore", player.m_h_score);
		ed.putString("Pole", player.m_pole);
		ed.putInt("CurLevel", player.m_curLevel);
		ed.putString("Levels", player.m_levels);
		ed.putString("Tasks", player.m_tasks);
		ed.commit();
	}

	@Override
	public void saveSurvival() {
		sPref = getSharedPreferences("Survival", MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putLong("Date", player.s_date);
		ed.putInt("PoleSizeX", player.s_psX);
		ed.putInt("PoleSizeY", player.s_psY);
		ed.putInt("Colors", player.s_c);
		ed.putInt("Line", player.s_l);
		ed.putFloat("Tern", player.s_t);
		ed.putInt("Score", player.s_score);
		ed.putInt("HScore", player.s_h_score);
		ed.putString("Pole", player.s_pole);
		ed.putInt("CurLevel", player.s_curLevel);
		ed.putFloat("CurMagic", player.s_curMagic);
		ed.putFloat("AddBall", player.s_addBall);
		ed.putBoolean("AddBallNextTern", player.s_addBallNextTern);
		ed.commit();
	}

	@Override
	public void loadPlayer() {
		sPref = getSharedPreferences("Player", MODE_PRIVATE);
		if (sPref != null) {
			player.level = sPref.getInt("Level", 1);
			player.experience = sPref.getInt("Exp", 0);
			player.date = sPref.getLong("Date", 0);
			player.skills = sPref.getString("Skills", "");
			// Toast t = Toast.makeText(this, String.valueOf(player.date),
			// Toast.LENGTH_LONG);
			// t.show();
		}
		sPref = getSharedPreferences("Classic", MODE_PRIVATE);
		if (sPref != null) {
			player.c_date = sPref.getLong("Date", 0);
			player.c_psX = sPref.getInt("PoleSizeX", 9);
			player.c_psY = sPref.getInt("PoleSizeY", 9);
			player.c_c = sPref.getInt("Colors", 7);
			player.c_l = sPref.getInt("Line", 5);
			player.c_t = sPref.getFloat("Tern", 3);
			player.c_score = sPref.getInt("Score", 0);
			player.c_h_score = sPref.getInt("HScore", 0);
			player.c_pole = sPref.getString("Pole", "0");
		}

		sPref = getSharedPreferences("Hardcore", MODE_PRIVATE);
		if (sPref != null) {
			player.h_date = sPref.getLong("Date", 0);
			player.h_psX = sPref.getInt("PoleSizeX", 9);
			player.h_psY = sPref.getInt("PoleSizeY", 9);
			player.h_c = sPref.getInt("Colors", 9);
			player.h_l = sPref.getInt("Line", 5);
			player.h_t = sPref.getFloat("Tern", 3);
			player.h_score = sPref.getInt("Score", 0);
			player.h_h_score = sPref.getInt("HScore", 0);
			player.h_pole = sPref.getString("Pole", "0");
		}

		sPref = getSharedPreferences("Campaign", MODE_PRIVATE);
		if (sPref != null) {
			player.m_date = sPref.getLong("Date", 0);
			player.m_psX = sPref.getInt("PoleSizeX", 9);
			player.m_psY = sPref.getInt("PoleSizeY", 9);
			player.m_c = sPref.getInt("Colors", 7);
			player.m_l = sPref.getInt("Line", 5);
			player.m_t = sPref.getFloat("Tern", 3);
			player.m_score = sPref.getInt("Score", 0);
			player.m_h_score = sPref.getInt("HScore", 0);
			player.m_pole = sPref.getString("Pole", "0");
			player.m_levels = sPref.getString("Levels", "");
			player.m_tasks = sPref.getString("Tasks", "");
			player.m_curLevel = sPref.getInt("CurLevel", 0);
		}

		sPref = getSharedPreferences("Survival", MODE_PRIVATE);
		if (sPref != null) {
			player.s_date = sPref.getLong("Date", 0);
			player.s_psX = sPref.getInt("PoleSizeX", 9);
			player.s_psY = sPref.getInt("PoleSizeY", 9);
			player.s_c = sPref.getInt("Colors", 7);
			player.s_l = sPref.getInt("Line", 5);
			player.s_t = sPref.getFloat("Tern", 3);
			player.s_score = sPref.getInt("Score", 0);
			player.s_h_score = sPref.getInt("HScore", 0);
			player.s_pole = sPref.getString("Pole", "0");
			player.s_curMagic = sPref.getFloat("CurMagic", 0);
			player.s_curLevel = sPref.getInt("CurLevel", 0);
			player.s_addBall = sPref.getFloat("AddBall", 0);
			player.s_addBallNextTern = sPref.getBoolean("AddBallNextTern",
					false);

		}

	}

	@Override
	public void clearGame(int type) {
		Editor ed;
		switch (type) {
		case 1:
			sPref = getSharedPreferences("Classic", MODE_PRIVATE);
			ed = sPref.edit();
			ed.putLong("Date", player.c_date);
			ed.putInt("Score", player.c_score);
			ed.putInt("HScore", player.c_h_score);
			ed.putString("Pole", player.c_pole);
			ed.commit();
			break;

		case 2:
			sPref = getSharedPreferences("Classic", MODE_PRIVATE);
			ed = sPref.edit();
			ed.putLong("Date", player.h_date);
			ed.putInt("Score", player.h_score);
			ed.putInt("HScore", player.h_h_score);
			ed.putString("Pole", player.h_pole);
			ed.commit();
			break;
		default:
			break;
		}

	}

	class ResultsTask extends AsyncTask<Void, Void, Integer> {
		Context cnt;

		public ResultsTask(Context c) {
			cnt = c;
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			// if (game.actionResolver.getSignedInGPGS()) {
			// for (int i = 0; i >= 5; i++)
			// submitScoreGPGS(1, i);
			// Log.d("aa",
			// "Ð”Ð¾Ð»Ð¶Ð½Ñ‹ Ð±Ñ‹Ð»Ð¸ Ð·Ð°ÐºÐ¸Ð½ÑƒÑ‚ÑŒ Ñ€ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚Ñ‹");
			//
			// }

			return null;
		}
	}

	@Override
	public void connectBtn(boolean b) {
		// TODO Auto-generated method stub

	}

}
