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

import android.os.AsyncTask;
import android.util.Log;

public class GetSurveysTask extends AsyncTask<Object, Object, Boolean> {

	InputStream is;
	DatabaseManager db;

	public GetSurveysTask(DatabaseManager db) {
		super();
		this.db = db;
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
					+ Consts.SURVEYS_SQRIPT);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// convert response to string

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			// Log.d("aa", result);

			// parse json data

			JSONArray allArray = new JSONArray(result);
			db.sqliteDB = DatabaseManager.getInstance().openDatabase();

			JSONArray jArray = allArray.getJSONArray(0);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("aa", "id: " + json_data.getInt("Q_ID") + ", text: "
						+ json_data.getString("Question") + ", pic: "
						+ json_data.getString("Picture"));
				db.addQuestion(json_data);
			}

			JSONArray lArray = allArray.getJSONArray(1);
			for (int i = 0; i < lArray.length(); i++) {
				JSONObject json_data = lArray.getJSONObject(i);
				Log.i("aa",
						"id: " + json_data.getInt("Q_ID") + ", text: "
								+ json_data.getString("Question") + ", pic: "
								+ json_data.getString("Picture") + ", l: "
								+ json_data.getString("L_ID"));
				db.addQuestionLanguage(json_data);
			}

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
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		Log.d("aa", "EndGetSurveysTask: " + result.toString());
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("aa", "Begin GetSurveysTask");
	}

}
