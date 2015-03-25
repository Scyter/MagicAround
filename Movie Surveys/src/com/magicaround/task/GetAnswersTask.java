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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.magicaround.db.DatabaseManager;
import com.magicaround.moviesurveys.Consts;

public class GetAnswersTask extends AsyncTask<Integer, Object, Boolean> {

	InputStream is;
	DatabaseManager db;
	protected Context ctx;

	public GetAnswersTask(Context c) {
		super();
		ctx = c;
		this.db = new DatabaseManager(ctx);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("aa", "Begin GetAnswersTask");
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		String result = "";
		for (Integer surveyID : params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
						+ Consts.ANSWERS_SQRIPT + "?" + Consts.ANSWERS_ID_PARAM
						+ "=" + surveyID.toString());
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

				// convert response to string

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf8"), 8);
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
				Log.d("aa", "Answers adding starts");

				JSONArray jArray = allArray.getJSONArray(0);
				JSONArray lArray = allArray.getJSONArray(1);
				db.addAnsewrs(jArray, lArray);

				// JSONArray jArray = new JSONArray(result);
				// for (int i = 0; i < jArray.length(); i++)
				// db.addAnswer(jArray.getJSONObject(i));
				// JSONObject json_data = jArray.getJSONObject(i);
				// Log.i("aa",
				// "id/q_id: " + json_data.getInt("A_ID") + "/"
				// + json_data.getInt("Q_ID") + ", text: "
				// + json_data.getString("Text") + ", pic: "
				// + json_data.getString("Picture"));
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
		return true;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		Log.d("aa", "End GetAnswersTask:" + result.toString());
	}

}
