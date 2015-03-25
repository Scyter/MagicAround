package com.magicaround.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.magicaround.db.DatabaseManager;
import com.magicaround.moviesurveys.Consts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetGraphTask extends AsyncTask<Integer, Void, Boolean> {

	InputStream is;
	DatabaseManager db;
	Integer questionID;

	public GetGraphTask(Context ctx, int id) {
		super();
		this.db = new DatabaseManager(ctx);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		questionID = id;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("aa", "Begin GetGraphTask");
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		String result = "";
		int a = 0;
		for (Integer answerID : params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
						+ Consts.GET_GRAPH_SQRIPT + "?"
						+ Consts.GRAPH_QUESTION_PARAM + "="
						+ questionID.toString() + "&"
						+ Consts.GRAPH_ANSWER_PARAM + "=" + answerID.toString());
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
				JSONArray jArray = new JSONArray(result);
				db.sqliteDB = DatabaseManager.getInstance().openDatabase();
				Log.d("aa", "Graph adding starts");
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					db.addGraph(json_data);
				}
				Log.d("aa", "Graph adding ends");
				DatabaseManager.getInstance().closeDatabase();
				a++;
			} catch (JSONException e) {
				Log.e("aa", "Error parsing data " + e.toString());
				return false;
			} catch (ClientProtocolException e) {
				Log.e("aa", "Error in http connection " + e.toString());
				return false;
			} catch (IOException e) {
				Log.e("aa", "Error in http connection " + e.toString());
				return false;
			} catch (Exception e) {
				Log.e("aa", "Error converting result " + e.toString());
				return false;
			}
			if (a == 0) {
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
							+ Consts.GET_GRAPH_SQRIPT + "?"
							+ Consts.GRAPH_QUESTION_PARAM + "="
							+ questionID.toString());
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
					JSONArray jArray = new JSONArray(result);
					db.sqliteDB = DatabaseManager.getInstance().openDatabase();
					Log.d("aa", "Graph adding starts");
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						db.addGraph(json_data);
					}
					Log.d("aa", "Graph adding ends");
					DatabaseManager.getInstance().closeDatabase();
				} catch (JSONException e) {
					Log.e("aa", "Error parsing data " + e.toString());
					return false;
				} catch (ClientProtocolException e) {
					Log.e("aa", "Error in http connection " + e.toString());
					return false;
				} catch (IOException e) {
					Log.e("aa", "Error in http connection " + e.toString());
					return false;
				} catch (Exception e) {
					Log.e("aa", "Error converting result " + e.toString());
					return false;
				}
			}
		}
		return true;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		Log.d("aa", "End GetGraphTask:" + result.toString());
	}
}
