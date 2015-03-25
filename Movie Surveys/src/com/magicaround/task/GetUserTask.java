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
import android.os.AsyncTask;
import android.util.Log;
import com.magicaround.moviesurveys.Consts;

public class GetUserTask extends AsyncTask<Object, Object, Object> {

	InputStream is;
	int userID;

	public GetUserTask(int id) {
		super();
		this.userID = id;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("aa", "Begin GetUserTask");
	}

	@Override
	protected Object doInBackground(Object... params) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
					+ Consts.USER_SQRIPT);
			Log.d("aa", Consts.SITE_ADDRESS + Consts.USER_SQRIPT);
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
			result = sb.toString();
			Log.d("aa", result);
			userID = Integer.parseInt(result);

		} catch (ClientProtocolException e) {
			Log.e("aa", "Error in http connection " + e.toString());
		} catch (IOException e) {
			Log.e("aa", "Error in http connection " + e.toString());
		} catch (Exception e) {
			Log.e("aa", "Error parsing data " + e.toString());
		}
		return userID;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		Log.d("aa", "End. Result = " + result);
	}

}
