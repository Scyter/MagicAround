package com.magicaround.moviesurveysactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.magicaround.db.DatabaseManager;
import com.magicaround.moviesurveys.Consts;
import com.magicaround.moviesurveys.R;
import com.magicaround.task.BackgroundService;
import com.magicaround.task.GetSurveysService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainMovieActivity extends Activity {
	final int TASK1_CODE = 1;
	final int TASK2_CODE = 2;
	final int TASK3_CODE = 3;
	DatabaseManager db;
	SimpleCursorAdapter scAdapter;
	public SharedPreferences sPref;
	int userID;
	public boolean settings;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		Consts.getLanguage(this);
		setContentView(R.layout.main_new);
		// intent = new Intent(Consts.BROADCAST_ACTION);
		intent = new Intent(this, GetSurveysService.class);
		startService(intent);

		sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
		userID = 0;
		settings = false;
		if (sPref != null) {
			userID = sPref.getInt(Consts.USER_ID, 0);
			settings = sPref.getBoolean(Consts.LANGUAGE_SETTED, false);
		}
		checkLanguage();
		Log.d("aa", String.valueOf(userID));

		if (userID == 0) {
			GetUserTask gut = new GetUserTask();
			gut.execute();
		}

		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		db.checkVotes(userID);
		NewActivityTask nat = new NewActivityTask(this);
		nat.execute();

	}

	private void checkLanguage() {
		String l = getResources().getConfiguration().locale.getLanguage();
		int lNum = -1;
		for (int i = 0; i < Consts.languages.length; i++) {
			if (Consts.languages[i].equalsIgnoreCase(l))
				lNum = i;
		}
		// Log.d("aa", l + "-" + String.valueOf(lNum));
		if (lNum < 0)
			Toast.makeText(this, R.string.no_language, Toast.LENGTH_LONG)
					.show();
		else {
			Editor ed = sPref.edit();
			ed.putInt(Consts.LANGUAGE_NUMBER, lNum);
			ed.putBoolean(Consts.LANGUAGE_SETTED, true);
			ed.commit();
		}

	}

	public void onClick(View v) {
		startActivities();
	}

	public void startActivities() {
		Intent intent = new Intent(this, SurveysActivity.class);
		startActivity(intent);
		startService(new Intent(this, BackgroundService.class));
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		stopService(intent);
	}

	class NewActivityTask extends AsyncTask<Void, Void, Boolean> {

		Context cnt;

		public NewActivityTask(Context c) {
			cnt = c;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			startActivities();
			// } else {
			// Intent intentActivity = new Intent(cnt, SettingsActivity.class);
			// startActivity(intentActivity);
			// }
		}
	}

	class GetUserTask extends AsyncTask<Void, Void, Integer> {
		InputStream is;

		public GetUserTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("aa", "Begin");
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String result = "";
			int id = 0;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
						+ Consts.USER_SQRIPT);
				// Log.d("aa", Consts.SITE_ADDRESS + Consts.USER_SQRIPT);
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
				sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
				Editor ed = sPref.edit();
				ed.putInt(Consts.USER_ID, userID);
				ed.commit();
			}
			Log.d("aa", "End. Result = " + userID);
		}

	}
}
