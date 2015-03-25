package com.ma.wcc.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.ma.wcc.Assets;
import com.ma.wcc.FootballTeam;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ResultsActivity extends Activity {
	DatabaseManager db;
	ResultsAdapter ra;
	ListView lvSimple;
	RelativeLayout rl;
	ArrayList<FootballTeam> teams;
	ArrayList<FootballTeam> newTeams;
	boolean newResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		teams = new ArrayList<FootballTeam>();

		for (int i = 0; i < 32; i++) {
			FootballTeam t = new FootballTeam(i, 0);// r.nextInt(2000000000));
			teams.add(t);
		}

		newTeams = new ArrayList<FootballTeam>();
		ra = new ResultsAdapter(this, teams);
		lvSimple = (ListView) findViewById(R.id.listView);
		lvSimple.setAdapter(ra);
		rl = (RelativeLayout) findViewById(R.id.rl);
		rl.setVisibility(View.VISIBLE);

		newResults = false;
		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		ArrayList<FootballTeam> t = new ArrayList<FootballTeam>();
		t = db.getTeams();

		// db.setTeamsForAssets();

		if (!newResults) {
			if (t.size() == 32) {
				teams = t;
				updateTeamsView();
			}
		}

		GetGraphTask ggt = new GetGraphTask();
		ggt.execute();

	}

	private void fillTeam(ArrayList<FootballTeam> t) {
		for (int i = 0; i < t.size(); i++) {
			t.get(i).name = Assets.teams.get(t.get(i).id).name;
			t.get(i).nameTMP = Assets.teams.get(t.get(i).id).nameTMP;
			t.get(i).countryID = Assets.teams.get(t.get(i).id).countryID;
		}
	}

	public void updateTeamsView() {
		fillTeam(teams);
		Collections.sort(teams);
		ra = new ResultsAdapter(this, teams);
		lvSimple.setAdapter(ra);
		try {
			if (teams.get(0).result > 0)
				rl.setVisibility(View.INVISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class GetGraphTask extends AsyncTask<Void, Void, Double> {
		InputStream is;

		public GetGraphTask() {
			is = null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("aa", "Start GetGraphTask");
		}

		@Override
		protected Double doInBackground(Void... params) {
			String result = "";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				String url = Consts.SITE_ADDRESS
						+ Consts.CALC_CLICKER_RESULT_SCRIPT;
				HttpPost httppost = new HttpPost(url);
				Log.d("aa", url);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
				Log.d("aa", result);

				// JSONArray jArray = new JSONArray(result);
				JSONObject json_data = new JSONObject(result);
				db.sqliteDB = DatabaseManager.getInstance().openDatabase();
				db.sqliteDB.beginTransaction();

				newTeams.clear();
				for (int i = 0; i < json_data.length(); i++) {
					if (i >= 32)
						continue;
					double res = json_data.getDouble(String.valueOf(i));

					FootballTeam t = new FootballTeam(i, res);
					newTeams.add(t);
					//
					// teams.get(i).result = res;
					db.addTeam(i, res);

				}
				db.sqliteDB.setTransactionSuccessful();
				db.sqliteDB.endTransaction();
				DatabaseManager.getInstance().closeDatabase();
				teams = newTeams;

			} catch (ClientProtocolException e) {
				Log.e("aa", "Error in http connection " + e.toString());
				return (double) 0;
			} catch (IOException e) {
				Log.e("aa", "Error in http connection " + e.toString());
				return (double) 0;
			} catch (Exception e) {
				Log.e("aa", "Error parsing data " + e.toString());
				return (double) 0;
			}
			return (double) 1;
		}

		@Override
		protected void onPostExecute(Double resultID) {
			super.onPostExecute(resultID);
			Log.d("aa", "End GetGraphTask:" + resultID.toString());

			updateTeamsView();
			newResults = true;
		}

	}

}
