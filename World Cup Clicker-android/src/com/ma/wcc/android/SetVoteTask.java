package com.ma.wcc.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import magicaround.GameResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class SetVoteTask extends AsyncTask<Void, Void, Integer> {
	Integer u;
	GameResult result;
	double dateTime;

	InputStream is;
	int globalID;
	double insertedID;

	public SetVoteTask(int u, GameResult r, double dt) {
		this.u = u;
		result = r;
		insertedID = 0;
		this.dateTime = dt;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("aa", "Start SetVoteTask");
	}

	@Override
	protected Integer doInBackground(Void... params) {
		String result_string = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String prm = Consts.PARAM_USERID + "=" + String.valueOf(u) + "&"
					+ Consts.PARAM_RESULT + "="
					+ String.valueOf((int) result.result) + "&"
					+ Consts.PARAM_LEVEL + "=" + String.valueOf(result.level)
					+ "&" + Consts.PARAM_TIME + "=" + String.valueOf(dateTime)
					+ "&" + Consts.PARAM_FAVORITE + "="
					+ String.valueOf(result.favorite) + "&"
					+ Consts.PARAM_HATED + "=" + String.valueOf(result.hated)
					+ "&" + Consts.PARAM_INFO + "=" + "sadkfas;d";
			String url = Consts.SITE_ADDRESS + Consts.ADD_CLICKER_RESULT_SCRIPT
					+ "?" + prm;
			HttpPost httppost = new HttpPost(url);
			Log.d("aa", url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			// String s = response.toString();
			// Log.d("aa", "response " + s);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf8"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine());
			is.close();
			result_string = sb.toString();
			Log.d("aa", result_string);
			globalID = Integer.parseInt(result_string);
			// HttpResponse response = httpclient.execute(httppost);
			// HttpEntity entity = response.getEntity();
			// is = entity.getContent();
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// is, "utf8"), 8);
			// StringBuilder sb = new StringBuilder();
			// sb.append(reader.readLine());
			// is.close();
			// result = sb.toString();
			// Log.d("aa", result);

		} catch (ClientProtocolException e) {
			Log.e("aa", "Error in http connection " + e.toString());
			return 0;
		} catch (IOException e) {
			Log.e("aa", "Error in http connection " + e.toString());
			return 0;
		} catch (Exception e) {
			Log.e("aa", "Error parsing data " + e.toString());
			return 0;
		}
		return globalID;
	}

	@Override
	protected void onPostExecute(Integer resultID) {
		super.onPostExecute(resultID);
		Log.d("aa", "End SetVoteTask:" + resultID.toString());
	}

}
