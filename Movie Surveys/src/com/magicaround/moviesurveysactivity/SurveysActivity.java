package com.magicaround.moviesurveysactivity;

import java.util.ArrayList;

import com.magicaround.db.DatabaseManager;
import com.magicaround.db.SurveysAdapter;
import com.magicaround.moviesurveys.Consts;
import com.magicaround.moviesurveys.R;
import com.magicaround.moviesurveys.Survey;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SurveysActivity extends Activity {
	final String LOG_TAG = "aa";

	ListView lvSimple;
	ProgressBar pb;
	RelativeLayout rl;
	DatabaseManager db;
	ArrayList<Survey> surveys;
	SurveysAdapter sAdapter;
	// ArrayList<Map<String, Object>> data;
	private GetSurveysBroadcastReciver br;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.surveys);
		pb = (ProgressBar) findViewById(R.id.pbS);
		rl = (RelativeLayout) findViewById(R.id.rlS);
		lvSimple = (ListView) findViewById(R.id.listSurveys);

		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		surveys = new ArrayList<Survey>();

		br = new GetSurveysBroadcastReciver();
		IntentFilter intFilt = new IntentFilter(Consts.BROADCAST_ACTION);
		registerReceiver(br, intFilt);

		setSurveys();

	}

	@Override
	protected void onStop() {
		if (br != null) {
			unregisterReceiver(br);
			br = null;
		}
		super.onStop();
	}

	public void setSurveys() {
		surveys = db.getAllQuestionsArray();
		if (surveys.size() > 0) {
			pb.setVisibility(View.INVISIBLE);
			rl.setVisibility(View.INVISIBLE);
			lvSimple.setVisibility(View.VISIBLE);
		} else
			return;
		/*
		 * data = new ArrayList<Map<String, Object>>(surveys.size());
		 * Map<String, Object> m; for (Survey s : surveys) { m = new
		 * HashMap<String, Object>(); m.put(DatabaseManager.COLUMN_Q_QUESTION,
		 * s.question); m.put(DatabaseManager.COLUMN_Q_PICTURE, s.picture);
		 * m.put(DatabaseManager.COLUMN_Q_INFO, s.info);
		 * m.put(DatabaseManager.COLUMN_Q_STATE, s.info); data.add(m);
		 * 
		 * }
		 */
		sAdapter = new SurveysAdapter(this, surveys);
		lvSimple.setAdapter(sAdapter);
		OnItemClickListener oicl = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				startQuestion(position);
				Log.d("aa", String.valueOf(position) + " " + String.valueOf(id));
			}
		};
		lvSimple.setOnItemClickListener(oicl);
	}

	public void startQuestion(int id) {
		Intent intent = new Intent(this, QuestionActivity.class);
		intent.putExtra("id", surveys.get(id).id);
		intent.putExtra(DatabaseManager.COLUMN_Q_QUESTION,
				surveys.get(id).question);
		intent.putExtra(DatabaseManager.COLUMN_Q_INFO, surveys.get(id).info);
		intent.putExtra(DatabaseManager.COLUMN_Q_PICTURE,
				surveys.get(id).picture);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public class GetSurveysBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			setSurveys();
			if (br != null) {
				unregisterReceiver(br);
				br = null;
			}
		}
	}
}
