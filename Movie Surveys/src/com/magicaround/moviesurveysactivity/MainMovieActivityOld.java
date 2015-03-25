package com.magicaround.moviesurveysactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.magicaround.db.DatabaseManager;
import com.magicaround.moviesurveys.Consts;
import com.magicaround.moviesurveys.R;
import com.magicaround.task.GetSurveysTask;
import com.magicaround.task.ImageChekerTask;

public class MainMovieActivityOld extends Activity {
	DatabaseManager db;
	SimpleCursorAdapter scAdapter;
	Cursor cursor;

	GetSurveysTask getSurveysTask;
	TextView tvInfo;
	ImageView iv;
	public SharedPreferences sPref;
	int userID;
	ImageView demoImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		// RelativeLayout layout = new RelativeLayout(this);
		// LayoutParams linLayParams = new
		// LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// setContentView(layout, linLayParams);
		//
		// LinearLayout.LayoutParams fonParams = new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		// LinearLayout fonView = new LinearLayout(this);
		//
		// fonView = (LinearLayout) findViewById(R.layout.fon);
		// layout.addView(fonView, fonParams);
		//
		// LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		// LinearLayout mainLayout = (LinearLayout) findViewById(R.layout.main);
		// layout.addView(mainLayout, mainParams);

		sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
		userID = 0;
		if (sPref != null)
			userID = sPref.getInt(Consts.USER_ID, 0);
		Log.d("aa", String.valueOf(userID));

		if (userID == 0) {
			GetUserTask gut = new GetUserTask(this);
			gut.execute();
		}

		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		db.checkVotes(userID);
		demoImage = (ImageView) findViewById(R.id.ib1);
		getSurveysTask = new GetSurveysTask(db);
		getSurveysTask.execute();

		tvInfo = (TextView) findViewById(R.id.tvInfo);
		iv = (ImageView) findViewById(R.id.imageView2);
		new ImageChekerMA(this);
	}

	private void animateIn(final ImageView imageView, String image) {

		int fadeInDuration = 3000; // Configure time values here
		int fadeOutDuration = 3000;
		// imageView.setVisibility(View.INVISIBLE);
		// imageView.setImageResource(image);
		imageView.setImageDrawable(Drawable.createFromPath(getFilesDir()
				.toString()
				+ File.separator
				+ "pictures"
				+ File.separator
				+ image));

		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		fadeIn.setDuration(fadeInDuration);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setDuration(fadeOutDuration);

		AnimationSet animation = new AnimationSet(false); // change to false
		animation.addAnimation(fadeIn);
		imageView.setAnimation(animation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClickStart(View v) {

		// Intent intent = new Intent(this, GraphActivity.class);
		Intent intent = new Intent(this, SurveysActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Log.d("aa", data.getExtras().toString());
		if (resultCode == RESULT_OK)
			tvInfo.setText(String.valueOf(data.getIntExtra("p", 0))
					+ String.valueOf(data.getLongExtra("id", 0)));
	}

	class GetUserTask extends AsyncTask<Void, Void, Integer> {
		InputStream is;
		Context c;

		public GetUserTask(Context c) {
			super();
			this.c = c;
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
				if (userID == 0) {
					Toast.makeText(c, R.string.no_connection, Toast.LENGTH_LONG)
							.show();
				}
			}
			Log.d("aa", "End. Result = " + userID);
		}

	}

	class ImageChekerMA {
		Context ctx;
		ImageChekerTaskUI ict;
		String str;
		File file;
		long fileLength;

		public ImageChekerMA(Context cntx) {
			Random r = new Random();
			String[] strs = { "a0001", "a0002", "a0003", "a0004" };
			str = strs[r.nextInt(strs.length)];
			ctx = cntx;
			ict = new ImageChekerTaskUI(ctx, str);
			fileLength = 0;
			File folder = new File(ctx.getFilesDir().toString()
					+ File.separator + "pictures");
			file = new File(folder, str);
			check();
		}

		public void check() {
			if (file != null)
				fileLength = file.length();
			// Log.d("aa", "fileLength = " + String.valueOf(fileLength));
			if (fileLength > 10) {// !NB
				animateIn(demoImage, str);
			}
			ict.execute(str);
		}
	}

	class ImageChekerTaskUI extends ImageChekerTask {
		String str;

		public ImageChekerTaskUI(Context cntx, String str) {
			super(cntx);
			this.str = str;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			tvInfo.setText(String.valueOf(result));
			if (result)

				animateIn(demoImage, str);
		}
	}
}
