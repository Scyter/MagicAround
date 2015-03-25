package com.magicaround.moviesurveysactivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.magicaround.db.DatabaseManager;
import com.magicaround.db.ImageLoader;
import com.magicaround.db.ResultAdapter;
import com.magicaround.db.ResultAdapterGlobal;
import com.magicaround.moviesurveys.Answer;
import com.magicaround.moviesurveys.R;
import com.magicaround.task.GetGraphTask;

public class ResultActivity extends Activity {

	ListView lvResult;
	ListView lvResult2;
	TextView tvQuestion;
	TextView tvInfo;
	DatabaseManager db;
	ArrayList<Answer> answersLocal;
	ArrayList<Answer> answersGlobal;
	int questionID;
	ResultAdapter sAdapter;
	ResultAdapterGlobal sAdapterGlobal;
	String questionStr;
	String infoStr;
	String picStr;

	ImageLoader imageLoader;
	ImageView questionImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.result);
		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		TabHost.TabSpec tabSpec;
		tabSpec = tabHost.newTabSpec("tag1");
		tabSpec.setIndicator(getResources().getString(R.string.local_rating),
				getResources().getDrawable(R.drawable.asd));
		tabSpec.setContent(R.id.tab1);
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tag2");
		tabSpec.setIndicator(getResources().getString(R.string.global_rating),
				getResources().getDrawable(R.drawable.asd));
		tabSpec.setContent(R.id.tab2);
		tabHost.addTab(tabSpec);
		// tabHost.setCurrentTabByTag("tag2");
		lvResult = (ListView) findViewById(R.id.lvResult);
		lvResult2 = (ListView) findViewById(R.id.lvResult2);
		tvQuestion = (TextView) findViewById(R.id.tvR);
		tvInfo = (TextView) findViewById(R.id.tvInfoR);
		questionImage = (ImageView) findViewById(R.id.ivR);
		questionID = getIntent().getExtras().getInt(DatabaseManager.ID, 0);
		questionStr = getIntent().getExtras().getString(
				DatabaseManager.COLUMN_Q_QUESTION);
		infoStr = getIntent().getExtras().getString(
				DatabaseManager.COLUMN_Q_INFO);
		picStr = getIntent().getExtras().getString(
				DatabaseManager.COLUMN_Q_PICTURE);
		Log.d("aa", String.valueOf(questionID));

		if (questionID > 0) {
			answersLocal = db.getAnswersByQID(questionID,
					DatabaseManager.ANSWER_SORT_RATING_LOCAL);
			answersGlobal = db.getAnswersByQID(questionID,
					DatabaseManager.ANSWER_SORT_RATING_GLOBAL);
			setResultsLocal();
			setResultsGlobal();
			tvQuestion.setText(questionStr);
			tvInfo.setText(infoStr);

		} else
			tvQuestion.setText(getResources().getString(R.string.smth_wrong));
		GetGraphTask ggt = new GetGraphTask(this, questionID);
		ggt.execute();
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.DisplayImage(picStr, questionImage);
	}

	private void setResultsLocal() {
		for (Answer a : answersLocal) {
			a.tmp1 = answersLocal.indexOf(a);
			for (Answer aGlobal : answersGlobal) {
				if (a.id == aGlobal.id) {
					a.tmp1 -= answersGlobal.indexOf(aGlobal);
					break;
				}
			}
		}
		// String[] from = { DatabaseManager.COLUMN_A_PICTURE,
		// DatabaseManager.COLUMN_A_TEXT, DatabaseManager.COLUMN_A_INFO,
		// Consts.GLOBAL_DIFF };
		// int[] to = { R.id.ivResItem, R.id.tvResItemAnswer,
		// R.id.tvResItemInfo,
		// R.id.textDiff };
		sAdapter = new ResultAdapter(this, answersLocal);

		lvResult.setAdapter(sAdapter);
		OnItemClickListener oicl = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// startQuestion(position);
				Log.d("aa", String.valueOf(position) + " " + String.valueOf(id));
				startGraph(position, true);
			}
		};
		lvResult.setOnItemClickListener(oicl); // NB!
	}

	private void setResultsGlobal() {
		// for (Answer a : answersGlobal) {
		// m = new HashMap<String, Object>();
		// m.put(DatabaseManager.COLUMN_A_PICTURE, a.picture);
		// m.put(DatabaseManager.COLUMN_A_TEXT, a.text);
		// m.put(DatabaseManager.COLUMN_A_INFO, a.info);
		// m.put(DatabaseManager.COLUMN_A_LAST_POSITION, a.lastPosition);
		// m.put(DatabaseManager.COLUMN_A_PRE_LAST_POSITION, a.preLastPosition);
		//
		// dataGlobal.add(m);
		// }
		// String[] from = { DatabaseManager.COLUMN_A_PICTURE,
		// DatabaseManager.COLUMN_A_TEXT, DatabaseManager.COLUMN_A_INFO,
		// DatabaseManager.COLUMN_A_LAST_POSITION };
		// int[] to = { R.id.ivResItem, R.id.tvResItemAnswer,
		// R.id.tvResItemInfo,
		// R.id.textDiff };
		sAdapterGlobal = new ResultAdapterGlobal(this, answersGlobal);

		lvResult2.setAdapter(sAdapterGlobal);
		OnItemClickListener oicl = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// startQuestion(position);
				Log.d("aa", String.valueOf(position) + " " + String.valueOf(id));
				startGraph(position, false);
			}
		};
		lvResult2.setOnItemClickListener(oicl);// NB!
	}

	protected void startGraph(int id, boolean local) {
		try {
			Intent intent = new Intent(this, GraphActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("local", local);
			intent.putParcelableArrayListExtra("answers", local ? answersLocal
					: answersGlobal);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
