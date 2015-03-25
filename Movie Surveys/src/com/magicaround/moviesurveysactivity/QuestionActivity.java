package com.magicaround.moviesurveysactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.magicaround.db.DatabaseManager;
import com.magicaround.db.FileCache;
import com.magicaround.db.ImageLoader;
import com.magicaround.db.Utils;
import com.magicaround.moviesurveys.Answer;
import com.magicaround.moviesurveys.Consts;
import com.magicaround.moviesurveys.QuetionRandom;
import com.magicaround.moviesurveys.R;
import com.magicaround.moviesurveys.SimpleAnimationListener;
import com.magicaround.task.GetAnswersTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
	ImageButton image1;
	ImageButton image2;
	ImageButton backImage1;
	ImageButton backImage2;
	ImageButton image1X;
	ImageButton image2X;
	TextView question;
	TextView info;
	TextView text1;
	TextView text1X;
	TextView text2;
	TextView text2X;
	TextView info1;
	TextView info1X;
	TextView info2;
	TextView info2X;

	LinearLayout ll;
	ProgressBar pb;
	RelativeLayout rl;
	Button unknown;
	Button resultBtn;
	ImageButton backBtn;
	ImageView questionImage;

	static Handler handler;

	DatabaseManager db;
	ArrayList<Answer> answers;
	ArrayList<Answer> answersNew;
	GetAnswersTaskUI getAnswersTaskUI;
	int questionID;
	int a1;
	int a2;
	boolean running;
	MyImageLoaderForQuestions il1;
	MyImageLoaderForQuestions il2;
	Thread t1;
	Thread t2;
	boolean updateAnswers;
	public SharedPreferences sPref;
	int userID;
	int votes;
	String questionStr;
	String infoStr;
	String picStr;
	FileCache fc;

	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.question);
		fc = new FileCache(this);
		ll = (LinearLayout) findViewById(R.id.llQ);
		pb = (ProgressBar) findViewById(R.id.pbQ);
		rl = (RelativeLayout) findViewById(R.id.rlQ);
		questionStr = getIntent().getExtras().getString(
				DatabaseManager.COLUMN_Q_QUESTION);
		infoStr = getIntent().getExtras().getString(
				DatabaseManager.COLUMN_Q_INFO);
		picStr = getIntent().getExtras().getString(
				DatabaseManager.COLUMN_Q_PICTURE);
		question = (TextView) findViewById(R.id.textS1);
		info = (TextView) findViewById(R.id.textSurveys);
		questionImage = (ImageView) findViewById(R.id.ivQuestion);
		question.setText(questionStr);
		info.setText(infoStr);
		questionID = getIntent().getExtras().getInt("id");
		Log.d("aa", String.valueOf(questionID));
		a1 = 0;
		a2 = 0;
		votes = 1;
		running = false;
		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		answers = new ArrayList<Answer>();
		answersNew = new ArrayList<Answer>();
		sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
		userID = 0;
		if (sPref != null)
			userID = sPref.getInt(Consts.USER_ID, 0);
		getAnswersTaskUI = new GetAnswersTaskUI(this);
		getAnswersTaskUI.execute(questionID);
		if (savedInstanceState != null) {
			answers = savedInstanceState.getParcelableArrayList("answers");
			a1 = savedInstanceState.getInt("a1");
			a2 = savedInstanceState.getInt("a2");
			showLayout();
			setQuestion();
		} else {
			GetAnswerFromBase();
			updateAnswers = false;
			if (answers.size() >= 2) {
				showLayout();
				generateQuestion();
			}
		}
		t1 = new Thread(new Runnable() {
			@Override
			public void run() {
			}
		});
		t1.start();
		t2 = new Thread(new Runnable() {
			@Override
			public void run() {
			}
		});
		t2.start();
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.DisplayImage(picStr, questionImage);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelableArrayList("answers", answers);
		savedInstanceState.putInt("a1", a1);
		savedInstanceState.putInt("a2", a2);

	}

	@SuppressLint("HandlerLeak")
	private void showLayout() {
		ll.setVisibility(View.VISIBLE);
		pb.setVisibility(View.INVISIBLE);
		rl.setVisibility(View.INVISIBLE);

		text1 = (TextView) findViewById(R.id.tv1);
		text1.setVisibility(View.INVISIBLE);
		text2 = (TextView) findViewById(R.id.tv2);
		text2.setVisibility(View.INVISIBLE);
		text1X = (TextView) findViewById(R.id.tv1X);
		text1X.setVisibility(View.INVISIBLE);
		text2X = (TextView) findViewById(R.id.tv2X);
		text2X.setVisibility(View.INVISIBLE);
		info1 = (TextView) findViewById(R.id.tvInfo1);
		info1.setVisibility(View.INVISIBLE);
		info1X = (TextView) findViewById(R.id.tvInfo1X);
		info1X.setVisibility(View.INVISIBLE);
		info2 = (TextView) findViewById(R.id.tvInfo2);
		info2.setVisibility(View.INVISIBLE);
		info2X = (TextView) findViewById(R.id.tvInfo2X);
		info2X.setVisibility(View.INVISIBLE);

		image1 = (ImageButton) findViewById(R.id.imageButton1);
		image1.setVisibility(View.INVISIBLE);
		image2 = (ImageButton) findViewById(R.id.imageButton2);
		image2.setVisibility(View.INVISIBLE);
		image1X = (ImageButton) findViewById(R.id.ib1X);
		image1X.setVisibility(View.INVISIBLE);
		image2X = (ImageButton) findViewById(R.id.ib2X);
		image2X.setVisibility(View.INVISIBLE);
		backImage1 = (ImageButton) findViewById(R.id.ib1);
		backImage2 = (ImageButton) findViewById(R.id.ib2);
		unknown = (Button) findViewById(R.id.bQEqual);
		unknown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id1 = answers.get(a1).id;
				int id2 = answers.get(a2).id;
				for (int i = 0; i <= 1; i++) {
					db.addVoteToAll(questionID, userID, id1, id2, 5);
				}
				votes++;
				generateQuestion();

			}
		});
		resultBtn = (Button) findViewById(R.id.bResult);
		resultBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openResults();
			}

		});
		backBtn = (ImageButton) findViewById(R.id.bBack);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// tmpFunction();
			}
		});
		MyImageClickListner icl1 = new MyImageClickListner(true);
		MyImageClickListner icl2 = new MyImageClickListner(false);
		image1.setOnClickListener(icl1);
		backImage1.setOnClickListener(icl1);
		image1X.setOnClickListener(icl1);
		text1.setOnClickListener(icl1);
		text1X.setOnClickListener(icl1);
		info1.setOnClickListener(icl1);
		info1X.setOnClickListener(icl1);

		image2.setOnClickListener(icl2);
		backImage2.setOnClickListener(icl2);
		image2X.setOnClickListener(icl2);
		text2.setOnClickListener(icl2);
		text2X.setOnClickListener(icl2);
		info2.setOnClickListener(icl2);
		info2X.setOnClickListener(icl2);

		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				Drawable d = null;
				Log.d("aa", "SetPicture " + String.valueOf(msg.what));
				// String path = "";
				String picUrl = "";
				File f = null;
				boolean w = false;
				switch (msg.what) {
				case 1:
					picUrl = Consts.PICTERES_ADDRESS + answers.get(a1).picture;
					f = fc.getFile(picUrl);
					w = true;
					// path = getFilesDir().toString() + File.separator
					// + "pictures" + File.separator
					// + answers.get(a1).picture;

					break;
				case 2:
					picUrl = Consts.PICTERES_ADDRESS + answers.get(a2).picture;
					// path = getFilesDir().toString() + File.separator
					// + "pictures" + File.separator
					// + answers.get(a2).picture;
					f = fc.getFile(picUrl);
					w = false;

					break;
				default:
					break;
				}
				try {
					d = Drawable.createFromPath(f.getPath());
					// d = Drawable.createFromPath(path);
					if (d != null) {
						animateIn(w, d);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (OutOfMemoryError error) {
					error.printStackTrace();
				}
			}
		};
	}

	private void GetAnswerFromBase() {
		if (answersNew.size() >= 2) {
			answers.clear();
			for (Answer a : answersNew) {
				answers.add(a);
			}
			answersNew.clear();
		}
		GetAnswersRunnuble gar = new GetAnswersRunnuble();
		gar.run();
	}

	private void generateQuestion() {
		int j = votes % 5;
		if ((updateAnswers) || (j == 0)) {
			GetAnswerFromBase();
			updateAnswers = false;
			if (answers.size() >= 2) {
			} else
				return;
		}
		QuetionRandom rnd = new QuetionRandom();
		a1 = rnd.nextQuestion(answers.size());
		a2 = rnd.nextQuestion(answers.size());
		while (a1 == a2) {
			a2 = rnd.nextInt(answers.size());
		}
		setQuestion();
	}

	private void setQuestion() {
		Answer ans1 = answers.get(a1);
		Answer ans2 = answers.get(a2);
		try {
			il1.is.close();
			il1.os.close();
			il2.is.close();
			il2.os.close();
		} catch (Exception e) {
		}
		if (!running) {

			text2.setText(ans2.text);
			info2.setText(ans2.info + " (" + String.valueOf(ans2.year) + ")");

			text1.setText(ans1.text);
			info2.setText(ans1.info + " (" + String.valueOf(ans1.year) + ")");
			running = true;

		}
		changeAnimation();
		il1 = new MyImageLoaderForQuestions(ans1.picture, true, this);
		il2 = new MyImageLoaderForQuestions(ans2.picture, false, this);
		t1 = new Thread(il1);
		t2 = new Thread(il2);
		t1.start();
		t2.start();
	}

	private void changeAnimation() {
		int fadeOutDuration = 950;
		if (text1.getVisibility() == View.VISIBLE) {
			Animation fadeOutText1 = new AlphaAnimation(1, 0);
			fadeOutText1.setInterpolator(new DecelerateInterpolator());
			fadeOutText1.setDuration(fadeOutDuration);
			fadeOutText1.setAnimationListener(new AnimationOutListner(text1));
			text1.startAnimation(fadeOutText1);
		}
		if (info1.getVisibility() == View.VISIBLE) {
			Animation fadeOutInfo1 = new AlphaAnimation(1, 0);
			fadeOutInfo1.setInterpolator(new DecelerateInterpolator());
			fadeOutInfo1.setDuration(fadeOutDuration);
			fadeOutInfo1.setAnimationListener(new AnimationOutListner(info1));
			info1.startAnimation(fadeOutInfo1);
		}
		if (image1.getVisibility() == View.VISIBLE) {
			Animation fadeOutPic1 = new AlphaAnimation(1, 0);
			fadeOutPic1.setInterpolator(new DecelerateInterpolator());
			fadeOutPic1.setDuration(fadeOutDuration);
			fadeOutPic1.setAnimationListener(new AnimationOutListner(image1));
			image1.startAnimation(fadeOutPic1);
		}
		if (image1X.getVisibility() == View.VISIBLE) {
			Animation fadeOutPic1X = new AlphaAnimation(1, 0);
			fadeOutPic1X.setInterpolator(new DecelerateInterpolator());
			fadeOutPic1X.setDuration(fadeOutDuration);
			fadeOutPic1X.setAnimationListener(new AnimationOutListner(image1X));
			image1X.startAnimation(fadeOutPic1X);
		}
		if (text2.getVisibility() == View.VISIBLE) {
			Animation fadeOutText2 = new AlphaAnimation(1, 0);
			fadeOutText2.setInterpolator(new DecelerateInterpolator());
			fadeOutText2.setDuration(fadeOutDuration);
			fadeOutText2.setAnimationListener(new AnimationOutListner(text2));
			text2.startAnimation(fadeOutText2);
		}
		if (info2.getVisibility() == View.VISIBLE) {
			Animation fadeOutInfo2 = new AlphaAnimation(1, 0);
			fadeOutInfo2.setInterpolator(new DecelerateInterpolator());
			fadeOutInfo2.setDuration(fadeOutDuration);
			fadeOutInfo2.setAnimationListener(new AnimationOutListner(info2));
			info2.startAnimation(fadeOutInfo2);
		}
		if (image2.getVisibility() == View.VISIBLE) {
			Animation fadeOutPic2 = new AlphaAnimation(1, 0);
			fadeOutPic2.setInterpolator(new DecelerateInterpolator());
			fadeOutPic2.setDuration(fadeOutDuration);
			fadeOutPic2.setAnimationListener(new AnimationOutListner(image2));
			image2.startAnimation(fadeOutPic2);
		}
		if (image2X.getVisibility() == View.VISIBLE) {
			Animation fadeOutPic2X = new AlphaAnimation(1, 0);
			fadeOutPic2X.setInterpolator(new DecelerateInterpolator());
			fadeOutPic2X.setDuration(fadeOutDuration);
			fadeOutPic2X.setAnimationListener(new AnimationOutListner(image2X));
			image2X.startAnimation(fadeOutPic2X);
		}
		setText();

		int fadeInDuration = 1000;
		Animation fadeInText1 = new AlphaAnimation(0, 1);
		fadeInText1.setInterpolator(new AccelerateInterpolator());
		fadeInText1.setDuration(fadeInDuration);
		fadeInText1.setAnimationListener(new AnimationInListner(text1X, text1));
		text1X.startAnimation(fadeInText1);
		Animation fadeInText2 = new AlphaAnimation(0, 1);
		fadeInText2.setInterpolator(new AccelerateInterpolator());
		fadeInText2.setDuration(fadeInDuration);
		fadeInText2.setAnimationListener(new AnimationInListner(text2X, text2));
		text2X.startAnimation(fadeInText2);
		Animation fadeInInfo1 = new AlphaAnimation(0, 1);
		fadeInInfo1.setInterpolator(new AccelerateInterpolator());
		fadeInInfo1.setDuration(fadeInDuration);
		fadeInInfo1.setAnimationListener(new AnimationInListner(info1X, info1));
		info1X.startAnimation(fadeInInfo1);
		Animation fadeInInfo2 = new AlphaAnimation(0, 1);
		fadeInInfo2.setInterpolator(new AccelerateInterpolator());
		fadeInInfo2.setDuration(fadeInDuration);
		fadeInInfo2.setAnimationListener(new AnimationInListner(info2X, info2));
		info2X.startAnimation(fadeInInfo2);
	}

	private void setText() {
		Answer ans1 = answers.get(a1);
		Answer ans2 = answers.get(a2);

		text1X.setText(ans1.text);
		if (ans1.year > 1000)
			info1X.setText(ans1.info + " (" + String.valueOf(ans1.year) + ")");
		else
			info1X.setText(ans1.info);
		text2X.setText(ans2.text);
		if (ans1.year > 1000)
			info2X.setText(ans2.info + " (" + String.valueOf(ans2.year) + ")");
		else
			info2X.setText(ans2.info);
	}

	private void animateIn(final boolean is1, final Drawable image) {
		int fadeInDuration = 1000;
		Animation fadeIn = new AlphaAnimation(0, 1f);
		// fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(fadeInDuration);
		fadeIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (is1) {
					image1.setImageDrawable(image1X.getDrawable());
					image1.setVisibility(View.VISIBLE);
					image1X.setVisibility(View.INVISIBLE);
				} else {
					image2.setImageDrawable(image2X.getDrawable());
					image2.setVisibility(View.VISIBLE);
					image2X.setVisibility(View.INVISIBLE);
				}
			}
		});
		if (is1) {
			image1X.setImageDrawable(image);
			Log.d("aa", "SetPicture 1 from file " + String.valueOf(is1));
			image1X.setVisibility(View.VISIBLE);
			image1X.startAnimation(fadeIn);

		} else {
			image2X.setImageDrawable(image);
			Log.d("aa", "SetPicture 1 from file " + String.valueOf(is1));
			image2X.setVisibility(View.VISIBLE);
			image2X.startAnimation(fadeIn);
		}
	}

	class AnimationOutListner extends SimpleAnimationListener {
		TextView tv;
		ImageButton iv;
		int viewType;

		public AnimationOutListner(View v) {
			if (v.getClass() == ImageButton.class) {
				iv = (ImageButton) v;
				viewType = 1;
			} else if (v.getClass() == TextView.class) {
				tv = (TextView) v;
				viewType = 2;
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {

			switch (viewType) {
			case 1:
				iv.setVisibility(View.INVISIBLE);
				break;
			case 2:
				tv.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}
	}

	class AnimationInListner extends SimpleAnimationListener {
		TextView tv;
		TextView tvX;

		public AnimationInListner(View vX, View v) {
			tv = (TextView) v;
			tvX = (TextView) vX;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			tvX.setVisibility(View.INVISIBLE);
			tv.setText(tvX.getText());
			tv.setVisibility(View.VISIBLE);
		}
	}

	class GetAnswersRunnuble extends Thread {
		@Override
		public void run() {
			answersNew = db.getAnswersByQID(questionID,
					DatabaseManager.ANSWER_SORT_FOR_QUESTION);
			if (answersNew.size() >= 2) {
				updateAnswers = true;
				if (answers.size() == 0) {
					showLayout();
					generateQuestion(); // NB
				}
			}
		}
	}

	class MyImageClickListner implements OnClickListener {
		int id1;
		int id2;
		int r;

		public MyImageClickListner(boolean a) {
			r = a ? 10 : 0;
		}

		@Override
		public void onClick(View v) {
			Log.d("aa", "onClick");
			id1 = answers.get(a1).id;
			id2 = answers.get(a2).id;
			for (int i = 0; i < 1; i++) {
				db.addVoteToAll(questionID, userID, id1, id2, r);
			}
			votes++;
			generateQuestion();
		}
	}

	class GetAnswersTaskUI extends GetAnswersTask {
		public GetAnswersTaskUI(Context c) {
			super(c);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				if (answers.size() == 0) {
					updateAnswers = true;
					showLayout();
					generateQuestion(); // NB
				}
			} else
				Toast.makeText(
						ctx,
						ctx.getResources().getString(
								R.string.connection_problem), Toast.LENGTH_LONG)
						.show();
		}
	}

	private void openResults() {
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra(DatabaseManager.ID, questionID);
		intent.putExtra(DatabaseManager.COLUMN_Q_QUESTION, questionStr);
		intent.putExtra(DatabaseManager.COLUMN_Q_INFO, infoStr);
		intent.putExtra(DatabaseManager.COLUMN_Q_PICTURE, picStr);

		startActivity(intent);
	}

	@Override
	public void finish() {
		try {
			t1.interrupt();
			t2.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			t1 = new Thread(il1);
			t2 = new Thread(il2);
		}
		super.finish();
	}

	class MyImageLoaderForQuestions implements Runnable {
		public String url;
		public String picUrl;
		public boolean isImage1;
		public boolean imageSeted;
		HttpURLConnection conn;
		InputStream is;
		OutputStream os;
		long fileLen;
		long urlLen;
		Bitmap b;
		Answer a;
		Context c;

		MyImageLoaderForQuestions(String str, boolean bln, Context cnt) {
			url = str;
			picUrl = Consts.PICTERES_ADDRESS + str;
			this.isImage1 = bln;
			fileLen = 0;
			urlLen = 0;
			imageSeted = false;
			c = cnt;
			a = isImage1 ? answers.get(a1) : answers.get(a2);
		}

		@Override
		public void run() {
			Log.d("aa", "ImageLoader Starts");
			// File dir = new File(getFilesDir().toString() + File.separator
			// + "pictures");
			// File f = new File(dir, url);
			File f = fc.getFile(picUrl);
			long fileLen;

			FileInputStream stream = null;
			Bitmap bitmap = null;
			// Drawable drawable = null;
			// String path = "";

			try {
				// dir = new File(getFilesDir().toString() + File.separator
				// + "pictures");
				// f = new File(dir, Consts.PICTERES_ADDRESS + a.picture);
				BitmapFactory.Options o = new BitmapFactory.Options();
				stream = new FileInputStream(f);
				fileLen = f.length();
				bitmap = BitmapFactory.decodeStream(stream, null, o);
				Log.d("aa", "Len existed file=" + String.valueOf(fileLen));
				if (bitmap != null) {

					// path = getFilesDir().toString() + File.separator
					// + "pictures" + File.separator + a.picture;
					// path = f.getPath();
					// d = Drawable.createFromPath(path);
					handler.sendEmptyMessage(isImage1 ? 1 : 2);
					imageSeted = true;

				}
			} catch (FileNotFoundException e1) {
				// e1.printStackTrace();
			} catch (OutOfMemoryError error) {
				error.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fileLen = f.length();
				Log.d("aa", "Len existed file=" + String.valueOf(fileLen));
				HttpClient httpclient = new DefaultHttpClient();
				String conStr = Consts.SITE_ADDRESS
						+ Consts.GET_IMAGE_SIZE_SQRIPT + "?"
						+ Consts.IMAGE_SIZE_PARAM + "=" + url;
				HttpPost httppost = new HttpPost(conStr);
				Log.d("aa", conStr);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf8"), 8);
				StringBuilder sb = new StringBuilder();
				sb.append(reader.readLine());
				is.close();
				String result = sb.toString();
				urlLen = Long.parseLong(result);
				Log.d("aa", "UrlLen=" + String.valueOf(urlLen));
				if (bitmap != null) {
					if ((urlLen == fileLen) && (urlLen > 1)) {
						// handler.sendEmptyMessage(isImage1 ? 1 : 2); //NB
						// ¬ÂÏÂÌÌÓ ÓÚÍÎ˛˜ËÎ Ó·ÌÓ‚ÎÂÌËÂ ËÁÓ·‡ÊÂÌËˇ ÔË Á‡„ÛÁÍÂ
						// ÌÓ‚Ó„Ó
						return;
					}
				}
			} catch (Throwable th) {
				th.printStackTrace();
			}
			try {
				URL imageUrl = new URL(Consts.PICTERES_ADDRESS + url);
				conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				Log.d("aa", "Start downloading image");
				is = conn.getInputStream();
				os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				conn.disconnect();
				Log.d("aa", "End download image");
				BitmapFactory.Options o = new BitmapFactory.Options();
				stream = new FileInputStream(f);
				bitmap = BitmapFactory.decodeStream(stream, null, o);
				if (bitmap != null) {
					if (!imageSeted)
						handler.sendEmptyMessage(isImage1 ? 1 : 2);
					return;
				}
			} catch (OutOfMemoryError error) {
				error.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} // public void runOld() {
			// Log.d("aa", "ImageLoader Starts");
			// File dir = new File(getFilesDir().toString() + File.separator
			// + "pictures");
			// File f = new File(dir, url);
			// long fileLen;
			//
			// FileInputStream stream = null;
			// Bitmap b = null;
			// Drawable d = null;
			// String path = "";
			//
			// try {
			// dir = new File(getFilesDir().toString() + File.separator
			// + "pictures");
			// f = new File(dir, a.picture);
			// BitmapFactory.Options o = new BitmapFactory.Options();
			// stream = new FileInputStream(f);
			// fileLen = f.length();
			// b = BitmapFactory.decodeStream(stream, null, o);
			// Log.d("aa", "Len existed file=" + String.valueOf(fileLen));
			// if (b != null) {
			// path = getFilesDir().toString() + File.separator
			// + "pictures" + File.separator + a.picture;
			// d = Drawable.createFromPath(path);
			// if (d != null) {
			// handler.sendEmptyMessage(isImage1 ? 1 : 2);
			// imageSeted = true;
			// }
			//
			// }
			// } catch (FileNotFoundException e1) {
			// e1.printStackTrace();
			// } catch (OutOfMemoryError error) {
			// error.printStackTrace();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// try {
			// fileLen = f.length();
			// Log.d("aa", "Len existed file=" + String.valueOf(fileLen));
			// HttpClient httpclient = new DefaultHttpClient();
			// String conStr = Consts.SITE_ADDRESS
			// + Consts.GET_IMAGE_SIZE_SQRIPT + "?"
			// + Consts.IMAGE_SIZE_PARAM + "=" + url;
			// HttpPost httppost = new HttpPost(conStr);
			// Log.d("aa", conStr);
			// HttpResponse response = httpclient.execute(httppost);
			// HttpEntity entity = response.getEntity();
			// is = entity.getContent();
			// BufferedReader reader = new BufferedReader(
			// new InputStreamReader(is, "utf8"), 8);
			// StringBuilder sb = new StringBuilder();
			// sb.append(reader.readLine());
			// is.close();
			// String result = sb.toString();
			// urlLen = Long.parseLong(result);
			// Log.d("aa", "UrlLen=" + String.valueOf(urlLen));
			// if (b != null) {
			// if ((urlLen == fileLen) && (urlLen > 1)) {
			// // handler.sendEmptyMessage(isImage1 ? 1 : 2); //NB
			// // –í—Ä–µ–º–µ–Ω–Ω–æ –æ—Ç–∫–ª—é—á–∏–ª –∏–∑–º–µ–Ω–µ–Ω–∏–µ
			// –∏–∑–æ–±—Ä–∞–∂–µ–Ω–∏—è –ø—Ä–∏ –∑–∞–≥—Ä—É–∑–∫–µ
			// // –Ω–æ–≤–æ–≥–æ
			// return;
			// }
			// }
			// } catch (Throwable th) {
			// th.printStackTrace();
			// }
			// try {
			// URL imageUrl = new URL(Consts.PICTERES_ADDRESS + url);
			// conn = (HttpURLConnection) imageUrl.openConnection();
			// conn.setConnectTimeout(30000);
			// conn.setReadTimeout(30000);
			// conn.setInstanceFollowRedirects(true);
			// Log.d("aa", "Start downloading image");
			// is = conn.getInputStream();
			// os = new FileOutputStream(f);
			// Utils.CopyStream(is, os);
			// os.close();
			// conn.disconnect();
			// Log.d("aa", "End download image");
			// BitmapFactory.Options o = new BitmapFactory.Options();
			// stream = new FileInputStream(f);
			// b = BitmapFactory.decodeStream(stream, null, o);
			// if (b != null) {
			// if (!imageSeted)
			// handler.sendEmptyMessage(isImage1 ? 1 : 2);
			// return;
			// }
			// } catch (OutOfMemoryError error) {
			// error.printStackTrace();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// }
	}
}
