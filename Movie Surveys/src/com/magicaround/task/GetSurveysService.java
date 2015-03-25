package com.magicaround.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import com.magicaround.db.DatabaseManager;
import com.magicaround.moviesurveys.Consts;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GetSurveysService extends Service {
	final String LOG_TAG = "aa";
	ExecutorService es;
	InputStream is;
	DatabaseManager db;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "GetSurveysService onCreate");
		es = Executors.newFixedThreadPool(2);
		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "GetSurveysService onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "GetSurveysService onStartCommand");
		if (intent == null)
			return super.onStartCommand(intent, flags, startId);
		;
		MyRun mr = new MyRun(startId);
		es.execute(mr);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	class MyRun implements Runnable {
		int startId;
		boolean result;

		public MyRun(int startId) {
			this.startId = startId;
			result = false;
			Log.d(LOG_TAG, "MyRun#" + startId + " create");
		}

		@Override
		public void run() {
			Log.d(LOG_TAG, "MyRun#" + startId + " start");
			String result = "";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.SITE_ADDRESS
						+ Consts.SURVEYS_SQRIPT);
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

				JSONArray allArray = new JSONArray(result);

				JSONArray jArray = allArray.getJSONArray(0);
				JSONArray lArray = allArray.getJSONArray(1);
				db.addQuestions(jArray, lArray);

			} catch (JSONException e) {
				Log.e("aa", "Error parsing data " + e.toString());
				return;
			} catch (ClientProtocolException e) {
				Log.e("aa", "Error in http connection " + e.toString());
				return;
			} catch (IOException e) {
				Log.e("aa", "Error in http connection " + e.toString());
				return;
			} catch (Exception e) {
				Log.e("aa", "Error converting result " + e.toString());
				return;
			}
			Intent intent = new Intent(Consts.BROADCAST_ACTION);
			intent.putExtra(Consts.PARAM_STATUS, Consts.STATUS_FINISH);
			sendBroadcast(intent);
		}

	}
}