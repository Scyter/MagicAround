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

import com.magicaround.moviesurveys.Consts;

import android.os.AsyncTask;
import android.util.Log;

public class SetVoteTask extends AsyncTask<Void, Void, Long> {
	Integer q;
	Integer u;
	Integer a1;
	Integer a2;
	Integer r;
	InputStream is;
	long voteID;

	public SetVoteTask(int q, int u, int a1, int a2, int r) {
		this.q = q;
		this.u = u;
		this.a1 = a1;
		this.a2 = a2;
		this.r = r;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("aa", "Start SetVoteTask");
	}

	@Override
	protected Long doInBackground(Void... params) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String prm = Consts.VOTE_QUESTION_PARAM + "=" + q.toString() + "&"
					+ Consts.VOTE_USER_PARAM + "=" + u.toString() + "&"
					+ Consts.VOTE_A1_PARAM + "=" + a1.toString() + "&"
					+ Consts.VOTE_A2_PARAM + "=" + a2.toString() + "&"
					+ Consts.VOTE_RESULT_PARAM + "=" + r.toString();
			String url = Consts.SITE_ADDRESS + Consts.ADD_VOTE_SQRIPT + "?"
					+ prm;
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
			result = sb.toString();
			Log.d("aa", result);
			voteID = Long.parseLong(result);
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
			return (long) 0;
		} catch (IOException e) {
			Log.e("aa", "Error in http connection " + e.toString());
			return (long) 0;
		} catch (Exception e) {
			Log.e("aa", "Error parsing data " + e.toString());
			return (long) 0;
		}
		return voteID;
	}

	@Override
	protected void onPostExecute(Long result) {
		super.onPostExecute(result);
		Log.d("aa", "End SetVoteTask:" + result.toString());
	}

}
